

package net.skydecade.protocol.packets.login;

import net.skydecade.server.ServerController;
import net.skydecade.connection.ClientConnection;
import net.skydecade.protocol.ByteMessage;
import net.skydecade.protocol.PacketIn;
import net.skydecade.protocol.registry.Version;

public class PacketLoginStart implements PacketIn {

    private String username;

    public String getUsername() {
        return username;
    }

    @Override
    public void decode(ByteMessage msg, Version version) {
        this.username = msg.readString();
    }

/*    @Override
    public void handle(ClientConnection conn, ServerController server) {
        server.getPacketHandler().handle(conn, this);
    }*/

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
