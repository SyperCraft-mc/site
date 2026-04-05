package fr.kainovaii.sypercraft.app.http.controllers;

import com.obsidian.core.di.annotations.Inject;
import com.obsidian.core.http.controller.BaseController;
import com.obsidian.core.http.controller.annotations.Controller;
import com.obsidian.core.routing.methods.GET;
import fr.kainovaii.sypercraft.app.domain.faction.models.Faction;
import fr.kainovaii.sypercraft.app.domain.user.User;
import fr.kainovaii.sypercraft.app.domain.user.UserRepository;
import fr.kainovaii.sypercraft.Main;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.Map;

@Controller
public class PlayerController extends BaseController
{
    @Inject
    private UserRepository userRepository;

    @GET(value = "/joueurs", name = "player.index")
    private Object index()
    {
        long allPlayerOnline = Main.loadRedis().keys("SERVER:*").size();

        return render("player/index.html", Map.of(
                "allPlayerOnline", allPlayerOnline
        ));
    }

    @GET(value = "/joueurs/s/:username", name = "player.single")
    private Object single(Request req, Response res)
    {
        String username = req.params("username");

        User player = userRepository.findByPseudo(username);
        if (player == null) {
            return redirectWithWarning("Player does not exist", "/joueurs");
        }

        boolean online = !Main.loadRedis().keys("SERVER:" + player.getUUID()).isEmpty();
        Faction faction = player.hasFaction()
                ? player.factionPlayer().first().faction().first()
                : null;

        Map<String, Object> model = new HashMap<>();
        model.put("player",     player);
        model.put("online",     online);
        model.put("faction",    faction);
        model.put("hasFaction", faction != null);

        return render("player/single.html", model);
    }
}