package net.skydecade.protocol.registry;

import net.skydecade.protocol.Packet;
import net.skydecade.protocol.exception.PacketRegistrationException;

import java.lang.reflect.InvocationTargetException;

public interface IPacketRegistry {

    void registerPacket(int packetId, Packet packet) throws PacketRegistrationException;

    void registerPacket(int packetId, Class<? extends Packet> packet) throws PacketRegistrationException;

    int getPacketId(Class<? extends Packet> packetClass);

    <T extends Packet> T constructPacket(int packetId) throws InvocationTargetException, InstantiationException, IllegalAccessException;

    boolean containsPacketId(int id);

}