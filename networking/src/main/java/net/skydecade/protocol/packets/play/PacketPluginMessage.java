package net.skydecade.protocol.packets.play;

import net.skydecade.protocol.ByteMessage;
import net.skydecade.protocol.PacketOut;
import net.skydecade.protocol.registry.Version;

public class PacketPluginMessage implements PacketOut {

    private String channel;
    private String message;

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public void encode(ByteMessage msg, Version version) {
        msg.writeString(channel);
        msg.writeString(message);
    }
}
