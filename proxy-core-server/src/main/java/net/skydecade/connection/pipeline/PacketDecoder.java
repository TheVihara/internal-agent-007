

package net.skydecade.connection.pipeline;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import net.skydecade.protocol.ByteMessage;
import net.skydecade.protocol.Packet;
import net.skydecade.protocol.registry.State;
import net.skydecade.protocol.registry.Version;
import net.skydecade.server.Logger;

import java.util.List;

public class PacketDecoder extends MessageToMessageDecoder<ByteBuf> {

    private State.PacketRegistry mappings;
    private Version version;

    public PacketDecoder() {
        updateVersion(Version.getMin());
        updateState(State.HANDSHAKING);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> out) throws Exception {
        if (!ctx.channel().isActive() || mappings == null) return;

        ByteMessage msg = new ByteMessage(buf);
        int packetId = msg.readVarInt();
        Packet packet = mappings.getPacket(packetId);

        if (packet != null) {
            Logger.debug("Received packet %s[0x%s]", packet.toString(), Integer.toHexString(packetId));
            try {
                packet.decode(msg, version);
            } catch (Exception e) {
                Logger.warning("Cannot decode packet 0x%s: %s", Integer.toHexString(packetId), e.getMessage());
            }

            ctx.fireChannelRead(packet);
        } else {
            Logger.debug("Undefined incoming packet: 0x" + Integer.toHexString(packetId));
        }
    }

    public void updateVersion(Version version) {
        this.version = version;
    }

    public void updateState(State state) {
        this.mappings = state.serverBound.getRegistry(version);
    }
}
