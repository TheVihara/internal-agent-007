package net.skydecade.listener;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.skydecade.BungeeConnector;

public class ServerConnectedListener implements Listener {
    private final BungeeConnector connector;

    public ServerConnectedListener(BungeeConnector connector) {
        this.connector = connector;
    }

    @EventHandler
    public void whenConnected(ServerConnectedEvent event) {
        ProxiedPlayer player = event.getPlayer();
        connector.sendMessageToServer(player.getName() + " connected to " + event.getServer().getInfo().getName());
    }
}
