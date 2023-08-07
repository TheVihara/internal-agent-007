

package net.skydecade.protocol;

import net.skydecade.models.ProxyServer;
import net.skydecade.connection.ClientConnection;
import net.skydecade.protocol.registry.Version;

public interface Packet {

    void encode(ByteMessage msg, Version version);

    void decode(ByteMessage msg, Version version);

    default void handle(ClientConnection conn, ProxyServer server) {
        // Ignored by default
    }

}
