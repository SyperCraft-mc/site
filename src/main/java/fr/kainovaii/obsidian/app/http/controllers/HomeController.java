package fr.kainovaii.obsidian.app.http.controllers;

import fr.kainovaii.obsidian.app.domain.post.Post;
import fr.kainovaii.obsidian.app.domain.post.PostRepository;
import fr.kainovaii.obsidian.database.DB;
import fr.kainovaii.obsidian.http.controller.BaseController;
import fr.kainovaii.obsidian.http.controller.annotations.Controller;
import fr.kainovaii.obsidian.routing.methods.GET;
import net.raphimc.minecraftauth.step.java.StepMCProfile;
import spark.Request;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class HomeController extends BaseController
{
    @GET(value = "/", name = "site.home")
    private Object homepage(Request req, PostRepository postRepository)
    {
        List<Post> posts = DB.withConnection(() -> postRepository.findAll().stream().toList());

        return render("home.html", Map.of("posts", posts));
    }

    @GET(value = "/building", name = "site.building")
    private Object building()
    {
        return render("building.html", Map.of());
    }
}