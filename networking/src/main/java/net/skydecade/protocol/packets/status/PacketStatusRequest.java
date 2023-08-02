package net.skydecade.protocol.packets.status;

import net.skydecade.MasterServer;
import net.skydecade.connection.ClientConnection;
import net.skydecade.protocol.ByteMessage;
import net.skydecade.protocol.PacketIn;
import net.skydecade.protocol.registry.Version;

public class PacketStatusRequest implements PacketIn {

    @Override
    public void decode(ByteMessage msg, Version version) {

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
