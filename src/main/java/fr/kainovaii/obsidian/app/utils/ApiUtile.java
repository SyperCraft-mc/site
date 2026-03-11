package fr.kainovaii.obsidian.app.utils;

import spark.Response;

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
}
