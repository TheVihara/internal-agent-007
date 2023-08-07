package net.skydecade.protocol;

import net.skydecade.protocol.registry.Version;

public interface PacketOut extends Packet {

    @Override
    default void decode(ByteMessage msg, Version version) {
        // Can be ignored for outgoing packets
    }

}
