package fr.kainovaii.sypercraft.app.http.controllers;

import com.obsidian.core.di.annotations.Inject;
import com.obsidian.core.http.controller.BaseController;
import com.obsidian.core.http.controller.annotations.Controller;
import com.obsidian.core.routing.methods.GET;
import fr.kainovaii.sypercraft.app.services.RedisService;
import spark.Response;

import java.util.Map;

@Controller
public class HomeController extends BaseController
{
    @Inject
    RedisService redisService;

    @GET(value = "/", name = "site.home")
    private Object index(Response res)
    {
        res.redirect("/accueil");
        return"";
    }

    @GET(value = "/accueil", name = "site.home")
    private Object homepage()
    {
        return render("home.html", Map.of());
    }

    @GET(value = "/construction", name = "site.building")
    private Object building() { return render("building.html", Map.of()); }

    @GET(value = "/mentions-legales", name = "site.legal")
    private Object legal() { return render("legal.html", Map.of()); }

    @GET(value = "/contact", name = "site.legal")
    private Object contact() { return render("contact.html", Map.of()); }
}