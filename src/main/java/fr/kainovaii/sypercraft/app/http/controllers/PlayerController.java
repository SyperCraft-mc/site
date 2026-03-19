package fr.kainovaii.sypercraft.app.http.controllers;

import com.obsidian.core.di.annotations.Inject;
import fr.kainovaii.sypercraft.app.domain.user.UserDTO;
import fr.kainovaii.sypercraft.app.domain.user.UserService;
import com.obsidian.core.database.DB;
import com.obsidian.core.http.controller.BaseController;
import com.obsidian.core.http.controller.annotations.Controller;
import com.obsidian.core.http.middleware.annotations.Before;
import com.obsidian.core.livecomponents.session.SessionMiddleware;
import com.obsidian.core.routing.methods.GET;
import spark.Request;
import spark.Response;

import java.util.List;
import java.util.Map;

@Controller
public class PlayerController extends BaseController
{
    @Inject
    UserService userService;

    @GET(value = "/joueurs", name = "player.index")
    private Object index()
    {
        List<UserDTO> players = userService.findAll().stream().toList();
        long allPlayerOnline = players.stream().filter(UserDTO::isOnline).count();

        return render("player/index.html", Map.of(
            "players", players,
            "allPlayerOnline", allPlayerOnline
        ));
    }

    @GET(value = "/joueurs/s/:username", name = "player.single")
    private Object single(Request req, Response res)
    {
        String username = req.params("username");

        UserDTO player = userService.findByPseudo(username);
        if (player == null) {
            return redirectWithWarning("Player does not exist", "/joueurs");
        }
        return render("player/single.html", Map.of("player", player));
    }
}