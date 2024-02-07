package dev.farid.skyflock.utils;

import com.mojang.realmsclient.gui.ChatFormatting;
import gg.essential.universal.UChat;
import net.minecraft.client.Minecraft;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class LocationUtils {

    private static final Minecraft mc = Minecraft.getMinecraft();
    public static boolean inSkyblock = false;
    public static boolean inDungeons = false;

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        inSkyblock = checkSkyblock();
        inDungeons = inSkyblock && checkDungeons();
    }

    private boolean checkSkyblock() {
        if (mc.theWorld == null || mc.thePlayer == null)
            return false;

        ScoreObjective so = mc.thePlayer.getWorldScoreboard().getObjectiveInDisplaySlot(1);
        if (so == null) {
            if (inSkyblock)
                UChat.chat("Exited skyblock");
            return false;
        }

        boolean b = ChatFormatting.stripFormatting(so.getDisplayName()).contains("SKYBLOCK");
        if (b && !inSkyblock)
            UChat.chat("Entered skyblock");
        else if (!b && inSkyblock)
            UChat.chat("Exited skyblock");

        return b;
    }

    private boolean checkDungeons() {
        // assumes inSkyblock is true
        Object match = TextUtils.getMatchFromLines(
                "(The Catacombs)|(Cleared:)",
                PlayerUtils.getScoreboardLines(false),
                null
        );
        return match != null;
    }
}
