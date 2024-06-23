package dev.farid.skyflock.config;

import dev.farid.skyflock.Skyflock;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;

import java.util.ArrayList;
import java.util.List;

public class KeybindManager {

    private final List<KeyBinding> keyBinds = new ArrayList<>();

    public KeybindManager() {
    }

    public void init() {
        for (KeyBinding k : this.keyBinds) {
            ClientRegistry.registerKeyBinding(k);
            Skyflock.printLogs(null, "Keybind registered: " + k.getKeyDescription(), false);
        }
        Skyflock.printLogs(null, "Keybinds registered", false);
    }
}
