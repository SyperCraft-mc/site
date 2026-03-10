package fr.kainovaii.obsidian.app.http.controllers;

import fr.kainovaii.obsidian.app.domain.user.UserRepository;
import fr.kainovaii.obsidian.http.controller.BaseController;
import fr.kainovaii.obsidian.http.controller.annotations.Controller;
import fr.kainovaii.obsidian.routing.methods.GET;

import java.util.Map;

@Controller
public class AccountController extends BaseController
{
    @GET(value = "/mon-compte", name = "account.index")
    private Object index(UserRepository userRepository)
    {
        return render("home.html", Map.of());
    }
}