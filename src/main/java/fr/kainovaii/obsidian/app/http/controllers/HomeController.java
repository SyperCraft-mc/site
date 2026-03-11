package fr.kainovaii.obsidian.app.http.controllers;

import fr.kainovaii.obsidian.Main;
import fr.kainovaii.obsidian.http.controller.BaseController;
import fr.kainovaii.obsidian.http.controller.annotations.Controller;
import fr.kainovaii.obsidian.routing.methods.GET;
import spark.Request;
import spark.Response;

import java.util.LinkedHashMap;
import java.util.Map;

@Controller
public class HomeController extends BaseController
{
    @GET(value = "/", name = "site.home")
    private Object homepage()
    {
        return render("home.html", Map.of());
    }

    @GET(value = "/building", name = "site.building")
    private Object building()
    {
        return render("building.html", Map.of());
    }

    @GET(value = "/api/debug/redis", name = "api.debug.redis")
    private Object debug(Request req, Response res)
    {
        Map<String, Object> result = new LinkedHashMap<>();

        // Player
        result.put("PLAYER", Main.loadRedis().getAll("PLAYER:e88cd61a-2ced-49d7-afa8-6309ecfb08bf"));

        // StaffRank
        result.put("STAFFRANK:5", Main.loadRedis().getAll("STAFFRANK:5"));

        // VipRank
        result.put("VIPRANK:1", Main.loadRedis().getAll("VIPRANK:1"));

        // Faction
        result.put("FACTION:56", Main.loadRedis().getAll("FACTION:56"));

        // FPlayer
        result.put("FPLAYER", Main.loadRedis().getAll("FPLAYER:e88cd61a-2ced-49d7-afa8-6309ecfb08bf"));

        // FactionRank
        result.put("FACTIONRANK:119", Main.loadRedis().getAll("FACTIONRANK:119"));

        res.type("application/json");
        return new com.google.gson.Gson().toJson(result);
    }
}