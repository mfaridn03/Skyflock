package dev.farid.skyflock.features.slayer;

import dev.farid.skyflock.Skyflock;
import dev.farid.skyflock.events.PacketEvent;
import dev.farid.skyflock.events.RenderLivingEvent;
import dev.farid.skyflock.features.Feature;
import dev.farid.skyflock.utils.RenderUtils;
import dev.farid.skyflock.utils.SlayerUtils;
import dev.farid.skyflock.utils.enums.slayer.SlayerBoss;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.network.play.server.S2APacketParticles;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.Comparator;
import java.util.List;

public class BlazeSlayerFeatures extends Feature {

    private EntityBlaze bossBlaze = null;

    public BlazeSlayerFeatures() {
        super("Blaze Slayer Features");
    }

    @SubscribeEvent
    public void onRenderLiving(RenderLivingEvent.Pre event) {
        if (!Skyflock.config.hideNearbyBlaze) return;
        if (mc.thePlayer == null || mc.theWorld == null) return;
        if (SlayerUtils.slayerBoss != SlayerBoss.BLAZE || SlayerUtils.bossArmorStand == null) return;

        if (event.entity.getDistanceToEntity(SlayerUtils.bossArmorStand) < Skyflock.config.blazeHideDistance) {
            // blaze mod
            if (event.entity instanceof EntityBlaze && event.entity != this.bossBlaze) {
                event.setCanceled(true);
                return;
            }
            // blaze armor stand check
            if (event.entity instanceof EntityArmorStand && event.entity.getName().contains(" Blaze") && event.entity.getName().contains("❤"))
                event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (!Skyflock.config.colourBlazeBoss) return;
        if (mc.thePlayer == null || mc.theWorld == null) return;
        if (SlayerUtils.slayerBoss != SlayerBoss.BLAZE || SlayerUtils.bossArmorStand == null) return;

        String name = SlayerUtils.bossArmorStand.getName();
        Color c1;
        if (name.contains("ASHEN"))
            c1 = Color.GRAY;
        else if (name.contains("SPIRIT"))
            c1 = Color.WHITE;
        else if (name.contains("AURIC"))
            c1 = Color.YELLOW;
        else
            c1 = Color.CYAN;

        Color c2 = new Color(c1.getRed(), c1.getBlue(), c1.getGreen(), (int) (255 * Skyflock.config.boxTransparency));

        // find the closest blaze to armor stand
        List<EntityCreature> blazes = mc.theWorld.getEntitiesWithinAABB(
                EntityCreature.class,
                SlayerUtils.bossArmorStand.getEntityBoundingBox().expand(1, 1, 1),
                e -> e instanceof EntityCreature && !e.isDead
        );
        if (blazes.isEmpty())
            return;

        blazes.sort(Comparator.comparingDouble(e -> e.getDistance(
                SlayerUtils.bossArmorStand.posX,
                SlayerUtils.bossArmorStand.posY - 2.2,
                SlayerUtils.bossArmorStand.posZ
        )));

        this.bossBlaze = (EntityBlaze) blazes.get(0);

        RenderUtils.Render3D.drawFilledBoundingBox(
                RenderUtils.getEntityRenderAABB(this.bossBlaze, event.partialTicks).expand(0.26, 0.2, 0.26),
                c2,
                event.partialTicks
        );
    }

    @SubscribeEvent
    public void onPacket(PacketEvent.Incoming event) {
        if (!Skyflock.config.hideNearbyParticles || mc.thePlayer == null || mc.theWorld == null) return;
        if (!(event.packet instanceof S2APacketParticles)) return;
        if (SlayerUtils.slayerBoss != SlayerBoss.BLAZE || SlayerUtils.bossArmorStand == null) return;

        S2APacketParticles particlePacket = (S2APacketParticles) event.packet;
        double dist = SlayerUtils.bossArmorStand.getDistance(
                particlePacket.getXCoordinate(),
                particlePacket.getYCoordinate(),
                particlePacket.getZCoordinate()
        );

        if (dist < Skyflock.config.particleHideDistance)
            event.setCanceled(true);
    }

    @Override
    public boolean getConfigStatus() {
        return Skyflock.config.hideNearbyBlaze || Skyflock.config.hideNearbyParticles || Skyflock.config.colourBlazeBoss;
    }
}
