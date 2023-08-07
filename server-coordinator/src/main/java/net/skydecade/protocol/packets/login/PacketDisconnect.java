

package net.skydecade.protocol.packets.login;

import net.skydecade.protocol.ByteMessage;
import net.skydecade.protocol.PacketOut;
import net.skydecade.protocol.registry.Version;

public class PacketDisconnect implements PacketOut {

    private String reason;

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public void encode(ByteMessage msg, Version version) {
        msg.writeString(String.format("{\"text\": \"%s\"}", reason));
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

}
