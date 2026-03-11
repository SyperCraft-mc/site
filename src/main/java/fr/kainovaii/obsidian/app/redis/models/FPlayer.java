package fr.kainovaii.obsidian.app.redis.models;

import fr.kainovaii.obsidian.app.utils.ApiUtile;

import java.util.Map;

public class FPlayer
{
    private String uuid;
    private int factionId;
    private int factionRankId;
    private String joinedAt;

    private FPlayer() {}

    public static FPlayer fromRedis(String uuid, Map<String, String> data)
    {
        FPlayer fp = new FPlayer();
        fp.uuid          = uuid;
        fp.factionId     = ApiUtile.parseIntOrZero(data.get("FACTIONID"));
        fp.factionRankId = ApiUtile.parseIntOrZero(data.get("FACTIONRANKID"));
        fp.joinedAt      = data.getOrDefault("JOINEDAT", null);
        return fp;
    }

    public boolean hasFaction() {
        return factionId > 0;
    }

    public String getUuid()       { return uuid; }
    public int getFactionId()     { return factionId; }
    public int getFactionRankId() { return factionRankId; }
    public String getJoinedAt()   { return joinedAt; }

}