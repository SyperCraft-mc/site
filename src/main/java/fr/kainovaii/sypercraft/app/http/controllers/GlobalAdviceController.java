package fr.kainovaii.sypercraft.app.http.controllers;

import fr.kainovaii.sypercraft.app.domain.user.UserService;
import fr.kainovaii.sypercraft.app.security.AppUserDetails;
import fr.kainovaii.obsidian.di.annotations.Inject;
import fr.kainovaii.obsidian.http.controller.AdviceControllerInterface;
import fr.kainovaii.obsidian.http.controller.BaseController;
import fr.kainovaii.obsidian.http.controller.annotations.GlobalAdvice;
import spark.Request;
import spark.Response;

import static fr.kainovaii.obsidian.template.TemplateManager.setGlobal;

@GlobalAdvice
public class GlobalAdviceController extends BaseController implements AdviceControllerInterface
{
    @Inject
    UserService userService;

    @Override
    public void applyGlobals(Request req, Response res)
    {
        AppUserDetails appUserDetails = getLoggedUser(req);
        setGlobal("loggedUser", appUserDetails);

        setGlobal("allPlayerOnline", userService.allPlayerOnline());
        setGlobal("allPlayers", userService.countAllerUser());
    }
}