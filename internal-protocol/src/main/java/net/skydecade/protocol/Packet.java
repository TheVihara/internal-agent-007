package net.skydecade.protocol;

import net.skydecade.protocol.io.Decoder;
import net.skydecade.protocol.io.Encoder;
import net.skydecade.protocol.io.Responder;

import java.util.concurrent.ThreadLocalRandom;

public abstract class Packet implements Encoder, Decoder {

    /**
     * SessionID is used for identification of the packet for use with {@link Responder}
     */
    private long sessionId = ThreadLocalRandom.current().nextLong();

    public void setSessionId(long sessionId) {
        this.sessionId = sessionId;
    }

    public long getSessionId() {
        return sessionId;
    }
}