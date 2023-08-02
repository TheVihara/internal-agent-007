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
import net.skydecade.handlers.GameServerHandler;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

public class GameServerConnectionInitializer extends ChannelInitializer<SocketChannel> {
    private final String clientHost;
    private final int clientPort;
    private final InetSocketAddress targetAddress;
    private final MasterServer masterServer;

    public GameServerConnectionInitializer(MasterServer masterServer, String clientHost, int clientPort, InetSocketAddress targetAddress) {
        this.masterServer = masterServer;
        this.clientHost = clientHost;
        this.clientPort = clientPort;
        this.targetAddress = targetAddress;
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