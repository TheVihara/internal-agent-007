package net.skydecade;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.util.CharsetUtil;
import net.skydecade.models.PlayerInfo;
import net.skydecade.models.ProxyServerInfo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class MasterServer {
    private final Map<String, PlayerInfo> players;
    private final List<ProxyServerInfo> proxyServers;

    public MasterServer() {
        this.players = new HashMap<>();
        this.proxyServers = new ArrayList<>();
    }

    public void addProxyServer(ProxyServerInfo proxyServer) {
        proxyServers.add(proxyServer);
    }

    public void start() {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new MasterServerInitializer(this))
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture future = bootstrap.bind(12345).sync();
            System.out.println("MasterServer started. Listening on port 12345");
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public synchronized void addPlayer(PlayerInfo player) {
        players.put(player.getIpAddress() + ":" + player.getPort(), player);
    }

    public synchronized void removePlayer(PlayerInfo player) {
        players.remove(player.getIpAddress() + ":" + player.getPort());
    }

    public synchronized PlayerInfo getPlayerByAddress(String ipAddress, int port) {
        return players.get(ipAddress + ":" + port);
    }

    public synchronized String getRandomProxyServer() {
        if (proxyServers.isEmpty()) {
            return null;
        }

        ProxyServerInfo randomServer = proxyServers.get(new Random().nextInt(proxyServers.size()));
        return randomServer.getServerName();
    }

    public synchronized void redirectPlayer(PlayerInfo player, String selectedServer) {
        InetSocketAddress targetAddress = getProxyServerAddress(selectedServer);
        if (targetAddress == null) {
            System.out.println("No game servers available. Cannot redirect player.");
            return;
        }

        player.setCurrentGameServer(selectedServer);

        try {
            System.out.println("Redirecting player " + player.getPlayerName() +
                    " to Game Server: " + selectedServer + " at " +
                    targetAddress.getHostString() + ":" + targetAddress.getPort());

            SocketChannel clientChannel = connectToGameServer(player.getIpAddress(), player.getPort(), targetAddress);
            forwardMessages(player, clientChannel);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private synchronized InetSocketAddress getProxyServerAddress(String selectedServer) {
        for (ProxyServerInfo proxyServer : proxyServers) {
            if (proxyServer.getServerName().equals(selectedServer)) {
                return new InetSocketAddress(proxyServer.getHost(), proxyServer.getPort());
            }
        }
        return null;
    }

    private SocketChannel connectToGameServer(String clientHost, int clientPort, InetSocketAddress targetAddress) throws IOException {
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workerGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new GameServerConnectionInitializer(clientHost, clientPort, targetAddress));

            ChannelFuture future = bootstrap.connect(targetAddress).sync();
            return (SocketChannel) future.channel();
        } catch (InterruptedException e) {
            throw new IOException("Failed to connect to game server", e);
        }
    }

    private void forwardMessages(PlayerInfo player, SocketChannel gameServerChannel) {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        BlockingQueue<String> playerQueue = new LinkedBlockingQueue<>();
        BlockingQueue<ByteBuf> gameServerQueue = new LinkedBlockingQueue<>();

        try {
            ChannelPipeline pipeline = gameServerChannel.pipeline();
            pipeline.addLast(new MessageToByteEncoder<String>() {
                @Override
                protected void encode(ChannelHandlerContext ctx, String message, ByteBuf out) throws Exception {
                    out.writeBytes(message.getBytes());
                }
            });
            pipeline.addLast(new MessageToMessageDecoder<ByteBuf>() {
                @Override
                protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
                    String message = in.toString(CharsetUtil.UTF_8);
                    out.add(message);
                }
            });

            // Create separate threads to handle communication between player and game server
            workerGroup.execute(() -> {
                try {
                    while (true) {
                        String message = playerQueue.take();
                        gameServerChannel.writeAndFlush(message);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });

            workerGroup.execute(() -> {
                try {
                    while (true) {
                        ByteBuf messageBuf = gameServerQueue.take();
                        gameServerChannel.writeAndFlush(messageBuf);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        MasterServer masterServer = new MasterServer();
        masterServer.addProxyServer(new ProxyServerInfo("proxy-1", "127.0.0.1", 25565));
        /*masterServer.addProxyServer(new ProxyServerInfo("proxy-2", "127.0.0.1", 25566));*/
        // Add more proxy servers as needed
        masterServer.start();
    }
}