package fr.kainovaii.obsidian.app.http.controllers;

import fr.kainovaii.obsidian.app.domain.user.User;
import fr.kainovaii.obsidian.app.domain.user.UserRepository;
import fr.kainovaii.obsidian.app.redis.models.*;
import fr.kainovaii.obsidian.app.redis.repositories.FactionRepository;
import fr.kainovaii.obsidian.app.redis.repositories.PlayerRepository;
import fr.kainovaii.obsidian.app.utils.ApiUtile;
import fr.kainovaii.obsidian.database.DB;
import fr.kainovaii.obsidian.http.controller.BaseController;
import fr.kainovaii.obsidian.http.controller.annotations.Controller;
import fr.kainovaii.obsidian.routing.methods.GET;
import spark.Request;
import spark.Response;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
public class PlayerController extends BaseController
{

    @GET(value = "/joueurs", name = "player.index")
    private Object index(UserRepository userRepository)
    {
        List<User> players = DB.withConnection(() -> userRepository.findAll().stream().toList());
        return render("player/index.html", Map.of("players", players));
    }

    @GET(value = "/joueurs/s/:username", name = "player.single")
    private Object single(Request req, Response res, UserRepository userRepository)
    {
        String username = req.params("username");
        boolean userExist = DB.withConnection(() -> UserRepository.userExist(username));

        if (userExist) {
            User player = DB.withConnection(() -> userRepository.findByUsername(username));
            return render("player/single.html", Map.of("player", player));
        } else {
            return redirectWithFlash(req, res, "warning", "Player does not exist", "/joueurs");
        }
    }
}