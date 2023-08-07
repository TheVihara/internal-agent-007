package net.skydecade.models;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import net.skydecade.connection.ClientChannelInitializer;
import net.skydecade.connection.ClientConnection;
import net.skydecade.connection.PacketHandler;
import net.skydecade.connection.PacketSnapshots;
import net.skydecade.server.Connections;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ProxyServer {
    private List<ProxyInstance> proxyInstanceList;
    private PacketHandler packetHandler;
    private ScheduledFuture<?> keepAliveTask;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private Connections connections;

    public ProxyServer() {
        this.proxyInstanceList = new ArrayList<>();
        this.packetHandler = new PacketHandler(this);
        this.connections = new Connections();
        proxyInstanceList.add(new ProxyInstance("1A", new InetSocketAddress("127.0.0.1", 25565)));
        PacketSnapshots.initPackets(this);
    }

    public void start() {
        bossGroup = new NioEventLoopGroup(1);
        workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ClientChannelInitializer(this))
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .localAddress(InetSocketAddress.createUnresolved("127.0.0.1", 12345))
                    .bind();

            ChannelFuture future = bootstrap.bind(12345).sync();
            System.out.println("Proxy Core Server started. Listening on port 12345");
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

        keepAliveTask = workerGroup.scheduleAtFixedRate(this::broadcastKeepAlive, 0L, 5L, TimeUnit.SECONDS);
    }

    private void broadcastKeepAlive() {
        connections.getAllConnections().forEach(ClientConnection::sendKeepAlive);
    }

    public Connections getConnections() {
        return connections;
    }

    public PacketHandler getPacketHandler() {
        return packetHandler;
    }
}
