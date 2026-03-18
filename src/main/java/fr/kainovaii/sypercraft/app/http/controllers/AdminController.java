package fr.kainovaii.sypercraft.app.http.controllers;

import com.obsidian.core.di.annotations.Inject;
import com.obsidian.core.security.user.RequireLogin;
import fr.kainovaii.sypercraft.app.domain.report.ReportDTO;
import fr.kainovaii.sypercraft.app.domain.report.ReportService;
import com.obsidian.core.database.DB;
import com.obsidian.core.http.controller.BaseController;
import com.obsidian.core.http.controller.annotations.Controller;
import com.obsidian.core.http.middleware.annotations.Before;
import com.obsidian.core.livecomponents.session.SessionMiddleware;
import com.obsidian.core.routing.methods.GET;
import com.obsidian.core.security.role.HasRole;
import spark.Response;

import java.util.List;
import java.util.Map;

@HasRole("ADMIN")
@Controller("/admin")
public class AdminController extends BaseController
{
    @Inject
    ReportService reportService;

    @GET(value = "", name = "admin.index")
    private Object index(Response res)
    {
        res.redirect("/admin/dashboard");
        return "";
    }

    @GET(value = "/dashboard", name = "admin.dashboard")
    private Object dashboard()
    {
        return render("admin/dashboard.html", Map.of());
    }

    @GET(value = "/players", name = "admin.players")
    private Object players()
    {
        return render("admin/player.html", Map.of());
    }

    @GET(value = "/factions", name = "admin.factions")
    private Object faction()
    {
        return render("admin/faction.html", Map.of());
    }

    @GET(value = "/reports", name = "admin.reports")
    private Object reports()
    {
        List<ReportDTO> reports = reportService.findAll().stream().toList();
        return render("admin/report.html", Map.of("reports", reports));
    }
}