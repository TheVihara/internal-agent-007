package net.skydecade.protocol.io;

import net.skydecade.protocol.buffer.PacketBuffer;

public interface Encoder {

    void write(PacketBuffer buffer);

}
