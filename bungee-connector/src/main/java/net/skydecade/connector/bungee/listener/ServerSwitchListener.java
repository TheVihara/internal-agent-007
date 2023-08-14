package net.skydecade.connector.bungee.listener;

import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.skydecade.connector.bungee.handler.ControllerClientHandler;
import net.skydecade.connector.bungee.BungeeConnector;
import net.skydecade.connector.bungee.connection.ControllerClient;
import net.skydecade.protocol.packets.InPlayerSwitchServerPacket;

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

        if (event.getFrom() != null) {
            ControllerClient controllerClient = connector.getControllerClient();
            ControllerClientHandler clientHandler = controllerClient.getHandler();
            InPlayerSwitchServerPacket packet = new InPlayerSwitchServerPacket();

            packet.setUuid(uuid);
            packet.setNewServer(serverInfo.getName());
            clientHandler.getConnectedChannel().writeAndFlush(packet);
        }
    }
}
