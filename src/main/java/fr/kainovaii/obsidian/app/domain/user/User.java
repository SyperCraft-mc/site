package fr.kainovaii.obsidian.app.domain.user;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;
import org.javalite.activejdbc.annotations.IdName;

import java.sql.Date;

@Table("game_users")
@IdName("UUID")
public class User extends Model
{
    public String getUUID() {
        return getString("UUID");
    }

    public String getPseudo() { return getString("Pseudo"); }

    public void setPseudo(String pseudo) {
        set("Pseudo", pseudo);
    }

    public int getStaffRankID() {
        return getInteger("StaffRankID");
    }

    public void setStaffRankID(int rankId) {
        set("StaffRankID", rankId);
    }

    public int getVipRankID() {
        return getInteger("VipRankID");
    }

    public void setVipRankID(int rankId) {
        set("VipRankID", rankId);
    }

    public Long getVipRankAssignedAt() {
        return getLong("vip_rank_assigned_at");
    }

    public void setVipRankAssignedAt(Long timestamp) {
        set("vip_rank_assigned_at", timestamp);
    }

    public int getPointBoutique() {
        return getInteger("PointBoutique");
    }

    public void setPointBoutique(int points) {
        set("PointBoutique", points);
    }

    public boolean isInCombat() {
        return getBoolean("combat");
    }

    public void setCombat(boolean combat) {
        set("combat", combat);
    }

    public String getIgnorePlayers() {
        return getString("ignorePlayers");
    }

    public void setIgnorePlayers(String ignorePlayers) {
        set("ignorePlayers", ignorePlayers);
    }

    public String getDiscordId() {
        return getString("discordid");
    }

    public void setDiscordId(String discordId) {
        set("discordid", discordId);
    }

    public long getPlaytimeSeconds() {
        return getLong("playtime_seconds");
    }

    public void setPlaytimeSeconds(long seconds) {
        set("playtime_seconds", seconds);
    }

    public static User findByUUID(String uuid) {
        return findFirst("UUID = ?", uuid);
    }

    public static User findByPseudo(String pseudo) {
        return findFirst("Pseudo = ?", pseudo);
    }

    public Date firstConnection() {
        Long timestamp = getLong("first_join");
        return timestamp != null ? new Date(timestamp) : null;
    }

    public Date lastConnection() {
        Long timestamp = getLong("last_logout");
        return timestamp != null ? new Date(timestamp) : null;
    }
}