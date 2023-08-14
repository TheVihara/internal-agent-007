package net.skydecade.protocol.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.DecoderException;
import net.skydecade.protocol.Packet;
import net.skydecade.protocol.buffer.PacketBuffer;
import net.skydecade.protocol.registry.IPacketRegistry;

import java.util.List;

public class PacketDecoder extends ByteToMessageDecoder {

    private final IPacketRegistry packetRegistry;

    public PacketDecoder(IPacketRegistry packetRegistry) {
        this.packetRegistry = packetRegistry;
    }

    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        int packetId = byteBuf.readInt();
        if (!packetRegistry.containsPacketId(packetId)) {
            throw new DecoderException("Received invalid packet id");
        }
        long sessionId = byteBuf.readLong();
        PacketBuffer buffer = new PacketBuffer(byteBuf.readBytes(byteBuf.readableBytes()));
        Packet packet = packetRegistry.constructPacket(packetId);
        packet.setSessionId(sessionId);
        packet.read(buffer);

        list.add(packet);
    }

}