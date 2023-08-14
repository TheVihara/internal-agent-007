package net.skydecade.connector.bungee.listener;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.skydecade.connector.bungee.BungeeConnector;
import net.skydecade.connector.bungee.connection.ControllerClient;
import net.skydecade.connector.bungee.handler.ControllerClientHandler;
import net.skydecade.protocol.packets.InPlayerQuitPacket;

import java.util.UUID;

public class ServerDisconnectListener implements Listener {
    private final BungeeConnector connector;

    public ServerDisconnectListener(BungeeConnector connector) {
        this.connector = connector;
    }

    @EventHandler
    public void onDisconnect(PlayerDisconnectEvent event) {
        ProxiedPlayer player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        if (!player.isConnected()) {
            ControllerClient controllerClient = connector.getControllerClient();
            ControllerClientHandler clientHandler = controllerClient.getHandler();
            InPlayerQuitPacket packet = new InPlayerQuitPacket();

            packet.setUuid(uuid);

            clientHandler.getConnectedChannel().writeAndFlush(packet);
        }
    }
}
