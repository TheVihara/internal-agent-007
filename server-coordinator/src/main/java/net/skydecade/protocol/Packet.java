

package net.skydecade.protocol;

import net.skydecade.server.ServerController;
import net.skydecade.connection.ClientConnection;
import net.skydecade.protocol.registry.Version;

public interface Packet {

    void encode(ByteMessage msg, Version version);

    void decode(ByteMessage msg, Version version);

    default void handle(ServerController server) {
        // Ignored by default
    }

}
