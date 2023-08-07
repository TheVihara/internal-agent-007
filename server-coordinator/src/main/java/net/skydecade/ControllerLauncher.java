package net.skydecade;

import net.skydecade.server.ServerController;

public class ControllerLauncher {
    public static void main(String[] args) {
        ServerController serverController = new ServerController();
        serverController.start();
    }
}
