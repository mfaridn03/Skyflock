package dev.farid.skyflock.utils;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;

import java.util.ArrayList;
import java.util.List;

public class PlayerUtils {

    private static final Minecraft mc = Minecraft.getMinecraft();

    /**
     * Retrieves the lines of the scoreboard.
     *
     * @param formatted If false, formatting will be stripped
     * @return A list of strings representing the lines of the scoreboard
     * */
    public static List<String> getScoreboardLines(boolean formatted) {
        if (mc.thePlayer == null)
            return null;

        Scoreboard sb = mc.thePlayer.getWorldScoreboard();
        if (sb.getObjectiveInDisplaySlot(1) == null)
            return null;

        List<String> lines = new ArrayList<>(0);
        ScoreObjective so = sb.getObjectiveInDisplaySlot(1);
        for (Score score : sb.getSortedScores(so)) {
            String temp = ScorePlayerTeam.formatPlayerName(sb.getPlayersTeam(score.getPlayerName()), score.getPlayerName());
            String temp2 = formatted ? temp : ChatFormatting.stripFormatting(temp);
            // TODO: separate check to include unicode or not
            lines.add(formatted ? temp2 : TextUtils.removeUnicode(temp2));
        }
        return lines;
    }

    public static List<String> getTablist(boolean formatted) {
        if (mc.thePlayer == null) return null;
        List<String> e = new ArrayList<>();

        for (NetworkPlayerInfo info : mc.thePlayer.sendQueue.getPlayerInfoMap()) {
            String name = mc.ingameGUI.getTabList().getPlayerName(info);

            if (!formatted)
                name = ChatFormatting.stripFormatting(name);
            e.add(name);
        }

        return e;
    }

    public static boolean inGuiChest() {
        if (mc.thePlayer == null || mc.theWorld == null) return false;
        return mc.currentScreen instanceof GuiChest;
    }

    public static String getCurrentChestName(boolean formatted) {
        if (!inGuiChest()) return null;

        IInventory c = ((ContainerChest) ((GuiChest) mc.currentScreen).inventorySlots).getLowerChestInventory();
        return formatted ? c.getDisplayName().getFormattedText() : ChatFormatting.stripFormatting(c.getDisplayName().getFormattedText());
    }

    public static IInventory getCurrentChestInventory() {
        if (!inGuiChest()) return null;
        return ((ContainerChest) ((GuiChest) mc.currentScreen).inventorySlots).getLowerChestInventory();
    }
}
