package fr.kainovaii.sypercraft.app.http.controllers;

import fr.kainovaii.obsidian.di.annotations.Inject;
import fr.kainovaii.sypercraft.app.domain.faction.FactionDTO;
import fr.kainovaii.sypercraft.app.domain.faction.FactionService;
import fr.kainovaii.sypercraft.app.domain.user.UserRepository;
import fr.kainovaii.sypercraft.app.security.AppUserDetails;
import fr.kainovaii.obsidian.database.DB;
import fr.kainovaii.obsidian.http.controller.BaseController;
import fr.kainovaii.obsidian.http.controller.annotations.Controller;
import fr.kainovaii.obsidian.routing.methods.GET;
import fr.kainovaii.obsidian.security.role.HasRole;
import spark.Request;

import java.util.Map;

@Controller
public class AccountController extends BaseController
{
    @Inject
    FactionService factionService;

    @GET(value = "/mon-compte", name = "account.index")
    private Object index(UserRepository userRepository)
    {
        return render("home.html", Map.of());
    }

    @HasRole("DEFAULT")
    @GET(value = "/mon-compte/faction", name = "account.index")
    private Object editFaction(Request req)
    {
        AppUserDetails loggedUser = getLoggedUser(req);
        FactionDTO faction = factionService.findByUser(loggedUser.getUUID());
        return render("faction/edit.html", Map.of("faction", faction));
    }
}