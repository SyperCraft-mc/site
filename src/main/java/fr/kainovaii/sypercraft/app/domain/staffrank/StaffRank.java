package fr.kainovaii.sypercraft.app.domain.staffrank;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

@Table("game_staffrank")
public class StaffRank extends Model
{
    public Object getId() {
        return getInteger("id");
    }

    public String getLabel() {
        return getString("label");
    }

    public void setLabel(String label) {
        set("label", label);
    }

    public int getHierarchy() {
        return getInteger("hierarchy");
    }

    public void setHierarchy(int hierarchy) {
        set("hierarchy", hierarchy);
    }

    public String getColorRank() {
        return getString("colorRank");
    }

    public void setColorRank(String colorRank) {
        set("colorRank", colorRank);
    }

    public String getColorPlayerName() {
        return getString("colorPlayerName");
    }

    public void setColorPlayerName(String colorPlayerName) {
        set("colorPlayerName", colorPlayerName);
    }

    public String getColorMessage() {
        return getString("colorMessage");
    }

    public void setColorMessage(String colorMessage) {
        set("colorMessage", colorMessage);
    }

    public String getPermissions() {
        return getString("permissions");
    }

    public void setPermissions(String permissions) {
        set("permissions", permissions);
    }

    public String getDiscordId() {
        return getString("discordid");
    }

    public void setDiscordId(String discordId) {
        set("discordid", discordId);
    }

    public String getColor() {
        return getString("color");
    }

    public void setColor(String color) {
        set("color", color);
    }

    public boolean hasPermission(String permission) {
        String perms = getPermissions();
        if (perms == null || perms.isEmpty()) return false;
        return perms.contains(permission);
    }

    public static StaffRank findByLabel(String label) {
        return findFirst("label = ?", label);
    }

    public static StaffRank findByHierarchy(int hierarchy) {
        return findFirst("hierarchy = ?", hierarchy);
    }
}