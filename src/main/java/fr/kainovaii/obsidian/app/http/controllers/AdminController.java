package fr.kainovaii.obsidian.app.http.controllers;

import fr.kainovaii.obsidian.app.domain.faction.FactionDTO;
import fr.kainovaii.obsidian.app.domain.faction.FactionService;
import fr.kainovaii.obsidian.app.domain.user.UserRepository;
import fr.kainovaii.obsidian.app.security.AppUserDetails;
import fr.kainovaii.obsidian.database.DB;
import fr.kainovaii.obsidian.http.controller.BaseController;
import fr.kainovaii.obsidian.http.controller.annotations.Controller;
import fr.kainovaii.obsidian.http.middleware.annotations.Before;
import fr.kainovaii.obsidian.livecomponents.session.SessionMiddleware;
import fr.kainovaii.obsidian.routing.methods.GET;
import fr.kainovaii.obsidian.security.role.HasRole;
import spark.Request;
import spark.Response;

import java.util.Map;

@Controller
public class AdminController extends BaseController
{
    @HasRole("ADMIN")
    @GET(value = "/admin", name = "admin.index")
    private Object index(Response res)
    {
        res.redirect("/admin/dashboard");
        return "";
    }

    @HasRole("ADMIN")
    @GET(value = "/admin/dashboard", name = "admin.dashboard")
    private Object dashboard()
    {
        return render("admin/dashboard.html", Map.of());
    }

    @HasRole("ADMIN")
    @Before(SessionMiddleware.class)
    @GET(value = "/admin/players", name = "admin.dashboard")
    private Object players()
    {
        return render("admin/player.html", Map.of());
    }

    @HasRole("ADMIN")
    @Before(SessionMiddleware.class)
    @GET(value = "/admin/factions", name = "admin.dashboard")
    private Object faction()
    {
        return render("admin/faction.html", Map.of());
    }
}