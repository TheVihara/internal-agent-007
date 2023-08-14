package net.skydecade.protocol;

import net.skydecade.protocol.packets.InPlayerChatMessagePacket;
import net.skydecade.protocol.packets.InPlayerJoinPacket;
import net.skydecade.protocol.packets.InPlayerQuitPacket;
import net.skydecade.protocol.packets.InPlayerSwitchServerPacket;
import net.skydecade.protocol.packets.OutPlayerChatMessagePacket;
import net.skydecade.protocol.event.EventRegistry;
import net.skydecade.protocol.exception.PacketRegistrationException;
import net.skydecade.protocol.registry.IPacketRegistry;
import net.skydecade.protocol.registry.SimplePacketRegistry;

public class ProtocolLauncher {
    public static final IPacketRegistry PACKET_REGISTRY = new SimplePacketRegistry();
    public static final EventRegistry EVENT_REGISTRY = new EventRegistry();
    public static void main(String[] args) {
        try {
            PACKET_REGISTRY.registerPacket(1, InPlayerJoinPacket.class);
            PACKET_REGISTRY.registerPacket(2, InPlayerSwitchServerPacket.class);
            PACKET_REGISTRY.registerPacket(3, InPlayerChatMessagePacket.class);
            PACKET_REGISTRY.registerPacket(4, OutPlayerChatMessagePacket.class);
            PACKET_REGISTRY.registerPacket(5, InPlayerQuitPacket.class);
        } catch (PacketRegistrationException e) {
            e.printStackTrace();
        }
    }
}
