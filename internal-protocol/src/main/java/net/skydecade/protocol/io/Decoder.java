package net.skydecade.protocol.io;

import net.skydecade.protocol.buffer.PacketBuffer;

public interface Decoder {

    void read(PacketBuffer buffer);

}
