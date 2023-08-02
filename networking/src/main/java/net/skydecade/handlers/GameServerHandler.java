package net.skydecade.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.net.InetSocketAddress;

public class GameServerHandler extends SimpleChannelInboundHandler<String> {
    private final String clientHost;
    private final int clientPort;
    private final InetSocketAddress targetAddress;

    public GameServerHandler(String clientHost, int clientPort, InetSocketAddress targetAddress) {
        this.clientHost = clientHost;
        this.clientPort = clientPort;
        this.targetAddress = targetAddress;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String message) throws Exception {
        // Process incoming messages from game server and forward to player
        System.out.println("Received message from game server: " + message);
        forwardMessageToPlayer(message, ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // Handle successful connection to game server
        System.out.println("Connected to game server: " + targetAddress.getHostString() + ":" + targetAddress.getPort());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        // Handle disconnection from game server
        System.out.println("Disconnected from game server: " + targetAddress.getHostString() + ":" + targetAddress.getPort());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    private void forwardMessageToPlayer(String message, ChannelHandlerContext gameServerCtx) {
        // Assuming you have access to the player's channel context (playerCtx)
        // Forward the message from the game server to the player
        // playerCtx.writeAndFlush(message);
        // For this part, you'll need to make sure you have a reference to the player's channel context.
    }
}