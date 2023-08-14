package net.skydecade.coordinator.handlers;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import net.skydecade.protocol.event.EventRegistry;
import net.skydecade.protocol.handler.PacketChannelInboundHandler;
import net.skydecade.protocol.handler.PacketDecoder;
import net.skydecade.protocol.handler.PacketEncoder;
import net.skydecade.protocol.registry.IPacketRegistry;

public class ControllerHandler extends ChannelInitializer<Channel> {
    private IPacketRegistry packetRegistry;
    private EventRegistry eventRegistry;
    private Channel connectedChannel;

    public ControllerHandler(IPacketRegistry packetRegistry, EventRegistry eventRegistry) {
        this.packetRegistry = packetRegistry;
        this.eventRegistry = eventRegistry;
    }

    @Override
    protected void initChannel(Channel channel) throws Exception {
        channel.pipeline()
                .addLast(new PacketDecoder(packetRegistry), new PacketEncoder(packetRegistry), new PacketChannelInboundHandler(eventRegistry));
        this.connectedChannel = channel;
    }
}


