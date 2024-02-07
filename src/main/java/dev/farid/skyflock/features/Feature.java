package dev.farid.skyflock.features;

import net.minecraft.client.Minecraft;

public abstract class Feature {

    protected final Minecraft mc = Minecraft.getMinecraft();
    public final String name;
    public boolean lastStatus; // for onEnable, onDisable

    public Feature(String name) {
        this.name = name;
        this.lastStatus = false;
    }

    public void onEnable() {}
    public void onDisable() {}

    public abstract boolean getConfigStatus();
}
