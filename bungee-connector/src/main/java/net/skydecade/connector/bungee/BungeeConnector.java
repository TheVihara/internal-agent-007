package net.skydecade.connector.bungee;

import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import net.skydecade.connector.bungee.connection.ControllerClient;
import net.skydecade.connector.bungee.listener.ServerConnectedListener;
import net.skydecade.connector.bungee.listener.ServerDisconnectListener;
import net.skydecade.connector.bungee.listener.ServerSwitchListener;
import net.skydecade.protocol.ProtocolLauncher;

import static net.skydecade.protocol.ProtocolLauncher.EVENT_REGISTRY;
import static net.skydecade.protocol.ProtocolLauncher.PACKET_REGISTRY;

public class BungeeConnector extends Plugin {
    private ControllerClient controllerClient;
    private ScheduledTask keepAliveTask;

    @Override
    public void onEnable() {
        ProtocolLauncher.main(null);
        controllerClient = new ControllerClient("127.0.0.1", 12345, PACKET_REGISTRY, callBack -> {
            System.out.println("yes");
        }, EVENT_REGISTRY);

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
        pluginManager.registerListener(this, new ServerDisconnectListener(this));
    }

    public ControllerClient getControllerClient() {
        return controllerClient;
    }
}
