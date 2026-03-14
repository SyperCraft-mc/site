package fr.kainovaii.sypercraft.app.http.websockets;

import fr.kainovaii.obsidian.routing.methods.WebSocket;
import net.raphimc.minecraftauth.step.java.StepMCProfile;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@org.eclipse.jetty.websocket.api.annotations.WebSocket
@WebSocket("/ws/auth")
public class AuthWebSocket
{
    public static final Map<String, Session> sessions = new ConcurrentHashMap<>();
    public static final Map<String, StepMCProfile.MCProfile> readyProfiles = new ConcurrentHashMap<>();

    @OnWebSocketConnect
    public void onConnect(Session session)
    {
        String sid = session.getUpgradeRequest().getParameterMap().getOrDefault("sid", java.util.List.of("unknown")).get(0);

        sessions.put(sid, session);
    }

    @OnWebSocketClose
    public void onClose(Session session, int code, String reason)
    {
        sessions.values().remove(session);
    }

    @OnWebSocketError
    public void onError(Session session, Throwable error)
    {
        sessions.values().remove(session);
    }

    public static void notifySuccess(String sessionId, StepMCProfile.MCProfile profile)
    {
        readyProfiles.put(sessionId, profile);
        send(sessionId, "{\"type\":\"auth_success\",\"pseudo\":\"" + profile.getName() + "\"}");
    }

    public static void notifyError(String sessionId, String message)
    {
        send(sessionId, "{\"type\":\"auth_error\",\"message\":\"" + message + "\"}");
    }

    private static void send(String sessionId, String json)
    {
        Session ws = sessions.get(sessionId);
        if (ws != null && ws.isOpen()) {
            try { ws.getRemote().sendString(json); }
            catch (Exception ignored) {}
        }
    }
}