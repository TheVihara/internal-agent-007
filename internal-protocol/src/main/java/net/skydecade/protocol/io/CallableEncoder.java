package net.skydecade.protocol.io;

import net.skydecade.protocol.buffer.PacketBuffer;

public interface CallableEncoder<T> {

    void write(T data, PacketBuffer buffer);

}
