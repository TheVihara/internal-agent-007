package net.skydecade.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import net.skydecade.connection.ClientConnection;
import net.skydecade.connection.PacketHandler;
import net.skydecade.connection.PacketSnapshots;
import net.skydecade.models.PlayerInfo;
import net.skydecade.protocol.ControllerInitializer;

import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ServerController {
    private final Map<String, PlayerInfo> players;
    private PacketHandler packetHandler;
    private Connections connections;
    private ScheduledFuture<?> keepAliveTask;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    public ServerController() {
        this.players = new HashMap<>();
        packetHandler = new PacketHandler(this);
        connections = new Connections();

        PacketSnapshots.initPackets(this);
    }

    public void start() {
        bossGroup = new NioEventLoopGroup(1);
        workerGroup = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ControllerInitializer(this))
                .childOption(ChannelOption.TCP_NODELAY, true)
                .localAddress(new InetSocketAddress("127.0.0.1", 12345));

        // Now that the server is successfully configured, let's start it on a separate thread
        new Thread(() -> {
            try {
                ChannelFuture future = bootstrap.bind().sync();
                System.out.println("ServerController started. Listening on port 12345");

                // Schedule the keep-alive task after the server is started and listening
                keepAliveTask = workerGroup.scheduleAtFixedRate(this::broadcastKeepAlive, 0L, 5L, TimeUnit.SECONDS);

                // Wait until the server socket is closed
                future.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                bossGroup.shutdownGracefully();
                workerGroup.shutdownGracefully();
            }
        }).start();
    }

    private void stop() {
        Logger.info("Stopping server...");

        if (keepAliveTask != null) {
            keepAliveTask.cancel(true);
        }

        if (bossGroup != null) {
            bossGroup.shutdownGracefully();
        }

        if (workerGroup != null) {
            workerGroup.shutdownGracefully();
        }

        Logger.info("Server stopped, Goodbye!");
    }

    private void broadcastKeepAlive() {
        Logger.info("broadcasting");
        connections.getAllConnections().forEach(ClientConnection::sendKeepAlive);
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

    public PacketHandler getPacketHandler() {
        return packetHandler;
    }

    public Connections getConnections() {
        return connections;
    }
}