

package net.skydecade.protocol.registry;

import net.skydecade.protocol.Packet;
import net.skydecade.protocol.packets.PacketHandshake;
import net.skydecade.protocol.packets.login.*;
import net.skydecade.protocol.packets.play.PacketKeepAlive;
import net.skydecade.protocol.packets.play.PacketPluginMessage;
import net.skydecade.protocol.packets.status.PacketStatusPing;
import net.skydecade.protocol.packets.status.PacketStatusRequest;
import net.skydecade.protocol.packets.status.PacketStatusResponse;

import java.util.*;
import java.util.function.Supplier;

import static net.skydecade.protocol.registry.Version.*;

public enum State {

    HANDSHAKING(0) {
        {
            serverBound.register(PacketHandshake::new,
                    map(0x00, Version.getMin(), Version.getMax())
            );
        }
    },
    STATUS(1) {
        {
            serverBound.register(PacketStatusRequest::new,
                    map(0x00, Version.getMin(), Version.getMax())
            );
            serverBound.register(PacketStatusPing::new,
                    map(0x01, Version.getMin(), Version.getMax())
            );
            clientBound.register(PacketStatusResponse::new,
                    map(0x00, Version.getMin(), Version.getMax())
            );
            clientBound.register(PacketStatusPing::new,
                    map(0x01, Version.getMin(), Version.getMax())
            );
        }
    },
    LOGIN(2) {
        {
            serverBound.register(PacketLoginStart::new,
                    map(0x00, Version.getMin(), Version.getMax())
            );
            serverBound.register(PacketLoginPluginResponse::new,
                    map(0x02, Version.getMin(), Version.getMax())
            );
            clientBound.register(PacketDisconnect::new,
                    map(0x00, Version.getMin(), Version.getMax())
            );
            clientBound.register(PacketLoginSuccess::new,
                    map(0x02, Version.getMin(), Version.getMax())
            );
            clientBound.register(PacketLoginPluginRequest::new,
                    map(0x04, Version.getMin(), Version.getMax())
            );
        }
    },
    PLAY(3) {
        {
            serverBound.register(PacketKeepAlive::new,
                    map(0x00, V1_7_2, V1_8),
                    map(0x0B, V1_9, V1_11_1),
                    map(0x0C, V1_12, V1_12),
                    map(0x0B, V1_12_1, V1_12_2),
                    map(0x0E, V1_13, V1_13_2),
                    map(0x0F, V1_14, V1_15_2),
                    map(0x10, V1_16, V1_16_4),
                    map(0x0F, V1_17, V1_18_2),
                    map(0x11, V1_19, V1_19),
                    map(0x12, V1_19_1, V1_19_1),
                    map(0x11, V1_19_3, V1_19_3),
                    map(0x12, V1_19_4, V1_20)
            );
            clientBound.register(PacketPluginMessage::new,
                    map(0x19, V1_13, V1_13_2),
                    map(0x18, V1_14, V1_14_4),
                    map(0x19, V1_15, V1_15_2),
                    map(0x18, V1_16, V1_16_1),
                    map(0x17, V1_16_2, V1_16_4),
                    map(0x18, V1_17, V1_18_2),
                    map(0x15, V1_19, V1_19),
                    map(0x16, V1_19_1, V1_19_1),
                    map(0x15, V1_19_3, V1_19_3),
                    map(0x17, V1_19_4, V1_20)
            );
            clientBound.register(PacketKeepAlive::new,
                    map(0x00, V1_7_2, V1_8),
                    map(0x1F, V1_9, V1_12_2),
                    map(0x21, V1_13, V1_13_2),
                    map(0x20, V1_14, V1_14_4),
                    map(0x21, V1_15, V1_15_2),
                    map(0x20, V1_16, V1_16_1),
                    map(0x1F, V1_16_2, V1_16_4),
                    map(0x21, V1_17, V1_18_2),
                    map(0x1E, V1_19, V1_19),
                    map(0x20, V1_19_1, V1_19_1),
                    map(0x1F, V1_19_3, V1_19_3),
                    map(0x23, V1_19_4, V1_20)
            );
        }
    };

    private static final Map<Integer, State> STATE_BY_ID = new HashMap<>();

    static {
        for (State registry : values()) {
            STATE_BY_ID.put(registry.stateId, registry);
        }
    }

    private final int stateId;
    public final ProtocolMappings serverBound = new ProtocolMappings();
    public final ProtocolMappings clientBound = new ProtocolMappings();

    State(int stateId) {
        this.stateId = stateId;
    }

    public static State getById(int stateId) {
        return STATE_BY_ID.get(stateId);
    }

    public static class ProtocolMappings {

        private final Map<Version, PacketRegistry> registry = new HashMap<>();

        public PacketRegistry getRegistry(Version version) {
            return registry.getOrDefault(version, registry.get(getMin()));
        }

        public void register(Supplier<?> packet, Mapping... mappings) {
            for (Mapping mapping : mappings) {
                for (Version ver : getRange(mapping)) {
                    PacketRegistry reg = registry.computeIfAbsent(ver, PacketRegistry::new);
                    reg.register(mapping.packetId, packet);
                }
            }
        }

        private Collection<Version> getRange(Mapping mapping) {
            Version from = mapping.from;
            Version curr = mapping.to;

            if (curr == from)
                return Collections.singletonList(from);

            List<Version> versions = new LinkedList<>();

            while (curr != from) {
                versions.add(curr);
                curr = curr.getPrev();
            }

            versions.add(from);

            return versions;
        }

    }

    public static class PacketRegistry {

        private final Version version;
        private final Map<Integer, Supplier<?>> packetsById = new HashMap<>();
        private final Map<Class<?>, Integer> packetIdByClass = new HashMap<>();

        public PacketRegistry(Version version) {
            this.version = version;
        }

        public Version getVersion() {
            return version;
        }

        public Packet getPacket(int packetId) {
            Supplier<?> supplier = packetsById.get(packetId);
            return supplier == null ? null : (Packet) supplier.get();
        }

        public int getPacketId(Class<?> packetClass) {
            return packetIdByClass.getOrDefault(packetClass, -1);
        }

        public void register(int packetId, Supplier<?> supplier) {
            packetsById.put(packetId, supplier);
            packetIdByClass.put(supplier.get().getClass(), packetId);
        }

    }

    private static class Mapping {

        private final int packetId;
        private final Version from;
        private final Version to;

        public Mapping(int packetId, Version from, Version to) {
            this.from = from;
            this.to = to;
            this.packetId = packetId;
        }
    }

    /**
     * Map packet id to version range
     * @param packetId Packet id
     * @param from Minimal version (include)
     * @param to Last version (include)
     * @return Created mapping
     */
    private static Mapping map(int packetId, Version from, Version to) {
        return new Mapping(packetId, from, to);
    }

}
