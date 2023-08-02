package net.skydecade.protocol;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.ReadTimeoutHandler;
import net.skydecade.MasterServer;
import net.skydecade.connection.ClientConnection;
import net.skydecade.connection.pipeline.PacketDecoder;
import net.skydecade.connection.pipeline.PacketEncoder;
import net.skydecade.connection.pipeline.VarIntFrameDecoder;
import net.skydecade.connection.pipeline.VarIntLengthEncoder;
import net.skydecade.handlers.MasterServerHandler;

import java.util.concurrent.TimeUnit;

public class MasterServerInitializer extends ChannelInitializer<SocketChannel> {
    private final MasterServer masterServer;

    public MasterServerInitializer(MasterServer masterServer) {
        this.masterServer = masterServer;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        PacketDecoder decoder = new PacketDecoder();
        PacketEncoder encoder = new PacketEncoder();
        ClientConnection connection = new ClientConnection(ch, masterServer, decoder, encoder);

        pipeline.addLast("timeout", new ReadTimeoutHandler(30000,
                TimeUnit.MILLISECONDS));
        pipeline.addLast("frame_decoder", new VarIntFrameDecoder());
        pipeline.addLast("frame_encoder", new VarIntLengthEncoder());
        pipeline.addLast("decoder", decoder);
        pipeline.addLast("encoder", encoder);
        pipeline.addLast("handler", connection);
    }
}