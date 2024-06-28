package dev.farid.skyflock.features.kuudra;

import dev.farid.skyflock.Skyflock;
import dev.farid.skyflock.events.RenderLivingEvent;
import dev.farid.skyflock.features.Feature;
import dev.farid.skyflock.utils.LocationUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.util.HashSet;
import java.util.Set;

public class MobOpacity extends Feature {

    public final Set<Class<? extends Entity>> validMobClasses = new HashSet<>();

    public MobOpacity() {
        super("Kuudra Mob Opacity");

        // zombie: most mobs + knockers (EntityPigZombie included)
        // magma cube: slasher, follower, inferno cubes
        // wither: sentry
        // golem: golem
        // silverfish: chaos mites
        this.validMobClasses.add(EntityZombie.class);
        this.validMobClasses.add(EntityMagmaCube.class);
        this.validMobClasses.add(EntityWither.class);
        this.validMobClasses.add(EntityIronGolem.class);
        this.validMobClasses.add(EntitySilverfish.class);
    }

    @SubscribeEvent
    public void onPreRenderLivingEvent(RenderLivingEvent.Pre event) {
        // TODO: option to replace with outline opacity maybe?
        if (getConfigStatus() && LocationUtils.inKuudra && mobCheck(event.entity)) {
            if (Skyflock.config.kuudraMobOpacity < 0.05f) {
                event.setCanceled(true);
                return;
            }

            // https://github.com/hannibal002/SkyHanni/blob/beta/src/main/java/at/hannibal2/skyhanni/features/event/hoppity/HoppityEggDisplayManager.kt#L30
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GlStateManager.color(1f, 1f, 1f, Skyflock.config.kuudraMobOpacity);
        }
    }

    @SubscribeEvent
    public void onPostRenderLivingEvent(RenderLivingEvent.Post event) {
        if (Skyflock.config.kuudraMobOpacity < 0.05f) return;
        if (getConfigStatus() && mobCheck(event.entity)) {
            GlStateManager.color(1f, 1f, 1f, 1f);
            GlStateManager.disableBlend();
        }
    }

    public boolean mobCheck(Entity e) {
        return this.validMobClasses.contains(e.getClass()) && e.getDistance(-101.5, e.posY, -105.5) < 10;
        // return this.validMobClasses.contains(e.getClass()) && e.getDistanceToEntity(mc.thePlayer) < 9; // DEBUG
    }

    @Override
    public boolean getConfigStatus() {
        return Skyflock.config.kuudraMobOpacity <= 0.95f;
    }
}
