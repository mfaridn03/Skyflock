package dev.farid.skyflock.utils;

import dev.farid.skyflock.utils.enums.slayer.SlayerBoss;
import dev.farid.skyflock.utils.enums.slayer.SlayerMiniboss;
import dev.farid.skyflock.utils.enums.slayer.SlayerTier;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.List;

public class SlayerUtils {

    public static SlayerBoss slayerBoss = null;
    public static SlayerTier slayerTier = null;
    private static final Minecraft mc = Minecraft.getMinecraft();
    private static final String SLAYER_REGEX = "(Revenant Horror|Tarantula Broodfather|Sven Packmaster|Voidgloom Seraph|Riftstalker Bloodfiend|Inferno Demonlord) (I|II|III|IV|V)(?!\\w)";

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (!LocationUtils.inSkyblock)
            return;

        List<String> scoreboard = PlayerUtils.getScoreboardLines(false);
        Object match = TextUtils.getMatchFromLines(SLAYER_REGEX, scoreboard, null, 0);

        if (match == null) {
            slayerBoss = null;
            slayerTier = null;
            return;
        }

        SlayerBoss boss = SlayerBoss.getBoss((String) match);
        if (boss != null)
            slayerBoss = boss;

        SlayerTier tier = SlayerTier.getTierFromBossString((String) match);
        if (tier != null)
            slayerTier = tier;
    }

    public static SlayerMiniboss getMiniboss(EntityArmorStand armorStand) {
        return SlayerMiniboss.getMiniboss(armorStand.getName());
    }
}
