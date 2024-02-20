package dev.farid.skyflock.features.slayer;

import com.mojang.realmsclient.gui.ChatFormatting;
import dev.farid.skyflock.Skyflock;
import dev.farid.skyflock.features.Feature;
import dev.farid.skyflock.utils.LocationUtils;
import dev.farid.skyflock.utils.RenderUtils;
import dev.farid.skyflock.utils.SlayerUtils;
import dev.farid.skyflock.utils.TextUtils;
import dev.farid.skyflock.utils.enums.slayer.SlayerMiniboss;
import kotlin.Pair;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.*;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Map;

public class MinibossHighlight extends Feature {

    private final Map<Entity, Pair<AxisAlignedBB, Color>> minibosses = new HashMap<>();

    public MinibossHighlight() {
        super("Slayer Miniboss Highlight");
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        this.minibosses.clear();
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (!getConfigStatus()) return;
        if (SlayerUtils.slayerBoss == null || !LocationUtils.inSkyblock || mc.theWorld == null)
            return;

        mc.theWorld.getEntities(
                EntityArmorStand.class,
                e -> {
                    SlayerMiniboss sm = SlayerUtils.getMiniboss(e);
                    if (sm == null)
                        return false;

                    // remove if dead
                    String hp = getMiniHpString(e);
                    if (e.isDead || hp.equals("0")) {
                        this.minibosses.computeIfPresent(e, (k, v) -> null);
                    }
                    // else, add
                    else {
                        AxisAlignedBB aabb;

                        if (sm.mobClass.isAssignableFrom(EntityEnderman.class)) {
                            aabb = new AxisAlignedBB(-0.5, -3.2, -0.5, 0.5, 0, 0.5);
                        }
                        else if (sm.mobClass.isAssignableFrom(EntitySpider.class)) {
                            aabb = new AxisAlignedBB(-0.875, -1, -0.875, 0.875, 0, 0.875);
                        }
                        else if (sm.mobClass.isAssignableFrom(EntityWolf.class)) {
                            aabb = new AxisAlignedBB(-0.55, -1.1, -0.55, 0.55, 0, 0.55);
                        }
                        else
                            aabb = new AxisAlignedBB(-0.5, -2.1, -0.5, 0.5, 0, 0.5);

                        this.minibosses.putIfAbsent(e, new Pair<>(aabb, sm.isRare ? Skyflock.config.rareMiniColour : Skyflock.config.normalMiniColour));
                    }

                    return true;
                }
        );
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (!getConfigStatus()) return;
        if (!LocationUtils.inSkyblock || (SlayerUtils.slayerBoss == null && !Skyflock.config.espWhenQuestInactive))
            return;

        try {
            for (Map.Entry<Entity, Pair<AxisAlignedBB, Color>> entry : this.minibosses.entrySet()) {
                // TODO: redo how this works
                // tries to fix error which causes box to be rendered despite entity being dead
                if (!mc.theWorld.loadedEntityList.contains(entry.getKey()))
                    continue;

                AxisAlignedBB aabb = entry.getValue().getFirst();
                Color c = entry.getValue().getSecond();
                double[] coords = RenderUtils.getEntityRenderCoords(entry.getKey(), event.partialTicks);

                if (Skyflock.config.slayerMiniEspType == 0) {
                    Color noAlpha = new Color(c.getRed(), c.getGreen(), c.getBlue(), 255);
                    RenderUtils.Render3D.drawOutlinedBoundingBox(aabb.offset(coords[0], coords[1], coords[2]), noAlpha, 3f, event.partialTicks);
                }
                else
                    RenderUtils.Render3D.drawFilledBoundingBox(aabb.offset(coords[0], coords[1], coords[2]), c, event.partialTicks);
            }
        } catch (ConcurrentModificationException ignored) {}
    }

    public String getMiniHpString(Entity e) {
        String unformatted = TextUtils.removeUnicode(ChatFormatting.stripFormatting(e.getName())).trim();
        String[] split = unformatted.split(" ");

        return split[split.length - 1];
    }

    @Override
    public boolean getConfigStatus() {
        return Skyflock.config.slayerMinibossEsp;
    }
}
