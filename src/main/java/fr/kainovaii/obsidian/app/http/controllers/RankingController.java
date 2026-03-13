package fr.kainovaii.obsidian.app.http.controllers;

import fr.kainovaii.obsidian.app.domain.faction.FactionDTO;
import fr.kainovaii.obsidian.app.domain.faction.FactionService;
import fr.kainovaii.obsidian.app.domain.user.UserDTO;
import fr.kainovaii.obsidian.app.domain.user.UserService;
import fr.kainovaii.obsidian.database.DB;
import fr.kainovaii.obsidian.http.controller.BaseController;
import fr.kainovaii.obsidian.http.controller.annotations.Controller;
import fr.kainovaii.obsidian.routing.methods.GET;
import spark.Response;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Controller
public class RankingController extends BaseController
{
    @GET(value = "classements", name = "site.ranking")
    private Object index(Response res)
    {
        res.redirect("/classements/faction");
        return "";
    }

    @GET(value = "/classements/faction", name = "site.ranking.faction")
    private Object faction(FactionService factionService)
    {
        List<FactionDTO> factions = DB.withConnection(() -> factionService.findAll())
                .stream()
                .sorted(Comparator.comparingInt(FactionDTO::getPoints).reversed())
                .toList();

        return render("ranking/faction.html", Map.of("factions", factions));
    }

    @GET(value = "/classements/richesse", name = "site.ranking.money")
    private Object money(UserService userService)
    {
        List<UserDTO> users = DB.withConnection(() -> userService.findAll().stream().toList())
                .stream()
                .sorted(Comparator.comparingLong(UserDTO::getSyscoins).reversed())
                .toList();

        return render("ranking/money.html", Map.of("users", users));
    }

    @GET(value = "/classements/temps", name = "site.ranking.playtime")
    private Object playtime(UserService userService)
    {
        List<UserDTO> users = DB.withConnection(() -> userService.findAll())
                .stream()
                .sorted(Comparator.comparingLong(UserDTO::getPlaytimeSeconds).reversed())
                .toList();

        return render("ranking/playtime.html", Map.of("users", users));
    }
}