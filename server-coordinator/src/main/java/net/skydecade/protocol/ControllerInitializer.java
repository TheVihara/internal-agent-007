package net.skydecade.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.timeout.ReadTimeoutHandler;
import net.skydecade.connection.ClientConnection;
import net.skydecade.handlers.ControllerHandler;
import net.skydecade.server.ServerController;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ControllerInitializer extends ChannelInitializer<SocketChannel> {
    private final ServerController serverController;

    public ControllerInitializer(ServerController serverController) {
        this.serverController = serverController;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        ClientConnection clientConnection = new ClientConnection(ch, serverController);

        pipeline.addLast("timeout", new ReadTimeoutHandler(60000, TimeUnit.MILLISECONDS));
        pipeline.addLast("decoder", new MessageDecoder());
        pipeline.addLast("encoder", new MessageEncoder());
        pipeline.addLast("handler", clientConnection);
    }

    private static class MessageDecoder extends ByteToMessageDecoder {
        @Override
        protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
            // Decode your messages directly from the ByteBuf
            // For example, if you expect messages as strings:
            String message = in.readCharSequence(in.readableBytes(), StandardCharsets.UTF_8).toString();
            out.add(message);
        }
    }

    private static class MessageEncoder extends MessageToByteEncoder<String> {
        @Override
        protected void encode(ChannelHandlerContext ctx, String message, ByteBuf out) {
            // Encode your messages directly to the ByteBuf
            // For example, if you are sending messages as strings:
            byte[] bytes = message.getBytes(StandardCharsets.UTF_8);
            out.writeInt(bytes.length);
            out.writeBytes(bytes);
        }
    }
}
