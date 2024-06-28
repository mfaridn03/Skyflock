package dev.farid.skyflock.features.kuudra;

import dev.farid.skyflock.Skyflock;
import dev.farid.skyflock.features.Feature;
import dev.farid.skyflock.utils.LocationUtils;
import dev.farid.skyflock.utils.RenderUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;

public class KuudraWaypoints extends Feature {

    private final AxisAlignedBB brickStunAABB = new AxisAlignedBB(
            -154, 29, -172, -153, 30, -171
    );

    public KuudraWaypoints() {
        super("Kuudra Waypoints");
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (getConfigStatus() && LocationUtils.inKuudra) {
            GlStateManager.disableDepth();
            RenderUtils.Render3D.drawOutlinedBoundingBox(this.brickStunAABB, Color.WHITE, 2, event.partialTicks, true);
            RenderUtils.Render3D.drawFilledBoundingBox(this.brickStunAABB, new Color(255, 255, 255, 100), event.partialTicks, true);
            GlStateManager.enableDepth();
        }
    }

    @Override
    public boolean getConfigStatus() {
        return Skyflock.config.brickStunWaypoint;
    }
}
