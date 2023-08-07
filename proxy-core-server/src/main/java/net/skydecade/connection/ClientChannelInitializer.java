

package net.skydecade.connection;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.timeout.ReadTimeoutHandler;
import net.skydecade.models.ProxyServer;
import net.skydecade.connection.pipeline.PacketDecoder;
import net.skydecade.connection.pipeline.PacketEncoder;
import net.skydecade.connection.pipeline.VarIntFrameDecoder;
import net.skydecade.connection.pipeline.VarIntLengthEncoder;

import java.util.concurrent.TimeUnit;

public class ClientChannelInitializer extends ChannelInitializer<Channel> {

    private final ProxyServer server;

    public ClientChannelInitializer(ProxyServer server) {
        this.server = server;
    }

    @Override
    protected void initChannel(Channel channel) {
        ChannelPipeline pipeline = channel.pipeline();

        PacketDecoder decoder = new PacketDecoder();
        PacketEncoder encoder = new PacketEncoder();
        ClientConnection connection = new ClientConnection(channel, server, decoder, encoder);

        pipeline.addLast("timeout", new ReadTimeoutHandler(30000,
                TimeUnit.MILLISECONDS));
        pipeline.addLast("frame_decoder", new VarIntFrameDecoder());
        pipeline.addLast("frame_encoder", new VarIntLengthEncoder());
        pipeline.addLast("decoder", decoder);
        pipeline.addLast("encoder", encoder);
        pipeline.addLast("handler", connection);
    }

}
