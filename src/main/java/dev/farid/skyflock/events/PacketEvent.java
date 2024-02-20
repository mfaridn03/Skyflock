package dev.farid.skyflock.events;

import net.minecraft.network.Packet;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

public class PacketEvent extends Event {

    public final Packet<?> packet;

    public PacketEvent(Packet<?> p) {
        this.packet = p;
    }

    @Cancelable
    public static class Incoming extends PacketEvent{

        public Incoming(Packet<?> packet) {
            super(packet);
        }
    }

    @Cancelable
    public static class Outgoing extends PacketEvent{

        public Outgoing(Packet<?> packet) {
            super(packet);
        }
    }
}
