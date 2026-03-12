package fr.kainovaii.obsidian.app.http.controllers;

import fr.kainovaii.obsidian.app.domain.faction.models.Faction;
import fr.kainovaii.obsidian.app.domain.faction.FactionDTO;
import fr.kainovaii.obsidian.app.domain.faction.repositories.FactionRepository;
import fr.kainovaii.obsidian.app.domain.faction.FactionService;
import fr.kainovaii.obsidian.database.DB;
import fr.kainovaii.obsidian.http.controller.BaseController;
import fr.kainovaii.obsidian.http.controller.annotations.Controller;
import fr.kainovaii.obsidian.http.middleware.annotations.Before;
import fr.kainovaii.obsidian.livecomponents.session.SessionMiddleware;
import fr.kainovaii.obsidian.routing.methods.GET;
import spark.Request;

import java.util.List;
import java.util.Map;

@Controller
public class FactionController extends BaseController
{
    @Before(SessionMiddleware.class)
    @GET(value = "/factions", name = "faction.index")
    private Object index()
    {
        return render("faction/index.html", Map.of());
    }

    @GET(value = "/factions/s/:name", name = "faction.single")
    private Object single(Request req, FactionService factionService)
    {
        String name = req.params("name");

        FactionDTO faction = DB.withConnection(() -> factionService.findByName(name));

        return render("faction/single.html", Map.of("faction", faction));
    }
}