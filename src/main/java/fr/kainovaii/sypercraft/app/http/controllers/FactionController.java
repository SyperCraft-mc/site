package fr.kainovaii.sypercraft.app.http.controllers;

import com.obsidian.core.di.annotations.Inject;
import fr.kainovaii.sypercraft.app.domain.faction.FactionDTO;
import fr.kainovaii.sypercraft.app.domain.faction.FactionService;
import com.obsidian.core.database.DB;
import com.obsidian.core.http.controller.BaseController;
import com.obsidian.core.http.controller.annotations.Controller;
import com.obsidian.core.http.middleware.annotations.Before;
import com.obsidian.core.livecomponents.session.SessionMiddleware;
import com.obsidian.core.routing.methods.GET;
import spark.Request;

import java.util.Map;

@Controller
public class FactionController extends BaseController
{
    @Inject
    FactionService factionService;

    @GET(value = "/factions", name = "faction.index")
    private Object index()
    {
        return render("faction/index.html", Map.of());
    }

    @GET(value = "/factions/s/:name", name = "faction.single")
    private Object single(Request req)
    {
        String name = req.params("name");
        FactionDTO faction = factionService.findByName(name);

        return render("faction/single.html", Map.of("faction", faction));
    }
}