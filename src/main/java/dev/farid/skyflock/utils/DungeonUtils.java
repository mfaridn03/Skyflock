package dev.farid.skyflock.utils;

import dev.farid.skyflock.utils.enums.dungeons.DungeonBoss;
import dev.farid.skyflock.utils.enums.dungeons.DungeonFloor;
import net.minecraft.client.Minecraft;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DungeonUtils {

    public static DungeonFloor currentFloor = null;
    public static DungeonBoss boss = null;

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (!LocationUtils.inDungeons) {
            currentFloor = null;
            return;
        }

        if (currentFloor != null)
            return;

        Object match = TextUtils.getMatchFromLines(
                "The Catacombs \\((E|F[1-7]|M[1-7])\\)",
                PlayerUtils.getScoreboardLines(false),
                null,
                1
        );

        if (match != null)
            currentFloor = DungeonFloor.getFloor((String) match);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST, receiveCanceled = true)
    public void onChat(ClientChatReceivedEvent event) {
        if (!LocationUtils.inDungeons || event.type == (byte) 2)
            return;

        String msg = StringUtils.stripControlCodes(event.message.getUnformattedText());

        // m7, hopefully this works (I've never done m7 before)
        if (msg.startsWith("[BOSS] ") && msg.endsWith("You.. again?") && boss == DungeonBoss.NECRON) {
            boss = DungeonBoss.WITHER_KING;
            return;
        }

        if (msg.startsWith("[BOSS] ")) {
            Object match = TextUtils.getMatchFromLines(
                    "\\[BOSS] (.+): .+",
                    Collections.singletonList(msg),
                    null,
                    1
            );
            if (match != null) {
                DungeonBoss b = DungeonBoss.getBoss((String) match);
                if (b == null) // nullify boss only on worldLoad event
                    return;

                // only set boss if previous boss is null EXCEPT on f/m7
                if (boss == null)
                    boss = b;

                // maxor -> storm
                else if (boss == DungeonBoss.MAXOR && b == DungeonBoss.STORM)
                    boss = b;

                // storm -> goldor
                else if (boss == DungeonBoss.STORM && b == DungeonBoss.GOLDOR)
                    boss = b;

                // goldor -> necron
                else if (boss == DungeonBoss.GOLDOR && b == DungeonBoss.NECRON)
                    boss = b;

                // necron -> p5
                else if (boss == DungeonBoss.NECRON && b == DungeonBoss.WITHER_KING)
                    boss = b;
            }
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        boss = null;
    }
}
