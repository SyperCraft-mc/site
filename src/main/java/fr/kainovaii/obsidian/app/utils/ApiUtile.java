package fr.kainovaii.obsidian.app.utils;

import com.google.gson.reflect.TypeToken;
import spark.Response;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static net.raphimc.minecraftauth.util.JsonUtil.GSON;

public class ApiUtile
{
    public static String error(Response res, String message) {
        res.status(404);
        res.type("application/json");
        return "{\"error\": \"" + message + "\"}";
    }

    public static String toJson(Object obj) {
        return new com.google.gson.Gson().toJson(obj);
    }

    public static int parseIntOrZero(String value) {
        if (value == null || value.isBlank()) return 0;
        try { return Integer.parseInt(value.trim()); }
        catch (NumberFormatException e) { return 0; }
    }

    public static List<String> parsePermissions(String json) {
        if (json == null || json.isBlank()) return new ArrayList<>();
        try {
            Type listType = new TypeToken<List<String>>(){}.getType();
            return GSON.fromJson(json, listType);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}
