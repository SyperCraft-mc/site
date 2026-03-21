package fr.kainovaii.sypercraft.app.http.controllers;

import com.obsidian.core.security.auth.Auth;
import com.obsidian.core.security.user.CurrentUser;
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
        if (Auth.isLogged()) {
            AppUserDetails appUserDetails = Auth.user();
            setGlobal("loggedUser", appUserDetails);
        } else {
            setGlobal("loggedUser", "");
        }

        setGlobal("allPlayerOnline", userService.allPlayerOnline());
        setGlobal("allPlayers", userService.countAllUsers());
        setGlobal("getEnv", Main.loadEnv().get("ENVIRONMENT"));
    }
}