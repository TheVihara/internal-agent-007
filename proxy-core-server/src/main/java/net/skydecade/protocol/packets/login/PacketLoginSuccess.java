

package net.skydecade.protocol.packets.login;

import net.skydecade.protocol.ByteMessage;
import net.skydecade.protocol.PacketOut;
import net.skydecade.protocol.registry.Version;

import java.util.UUID;

public class PacketLoginSuccess implements PacketOut {

    private UUID uuid;
    private String username;

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public void encode(ByteMessage msg, Version version) {
        if (version.moreOrEqual(Version.V1_16)) {
            msg.writeUuid(uuid);
        } else if (version.moreOrEqual(Version.V1_7_6)) {
            msg.writeString(uuid.toString());
        } else {
            msg.writeString(uuid.toString().replace("-", ""));
        }
        msg.writeString(username);
        if (version.moreOrEqual(Version.V1_19)) {
            msg.writeVarInt(0);
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

}
