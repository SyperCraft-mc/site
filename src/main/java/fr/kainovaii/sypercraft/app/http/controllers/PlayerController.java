package fr.kainovaii.sypercraft.app.http.controllers;

import fr.kainovaii.sypercraft.app.domain.user.UserDTO;
import fr.kainovaii.sypercraft.app.domain.user.UserService;
import fr.kainovaii.obsidian.database.DB;
import fr.kainovaii.obsidian.http.controller.BaseController;
import fr.kainovaii.obsidian.http.controller.annotations.Controller;
import fr.kainovaii.obsidian.http.middleware.annotations.Before;
import fr.kainovaii.obsidian.livecomponents.session.SessionMiddleware;
import fr.kainovaii.obsidian.routing.methods.GET;
import spark.Request;
import spark.Response;

import java.util.List;
import java.util.Map;

@Controller
public class PlayerController extends BaseController
{
    @Before(SessionMiddleware.class)
    @GET(value = "/joueurs", name = "player.index")
    private Object index(UserService userService)
    {
        List<UserDTO> players = DB.withConnection(() -> userService.findAll().stream().toList());
        long allPlayerOnline = players.stream().filter(UserDTO::isOnline).count();

        return render("player/index.html", Map.of(
            "players", players,
            "allPlayerOnline", allPlayerOnline
        ));
    }

    @GET(value = "/joueurs/s/:username", name = "player.single")
    private Object single(Request req, Response res, UserService userService)
    {
        String username = req.params("username");
        return DB.withConnection(() -> {
            UserDTO player = userService.findByPseudo(username);
            if (player == null) {
                return redirectWithFlash(req, res, "warning", "Player does not exist", "/joueurs");
            }
            return render("player/single.html", Map.of("player", player));
        });
    }
}