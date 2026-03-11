package fr.kainovaii.obsidian.app.redis.models;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class VipRank {

    private int id;
    private String label;
    private int hierarchy;
    private String colorRank;
    private String colorPlayer;
    private String colorMessage;
    private List<String> permissionList;

    private static final Gson GSON = new Gson();

    private VipRank() {}

    /**
     * Construit un VipRank depuis un Hash Redis (VIPRANK:{id})
     */
    public static VipRank fromRedis(Map<String, String> data) {
        VipRank r = new VipRank();
        r.id           = parseIntOrZero(data.get("ID"));
        r.label        = data.getOrDefault("LABEL", "");
        r.hierarchy    = parseIntOrZero(data.get("HIERARCHY"));
        r.colorRank    = data.getOrDefault("COLORRANK", "§f");
        r.colorPlayer  = data.getOrDefault("COLORPLAYER", "§f");
        r.colorMessage = data.getOrDefault("COLORMESSAGE", "§f");
        r.permissionList = parsePermissions(data.get("PERMISSIONLIST"));
        return r;
    }

    // -------------------------------------------------------------------------
    // Getters
    // -------------------------------------------------------------------------

    public int getId()                     { return id; }
    public String getLabel()               { return label; }
    public int getHierarchy()              { return hierarchy; }
    public String getColorRank()           { return colorRank; }
    public String getColorPlayer()         { return colorPlayer; }
    public String getColorMessage()        { return colorMessage; }
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
        return "VipRank{id=" + id + ", label='" + label +
                "', hierarchy=" + hierarchy + "}";
    }
}