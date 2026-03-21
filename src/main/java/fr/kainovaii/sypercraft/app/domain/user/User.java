package fr.kainovaii.sypercraft.app.domain.user;

import com.obsidian.core.database.orm.model.Model;
import com.obsidian.core.database.orm.model.Table;

import java.sql.Date;

@Table("game_users")
public class User extends Model
{
    @Override
    public String primaryKey() {
        return "UUID";
    }

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
        return Model.query(User.class).where("UUID", uuid).first();
    }

    public static User findByPseudo(String pseudo) {
        return Model.query(User.class).where("Pseudo", pseudo).first();
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