package dev.farid.skyflock.utils.enums.slayer;

import dev.farid.skyflock.utils.TextUtils;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityWolf;

public enum SlayerBoss {
    // rev
    ZOMBIE("Revenant Horror", EntityZombie.class),
    // EntitySpider.class NOTE: CaveSpider inherits this
    SPIDER("Tarantula Broodfather", EntitySpider.class),
    // EntityWolf.class
    WOLF("Sven Packmaster", EntityWolf.class),
    // EntityEnderman.class
    ENDERMAN("Voidgloom Seraph", EntityEnderman.class),
    // EntityOtherPlayerMP.class
    VAMPIRE("Riftstalker Bloodfiend", EntityOtherPlayerMP.class),
    // EntityBlaze.class
    BLAZE("Inferno Demonlord", EntityBlaze.class);

    private final String name;
    private final Class<? extends EntityLivingBase> mobClass;

    SlayerBoss(String name, Class<? extends EntityLivingBase> mobClass) {
        this.name = name;
        this.mobClass = mobClass;
    }

    public String getSlayerName() {
        return this.name;
    }

    public Class<? extends EntityLivingBase> getMobClass() {
        return this.mobClass;
    }
    public static SlayerBoss getBoss(String fullNameAndTier) {
        String[] parts = fullNameAndTier.split(" ");
        String slayerName = parts[0] + " " + parts[1];
        String tierString = parts[2];

        Integer sTier = TextUtils.romanToInt(tierString);
        if (sTier == null)
            return null;

        for (SlayerBoss boss : SlayerBoss.values()) {
            if (boss.getSlayerName().equals(slayerName))
                return boss;
        }
        return null;
    }

    public String toString() {
        return this.name;
    }
}
