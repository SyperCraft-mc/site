package fr.kainovaii.obsidian.app.http.controllers;

import fr.kainovaii.obsidian.app.domain.faction.FactionDTO;
import fr.kainovaii.obsidian.app.domain.faction.FactionService;
import fr.kainovaii.obsidian.app.domain.user.UserRepository;
import fr.kainovaii.obsidian.app.security.AppUserDetails;
import fr.kainovaii.obsidian.database.DB;
import fr.kainovaii.obsidian.http.controller.BaseController;
import fr.kainovaii.obsidian.http.controller.annotations.Controller;
import fr.kainovaii.obsidian.routing.methods.GET;
import fr.kainovaii.obsidian.security.role.HasRole;
import spark.Request;

import java.util.Map;

@Controller
public class AdminController extends BaseController
{
    @HasRole("ADMIN")
    @GET(value = "/admin", name = "admin.dashboard")
    private Object index()
    {
        return render("admin/dashboard.html", Map.of());
    }
}