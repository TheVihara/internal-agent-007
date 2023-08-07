package net.skydecade;

import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import net.skydecade.connection.ControllerClient;
import net.skydecade.listener.ServerConnectedListener;

import java.util.concurrent.TimeUnit;

public class BungeeConnector extends Plugin {
    private ControllerClient controllerClient;
    private ScheduledTask keepAliveTask;

    @Override
    public void onLoad() {
        controllerClient = new ControllerClient("127.0.0.1", 12345);
    }

    @Override
    public void onEnable() {
        try {
            controllerClient.start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        keepAliveTask = getProxy().getScheduler().schedule(this, () -> {
            controllerClient.sendKeepAlive();
        }, 0L, 5L, TimeUnit.SECONDS);

        registerListeners();
    }

    @Override
    public void onDisable() {
        controllerClient.stop();
        keepAliveTask.cancel();
    }

    private void registerListeners() {
        PluginManager pluginManager = getProxy().getPluginManager();
        pluginManager.registerListener(this, new ServerConnectedListener(this));
    }

    public void sendMessageToServer(Object message) {
        controllerClient.sendMessage(message);
    }
}
