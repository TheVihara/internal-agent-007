

package net.skydecade.protocol.packets.login;

import io.netty.buffer.ByteBuf;
import net.skydecade.protocol.ByteMessage;
import net.skydecade.protocol.PacketOut;
import net.skydecade.protocol.registry.Version;

public class PacketLoginPluginRequest implements PacketOut {

    private int messageId;
    private String channel;
    private ByteBuf data;

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public void setData(ByteBuf data) {
        this.data = data;
    }

    @Override
    public void encode(ByteMessage msg, Version version) {
        msg.writeVarInt(messageId);
        msg.writeString(channel);
        msg.writeBytes(data);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

}
