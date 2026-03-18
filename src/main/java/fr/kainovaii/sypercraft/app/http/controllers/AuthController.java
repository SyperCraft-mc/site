package fr.kainovaii.sypercraft.app.http.controllers;

import com.obsidian.core.di.annotations.Inject;
import com.obsidian.core.security.auth.Auth;
import com.obsidian.core.security.user.RequireLogin;
import fr.kainovaii.sypercraft.app.domain.user.User;
import fr.kainovaii.sypercraft.app.domain.user.UserRepository;
import fr.kainovaii.sypercraft.app.http.websockets.AuthWebSocket;
import fr.kainovaii.sypercraft.app.services.MicrosoftAuthService;
import com.obsidian.core.database.DB;
import com.obsidian.core.http.controller.BaseController;
import com.obsidian.core.http.controller.annotations.Controller;
import com.obsidian.core.routing.methods.GET;
import com.obsidian.core.security.role.HasRole;
import spark.Request;
import spark.Response;
import spark.Session;

import java.util.Map;

@Controller
public class AuthController extends BaseController
{
    @Inject
    UserRepository userRepository;

    @Inject
    MicrosoftAuthService minecraftDeviceAuth;

    @GET(value = "/login", name = "redirectLogin")
    public Object redirectLogin(Request req, Response res)
    {
        res.redirect("/mon-compte/login");
        return "";
    }

    @GET(value = "/mon-compte/login", name = "login")
    public Object loginPage(Request req, Response res)
    {
        return render("user/login.html", Map.of());
    }

    @GET("/minecraft-auth/start")
    public Object startAuth(Request req, Response res)
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
    public Object saveSession(Request req, Response res)
    {
        String sessionId = req.session().id();
        var profile = AuthWebSocket.readyProfiles.remove(sessionId);

        if (profile == null) {
            res.status(401);
            return "No profile ready for this session.";
        }

        if (!UserRepository.userExist(profile.getName())) {
            res.status(401);
            return "No profile ready for this session.";
        }

        User user = userRepository.findByPseudo(profile.getName());

        Session session = req.session(true);
        session.attribute("logged", true);
        session.attribute("user_id", user.getUUID());
        session.attribute("username", user.getPseudo());
        session.attribute("role", "DEFAULT");

        return profile;
    }

    @RequireLogin
    @GET("/mon-compte/logout")
    private Object logout(Request req, Response res)
    {
        Auth.logout(req.session());
        return redirectWithFlash(req, res, "success","Logged out", "/");
    }
}