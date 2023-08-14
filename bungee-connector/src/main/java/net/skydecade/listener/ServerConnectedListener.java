package net.skydecade.listener;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.skydecade.BungeeConnector;
import net.skydecade.connection.ControllerClient;
import net.skydecade.handler.ControllerClientHandler;
import net.skydecade.packets.InPlayerChatMessagePacket;
import net.skydecade.packets.InPlayerJoinPacket;

import java.util.List;

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

        packet.setUuid(event.getPlayer().getUniqueId());
        packet.setUsername(event.getPlayer().getName());
        packet.setConnectedServer(event.getServer().getInfo().getName());

        clientHandler.getConnectedChannel().writeAndFlush(packet);
    }
}
