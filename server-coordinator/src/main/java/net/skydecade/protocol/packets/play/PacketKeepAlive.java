

package net.skydecade.protocol.packets.play;

import net.skydecade.protocol.ByteMessage;
import net.skydecade.protocol.Packet;
import net.skydecade.protocol.registry.Version;

public class PacketKeepAlive implements Packet {

    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public void encode(ByteMessage msg, Version version) {
        if (version.moreOrEqual(Version.V1_12_2)) {
            msg.writeLong(id);
        } else if (version.moreOrEqual(Version.V1_8)) {
            msg.writeVarInt((int) id);
        } else {
            msg.writeInt((int) id);
        }
    }

    @Override
    public void decode(ByteMessage msg, Version version) {
        if (version.moreOrEqual(Version.V1_12_2)) {
            this.id = msg.readLong();
        } else if (version.moreOrEqual(Version.V1_8)) {
            this.id = msg.readVarInt();
        } else {
            this.id = msg.readInt();
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
