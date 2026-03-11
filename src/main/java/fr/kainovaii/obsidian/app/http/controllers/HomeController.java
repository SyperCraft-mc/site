package fr.kainovaii.obsidian.app.http.controllers;

import fr.kainovaii.obsidian.Main;
import fr.kainovaii.obsidian.http.controller.BaseController;
import fr.kainovaii.obsidian.http.controller.annotations.Controller;
import fr.kainovaii.obsidian.routing.methods.GET;
import spark.Request;
import spark.Response;

import java.util.LinkedHashMap;
import java.util.Map;

@Controller
public class HomeController extends BaseController
{
    @GET(value = "/", name = "site.home")
    private Object homepage()
    {
        return render("home.html", Map.of());
    }

    @GET(value = "/building", name = "site.building")
    private Object building()
    {
        return render("building.html", Map.of());
    }
}