

package net.skydecade.protocol.packets.status;

import net.skydecade.MasterServer;
import net.skydecade.protocol.ByteMessage;
import net.skydecade.protocol.PacketOut;
import net.skydecade.protocol.registry.Version;

public class PacketStatusResponse implements PacketOut {

    private static final String TEMPLATE = "{ \"version\": { \"name\": \"%s\", \"protocol\": %d }, \"players\": { \"max\": %d, \"online\": %d, \"sample\": [] }, \"description\": %s }";

    private MasterServer server;

    public PacketStatusResponse() { }

    public PacketStatusResponse(MasterServer server) {
        this.server = server;
    }

    @Override
    public void encode(ByteMessage msg, Version version) {
        int protocol;
        int staticProtocol = -1;

        if (staticProtocol > 0) {
            protocol = staticProtocol;
        } else {
            protocol = Version.getMax().getProtocolNumber();
        }

        String ver = "limbo-server";
        String desc = "{\"text\": \"&9limbo-server\"}";

        msg.writeString(getResponseJson(ver, protocol,
                100,
                server.getConnections().getCount(), desc));
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

    private String getResponseJson(String version, int protocol, int maxPlayers, int online, String description) {
        return String.format(TEMPLATE, version, protocol, maxPlayers, online, description);
    }
}
