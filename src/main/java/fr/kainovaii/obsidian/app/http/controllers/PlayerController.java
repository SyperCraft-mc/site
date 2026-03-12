package fr.kainovaii.obsidian.app.http.controllers;

import fr.kainovaii.obsidian.app.domain.staffrank.StaffRank;
import fr.kainovaii.obsidian.app.domain.staffrank.StaffRankRepository;
import fr.kainovaii.obsidian.app.domain.user.User;
import fr.kainovaii.obsidian.app.domain.user.UserDTO;
import fr.kainovaii.obsidian.app.domain.user.UserRepository;
import fr.kainovaii.obsidian.app.domain.user.UserService;
import fr.kainovaii.obsidian.app.domain.viprank.VipRank;
import fr.kainovaii.obsidian.app.domain.viprank.VipRankRepository;
import fr.kainovaii.obsidian.app.redis.models.*;
import fr.kainovaii.obsidian.database.DB;
import fr.kainovaii.obsidian.http.controller.BaseController;
import fr.kainovaii.obsidian.http.controller.annotations.Controller;
import fr.kainovaii.obsidian.routing.methods.GET;
import spark.Request;
import spark.Response;

import java.util.List;
import java.util.Map;

@Controller
public class PlayerController extends BaseController
{
    @GET(value = "/joueurs", name = "player.index")
    private Object index(UserService userService)
    {
        List<UserDTO> players = DB.withConnection(() -> userService.findAll().stream().toList());
        return render("player/index.html", Map.of("players", players));
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