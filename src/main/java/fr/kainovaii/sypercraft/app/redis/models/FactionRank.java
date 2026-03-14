package fr.kainovaii.sypercraft.app.redis.models;

import com.google.gson.Gson;
import fr.kainovaii.sypercraft.app.utils.ApiUtile;

import java.util.List;
import java.util.Map;

public class FactionRank
{
    private int id;
    private String label;
    private int hierarchy;
    private int factionId;
    private String colorRank;
    private List<String> permissionList;

    private static final Gson GSON = new Gson();

    private FactionRank() {}

    public static FactionRank fromRedis(Map<String, String> data)
    {
        FactionRank r = new FactionRank();
        r.id           = ApiUtile.parseIntOrZero(data.get("ID"));
        r.label        = data.getOrDefault("LABEL", "");
        r.hierarchy    = ApiUtile.parseIntOrZero(data.get("HIERARCHY"));
        r.factionId    = ApiUtile.parseIntOrZero(data.get("FACTIONID"));
        r.colorRank    = data.getOrDefault("COLORRANK", "§f");
        r.permissionList = ApiUtile.parsePermissions(data.get("PERMISSIONLIST"));
        return r;
    }

    public int getId()                     { return id; }
    public String getLabel()               { return label; }
    public int getHierarchy()              { return hierarchy; }
    public int getFactionId()              { return factionId; }
    public String getColorRank()           { return colorRank; }
    public List<String> getPermissionList(){ return permissionList; }
}