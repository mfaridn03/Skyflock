package dev.farid.skyflock;


import dev.farid.skyflock.command.CommandManager;
import dev.farid.skyflock.config.SkyflockConfig;
import dev.farid.skyflock.features.FeatureManager;
import dev.farid.skyflock.utils.DungeonUtils;
import dev.farid.skyflock.utils.LocationUtils;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

@Mod(modid = Skyflock.MODID, name = Skyflock.MODNAME, version = Skyflock.VERSION, clientSideOnly = true)
public class Skyflock {
    public static final String MODID = "skyflock";
    public static final String MODNAME = "Skyflock";
    public static final String VERSION = "1.0.0";
    public static final Logger LOGGER = LogManager.getLogger(MODID);
    public static final CommandManager commandManager = new CommandManager();
    public static final FeatureManager featureManager = new FeatureManager();
    public static SkyflockConfig config;

    @Mod.Instance(MODID)
    public static Skyflock instance;

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        config = new SkyflockConfig();

        featureManager.init();
        commandManager.init();

        registerEvents(
                this,
                new LocationUtils(),
                new DungeonUtils()
        );
    }

    public void registerEvents(Object... objects) {
        for (Object o : objects) {
            MinecraftForge.EVENT_BUS.register(o);
            printLogs(null, "Successfully registered to event bus: " + o.toString(), false);
        }
    }

    public static void printLogs(@Nullable EntityPlayerSP player, String message, boolean playSound) {
        String realMessage = ">> " + MODNAME + ": " + message;

        if (player != null) {
            player.addChatMessage(new ChatComponentText(realMessage));

            if (playSound) {
                player.playSound("game.player.hurt", 1, 1);
            }
        }
        LOGGER.info(realMessage);
    }
}
