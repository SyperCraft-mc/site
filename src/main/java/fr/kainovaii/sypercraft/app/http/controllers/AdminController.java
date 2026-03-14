package fr.kainovaii.sypercraft.app.http.controllers;

import fr.kainovaii.obsidian.di.annotations.Inject;
import fr.kainovaii.sypercraft.app.domain.report.ReportDTO;
import fr.kainovaii.sypercraft.app.domain.report.ReportService;
import fr.kainovaii.obsidian.database.DB;
import fr.kainovaii.obsidian.http.controller.BaseController;
import fr.kainovaii.obsidian.http.controller.annotations.Controller;
import fr.kainovaii.obsidian.http.middleware.annotations.Before;
import fr.kainovaii.obsidian.livecomponents.session.SessionMiddleware;
import fr.kainovaii.obsidian.routing.methods.GET;
import fr.kainovaii.obsidian.security.role.HasRole;
import spark.Response;

import java.util.List;
import java.util.Map;

@Controller
public class AdminController extends BaseController
{
    @Inject
    ReportService reportService;

    @HasRole("DEFAULT")
    @GET(value = "/admin", name = "admin.index")
    private Object index(Response res)
    {
        res.redirect("/admin/dashboard");
        return "";
    }

    @HasRole("DEFAULT")
    @GET(value = "/admin/dashboard", name = "admin.dashboard")
    private Object dashboard()
    {
        return render("admin/dashboard.html", Map.of());
    }

    @HasRole("DEFAULT")
    @Before(SessionMiddleware.class)
    @GET(value = "/admin/players", name = "admin.players")
    private Object players()
    {
        return render("admin/player.html", Map.of());
    }

    @HasRole("DEFAULT")
    @Before(SessionMiddleware.class)
    @GET(value = "/admin/factions", name = "admin.factions")
    private Object faction()
    {
        return render("admin/faction.html", Map.of());
    }

    @HasRole("DEFAULT")
    @Before(SessionMiddleware.class)
    @GET(value = "/admin/reports", name = "admin.reports")
    private Object reports()
    {
        List<ReportDTO> reports = reportService.findAll().stream().toList();
        return render("admin/report.html", Map.of("reports", reports));
    }
}