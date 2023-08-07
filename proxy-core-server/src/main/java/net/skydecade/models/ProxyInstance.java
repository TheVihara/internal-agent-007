package net.skydecade.models;

import net.skydecade.connection.GameProfile;
import net.skydecade.server.Connections;

import java.net.SocketAddress;
import java.util.Collection;

public class ProxyInstance {
    private Collection<GameProfile> gameProfiles;
    private Connections connections;
    private final String id;
    private final SocketAddress address;

    public ProxyInstance(String id, SocketAddress address) {
        this.id = id;
        this.address = address;
        this.connections = new Connections();
    }

    public Connections getConnections() {
        return connections;
    }

    public SocketAddress getAddress() {
        return address;
    }

    public String getId() {
        return id;
    }
}
