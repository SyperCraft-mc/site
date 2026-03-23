package fr.kainovaii.sypercraft.app.http.controllers;

import com.obsidian.core.di.annotations.Inject;
import com.obsidian.core.http.controller.BaseController;
import com.obsidian.core.http.controller.annotations.Controller;
import com.obsidian.core.routing.methods.GET;
import fr.kainovaii.sypercraft.app.domain.faction.models.Faction;
import fr.kainovaii.sypercraft.app.domain.faction.FactionRepository;
import spark.Request;

import java.util.Map;

@Controller
public class FactionController extends BaseController
{
    @Inject
    private FactionRepository factionRepository;

    @GET(value = "/factions", name = "faction.index")
    private Object index()
    {
        return render("faction/index.html", Map.of());
    }

    @GET(value = "/factions/s/:name", name = "faction.single")
    private Object single(Request req)
    {
        String name = req.params("name");
        Faction faction = factionRepository.findByName(name);

        return render("faction/single.html", Map.of("faction", faction));
    }
}