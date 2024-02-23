package dev.farid.skyflock.features.slayer;

import dev.farid.skyflock.Skyflock;
import dev.farid.skyflock.features.Feature;
import dev.farid.skyflock.utils.RenderUtils;
import dev.farid.skyflock.utils.SlayerUtils;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ArrowToBoss extends Feature {

    public ArrowToBoss() {
        super("Arrow To Boss");
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (!getConfigStatus()) return;
        if (SlayerUtils.bossArmorStand == null || !SlayerUtils.isFighting || mc.thePlayer == null) return;

        RenderUtils.Render3D.drawArrowToEntity(
                SlayerUtils.bossArmorStand,
                Skyflock.config.bossPointerColour,
                Skyflock.config.bossPointerThickness,
                Math.min(3D, SlayerUtils.bossArmorStand.getDistance(mc.thePlayer.posX, SlayerUtils.bossArmorStand.posY, mc.thePlayer.posZ)),
                event.partialTicks
        );
    }

    @Override
    public boolean getConfigStatus() {
        return Skyflock.config.pointToBoss;
    }
}
