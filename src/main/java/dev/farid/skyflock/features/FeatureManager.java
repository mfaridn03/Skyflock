package dev.farid.skyflock.features;

import dev.farid.skyflock.features.qol.NoEntityDeath;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.List;

public class FeatureManager {

    private final List<Feature> features = new ArrayList<>();
    private boolean initialised;

    public FeatureManager() {
        initialised = false;

        // qol
        this.features.add(new NoEntityDeath());
    }

    public void init() {
        if (initialised)
            return;

        for (Feature f : this.features) {
            MinecraftForge.EVENT_BUS.register(f);
        }
        initialised = true;
    }

    @SubscribeEvent
    public void updateStatus(TickEvent.ClientTickEvent event) {
        if (!initialised)
            return;

        for (Feature f : this.features) {
            // do nothing if setting is still the same as last time
            if (f.getConfigStatus() == f.lastStatus)
                continue;

            // lastStatus = false, currentStatus = true -> onEnable
            if (f.getConfigStatus()) {
                f.lastStatus = true;
                f.onEnable();
            }
            // lastStatus = true, currentStatus = false -> onDisable
            else {
                f.lastStatus = false;
                f.onDisable();
            }
        }
    }
}