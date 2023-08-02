package net.skydecade;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;

public class GameServerConnectionInitializer extends ChannelInitializer<SocketChannel> {
    private final String clientHost;
    private final int clientPort;
    private final InetSocketAddress targetAddress;

    public GameServerConnectionInitializer(String clientHost, int clientPort, InetSocketAddress targetAddress) {
        this.clientHost = clientHost;
        this.clientPort = clientPort;
        this.targetAddress = targetAddress;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new StringDecoder());
        pipeline.addLast(new StringEncoder());
        pipeline.addLast(new GameServerHandler(clientHost, clientPort, targetAddress));
    }
}