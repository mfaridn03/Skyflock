package dev.farid.skyflock.utils;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
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
            lines.add(TextUtils.removeUnicode(temp2));
        }
        return lines;
    }
}
