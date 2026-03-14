package fr.kainovaii.sypercraft.app.redis.models;

import fr.kainovaii.sypercraft.app.utils.ApiUtile;

import java.util.Map;

public class Faction
{
    private int id;
    private String name;
    private String ownerUuid;
    private int power;
    private int maxPower;
    private String createdAt;
    private String description;

    private Faction() {}

    public static Faction fromRedis(Map<String, String> data)
    {
        Faction f = new Faction();
        f.id          = ApiUtile.parseIntOrZero(data.get("ID"));
        f.name        = data.getOrDefault("NAME", "");
        f.ownerUuid   = data.getOrDefault("OWNERUUID", null);
        f.power       = ApiUtile.parseIntOrZero(data.get("POWER"));
        f.maxPower    = ApiUtile.parseIntOrZero(data.get("MAXPOWER"));
        f.createdAt   = data.getOrDefault("CREATEDAT", null);
        f.description = data.getOrDefault("DESCRIPTION", "");
        return f;
    }

    public int getId()            { return id; }
    public String getName()       { return name; }
    public String getOwnerUuid()  { return ownerUuid; }
    public int getPower()         { return power; }
    public int getMaxPower()      { return maxPower; }
    public String getCreatedAt()  { return createdAt; }
    public String getDescription(){ return description; }
}