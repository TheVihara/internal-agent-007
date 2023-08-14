package net.skydecade.protocol.io;

import net.skydecade.protocol.Packet;
import io.netty.channel.ChannelOutboundInvoker;

/**
 * Used to directly respond to sent packets
 */
public interface Responder {

    /**
     * Respond to the received packet using another packet.
     *
     * @param packet The packet which contains the answer.
     */
    void respond(Packet packet);

    static Responder forId(Long sessionId, ChannelOutboundInvoker channelOutboundInvoker) {
        return packet -> {
            packet.setSessionId(sessionId);
            channelOutboundInvoker.writeAndFlush(packet);
        };
    }

}
