package dev.farid.skyflock.events;

import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

public class RenderLivingEvent extends Event {

    public final EntityLivingBase entity;
    public final double x, y, z;
    public final float entityYaw, partialTicks;

    public RenderLivingEvent(EntityLivingBase entity, double x, double y, double z, float entityYaw, float partialTicks) {
        this.entity = entity;
        this.x = x;
        this.y = y;
        this.z = z;
        this.entityYaw = entityYaw;
        this.partialTicks = partialTicks;
    }

    @Cancelable
    public static class Pre extends RenderLivingEvent {

        public Pre(EntityLivingBase entity, double x, double y, double z, float entityYaw, float partialTicks) {
            super(entity, x, y, z, entityYaw, partialTicks);
        }
    }

    public static class Post extends RenderLivingEvent {

        public Post(EntityLivingBase entity, double x, double y, double z, float entityYaw, float partialTicks) {
            super(entity, x, y, z, entityYaw, partialTicks);
        }
    }
}
