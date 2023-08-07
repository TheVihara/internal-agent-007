package net.skydecade.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import net.skydecade.Logger;
import java.nio.charset.StandardCharsets;

public class ControllerClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        // Assuming 'msg' is a ByteBuf, convert it to a String and log the content
        if (msg instanceof ByteBuf) {
            ByteBuf byteBuf = (ByteBuf) msg;
            String message = byteBuf.toString(StandardCharsets.UTF_8);
            Logger.info("New controller message: " + message);
            ctx.pipeline().channel().writeAndFlush(msg);
        } else {
            // Handle other types of messages as needed
            Logger.info("Received a message of unknown type: " + msg.getClass().getSimpleName());
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}