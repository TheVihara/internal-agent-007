package net.skydecade.connector.bukkit.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import net.skydecade.protocol.event.EventRegistry;
import net.skydecade.protocol.handler.PacketChannelInboundHandler;
import net.skydecade.protocol.handler.PacketDecoder;
import net.skydecade.protocol.handler.PacketEncoder;
import net.skydecade.protocol.registry.IPacketRegistry;

public class ControllerClientHandler extends ChannelInitializer<Channel> {
    private final IPacketRegistry packetRegistry;
    private final EventRegistry eventRegistry;
    private Channel connectedChannel;

    public ControllerClientHandler(IPacketRegistry packetRegistry, EventRegistry eventRegistry) {
        this.packetRegistry = packetRegistry;
        this.eventRegistry = eventRegistry;
    }

    @Override
    protected void initChannel(Channel channel) throws Exception {
        channel.pipeline()
                .addLast(new PacketDecoder(packetRegistry), new PacketEncoder(packetRegistry), new PacketChannelInboundHandler(eventRegistry));
        this.connectedChannel = channel;
    }

    public Channel getConnectedChannel() {
        return connectedChannel;
    }
}