package net.skydecade.listener;

import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.skydecade.BungeeConnector;
import net.skydecade.connection.ControllerClient;
import net.skydecade.handler.ControllerClientHandler;
import net.skydecade.packets.InPlayerSwitchServerPacket;

import java.util.UUID;

public class ServerSwitchListener implements Listener {
    private final BungeeConnector connector;

    public ServerSwitchListener(BungeeConnector connector) {
        this.connector = connector;
    }

    @EventHandler
    public void onSwitch(ServerSwitchEvent event) {
        ProxiedPlayer player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        ServerInfo serverInfo = player.getServer().getInfo();
        ControllerClient controllerClient = connector.getControllerClient();
        ControllerClientHandler clientHandler = controllerClient.getHandler();
        InPlayerSwitchServerPacket packet = new InPlayerSwitchServerPacket();

        packet.setUuid(event.getPlayer().getUniqueId());
        packet.setNewServer(serverInfo.getName());
        clientHandler.getConnectedChannel().writeAndFlush(packet);
    }
}
