package net.skydecade.packets;

import net.skydecade.protocol.Packet;
import net.skydecade.protocol.buffer.PacketBuffer;

import java.util.UUID;

public class InPlayerSwitchServerPacket extends Packet {
    private UUID uuid;
    private String newServer;

    @Override
    public void read(PacketBuffer buffer) {
        this.uuid = buffer.readUUID();
        this.newServer = buffer.readUTF8();
    }

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeUUID(uuid);
        buffer.writeUTF8(newServer);
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getNewServer() {
        return newServer;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public void setNewServer(String newServer) {
        this.newServer = newServer;
    }
}
