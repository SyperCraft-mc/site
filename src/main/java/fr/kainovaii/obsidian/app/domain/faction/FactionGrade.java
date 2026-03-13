package fr.kainovaii.obsidian.app.domain.faction;

public enum FactionGrade
{

    VAGABOND("Vagabond", "VG", "§7", 0, 0,
            2, 0, 0, 0, 0,
            0, false,
            15, 1, false,
            2, 3, 50, false,
            1, 0, 0),

    CLAN("Clan", "CL", "§a", 1, 1,
            2, 0, 0, 10, 30,
            0, false,
            15, 0, false,
            4, 5, 100, false,
            2, 1, 5),

    COALITION("Coalition", "CO", "§6", 2, 2,
            4, 15, 10, 20, 50,
            0, false,
            16, 3, false,
            6, 6, 500, false,
            2, 1, 10),

    CONQUETE("Conquête", "CQ", "§c", 3, 3,
            4, 15, 10, 20, 70,
            5, true,
            17, 4, true,
            8, 6, 800, false,
            3, 2, 10),

    SUPREMATIE("Suprématie", "SP", "§5", 4, Integer.MAX_VALUE,
            6, 20, 10, 30, 100,
            15, true,
            18, 5, true,
            10, 8, 1300, true,
            4, 3, 10);

    private final String displayName;
    private final String tag;
    private final String color;
    private final int minLevel;
    private final int maxLevel;

    // Avantages
    private final int breakerLimit;
    private final int spawnerLimit;
    private final int hopperLimit;
    private final int dropperLimit;
    private final int redstoneLimit;
    private final int damageReduction;
    private final boolean healEnabled;
    private final int maxMember;
    private final int relationLimit;
    private final boolean classementEnabled;
    private final int zoneLimit;
    private final int rankLimit;
    private final int mobStackLimit;
    private final boolean pvpArenaEnabled;
    private final int factionHomeLimit;
    private final int factionShopLimit;
    private final int factionShopSlotLimit;

    FactionGrade(String displayName, String tag, String color, int minLevel, int maxLevel,
                 int breakerLimit, int spawnerLimit, int hopperLimit, int dropperLimit, int redstoneLimit,
                 int damageReduction, boolean healEnabled,
                 int maxMember, int relationLimit, boolean classementEnabled,
                 int zoneLimit, int rankLimit, int mobStackLimit, boolean pvpArenaEnabled,
                 int factionHomeLimit, int factionShopLimit, int factionShopSlotLimit) {
        this.displayName = displayName;
        this.tag = tag;
        this.color = color;
        this.minLevel = minLevel;
        this.maxLevel = maxLevel;
        this.breakerLimit = breakerLimit;
        this.spawnerLimit = spawnerLimit;
        this.hopperLimit = hopperLimit;
        this.dropperLimit = dropperLimit;
        this.redstoneLimit = redstoneLimit;
        this.damageReduction = damageReduction;
        this.healEnabled = healEnabled;
        this.maxMember = maxMember;
        this.relationLimit = relationLimit;
        this.classementEnabled = classementEnabled;
        this.zoneLimit = zoneLimit;
        this.rankLimit = rankLimit;
        this.mobStackLimit = mobStackLimit;
        this.pvpArenaEnabled = pvpArenaEnabled;
        this.factionHomeLimit = factionHomeLimit;
        this.factionShopLimit = factionShopLimit;
        this.factionShopSlotLimit = factionShopSlotLimit;
    }

    // ── Identité ──

    public String getDisplayName() { return displayName; }
    public String getTag() { return tag; }
    public String getColor() { return color; }
    public String getColoredName() { return color + displayName; }
    public String getColoredTag() { return color + "[" + tag + "]"; }
    public int getMinLevel() { return minLevel; }
    public int getMaxLevel() { return maxLevel; }

    // ── Avantages — Limites blocs ──

    public int getBreakerLimit() { return breakerLimit; }
    public int getSpawnerLimit() { return spawnerLimit; }
    public int getHopperLimit() { return hopperLimit; }
    public int getDropperLimit() { return dropperLimit; }
    public int getRedstoneLimit() { return redstoneLimit; }

    // ── Avantages — Combat ──

    public int getDamageReduction() { return damageReduction; }
    public boolean isHealEnabled() { return healEnabled; }
    public int getHealLevel() {
        if (this == SUPREMATIE) return 2;
        if (healEnabled) return 1;
        return 0;
    }

    // ── Avantages — Faction ──

    public int getMaxMember() { return maxMember; }
    public int getRelationLimit() { return relationLimit; }
    public boolean isClassementEnabled() { return classementEnabled; }
    public int getZoneLimit() { return zoneLimit; }
    public int getRankLimit() { return rankLimit; }
    public int getMobStackLimit() { return mobStackLimit; }
    public boolean isPvpArenaEnabled() { return pvpArenaEnabled; }
    public int getFactionHomeLimit() { return factionHomeLimit; }
    public int getFactionShopLimit() { return factionShopLimit; }
    public int getFactionShopSlotLimit() { return factionShopSlotLimit; }

    // ── Lookup ──

    public static FactionGrade fromLevel(int level) {
        if (level >= 4) return SUPREMATIE;
        if (level == 3) return CONQUETE;
        if (level == 2) return COALITION;
        if (level == 1) return CLAN;
        return VAGABOND;
    }

    public FactionGrade getNextGrade() {
        switch (this) {
            case VAGABOND:   return CLAN;
            case CLAN:       return COALITION;
            case COALITION:  return CONQUETE;
            case CONQUETE:   return SUPREMATIE;
            default:         return null;
        }
    }

    public boolean isMaxGrade() {
        return this == SUPREMATIE;
    }
}