package fr.kainovaii.obsidian.app.services;

import fr.kainovaii.obsidian.app.http.websockets.AuthWebSocket;
import fr.kainovaii.obsidian.di.annotations.Service;
import net.raphimc.minecraftauth.MinecraftAuth;
import net.raphimc.minecraftauth.step.java.StepMCProfile;
import net.raphimc.minecraftauth.step.msa.StepMsaDeviceCode;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class MicrosoftAuthService
{
    private final Map<String, PendingAuth> pendingAuths = new ConcurrentHashMap<>();

    public PendingAuth startAuth(String sessionId) throws Exception
    {
        var httpClient = MinecraftAuth.createHttpClient();
        var pending = new PendingAuth();

        CompletableFuture.runAsync(() -> {
            try {
                var session = MinecraftAuth.JAVA_DEVICE_CODE_LOGIN.getFromInput(
                    httpClient,
                    new StepMsaDeviceCode.MsaDeviceCodeCallback(code -> {
                        pending.link = code.getVerificationUri();
                        pending.code = code.getUserCode();
                    })
                );

                pending.profile = session.getMcProfile();
                pending.done    = true;

                AuthWebSocket.notifySuccess(sessionId, pending.profile);

            } catch (Exception e) {
                pending.error = e.getMessage();
                pending.done  = true;

                AuthWebSocket.notifyError(sessionId, e.getMessage());
            }
        });

        long deadline = System.currentTimeMillis() + 5000;
        while (pending.link == null && System.currentTimeMillis() < deadline) {
            Thread.sleep(100);
        }

        pendingAuths.put(sessionId, pending);
        return pending;
    }

    public PendingAuth getPendingAuth(String sessionId)
    {
        return pendingAuths.get(sessionId);
    }

    public void clearAuth(String sessionId)
    {
        pendingAuths.remove(sessionId);
    }

    public static class PendingAuth
    {
        public volatile String link;
        public volatile String code;
        public volatile StepMCProfile.MCProfile profile;
        public volatile boolean done  = false;
        public volatile String  error;
    }
}