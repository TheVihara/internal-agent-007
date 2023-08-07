

package net.skydecade.protocol.packets.login;

import net.skydecade.server.ServerController;
import net.skydecade.connection.ClientConnection;
import net.skydecade.protocol.ByteMessage;
import net.skydecade.protocol.PacketIn;
import net.skydecade.protocol.registry.Version;

public class PacketLoginPluginResponse implements PacketIn {

    private int messageId;
    private boolean successful;
    private ByteMessage data;

    public int getMessageId() {
        return messageId;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public ByteMessage getData() {
        return data;
    }

    @Override
    public void decode(ByteMessage msg, Version version) {
        messageId = msg.readVarInt();
        successful = msg.readBoolean();

        if (msg.readableBytes() > 0) {
            int i = msg.readableBytes();
            data = new ByteMessage(msg.readBytes(i));
        }
    }

/*    @Override
    public void handle(ClientConnection conn, ServerController server) {
        server.getPacketHandler().handle(conn, this);
    }*/

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

}
