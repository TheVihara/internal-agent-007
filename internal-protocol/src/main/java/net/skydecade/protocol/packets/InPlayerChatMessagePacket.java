package net.skydecade.protocol.packets;

import net.skydecade.protocol.Packet;
import net.skydecade.protocol.buffer.PacketBuffer;

import java.util.List;
import java.util.UUID;

public class InPlayerChatMessagePacket extends Packet {
    private UUID uuid;
    private String msg;
    private List<UUID> recievers;

    @Override
    public void read(PacketBuffer buffer) {
        this.uuid = buffer.readUUID();
        this.msg = buffer.readUTF8();
        this.recievers = buffer.readUuidCollection();
    }

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeUUID(uuid);
        buffer.writeUTF8(msg);
        buffer.writeUuidCollection(recievers);
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public List<UUID> getRecievers() {
        return recievers;
    }

    public void setRecievers(List<UUID> recievers) {
        this.recievers = recievers;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
