package net.skydecade.models;

public class ProxyServerInfo {
    private String serverName;
    private String host;
    private int port;

    public ProxyServerInfo(String serverName, String host, int port) {
        this.serverName = serverName;
        this.host = host;
        this.port = port;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
