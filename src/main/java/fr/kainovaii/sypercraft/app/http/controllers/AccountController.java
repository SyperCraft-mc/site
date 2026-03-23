package fr.kainovaii.sypercraft.app.http.controllers;

import com.obsidian.core.di.annotations.Inject;
import com.obsidian.core.http.controller.BaseController;
import com.obsidian.core.http.controller.annotations.Controller;
import com.obsidian.core.routing.methods.GET;
import com.obsidian.core.security.user.CurrentUser;
import com.obsidian.core.security.user.RequireLogin;
import fr.kainovaii.sypercraft.app.domain.faction.models.Faction;
import fr.kainovaii.sypercraft.app.domain.faction.FactionRepository;
import fr.kainovaii.sypercraft.app.domain.user.User;
import fr.kainovaii.sypercraft.app.domain.user.UserRepository;
import fr.kainovaii.sypercraft.app.security.AppUserDetails;
import spark.Request;
import spark.Response;

import java.util.Map;

@Controller("/mon-compte")
public class AccountController extends BaseController
{
    @Inject
    private UserRepository userRepository;

    @Inject
    private FactionRepository factionRepository;

    @GET(value = "", name = "account.index")
    private Object index()
    {
        return render("home.html", Map.of());
    }

    @RequireLogin
    @GET(value = "/faction", name = "account.faction")
    private Object editFaction(Request req, Response res, @CurrentUser AppUserDetails loggedUser)
    {
        User player = userRepository.findByUUID(loggedUser.getUUID());

        Faction faction = factionRepository.findByUser(loggedUser.getUUID());
        if (faction == null) {
            return redirectWithError("Vous n'avez pas de faction", "/joueurs/s/" + player.getPseudo());
        }

        return render("faction/edit.html", Map.of("faction", faction));
    }
}