

package net.skydecade.connection;

import net.skydecade.server.ServerController;
import net.skydecade.protocol.PacketSnapshot;
import net.skydecade.protocol.packets.login.PacketLoginSuccess;
import net.skydecade.utils.UuidUtil;

import java.util.UUID;

public final class PacketSnapshots {

    public static PacketSnapshot PACKET_LOGIN_SUCCESS;
    public static PacketSnapshot PACKET_PLAYER_INFO;


    private PacketSnapshots() { }

    public static void initPackets(ServerController server) {
        final String username = "limbo-server";
        final UUID uuid = UuidUtil.getOfflineModeUuid(username);

        PacketLoginSuccess loginSuccess = new PacketLoginSuccess();
        loginSuccess.setUsername(username);
        loginSuccess.setUuid(uuid);

        PACKET_LOGIN_SUCCESS = PacketSnapshot.of(loginSuccess);
    }
}
