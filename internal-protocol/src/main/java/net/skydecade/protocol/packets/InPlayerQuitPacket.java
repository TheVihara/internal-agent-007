package net.skydecade.protocol.packets;

import net.skydecade.protocol.Packet;
import net.skydecade.protocol.buffer.PacketBuffer;

import java.util.UUID;

public class InPlayerQuitPacket extends Packet {
    private UUID uuid;

    @Override
    public void read(PacketBuffer buffer) {
        this.uuid = buffer.readUUID();
    }

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeUUID(uuid);
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
}
