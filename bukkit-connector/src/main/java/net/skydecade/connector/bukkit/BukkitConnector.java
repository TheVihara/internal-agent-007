package net.skydecade.connector.bukkit;

import io.netty.channel.ChannelHandlerContext;
import net.skydecade.connector.bukkit.connection.ControllerClient;
import net.skydecade.protocol.ProtocolLauncher;
import net.skydecade.protocol.event.PacketSubscriber;
import net.skydecade.connector.bukkit.listener.PlayerChatListener;
import net.skydecade.protocol.packets.OutPlayerChatMessagePacket;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

import static net.skydecade.protocol.ProtocolLauncher.EVENT_REGISTRY;
import static net.skydecade.protocol.ProtocolLauncher.PACKET_REGISTRY;

public class BukkitConnector extends JavaPlugin {
    private ControllerClient controllerClient;

    @Override
    public void onEnable() {
        ProtocolLauncher.main(null);
        EVENT_REGISTRY.registerEvents(new Object() {
            @PacketSubscriber
            public void onPacketReceive(OutPlayerChatMessagePacket packet, ChannelHandlerContext ctx) {
                UUID uuid = packet.getUuid();
                String msg = packet.getMsg();

                Player player = Bukkit.getPlayer(uuid);
                Logger.info("Received the OutPlayerChatMessagePacket btw.");
                if (player != null) {
                    player.sendMessage(msg);
                }
            }
        });

        controllerClient = new ControllerClient("127.0.0.1", 12345, PACKET_REGISTRY, callBack -> {
            System.out.println("yes");
        }, EVENT_REGISTRY);

        registerListeners();
    }

    @Override
    public void onDisable() {
        controllerClient.shutdown();
    }

    private void registerListeners() {
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new PlayerChatListener(controllerClient), this);
    }

    public ControllerClient getControllerClient() {
        return controllerClient;
    }
}
