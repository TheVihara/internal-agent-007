

package net.skydecade.connection.pipeline;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import net.skydecade.protocol.ByteMessage;
import net.skydecade.protocol.Packet;
import net.skydecade.protocol.PacketSnapshot;
import net.skydecade.protocol.registry.State;
import net.skydecade.protocol.registry.Version;
import net.skydecade.server.Logger;

public class PacketEncoder extends MessageToByteEncoder<Packet> {

    private State.PacketRegistry registry;
    private Version version;

    public PacketEncoder() {
        updateVersion(Version.getMin());
        updateState(State.HANDSHAKING);
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Packet packet, ByteBuf out) throws Exception {
        if (registry == null) return;

        ByteMessage msg = new ByteMessage(out);
        int packetId;

        if (packet instanceof PacketSnapshot) {
            packetId = registry.getPacketId(((PacketSnapshot)packet).getWrappedPacket().getClass());
        } else {
            packetId = registry.getPacketId(packet.getClass());
        }

        if (packetId == -1) {
            Logger.warning("Undefined packet class: %s[0x%s]", packet.getClass().getName(), Integer.toHexString(packetId));
            return;
        }

        msg.writeVarInt(packetId);

        try {
            packet.encode(msg, version);

            if (Logger.getLevel() >= Logger.Level.DEBUG.getIndex()) {
                Logger.debug("Sending %s[0x%s] packet (%d bytes)", packet.toString(), Integer.toHexString(packetId), msg.readableBytes());
            }
        } catch (Exception e) {
            Logger.error("Cannot encode packet 0x%s: %s", Integer.toHexString(packetId), e.getMessage());
        }
    }

    public void updateVersion(Version version) {
        this.version = version;
    }

    public void updateState(State state) {
        this.registry = state.clientBound.getRegistry(version);
    }

}
