package dev.farid.skyflock.features.qol;

import dev.farid.skyflock.Skyflock;
import dev.farid.skyflock.features.Feature;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class NoEntityDeath extends Feature {

    public NoEntityDeath() {
        super("No Entity Death");
    }

    @SubscribeEvent
    public void onEntityDeath(LivingDeathEvent event) {
        if (!getConfigStatus())
            return;

        event.entity.setDead();
    }

    @Override
    public boolean getConfigStatus() {
        return Skyflock.config.noDeathAnim;
    }
}
