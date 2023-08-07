

package net.skydecade.protocol.packets;

import net.skydecade.connection.ClientConnection;
import net.skydecade.models.ProxyServer;
import net.skydecade.protocol.ByteMessage;
import net.skydecade.protocol.PacketIn;
import net.skydecade.protocol.registry.State;
import net.skydecade.protocol.registry.Version;

public class PacketHandshake implements PacketIn {

    private Version version;
    private String host;
    private int port;
    private State nextState;

    public Version getVersion() {
        return version;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public State getNextState() {
        return nextState;
    }

    @Override
    public void decode(ByteMessage msg, Version version) {
        try {
            this.version = Version.of(msg.readVarInt());
        } catch (IllegalArgumentException e) {
            this.version = Version.UNDEFINED;
        }

        this.host = msg.readString();
        this.port = msg.readUnsignedShort();
        this.nextState = State.getById(msg.readVarInt());
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

    @Override
    public void handle(ClientConnection conn, ProxyServer server) {
        server.getPacketHandler().handle(conn, this);
    }
}
