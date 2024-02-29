package dev.farid.skyflock.features;

import dev.farid.skyflock.features.dungeons.LividHelper;
import dev.farid.skyflock.features.dungeons.MinibossHp;
import dev.farid.skyflock.features.misc.AutoWave;
import dev.farid.skyflock.features.misc.HideFireSale;
import dev.farid.skyflock.features.qol.NoEntityDeath;
import dev.farid.skyflock.features.slayer.ArrowToBoss;
import dev.farid.skyflock.features.slayer.BlazeSlayerFeatures;
import dev.farid.skyflock.features.slayer.CarryHelper;
import dev.farid.skyflock.features.slayer.MinibossHighlight;
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

        // dungeons
        this.features.add(new MinibossHp());
        this.features.add(new LividHelper());

        // slayer
        this.features.add(new MinibossHighlight());
        this.features.add(new BlazeSlayerFeatures());
        this.features.add(new ArrowToBoss());
        this.features.add(new CarryHelper());

        // fun
        this.features.add(new AutoWave());
        this.features.add(new HideFireSale());
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
