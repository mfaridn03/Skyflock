package dev.farid.skyflock.utils;

import gg.essential.universal.UChat;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DungeonUtils {

    private static final Minecraft mc = Minecraft.getMinecraft();
    private static final List<String> bossNames = Arrays.asList(
            "Bonzo",
            "Scarf",
            "The Professor",
            "Thorn",
            "Livid",
            "Sadan",
            "Necron"
    );
    public static String currentFloor = null;
    public static Boss boss = null;

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
            currentFloor = (String) match;
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST, receiveCanceled = true)
    public void onChat(ClientChatReceivedEvent event) {
        if (!LocationUtils.inDungeons || event.type == (byte) 2)
            return;

        String msg = StringUtils.stripControlCodes(event.message.getUnformattedText());

        // m7, hopefully this works (I've never done m7 before)
        if (msg.startsWith("[BOSS] ") && msg.endsWith("You.. again?") && boss == Boss.NECRON) {
            boss = Boss.WITHER_KING;
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
                Boss b = Boss.getBoss((String) match);
                if (b != null) // nullify boss only on worldLoad event
                    boss = b;
            }
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        boss = null;
    }

    public enum Boss {

        BONZO("Bonzo"),
        SCARF("Scarf"),
        PROFESSOR("The Professor"),
        THORN("Thorn"),
        LIVID("Livid"),
        SADAN("Sadan"),
        // f7 stuff
        MAXOR("Maxor"),
        STORM("Storm"),
        GOLDOR("Goldor"),
        NECRON("Necron"),
        // No m7 features yet but hopefully this works
        WITHER_KING("The Wither King");
        private final String name;

        Boss(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }

        public static Boss getBoss(String name) {
            for (Boss b : Boss.values()) {
                if (b.getName().equals(name))
                    return b;
            }
            return null;
        }
    }
}
