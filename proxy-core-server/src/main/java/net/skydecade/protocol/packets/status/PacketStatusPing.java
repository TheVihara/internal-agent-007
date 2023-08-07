package net.skydecade.protocol.packets.status;

import net.skydecade.models.ProxyServer;
import net.skydecade.connection.ClientConnection;
import net.skydecade.protocol.ByteMessage;
import net.skydecade.protocol.Packet;
import net.skydecade.protocol.registry.Version;

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
    public void handle(ClientConnection conn, ProxyServer server) {
        server.getPacketHandler().handle(conn, this);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

}
