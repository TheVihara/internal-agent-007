package net.skydecade.connection;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import net.skydecade.server.ServerController;
import net.skydecade.server.Logger;

import java.net.SocketAddress;

public class ClientConnection extends ChannelInboundHandlerAdapter {
    private final ServerController server;
    private final Channel channel;
    private SocketAddress address;

    public ClientConnection(Channel channel, ServerController server) {
        this.server = server;
        this.channel = channel;
        this.address = channel.remoteAddress();
    }

    public SocketAddress getAddress() {
        return address;
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Logger.info("Removed connection.");
        server.getConnections().removeConnection(this);
        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        if (channel.isActive()) {
            Logger.error("Unhandled exception: ", cause);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        Logger.info("Read Test: " + msg);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // Handle new connections from players or proxy servers
        Logger.info("New connection established: " + ctx.channel().remoteAddress());
        server.getConnections().addConnection(this);
        ctx.writeAndFlush("hey");

        // Implement any necessary logic for new connections
        // For example, you may want to set up the connection state or respond to the client.
    }

    public void sendKeepAlive() {
        Logger.info("Sent keep alive.");
        channel.writeAndFlush("yes", channel.voidPromise());
    }

    public boolean isConnected() {
        return channel.isActive();
    }
}
