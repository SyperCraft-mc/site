package fr.kainovaii.sypercraft.app.http.controllers;

import com.obsidian.core.di.annotations.Inject;
import com.obsidian.core.security.user.CurrentUser;
import com.obsidian.core.security.user.RequireLogin;
import fr.kainovaii.sypercraft.app.domain.faction.FactionDTO;
import fr.kainovaii.sypercraft.app.domain.faction.FactionService;
import fr.kainovaii.sypercraft.app.domain.user.UserDTO;
import fr.kainovaii.sypercraft.app.domain.user.UserRepository;
import fr.kainovaii.sypercraft.app.domain.user.UserService;
import fr.kainovaii.sypercraft.app.security.AppUserDetails;
import com.obsidian.core.http.controller.BaseController;
import com.obsidian.core.http.controller.annotations.Controller;
import com.obsidian.core.routing.methods.GET;
import spark.Request;
import spark.Response;

import java.util.Map;

@Controller("/mon-compte")
public class AccountController extends BaseController
{
    @Inject
    FactionService factionService;

    @Inject
    UserService userService;

    @GET(value = "", name = "account.index")
    private Object index(UserRepository userRepository)
    {
        return render("home.html", Map.of());
    }

    @RequireLogin
    @GET(value = "/faction", name = "account.index")
    private Object editFaction(Request req, Response res, @CurrentUser AppUserDetails loggedUser)
    {
        UserDTO player = userService.findByUUID(loggedUser.getUUID());
        if (player.hasFaction()) {
            FactionDTO faction = factionService.findByUser(loggedUser.getUUID());
            return render("faction/edit.html", Map.of("faction", faction));
        } else {
            redirectWithError("Vous n'avez pas de faction", "/joueurs/s/" + player.getPseudo());
        }
        return "";
    }
}