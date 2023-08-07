

package net.skydecade.protocol;

import net.skydecade.protocol.registry.Version;

public interface PacketIn extends Packet {

    @Override
    default void encode(ByteMessage msg, Version version) {
        // Can be ignored for incoming packets
    }

}
