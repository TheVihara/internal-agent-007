package net.skydecade;

import net.skydecade.packets.InPlayerChatMessagePacket;
import net.skydecade.packets.InPlayerJoinPacket;
import net.skydecade.packets.InPlayerSwitchServerPacket;
import net.skydecade.packets.OutPlayerChatMessagePacket;
import net.skydecade.protocol.event.EventRegistry;
import net.skydecade.protocol.exception.PacketRegistrationException;
import net.skydecade.protocol.registry.IPacketRegistry;
import net.skydecade.protocol.registry.SimplePacketRegistry;
import net.skydecade.server.Logger;
import net.skydecade.server.ServerController;

public class ControllerLauncher {
    public static void main(String[] args) {
        IPacketRegistry packetRegistry = new SimplePacketRegistry();
        EventRegistry eventRegistry = new EventRegistry();

        try {
            packetRegistry.registerPacket(1, InPlayerJoinPacket.class);
            packetRegistry.registerPacket(2, InPlayerSwitchServerPacket.class);
            packetRegistry.registerPacket(3, InPlayerChatMessagePacket.class);
            packetRegistry.registerPacket(4, OutPlayerChatMessagePacket.class);
        } catch (PacketRegistrationException e) {
            e.printStackTrace();
        }

        ServerController serverController = new ServerController(packetRegistry, callBack -> {
            Logger.info("I'm running xD");
        }, eventRegistry);
    }
}
