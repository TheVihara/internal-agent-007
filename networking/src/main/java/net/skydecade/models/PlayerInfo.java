package net.skydecade.models;


public class PlayerInfo {
    private String playerName;
    private String ipAddress;
    private int port;
    private String currentGameServer;
    private String serverAddress;
    private int serverPort;

    public PlayerInfo(String playerName, String ipAddress, int port) {
        this.playerName = playerName;
        this.ipAddress = ipAddress;
        this.port = port;
        this.currentGameServer = null;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public int getPort() {
        return port;
    }

    public String getCurrentGameServer() {
        return currentGameServer;
    }

    public void setCurrentGameServer(String currentGameServer) {
        this.currentGameServer = currentGameServer;
    }
}
