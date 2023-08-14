package net.skydecade.packets;

import net.skydecade.protocol.Packet;
import net.skydecade.protocol.buffer.PacketBuffer;

import java.util.UUID;

public class OutPlayerChatMessagePacket extends Packet {
    private UUID uuid;
    private String msg;

    @Override
    public void read(PacketBuffer buffer) {
        this.uuid = buffer.readUUID();
        this.msg = buffer.readUTF8();
    }

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeUUID(uuid);
        buffer.writeUTF8(msg);
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getMsg() {
        return msg;
    }
}
