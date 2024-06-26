package dev.farid.skyflock.utils;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.List;

public class LocationUtils {

    private static final Minecraft mc = Minecraft.getMinecraft();
    public static boolean inSkyblock = false;
    public static boolean inDungeons = false;
    public static String location = null;
    public static String area = null;

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        inSkyblock = checkSkyblock();
        inDungeons = inSkyblock && checkDungeons();
        location = getLocation();
        area = getArea();
    }

    private boolean checkSkyblock() {
        if (mc.theWorld == null || mc.thePlayer == null)
            return false;

        ScoreObjective so = mc.thePlayer.getWorldScoreboard().getObjectiveInDisplaySlot(1);
        if (so == null) {
            return false;
        }

        return ChatFormatting.stripFormatting(so.getDisplayName()).contains("SKYBLOCK");
    }

    private boolean checkDungeons() {
        // assumes inSkyblock is true
        Object match = TextUtils.getMatchFromLines(
                "(The Catacombs)|(Cleared\\:)",
                PlayerUtils.getScoreboardLines(false),
                null,
                0
        );
        return match != null;
    }

    private String getLocation() {
        if (!inSkyblock) return null;

        List<String> lines = PlayerUtils.getScoreboardLines(true);
        if (lines != null) {
            for (String line : lines) {
                if (line.contains("⏣")) {
                    line = TextUtils.removeUnicode(ChatFormatting.stripFormatting(line));
                    return line.trim();
                }
            }
        }
        return null;
    }

    private String getArea() {
        if (!inSkyblock) return null;
        return null;
    }
}
