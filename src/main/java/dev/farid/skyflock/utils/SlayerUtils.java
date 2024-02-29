package dev.farid.skyflock.utils;

import com.mojang.realmsclient.gui.ChatFormatting;
import dev.farid.skyflock.utils.enums.slayer.SlayerBoss;
import dev.farid.skyflock.utils.enums.slayer.SlayerMiniboss;
import dev.farid.skyflock.utils.enums.slayer.SlayerTier;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SlayerUtils {

    public static SlayerBoss slayerBoss = null;
    public static SlayerTier slayerTier = null;
    public static EntityArmorStand bossArmorStand = null;
    public static boolean isFighting = false;
    public static List<String> carriedPlayers = new ArrayList<>();
    private static final Minecraft mc = Minecraft.getMinecraft();
    private static final String SLAYER_REGEX = "(Revenant Horror|Tarantula Broodfather|Sven Packmaster|Voidgloom Seraph|Riftstalker Bloodfiend|Inferno Demonlord) (I|II|III|IV|V)(?!\\w)";
    private static final String BLAZE_ATTUNEMENT_REGEX = "(ASHEN|SPIRIT|AURIC|CRYSTAL) ♨.+";

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (!LocationUtils.inSkyblock)
            return;

        List<String> scoreboard = PlayerUtils.getScoreboardLines(false);
        Object match = TextUtils.getMatchFromLines(SLAYER_REGEX, scoreboard, null, 0);
        Object fightingMatch = TextUtils.getMatchFromLines("Slay the boss", scoreboard, null, 0);

        isFighting = fightingMatch != null;

        if (match == null) {
            slayerBoss = null;
            slayerTier = null;
            bossArmorStand = null;
            return;
        }

        SlayerBoss boss = SlayerBoss.getBoss((String) match);
        if (boss != null)
            slayerBoss = boss;

        SlayerTier tier = SlayerTier.getTierFromBossString((String) match);
        if (tier != null)
            slayerTier = tier;

        if (slayerBoss == null)
            return;

        // get boss armour stand
        if (!isFighting) {
            bossArmorStand = null;
            return;
        }
        List<EntityArmorStand> stands = mc.theWorld.getEntities(
                EntityArmorStand.class,
                e -> {
                    if (slayerBoss == SlayerBoss.BLAZE) {
                        // find the emoji and attunement
                        // buggy if the boss spawns when you die/far away
                        String n = ChatFormatting.stripFormatting(e.getName());
                        return isFighting && TextUtils.getMatchFromLines(BLAZE_ATTUNEMENT_REGEX, Collections.singletonList(n), null, 0) != null;
                    }
                    else {
                        return e.getName().contains("Spawned by:") && e.getName().contains(mc.thePlayer.getName());
                    }
                }
        );
        if (stands.isEmpty()) {
            bossArmorStand = null;
            return;
        }
        if (stands.size() > 1)
            stands.sort(Comparator.comparingDouble(e -> e.getDistanceToEntity(mc.thePlayer)));

        bossArmorStand = stands.get(0);
    }

    public static SlayerMiniboss getMiniboss(EntityArmorStand armorStand) {
        return SlayerMiniboss.getMiniboss(armorStand.getName());
    }

    public static void addCarry(String name) {
        carriedPlayers.add(name);
    }

    public static void removeCarry(String name) {
        carriedPlayers.remove(name);
    }
}
