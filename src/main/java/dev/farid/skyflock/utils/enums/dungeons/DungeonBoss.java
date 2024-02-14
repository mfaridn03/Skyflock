package dev.farid.skyflock.utils.enums.dungeons;

public enum DungeonBoss {

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
    WITHER_KING("The Wither King");
    private final String name;

    DungeonBoss(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public static DungeonBoss getBoss(String name) {
        for (DungeonBoss b : DungeonBoss.values()) {
            if (b.getName().equals(name))
                return b;
        }
        return null;
    }
}
