package dev.farid.skyflock.utils.enums.slayer;

import dev.farid.skyflock.utils.TextUtils;

public enum SlayerTier {
    I(1),
    II(2),
    III(3),
    IV(4),
    V(5);

    private final int num;

    SlayerTier(int num) {
        this.num = num;
    }

    public int getTier() {
        return this.num;
    }

    public static SlayerTier getTier(int inNum) {
        for (SlayerTier t : SlayerTier.values()) {
            if (t.getTier() == inNum)
                return t;
        }
        return null;
    }

    public static SlayerTier getTierFromBossString(String fullNameAndTier) {
        String[] parts = fullNameAndTier.split(" ");
        String tierString = parts[2];

        Integer sTier = TextUtils.romanToInt(tierString);
        if (sTier == null)
            return null;

        for (SlayerTier t : SlayerTier.values()) {
            if (t.getTier() == sTier)
                return t;
        }
        return null;
    }
}
