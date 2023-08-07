package net.skydecade.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import net.skydecade.server.ServerController;
import net.skydecade.server.Logger;

public class ControllerHandler extends ChannelInboundHandlerAdapter {
    private final ServerController serverController;

    public ControllerHandler(ServerController serverController) {
        this.serverController = serverController;
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Logger.info("Disconnected: " + ctx.channel().remoteAddress());
        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        Logger.error("Unhandled exception: ", cause);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        // Assuming 'msg' is a String, handle the keep-alive message specifically
        if (msg instanceof String && ((String) msg).equalsIgnoreCase("yes")) {
            Logger.info("Received keep-alive message.");
            return;
        }

        // Handle other non-keep-alive messages here and send a response if required.
        Logger.info("Received message: " + msg);
        String text = "Hey, I got the message.";
        ctx.writeAndFlush(text);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // Handle new connections from players or proxy servers
        Logger.info("New connection established: " + ctx.channel().remoteAddress());
        ctx.writeAndFlush("hey");

        // Implement any necessary logic for new connections
        // For example, you may want to set up the connection state or respond to the client.
    }
}


