package net.skydecade.protocol.packets.status;

import net.skydecade.MasterServer;
import net.skydecade.connection.ClientConnection;
import net.skydecade.protocol.ByteMessage;
import net.skydecade.protocol.registry.Version;
import net.skydecade.protocol.Packet;

public class PacketStatusPing implements Packet {

    private long randomId;

    @Override
    public void encode(ByteMessage msg, Version version) {
        msg.writeLong(randomId);
    }

    @Override
    public void decode(ByteMessage msg, Version version) {
        this.randomId = msg.readLong();
    }

    @Override
    public void handle(ClientConnection conn, MasterServer server) {
        server.getPacketHandler().handle(conn, this);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

}
