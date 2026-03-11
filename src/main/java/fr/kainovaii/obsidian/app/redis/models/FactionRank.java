package fr.kainovaii.obsidian.app.redis.models;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FactionRank {

    private int id;
    private String label;
    private int hierarchy;
    private int factionId;
    private String colorRank;
    private List<String> permissionList;

    private static final Gson GSON = new Gson();

    private FactionRank() {}

    /**
     * Construit un FactionRank depuis un Hash Redis (FACTIONRANK:{id})
     */
    public static FactionRank fromRedis(Map<String, String> data) {
        FactionRank r = new FactionRank();
        r.id           = parseIntOrZero(data.get("ID"));
        r.label        = data.getOrDefault("LABEL", "");
        r.hierarchy    = parseIntOrZero(data.get("HIERARCHY"));
        r.factionId    = parseIntOrZero(data.get("FACTIONID"));
        r.colorRank    = data.getOrDefault("COLORRANK", "§f");
        r.permissionList = parsePermissions(data.get("PERMISSIONLIST"));
        return r;
    }

    // -------------------------------------------------------------------------
    // Getters
    // -------------------------------------------------------------------------

    public int getId()                     { return id; }
    public String getLabel()               { return label; }
    public int getHierarchy()              { return hierarchy; }
    public int getFactionId()              { return factionId; }
    public String getColorRank()           { return colorRank; }
    public List<String> getPermissionList(){ return permissionList; }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private static List<String> parsePermissions(String json) {
        if (json == null || json.isBlank()) return new ArrayList<>();
        try {
            Type listType = new TypeToken<List<String>>(){}.getType();
            return GSON.fromJson(json, listType);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    private static int parseIntOrZero(String value) {
        if (value == null || value.isBlank()) return 0;
        try { return Integer.parseInt(value.trim()); }
        catch (NumberFormatException e) { return 0; }
    }

    @Override
    public String toString() {
        return "FactionRank{id=" + id + ", label='" + label +
                "', factionId=" + factionId + ", hierarchy=" + hierarchy + "}";
    }
}