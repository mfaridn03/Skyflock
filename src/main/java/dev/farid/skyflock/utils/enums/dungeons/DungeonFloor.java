package dev.farid.skyflock.utils.enums.dungeons;

public enum DungeonFloor {

    // normal mode
    ENTRANCE(0, DungeonMode.NORMAL),
    F1(1, DungeonMode.NORMAL),
    F2(2, DungeonMode.NORMAL),
    F3(3, DungeonMode.NORMAL),
    F4(4, DungeonMode.NORMAL),
    F5(5, DungeonMode.NORMAL),
    F6(6, DungeonMode.NORMAL),
    F7(7, DungeonMode.NORMAL),

    // master mode
    M1(1, DungeonMode.MASTER),
    M2(2, DungeonMode.MASTER),
    M3(3, DungeonMode.MASTER),
    M4(4, DungeonMode.MASTER),
    M5(5, DungeonMode.MASTER),
    M6(6, DungeonMode.MASTER),
    M7(7, DungeonMode.MASTER);

    private final int floorNum;
    private final DungeonMode mode;

    DungeonFloor(int floorNum, DungeonMode mode) {
        this.floorNum = floorNum;
        this.mode = mode;
    }

    public static DungeonFloor getFloor(String stringIn) {
        // "M3", "E", "F6" to enum
        if (stringIn.equals("E"))
            return ENTRANCE;

        if (stringIn.length() != 2)
            return null;

        char modeChar = stringIn.charAt(0);
        char floorChar = stringIn.charAt(1);

        if (modeChar != 'F' && modeChar != 'M')
            return null;

        int num = Character.getNumericValue(floorChar);
        DungeonMode mode = (modeChar == 'F') ? DungeonMode.NORMAL : DungeonMode.MASTER;

        for (DungeonFloor f : DungeonFloor.values()) {
            if (f.mode != mode)
                continue;

            if (f.floorNum == num)
                return f;
        }
        return null;
    }
}
