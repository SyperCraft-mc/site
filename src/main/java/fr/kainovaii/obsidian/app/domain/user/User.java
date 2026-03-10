package fr.kainovaii.obsidian.app.domain.user;

import fr.kainovaii.obsidian.app.security.AppUserDetails;
import org.javalite.activejdbc.Model;

public class User extends Model implements AppUserDetails
{
    // Getters
    public String getUsername() {
        return getString("username");
    }

    @Override
    public String getPassword() {
        return "";
    }

    public String getRole() {
        return getString("role");
    }

    public String getMinecraftUuid() {
        return getString("minecraft_uuid");
    }

    // Setters
    public void setUsername(String username) {
        set("username", username);
    }

    public void setRole(String role) {
        set("role", role);
    }

    public void setMinecraftUuid(String uuid) {
        set("minecraft_uuid", uuid);
    }
}