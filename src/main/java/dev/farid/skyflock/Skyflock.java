package dev.farid.skyflock;


import dev.farid.skyflock.command.CommandManager;
import dev.farid.skyflock.command.commands.ConfigCommand;
import dev.farid.skyflock.command.commands.ConfigCommandAlias;
import dev.farid.skyflock.config.SkyflockConfig;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
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
    public static SkyflockConfig config;

    @Mod.Instance(MODID)
    public static Skyflock instance;

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {

        // init config
        config = new SkyflockConfig();

        // init commands
        commandManager.add(new ConfigCommand());
        commandManager.add(new ConfigCommandAlias());
        commandManager.registerAll();

        MinecraftForge.EVENT_BUS.register(this);
        printLogs(null, "Successfully loaded commands", false);
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (config.test)
            printLogs(null, "Working", false);
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
