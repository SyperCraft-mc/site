package fr.kainovaii.sypercraft.app.http.controllers;

import com.obsidian.core.di.annotations.Inject;
import com.obsidian.core.http.controller.BaseController;
import com.obsidian.core.http.controller.annotations.Controller;
import com.obsidian.core.routing.methods.GET;
import fr.kainovaii.sypercraft.app.domain.faction.models.Faction;
import fr.kainovaii.sypercraft.app.domain.faction.FactionRepository;
import fr.kainovaii.sypercraft.app.domain.user.User;
import fr.kainovaii.sypercraft.app.domain.user.UserRepository;
import spark.Response;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Controller
public class RankingController extends BaseController
{
    @Inject
    private UserRepository userRepository;

    @Inject
    private FactionRepository factionRepository;

    @GET(value = "classements", name = "site.ranking")
    private Object index(Response res)
    {
        res.redirect("/classements/faction");
        return "";
    }

    @GET(value = "/classements/faction", name = "site.ranking.faction")
    private Object faction()
    {
        List<Faction> factions = factionRepository.findAll()
                .stream()
                .sorted(Comparator.comparingInt(Faction::getPoint).reversed())
                .toList();

        return render("ranking/faction.html", Map.of("factions", factions));
    }

    @GET(value = "/classements/richesse", name = "site.ranking.money")
    private Object money()
    {
        List<User> users = userRepository.findAll()
                .stream()
                .sorted(Comparator.comparingLong((User u) -> {
                    var fp = u.factionPlayer().first();
                    return fp != null ? fp.getSyscoins() : 0L;
                }).reversed())
                .toList();

        return render("ranking/money.html", Map.of("users", users));
    }

    @GET(value = "/classements/temps", name = "site.ranking.playtime")
    private Object playtime()
    {
        List<User> users = userRepository.findAll()
                .stream()
                .sorted(Comparator.comparingLong(User::getPlaytimeSeconds).reversed())
                .toList();

        return render("ranking/playtime.html", Map.of("users", users));
    }
}