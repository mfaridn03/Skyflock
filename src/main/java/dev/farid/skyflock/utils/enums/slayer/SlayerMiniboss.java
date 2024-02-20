package dev.farid.skyflock.utils.enums.slayer;

import com.mojang.realmsclient.gui.ChatFormatting;
import dev.farid.skyflock.utils.TextUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityWolf;

public enum SlayerMiniboss {

    // zombie
    REVENANT_SYCOPHANT("Revenant Sycophant", false, EntityZombie.class),
    REVENANT_CHAMPION("Revenant Champion", false, EntityZombie.class),
    DEFORMED_REVENANT("Deformed Revenant", true, EntityZombie.class),
    ATONED_CHAMPION("Atoned Champion", false, EntityZombie.class),
    ATONED_REVENANT("Atoned Revenant", true, EntityZombie.class),

    // spider
    TARANTULA_VERMIN("Tarantula Vermin", false, EntitySpider.class),
    TARANTULA_BEAST("Tarantula Beast", false, EntitySpider.class),
    MUTANT_TARANTULA("Mutant Tarantula", true, EntitySpider.class),

    // sven
    PACK_ENFORCER("Pack Enforcer", false, EntityWolf.class),
    SVEN_FOLLOWER("Sven Follower", false, EntityWolf.class),
    SVEN_ALPHA("Sven Alpha", true, EntityWolf.class),

    // voidgloom
    VOIDLING_DEVOTEE("Voidling Devotee", false, EntityEnderman.class),
    VOIDLING_RADICAL("Voidling Radical", false, EntityEnderman.class),
    VOIDCRAZED_MANIAC("Voidcrazed Maniac", true, EntityEnderman.class),

    // blaze
    FLARE_DEMON("Flare Demon", false, EntityBlaze.class),
    KINDLEHEART_DEMON("Kindleheart Demon", false, EntityBlaze.class),
    BURNINGSOUL_DEMON("Burningsoul Demon", true, EntityBlaze.class);

    public final String name;
    public final boolean isRare;
    public final Class<? extends EntityLivingBase> mobClass;

    SlayerMiniboss(String name, boolean isRare, Class<? extends EntityLivingBase> mobClass) {
        this.name = name;
        this.isRare = isRare;
        this.mobClass = mobClass;
    }

    public String getName() {
        return this.name;
    }

    public String toString() {
        return this.name;
    }

    public static SlayerMiniboss getMiniboss(String armorStandName) {
        String unformatted = ChatFormatting.stripFormatting(armorStandName);
        unformatted = TextUtils.removeUnicode(unformatted);
        String[] split = unformatted.trim().split(" ");
        if (split.length < 2)
            return null;

        String sName = split[0] + " " + split[1];

        for (SlayerMiniboss mini : SlayerMiniboss.values()) {
            if (mini.getName().equals(sName)) {
                return mini;
            }
        }
        return null;
    }
}
