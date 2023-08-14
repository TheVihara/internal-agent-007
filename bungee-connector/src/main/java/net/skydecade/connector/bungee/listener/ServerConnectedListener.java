package net.skydecade.connector.bungee.listener;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.skydecade.connector.bungee.handler.ControllerClientHandler;
import net.skydecade.connector.bungee.BungeeConnector;
import net.skydecade.connector.bungee.connection.ControllerClient;
import net.skydecade.protocol.packets.InPlayerJoinPacket;

public class ServerConnectedListener implements Listener {
    private final BungeeConnector connector;

    public ServerConnectedListener(BungeeConnector connector) {
        this.connector = connector;
    }

    @EventHandler
    public void whenConnected(ServerConnectedEvent event) {
        ProxiedPlayer player = event.getPlayer();
        ControllerClient controllerClient = connector.getControllerClient();
        ControllerClientHandler clientHandler = controllerClient.getHandler();
        InPlayerJoinPacket packet = new InPlayerJoinPacket();

        packet.setUuid(player.getUniqueId());
        packet.setUsername(player.getName());
        packet.setConnectedServer(event.getServer().getInfo().getName());

        clientHandler.getConnectedChannel().writeAndFlush(packet);
    }
}
