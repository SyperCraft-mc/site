package fr.kainovaii.sypercraft.app.redis.models;

import com.google.gson.Gson;
import fr.kainovaii.sypercraft.app.utils.ApiUtile;

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

    public static VipRank fromRedis(Map<String, String> data)
    {
        VipRank r = new VipRank();
        r.id           = ApiUtile.parseIntOrZero(data.get("ID"));
        r.label        = data.getOrDefault("LABEL", "");
        r.hierarchy    = ApiUtile.parseIntOrZero(data.get("HIERARCHY"));
        r.colorRank    = data.getOrDefault("COLORRANK", "§f");
        r.colorPlayer  = data.getOrDefault("COLORPLAYER", "§f");
        r.colorMessage = data.getOrDefault("COLORMESSAGE", "§f");
        r.permissionList = ApiUtile.parsePermissions(data.get("PERMISSIONLIST"));
        return r;
    }

    public int getId()                     { return id; }
    public String getLabel()               { return label; }
    public int getHierarchy()              { return hierarchy; }
    public String getColorRank()           { return colorRank; }
    public String getColorPlayer()         { return colorPlayer; }
    public String getColorMessage()        { return colorMessage; }
    public List<String> getPermissionList(){ return permissionList; }
}