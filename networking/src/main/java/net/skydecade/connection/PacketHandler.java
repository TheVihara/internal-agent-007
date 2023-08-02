package net.skydecade.connection;

import net.skydecade.MasterServer;
import net.skydecade.protocol.packets.PacketHandshake;
import net.skydecade.protocol.packets.login.PacketLoginPluginResponse;
import net.skydecade.protocol.packets.login.PacketLoginStart;
import net.skydecade.protocol.packets.status.PacketStatusPing;
import net.skydecade.protocol.packets.status.PacketStatusRequest;
import net.skydecade.protocol.packets.status.PacketStatusResponse;
import net.skydecade.server.Logger;
import net.skydecade.utils.UuidUtil;

public class PacketHandler {

    private final MasterServer server;

    public PacketHandler(MasterServer server) {
        this.server = server;
    }

    public void handle(ClientConnection conn, PacketHandshake packet) {
        conn.updateVersion(packet.getVersion());
        conn.updateState(packet.getNextState());

        Logger.debug("Pinged from %s [%s]", conn.getAddress(),
                conn.getClientVersion().toString());

/*            String[] split = packet.getHost().split("\00");

            Logger.info(packet);

        if (split.length == 3 || split.length == 4) {
            conn.setAddress(split[1]);
            conn.getGameProfile().setUuid(UuidUtil.fromString(split[2]));
        } else {
            conn.disconnectLogin("You've enabled player info forwarding. You need to connect with proxy");
        }*/
    }

    public void handle(ClientConnection conn, PacketStatusRequest packet) {
        conn.sendPacket(new PacketStatusResponse(server));
    }

    public void handle(ClientConnection conn, PacketStatusPing packet) {
        conn.sendPacketAndClose(packet);
    }

    public void handle(ClientConnection conn, PacketLoginStart packet) {
        if (server.getConnections().getCount() >= 100) {
            conn.disconnectLogin("Too many players connected");
            return;
        }

        if (!conn.getClientVersion().isSupported()) {
            conn.disconnectLogin("Unsupported client version");
            return;
        }

        conn.getGameProfile().setUsername(packet.getUsername());
        conn.getGameProfile().setUuid(UuidUtil.getOfflineModeUuid(packet.getUsername()));

        conn.fireLoginSuccess();
    }

    public void handle(ClientConnection conn, PacketLoginPluginResponse packet) {
    }

}
