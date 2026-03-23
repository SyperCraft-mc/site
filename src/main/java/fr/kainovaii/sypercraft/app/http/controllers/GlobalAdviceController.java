package fr.kainovaii.sypercraft.app.http.controllers;

import com.obsidian.core.database.orm.model.Model;
import com.obsidian.core.di.annotations.Inject;
import com.obsidian.core.http.controller.AdviceControllerInterface;
import com.obsidian.core.http.controller.BaseController;
import com.obsidian.core.http.controller.annotations.GlobalAdvice;
import com.obsidian.core.security.auth.Auth;
import fr.kainovaii.sypercraft.Main;
import fr.kainovaii.sypercraft.app.domain.user.User;
import fr.kainovaii.sypercraft.app.domain.user.UserRepository;
import fr.kainovaii.sypercraft.app.security.AppUserDetails;
import spark.Request;
import spark.Response;

import static com.obsidian.core.template.TemplateManager.setGlobal;

@GlobalAdvice
public class GlobalAdviceController extends BaseController implements AdviceControllerInterface
{
    @Inject
    private UserRepository userRepository;

    @Override
    public void applyGlobals(Request req, Response res)
    {
        if (Auth.isLogged()) {
            AppUserDetails appUserDetails = Auth.user();
            setGlobal("loggedUser", appUserDetails);
        }

        setGlobal("allPlayerOnline", Main.loadRedis().keys("SERVER:*").size());
        setGlobal("allPlayers", userRepository.count());
        setGlobal("getEnv", Main.loadEnv().get("ENVIRONMENT"));
    }
}