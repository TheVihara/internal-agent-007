package net.skydecade.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.Future;
import net.skydecade.handlers.ControllerHandler;
import net.skydecade.models.PlayerInfo;
import net.skydecade.packets.InPlayerChatMessagePacket;
import net.skydecade.packets.InPlayerJoinPacket;
import net.skydecade.packets.InPlayerSwitchServerPacket;
import net.skydecade.packets.OutPlayerChatMessagePacket;
import net.skydecade.protocol.event.EventRegistry;
import net.skydecade.protocol.event.PacketSubscriber;
import net.skydecade.protocol.registry.IPacketRegistry;

import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

public class ServerController {
    private final Map<UUID, PlayerInfo> players;
    private final ServerBootstrap bootstrap;
    private final IPacketRegistry packetRegistry;
    private final EventRegistry eventRegistry;

    private EventLoopGroup parentGroup = new NioEventLoopGroup();
    private EventLoopGroup workerGroup = new NioEventLoopGroup();

    public ServerController(IPacketRegistry packetRegistry, Consumer<Future<? super Void>> doneCallback, EventRegistry eventRegistry) {
        this.players = new HashMap<>();
        this.packetRegistry = packetRegistry;
        this.eventRegistry = eventRegistry;

        eventRegistry.registerEvents(new Object() {
            // The method signature of a PacketSubscriber must contain a valid packet and may contain the ChannelHandlerContext (optional)
            @PacketSubscriber
            public void onPacketReceive(InPlayerJoinPacket packet, ChannelHandlerContext ctx) {
                UUID uuid = packet.getUuid();
                if (!players.containsKey(uuid)) {
                    PlayerInfo playerInfo = new PlayerInfo(packet.getUsername());
                    playerInfo.setCurrentGameServer(packet.getConnectedServer());
                    System.out.println("Player with the username " + packet.getUsername() + " and uuid of " + packet.getUuid().toString() + " joined from " + ctx.channel().remoteAddress().toString() + " to server " + packet.getConnectedServer());
                    players.put(packet.getUuid(), playerInfo);
                }
            }

            @PacketSubscriber
            public void onPacketReceive(InPlayerSwitchServerPacket packet, ChannelHandlerContext ctx) {
                PlayerInfo playerInfo = players.get(packet.getUuid());
                String newServer = packet.getNewServer();
                System.out.println("Player " + playerInfo.getPlayerName() + " switched from server " + playerInfo.getCurrentGameServer() + " to " + newServer);
                playerInfo.setCurrentGameServer(packet.getNewServer());
            }

            @PacketSubscriber
            public void onPacketReceive(InPlayerChatMessagePacket packet, ChannelHandlerContext ctx) {
                PlayerInfo playerInfo = players.get(packet.getUuid());
                String msg = packet.getMsg();
                List<UUID> receivers = packet.getRecievers();

                System.out.println("Player " + playerInfo.getPlayerName() + " sent message '" + msg + "' to " + Arrays.toString(receivers.toArray()));

                for (UUID receiver : receivers) {
                    if (players.containsKey(receiver)) {
                        OutPlayerChatMessagePacket outPacket = new OutPlayerChatMessagePacket();

                        outPacket.setUuid(receiver);
                        outPacket.setMsg(msg);

                        ctx.writeAndFlush(outPacket);
                    }
                }
            }
        });

        this.bootstrap = new ServerBootstrap()
                .option(ChannelOption.AUTO_READ, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .group(parentGroup, workerGroup)
                .childHandler(new ControllerHandler(packetRegistry, eventRegistry))
                .channel(NioServerSocketChannel.class);

        try {
            this.bootstrap.bind(new InetSocketAddress("127.0.0.1", 12345))
                    .awaitUninterruptibly().sync().addListener(doneCallback::accept);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void stop() {
        Logger.info("Stopping server...");

        try {
            parentGroup.shutdownGracefully().get();
            workerGroup.shutdownGracefully().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        Logger.info("Server stopped, Goodbye!");
    }
}