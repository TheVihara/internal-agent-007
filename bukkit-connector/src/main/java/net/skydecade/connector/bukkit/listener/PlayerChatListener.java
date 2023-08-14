package net.skydecade.connector.bukkit.listener;

import net.skydecade.connector.bukkit.connection.ControllerClient;
import net.skydecade.connector.bukkit.handler.ControllerClientHandler;
import net.skydecade.protocol.packets.InPlayerChatMessagePacket;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.List;
import java.util.UUID;

public class PlayerChatListener implements Listener {
    private ControllerClient controller;

    public PlayerChatListener(ControllerClient controller) {
        this.controller = controller;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        ControllerClientHandler handler = controller.getHandler();
        InPlayerChatMessagePacket packet = new InPlayerChatMessagePacket();

        packet.setUuid(uuid);
        packet.setMsg("§aThis is a test message!§e BEEP BOOP");
        packet.setRecievers(List.of(
                uuid
        ));

        handler.getConnectedChannel().writeAndFlush(packet);
    }
}
