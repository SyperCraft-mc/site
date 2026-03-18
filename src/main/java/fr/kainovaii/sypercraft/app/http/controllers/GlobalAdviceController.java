package fr.kainovaii.sypercraft.app.http.controllers;

import fr.kainovaii.sypercraft.Main;
import fr.kainovaii.sypercraft.app.domain.user.UserService;
import fr.kainovaii.sypercraft.app.security.AppUserDetails;
import com.obsidian.core.di.annotations.Inject;
import com.obsidian.core.http.controller.AdviceControllerInterface;
import com.obsidian.core.http.controller.BaseController;
import com.obsidian.core.http.controller.annotations.GlobalAdvice;
import spark.Request;
import spark.Response;

import static com.obsidian.core.template.TemplateManager.setGlobal;

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
        setGlobal("getEnv", Main.loadEnv().get("ENVIRONMENT"));
    }
}