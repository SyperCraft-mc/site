package fr.kainovaii.obsidian.app.redis.models;

import fr.kainovaii.obsidian.app.utils.ApiUtile;

import java.util.Map;

public class Player
{
    private String uuid;
    private String name;
    private String ip;
    private String lastSeen;
    private int staffRankId;
    private int vipRankId;
    private int playerRankId;

    private Player() {}

    public static Player fromRedis(String uuid, Map<String, String> data)
    {
        Player p = new Player();
        p.uuid        = uuid;
        p.name        = data.getOrDefault("NAME", null);
        p.ip          = data.getOrDefault("IP", null);
        p.lastSeen    = data.getOrDefault("LASTSEEN", null);
        p.staffRankId = ApiUtile.parseIntOrZero(data.get("STAFFRANKID"));
        p.vipRankId   = ApiUtile.parseIntOrZero(data.get("VIPRANKID"));
        p.playerRankId= ApiUtile.parseIntOrZero(data.get("PLAYERRANKID"));
        return p;
    }

    public boolean hasStaffRank() {
        return staffRankId > 0;
    }

    public boolean hasVipRank() {
        return vipRankId > 0;
    }


    public String getUuid()        { return uuid; }
    public String getName()        { return name; }
    public String getIp()          { return ip; }
    public String getLastSeen()    { return lastSeen; }
    public int getStaffRankId()    { return staffRankId; }
    public int getVipRankId()      { return vipRankId; }
    public int getPlayerRankId()   { return playerRankId; }
}