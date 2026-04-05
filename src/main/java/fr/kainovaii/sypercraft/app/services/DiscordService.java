package fr.kainovaii.sypercraft.app.services;

import com.obsidian.core.di.annotations.Service;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class DiscordService
{
    private static final String SERVER_ID = "1461904789347762229";
    private static final HttpClient CLIENT = HttpClient.newHttpClient();

    public int memberCount() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://discord.com/api/v10/guilds/" + SERVER_ID + "/widget.json"))
                .GET()
                .build();
        try {
            HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
            return new JSONObject(response.body()).getJSONArray("members").length();
        } catch (Exception e) {
            return 0;
        }
    }
}