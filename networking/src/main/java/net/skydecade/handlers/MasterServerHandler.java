package net.skydecade.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import net.skydecade.MasterServer;
import net.skydecade.models.PlayerInfo;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class MasterServerHandler extends SimpleChannelInboundHandler<ByteBuf> {
    private final MasterServer masterServer;

    public MasterServerHandler(MasterServer masterServer) {
        this.masterServer = masterServer;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf message) throws Exception {
        String msg = message.toString(CharsetUtil.UTF_8);
        System.out.println("Received message from client: " + msg); // Add this line to see the received message
        String[] parts = msg.split(",");

        // Assuming the message is of the format "CLIENT_CONNECTED,<PlayerName>,<IP>,<PORT>"
        if (parts.length == 4 && parts[0].equals("CLIENT_CONNECTED")) {
            String playerName = parts[1];
            String ipAddress = parts[2];
            int port = Integer.parseInt(parts[3]);

            // Get the player or create a new player if not found
            PlayerInfo player = masterServer.getPlayerByAddress(ipAddress, port);
            if (player == null) {
                player = new PlayerInfo(playerName, ipAddress, port);
                masterServer.addPlayer(player);
            }

            // Redirect the player to a random game server
            String selectedServer = masterServer.getRandomProxyServer();
            masterServer.redirectPlayer(player, selectedServer);

/*            byte[] handshakeMessage = createHandshakeMessage("127.0.0.1", 25565);
            ctx.writeAndFlush(Unpooled.wrappedBuffer(handshakeMessage));*/
        } else {
            System.out.println("Invalid message format from client: " + msg);
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // Handle new connections from players or proxy servers
        System.out.println("New connection established: " + ctx.channel().remoteAddress());

        byte[] handshakeMessage = createHandshakeMessage("127.0.0.1", 25565);
        ctx.writeAndFlush(Unpooled.wrappedBuffer(handshakeMessage));
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        // Handle disconnections from players or proxy servers
        System.out.println("Connection closed: " + ctx.channel().remoteAddress());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    public static byte[] createHandshakeMessage(String host, int port) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        DataOutputStream handshake = new DataOutputStream(buffer);

        handshake.writeByte(0x00); // Handshake packet ID
        writeVarInt(handshake, 	759); // Protocol version (Minecraft 1.17.1 uses protocol version 404)
        writeString(handshake, host, StandardCharsets.UTF_8);
        handshake.writeShort(port);
        writeVarInt(handshake, 2); // Next State (1 for Status)

        return buffer.toByteArray();
    }

    public static void writeString(DataOutputStream out, String string, Charset charset) throws IOException {
        byte[] bytes = string.getBytes(charset);
        writeVarInt(out, bytes.length);
        out.write(bytes);
    }

    public static void writeVarInt(DataOutputStream out, int paramInt) throws IOException {
        while (true) {
            if ((paramInt & 0xFFFFFF80) == 0) {
                out.writeByte(paramInt);
                return;
            }

            out.writeByte(paramInt & 0x7F | 0x80);
            paramInt >>>= 7;
        }
    }

    public static int readVarInt(DataInputStream in) throws IOException {
        int i = 0;
        int j = 0;
        while (true) {
            int k = in.readByte();
            i |= (k & 0x7F) << j++ * 7;
            if (j > 5) throw new RuntimeException("VarInt too big");
            if ((k & 0x80) != 128) break;
        }
        return i;
    }
}