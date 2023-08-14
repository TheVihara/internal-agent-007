package net.skydecade.coordinator;

import net.skydecade.coordinator.server.Logger;
import net.skydecade.coordinator.server.ServerController;
import net.skydecade.protocol.ProtocolLauncher;

import static net.skydecade.protocol.ProtocolLauncher.EVENT_REGISTRY;
import static net.skydecade.protocol.ProtocolLauncher.PACKET_REGISTRY;

public class ControllerLauncher {
    public static void main(String[] args) {
        ProtocolLauncher.main(args);
        ServerController serverController = new ServerController(PACKET_REGISTRY, callBack -> {
            Logger.info("I'm running xD");
        }, EVENT_REGISTRY);
    }
}
