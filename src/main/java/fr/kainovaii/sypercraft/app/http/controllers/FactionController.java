package fr.kainovaii.sypercraft.app.http.controllers;

import fr.kainovaii.sypercraft.app.domain.faction.FactionDTO;
import fr.kainovaii.sypercraft.app.domain.faction.FactionService;
import fr.kainovaii.obsidian.database.DB;
import fr.kainovaii.obsidian.http.controller.BaseController;
import fr.kainovaii.obsidian.http.controller.annotations.Controller;
import fr.kainovaii.obsidian.http.middleware.annotations.Before;
import fr.kainovaii.obsidian.livecomponents.session.SessionMiddleware;
import fr.kainovaii.obsidian.routing.methods.GET;
import spark.Request;

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