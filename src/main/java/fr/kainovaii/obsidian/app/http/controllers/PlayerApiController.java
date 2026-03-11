package fr.kainovaii.obsidian.app.http.controllers;

import fr.kainovaii.obsidian.app.domain.user.User;
import fr.kainovaii.obsidian.app.domain.user.UserRepository;
import fr.kainovaii.obsidian.app.redis.models.*;
import fr.kainovaii.obsidian.app.redis.repositories.FactionRepository;
import fr.kainovaii.obsidian.app.redis.repositories.PlayerRepository;
import fr.kainovaii.obsidian.app.utils.ApiUtile;
import fr.kainovaii.obsidian.database.DB;
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
public class PlayerApiController extends BaseController
{
    @GET(value = "/api/player/:uuid", name = "api.player.show")
    private Object show(Request req, Response res, PlayerRepository playerRepo, FactionRepository factionRepo)
    {
        String uuid = req.params(":uuid");

        Player player = playerRepo.findByUuid(uuid);
        if (player == null) return ApiUtile.error(res, "Joueur introuvable");

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("uuid",  player.getUuid());
        result.put("name",  player.getName());

        // Staff rank
        StaffRank staffRank = playerRepo.getStaffRank(player);
        if (staffRank != null) {
            Map<String, Object> sr = new LinkedHashMap<>();
            sr.put("id",          staffRank.getId());
            sr.put("label",       staffRank.getLabel());
            sr.put("hierarchy",   staffRank.getHierarchy());
            sr.put("colorRank",   staffRank.getColorRank());
            sr.put("permissions", staffRank.getPermissionList());
            result.put("staffRank", sr);
        }

        // Vip rank
        VipRank vipRank = playerRepo.getVipRank(player);
        if (vipRank != null) {
            Map<String, Object> vr = new LinkedHashMap<>();
            vr.put("id",        vipRank.getId());
            vr.put("label",     vipRank.getLabel());
            vr.put("hierarchy", vipRank.getHierarchy());
            vr.put("colorRank", vipRank.getColorRank());
            result.put("vipRank", vr);
        }

        // Faction
        FPlayer fplayer = factionRepo.findFPlayer(uuid);
        if (fplayer != null && fplayer.hasFaction()) {
            Faction faction = factionRepo.findById(fplayer.getFactionId());
            if (faction != null) {
                Map<String, Object> fac = new LinkedHashMap<>();
                fac.put("id",    faction.getId());
                fac.put("name",  faction.getName());
                fac.put("power", faction.getPower());
                result.put("faction", fac);
            }
        }

        res.type("application/json");
        return ApiUtile.toJson(result);
    }

    @GET(value = "/api/player/:uuid/staffrank", name = "api.player.staffrank")
    private Object staffRank(Request req, Response res, PlayerRepository playerRepo)
    {
        String uuid = req.params(":uuid");

        Player player = playerRepo.findByUuid(uuid);
        if (player == null) return ApiUtile.error(res, "Joueur introuvable");

        StaffRank rank = playerRepo.getStaffRank(player);
        if (rank == null) return ApiUtile.error(res, "Pas de staff rank");

        res.type("application/json");
        return ApiUtile.toJson(Map.of(
                "id",          rank.getId(),
                "label",       rank.getLabel(),
                "hierarchy",   rank.getHierarchy(),
                "colorRank",   rank.getColorRank(),
                "colorPlayer", rank.getColorPlayer(),
                "colorMessage",rank.getColorMessage(),
                "permissions", rank.getPermissionList()
        ));
    }

    @GET(value = "/api/players", name = "api.players")
    private Object all(Request req, Response res, PlayerRepository playerRepo)
    {
        List<Player> players = playerRepo.findAll();
        List<Map<String, Object>> result = new ArrayList<>();

        for (Player player : players)
        {
            result.add(Map.of(
                "name", player.getName(),
                "uuid", player.getUuid()
            ));
        }

        res.type("application/json");
        return ApiUtile.toJson(result);
    }
}