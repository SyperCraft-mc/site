package fr.kainovaii.sypercraft.app.http.controllers;

import com.obsidian.core.di.annotations.Inject;
import fr.kainovaii.sypercraft.app.domain.faction.FactionDTO;
import fr.kainovaii.sypercraft.app.domain.faction.FactionService;
import fr.kainovaii.sypercraft.app.domain.user.UserDTO;
import fr.kainovaii.sypercraft.app.domain.user.UserService;
import com.obsidian.core.database.DB;
import com.obsidian.core.http.controller.BaseController;
import com.obsidian.core.http.controller.annotations.Controller;
import com.obsidian.core.routing.methods.GET;
import spark.Response;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Controller
public class RankingController extends BaseController
{
    @Inject
    UserService userService;

    @GET(value = "classements", name = "site.ranking")
    private Object index(Response res)
    {
        res.redirect("/classements/faction");
        return "";
    }

    @GET(value = "/classements/faction", name = "site.ranking.faction")
    private Object faction(FactionService factionService)
    {
        List<FactionDTO> factions = factionService.findAll()
                .stream()
                .sorted(Comparator.comparingInt(FactionDTO::getPoints).reversed())
                .toList();

        return render("ranking/faction.html", Map.of("factions", factions));
    }

    @GET(value = "/classements/richesse", name = "site.ranking.money")
    private Object money()
    {
        List<UserDTO> users = userService.findAll().stream().toList()
                .stream()
                .sorted(Comparator.comparingLong(UserDTO::getSyscoins).reversed())
                .toList();

        return render("ranking/money.html", Map.of("users", users));
    }

    @GET(value = "/classements/temps", name = "site.ranking.playtime")
    private Object playtime()
    {
        List<UserDTO> users = userService.findAll()
                .stream()
                .sorted(Comparator.comparingLong(UserDTO::getPlaytimeSeconds).reversed())
                .toList();

        return render("ranking/playtime.html", Map.of("users", users));
    }
}