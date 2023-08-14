package net.skydecade.connector.bukkit.connection;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import net.skydecade.protocol.event.EventRegistry;
import net.skydecade.connector.bukkit.handler.ControllerClientHandler;
import net.skydecade.protocol.registry.IPacketRegistry;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

public class ControllerClient {
    private final String host;
    private final int port;
    private final Bootstrap bootstrap;
    private final IPacketRegistry packetRegistry;
    private final EventRegistry eventRegistry;
    private final ControllerClientHandler handler;

    private final EventLoopGroup workerGroup = new NioEventLoopGroup();

    public ControllerClient(String host, int port, IPacketRegistry packetRegistry, Consumer<Future<? super Void>> doneCallback, EventRegistry eventRegistry) {
        this.host = host;
        this.port = port;
        this.packetRegistry = packetRegistry;
        this.eventRegistry = eventRegistry;
        this.handler = new ControllerClientHandler(packetRegistry, eventRegistry);
        this.bootstrap = new Bootstrap()
                .option(ChannelOption.AUTO_READ, true)
                .group(workerGroup)
                .handler(handler)
                .channel(NioSocketChannel.class);

        try {
            this.bootstrap.connect(new InetSocketAddress(host, port))
                    .awaitUninterruptibly().sync().addListener(doneCallback::accept);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void shutdown() {
        try {
            workerGroup.shutdownGracefully().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public ControllerClientHandler getHandler() {
        return handler;
    }
}