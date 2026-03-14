package fr.kainovaii.sypercraft.app.http.controllers;

import fr.kainovaii.obsidian.http.controller.BaseController;
import fr.kainovaii.obsidian.http.controller.annotations.Controller;
import fr.kainovaii.obsidian.routing.methods.GET;
import spark.Response;

import java.util.Map;

@Controller
public class HomeController extends BaseController
{
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