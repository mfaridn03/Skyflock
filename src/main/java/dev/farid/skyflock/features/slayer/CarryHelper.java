package dev.farid.skyflock.features.slayer;

import dev.farid.skyflock.Skyflock;
import dev.farid.skyflock.features.Feature;
import dev.farid.skyflock.utils.RenderUtils;
import dev.farid.skyflock.utils.SlayerUtils;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.ConcurrentModificationException;
import java.util.List;

public class CarryHelper extends Feature {
    public CarryHelper() {
        super("Slayer Carry Helper");
        // TODO: boss health display for each carry, spawn status, maybe a tracer for something relevant
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (!getConfigStatus() || SlayerUtils.carriedPlayers.isEmpty() || mc.theWorld == null) return;

        try {
            for (String name : SlayerUtils.carriedPlayers) {
                List<EntityOtherPlayerMP> pList = mc.theWorld.getEntities(
                        EntityOtherPlayerMP.class,
                        e -> e.getName().equalsIgnoreCase(name)
                );
                if (pList.isEmpty()) continue;

                EntityOtherPlayerMP player = pList.get(0);
                if (player == null) continue;

                RenderUtils.Render3D.drawOutlinedBoundingBox(player, Color.GREEN, 2f, event.partialTicks, true);
                // render nametag
                Vec3 target = RenderUtils.getEntityRenderCoords(player, event.partialTicks);
                RenderUtils.Render3D.drawString(
                        name,
                        target.addVector(0, player.height + 0.75D, 0),
                        Color.WHITE,
                        1f,
                        true,
                        event.partialTicks,
                        true
                );
            }
        }
        catch (Exception ignored) {}
    }

    @Override
    public boolean getConfigStatus() {
        return Skyflock.config.highlightCarried;
    }
}
