package net.skydecade;

import net.skydecade.models.ProxyServer;

public class ProxyLauncher {
    public static void main(String[] args) {
        ProxyServer proxyServer = new ProxyServer();
        proxyServer.start();
    }
}