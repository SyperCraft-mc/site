package fr.kainovaii.obsidian.app.http.controllers;

import fr.kainovaii.obsidian.app.redis.models.FPlayer;
import fr.kainovaii.obsidian.app.redis.models.Faction;
import fr.kainovaii.obsidian.app.redis.models.FactionRank;
import fr.kainovaii.obsidian.app.redis.repositories.FactionRepository;
import fr.kainovaii.obsidian.app.redis.repositories.PlayerRepository;
import fr.kainovaii.obsidian.app.utils.ApiUtile;
import fr.kainovaii.obsidian.http.controller.BaseController;
import fr.kainovaii.obsidian.http.controller.annotations.Controller;
import fr.kainovaii.obsidian.routing.methods.GET;
import spark.Request;
import spark.Response;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
public class FactionController extends BaseController
{
    @GET(value = "/factions", name = "faction.index")
    private Object index()
    {
        return render("faction/index.html", Map.of());
    }

    @GET(value = "/factions/s/:name", name = "faction.single")
    private Object single()
    {
        return render("faction/single.html", Map.of());
    }

    @GET(value = "/api/factions", name = "api.factions.index")
    private Object index(Request req, Response res, PlayerRepository playerRepo, FactionRepository factionRepo)
    {
        List<Faction> factions = factionRepo.findAll();

        List<Map<String, Object>> result = new ArrayList<>();
        for (Faction f : factions) {
            Map<String, Object> entry = new LinkedHashMap<>();
            entry.put("id",    f.getId());
            entry.put("name",  f.getName());
            entry.put("power", f.getPower());
            result.add(entry);
        }

        res.type("application/json");
        return ApiUtile.toJson(result);
    }

    // GET /api/faction/:id
    @GET(value = "/api/faction/:id", name = "api.faction.show")
    private Object show(Request req, Response res, FactionRepository factionRepo)
    {
        int id = ApiUtile.parseIntOrZero(req.params(":id"));
        if (id == 0) return ApiUtile.error(res, "ID invalide");

        Faction faction = factionRepo.findById(id);
        if (faction == null) return ApiUtile.error(res, "Faction introuvable");

        List<FPlayer> members = factionRepo.getMembers(id);
        List<FactionRank> ranks = factionRepo.getRanks(id);

        // Membres
        List<Map<String, Object>> membersJson = new ArrayList<>();
        for (FPlayer fp : members) {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("uuid",          fp.getUuid());
            m.put("factionRankId", fp.getFactionRankId());
            membersJson.add(m);
        }

        // Rangs
        List<Map<String, Object>> ranksJson = new ArrayList<>();
        for (FactionRank rank : ranks) {
            Map<String, Object> r = new LinkedHashMap<>();
            r.put("id",          rank.getId());
            r.put("label",       rank.getLabel());
            r.put("hierarchy",   rank.getHierarchy());
            r.put("colorRank",   rank.getColorRank());
            r.put("permissions", rank.getPermissionList());
            ranksJson.add(r);
        }

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("id",          faction.getId());
        result.put("name",        faction.getName());
        result.put("ownerUuid",   faction.getOwnerUuid());
        result.put("power",       faction.getPower());
        result.put("maxPower",    faction.getMaxPower());
        result.put("description", faction.getDescription());
        result.put("createdAt",   faction.getCreatedAt());
        result.put("members",     membersJson);
        result.put("ranks",       ranksJson);

        res.type("application/json");
        return ApiUtile.toJson(result);
    }

    // GET /api/faction/name/:name
    @GET(value = "/api/faction/name/:name", name = "api.faction.byName")
    private Object byName(Request req, Response res, FactionRepository factionRepo)
    {
        String name = req.params(":name");

        Faction faction = factionRepo.findByName(name);
        if (faction == null) return ApiUtile.error(res, "Faction introuvable");

        res.type("application/json");
        return ApiUtile.toJson(Map.of(
                "id",    faction.getId(),
                "name",  faction.getName(),
                "power", faction.getPower()
        ));
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------


}