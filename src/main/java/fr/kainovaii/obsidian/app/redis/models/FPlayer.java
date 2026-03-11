package fr.kainovaii.obsidian.app.redis.models;

import java.util.Map;

public class FPlayer {

    private String uuid;
    private int factionId;
    private int factionRankId;
    private String joinedAt;

    private FPlayer() {}

    /**
     * Construit un FPlayer depuis un Hash Redis (FPLAYER:{uuid})
     */
    public static FPlayer fromRedis(String uuid, Map<String, String> data) {
        FPlayer fp = new FPlayer();
        fp.uuid          = uuid;
        fp.factionId     = parseIntOrZero(data.get("FACTIONID"));
        fp.factionRankId = parseIntOrZero(data.get("FACTIONRANKID"));
        fp.joinedAt      = data.getOrDefault("JOINEDAT", null);
        return fp;
    }

    public boolean hasFaction() {
        return factionId > 0;
    }

    // -------------------------------------------------------------------------
    // Getters
    // -------------------------------------------------------------------------

    public String getUuid()       { return uuid; }
    public int getFactionId()     { return factionId; }
    public int getFactionRankId() { return factionRankId; }
    public String getJoinedAt()   { return joinedAt; }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private static int parseIntOrZero(String value) {
        if (value == null || value.isBlank()) return 0;
        try { return Integer.parseInt(value.trim()); }
        catch (NumberFormatException e) { return 0; }
    }

    @Override
    public String toString() {
        return "FPlayer{uuid='" + uuid + "', factionId=" + factionId +
                ", factionRankId=" + factionRankId + "}";
    }
}