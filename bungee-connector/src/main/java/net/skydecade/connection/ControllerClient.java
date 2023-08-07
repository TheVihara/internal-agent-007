package net.skydecade.connection;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import net.skydecade.handler.ControllerClientHandler;
import net.skydecade.Logger;

public class ControllerClient {
    private final String host;
    private final int port;
    private EventLoopGroup group;
    private Channel channel;

    public ControllerClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() throws InterruptedException {
        group = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) {
                            ch.pipeline().addLast(new ControllerClientHandler());
                        }
                    });

            ChannelFuture future = bootstrap.connect(host, port).sync();
            channel = future.channel();

            // Add some debugging logs to check the state of the channel
            Logger.info("Connection established: " + channel.remoteAddress());
            Logger.info("Channel active: " + channel.isActive());
            Logger.info("Channel open: " + channel.isOpen());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        if (channel != null && channel.isOpen()) {
            channel.close();
        }
        group.shutdownGracefully();
    }

    public void sendKeepAlive() {
        if (isConnected()) {
            Logger.info("Sent keep alive.");
            channel.writeAndFlush("yes", channel.voidPromise());
            sendMessage("maybe");
        }
    }

    public boolean isConnected() {
        return channel.isActive();
    }

    // You can use this method to send messages to the server
    public void sendMessage(Object message) {
        if (channel != null && channel.isActive()) {
            channel.writeAndFlush(message);
        }
    }
}