package net.skydecade;

import io.netty.channel.ChannelHandlerContext;
import net.skydecade.connection.ControllerClient;
import net.skydecade.packets.InPlayerChatMessagePacket;
import net.skydecade.packets.OutPlayerChatMessagePacket;
import net.skydecade.protocol.event.EventRegistry;
import net.skydecade.protocol.event.PacketSubscriber;
import net.skydecade.protocol.exception.PacketRegistrationException;
import net.skydecade.protocol.registry.IPacketRegistry;
import net.skydecade.protocol.registry.SimplePacketRegistry;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public class BukkitConnector extends JavaPlugin {
    private ControllerClient controllerClient;

    @Override
    public void onEnable() {
        EventRegistry eventRegistry = new EventRegistry();
        IPacketRegistry registry = new SimplePacketRegistry();

        try {
            registry.registerPacket(3, InPlayerChatMessagePacket.class);
            registry.registerPacket(4, OutPlayerChatMessagePacket.class);
        } catch (PacketRegistrationException e) {
            throw new RuntimeException(e);
        }

        eventRegistry.registerEvents(new Object() {
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

        controllerClient = new ControllerClient("127.0.0.1", 12345, registry, callBack -> {
            System.out.println("yes");
        }, eventRegistry);

        registerListeners();
    }

    @Override
    public void onDisable() {
        controllerClient.shutdown();
    }

    private void registerListeners() {
        PluginManager pluginManager = getServer().getPluginManager();
    }

    public ControllerClient getControllerClient() {
        return controllerClient;
    }
}
