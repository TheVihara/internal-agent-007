package net.skydecade.protocol.io;

import net.skydecade.protocol.buffer.PacketBuffer;

public interface CallableDecoder<T> {

    T read(PacketBuffer buffer);

}
