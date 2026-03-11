package fr.kainovaii.obsidian.app.redis.models;

import java.util.Map;

public class Faction {

    private int id;
    private String name;
    private String ownerUuid;
    private int power;
    private int maxPower;
    private String createdAt;
    private String description;

    private Faction() {}

    /**
     * Construit une Faction depuis un Hash Redis (FACTION:{id})
     */
    public static Faction fromRedis(Map<String, String> data) {
        Faction f = new Faction();
        f.id          = parseIntOrZero(data.get("ID"));
        f.name        = data.getOrDefault("NAME", "");
        f.ownerUuid   = data.getOrDefault("OWNERUUID", null);
        f.power       = parseIntOrZero(data.get("POWER"));
        f.maxPower    = parseIntOrZero(data.get("MAXPOWER"));
        f.createdAt   = data.getOrDefault("CREATEDAT", null);
        f.description = data.getOrDefault("DESCRIPTION", "");
        return f;
    }

    // -------------------------------------------------------------------------
    // Getters
    // -------------------------------------------------------------------------

    public int getId()            { return id; }
    public String getName()       { return name; }
    public String getOwnerUuid()  { return ownerUuid; }
    public int getPower()         { return power; }
    public int getMaxPower()      { return maxPower; }
    public String getCreatedAt()  { return createdAt; }
    public String getDescription(){ return description; }

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
        return "Faction{id=" + id + ", name='" + name +
                "', ownerUuid='" + ownerUuid + "', power=" + power + "}";
    }
}