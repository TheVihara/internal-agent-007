package net.skydecade.packets;

import net.skydecade.protocol.Packet;
import net.skydecade.protocol.buffer.PacketBuffer;

import java.util.UUID;

public class InPlayerJoinPacket extends Packet {
    private UUID uuid;
    private String username;
    private String connectedServer;

    @Override
    public void read(PacketBuffer buffer) {
        this.uuid = buffer.readUUID();
        this.username = buffer.readUTF8();
        this.connectedServer = buffer.readUTF8();
    }

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeUUID(uuid);
        buffer.writeUTF8(username);
        buffer.writeUTF8(connectedServer);
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public String getConnectedServer() {
        return connectedServer;
    }

    public void setConnectedServer(String connectedServer) {
        this.connectedServer = connectedServer;
    }
}
