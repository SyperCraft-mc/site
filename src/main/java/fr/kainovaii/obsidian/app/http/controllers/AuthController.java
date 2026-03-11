package fr.kainovaii.obsidian.app.http.controllers;

import fr.kainovaii.obsidian.app.domain.user.User;
import fr.kainovaii.obsidian.app.domain.user.UserRepository;
import fr.kainovaii.obsidian.app.http.websockets.AuthWebSocket;
import fr.kainovaii.obsidian.app.services.MicrosoftAuthService;
import fr.kainovaii.obsidian.database.DB;
import fr.kainovaii.obsidian.http.controller.BaseController;
import fr.kainovaii.obsidian.http.controller.annotations.Controller;
import fr.kainovaii.obsidian.routing.methods.GET;
import fr.kainovaii.obsidian.security.role.HasRole;
import spark.Request;
import spark.Response;
import spark.Session;

import java.util.Map;

@Controller
public class AuthController extends BaseController
{
    @GET(value = "/mon-compte/login", name = "login")
    public Object loginPage(Request req, Response res)
    {
        return render("user/login.html", Map.of());
    }

    @GET("/minecraft-auth/start")
    public Object startAuth(Request req, Response res, MicrosoftAuthService minecraftDeviceAuth)
    {
        String sessionId = req.session().id();

        try {
            var pending = minecraftDeviceAuth.startAuth(sessionId);

            if (pending.link == null) {
                res.status(500);
                return "{\"error\":\"Failed to retrieve device code in time.\"}";
            }

            res.type("application/json");
            return "{\"code\":\"" + pending.code + "\","
                    + "\"link\":\"" + pending.link + "\","
                    + "\"sessionId\":\"" + sessionId + "\"}";

        } catch (Exception e) {
            e.printStackTrace();
            res.status(500);
            return "{\"error\":\"" + e.getMessage() + "\"}";
        }
    }

    @GET("/minecraft-auth/session")
    public Object saveSession(Request req, Response res, UserRepository userRepository)
    {
        String sessionId = req.session().id();
        var profile = AuthWebSocket.readyProfiles.remove(sessionId);

        if (profile == null) {
            res.status(401);
            return "No profile ready for this session.";
        }

        if (!DB.withConnection(() -> UserRepository.userExist(profile.getName()))) {
            res.status(401);
            return "No profile ready for this session.";
        }

        User user = DB.withConnection(() -> userRepository.findByPseudo(profile.getName()));

        Session session = req.session(true);
        session.attribute("logged", true);
        session.attribute("user_id", user.getUUID());
        session.attribute("username", user.getPseudo());
        session.attribute("role", "DEFAULT");

        return profile;
    }

    @HasRole("DEFAULT")
    @GET("/mon-compte/logout")
    private Object logout(Request req, Response res)
    {
        logout(req.session(true));
        return redirectWithFlash(req, res, "success","Logged out", "/");
    }
}