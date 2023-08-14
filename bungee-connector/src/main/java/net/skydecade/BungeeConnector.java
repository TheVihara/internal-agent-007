package net.skydecade;

import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import net.skydecade.connection.ControllerClient;
import net.skydecade.listener.ServerConnectedListener;
import net.skydecade.listener.ServerSwitchListener;
import net.skydecade.packets.InPlayerChatMessagePacket;
import net.skydecade.packets.InPlayerJoinPacket;
import net.skydecade.packets.InPlayerSwitchServerPacket;
import net.skydecade.packets.OutPlayerChatMessagePacket;
import net.skydecade.protocol.event.EventRegistry;
import net.skydecade.protocol.exception.PacketRegistrationException;
import net.skydecade.protocol.registry.IPacketRegistry;
import net.skydecade.protocol.registry.SimplePacketRegistry;

public class BungeeConnector extends Plugin {
    private ControllerClient controllerClient;
    private ScheduledTask keepAliveTask;

    @Override
    public void onEnable() {
        EventRegistry eventRegistry = new EventRegistry();
        IPacketRegistry registry = new SimplePacketRegistry();

        try {
            registry.registerPacket(1, InPlayerJoinPacket.class);
            registry.registerPacket(2, InPlayerSwitchServerPacket.class);
            registry.registerPacket(3, InPlayerChatMessagePacket.class);
            registry.registerPacket(4, OutPlayerChatMessagePacket.class);
        } catch (PacketRegistrationException e) {
            throw new RuntimeException(e);
        }

        controllerClient = new ControllerClient("127.0.0.1", 12345, registry, callBack -> {
            System.out.println("yes");
        }, eventRegistry);

        registerListeners();
    }

    @Override
    public void onDisable() {
        controllerClient.shutdown();
        keepAliveTask.cancel();
    }

    private void registerListeners() {
        PluginManager pluginManager = getProxy().getPluginManager();
        pluginManager.registerListener(this, new ServerConnectedListener(this));
        pluginManager.registerListener(this, new ServerSwitchListener(this));
    }

    public ControllerClient getControllerClient() {
        return controllerClient;
    }
}
