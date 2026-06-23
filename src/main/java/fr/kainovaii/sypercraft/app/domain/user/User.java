package fr.kainovaii.sypercraft.app.domain.user;

import com.obsidian.core.database.orm.model.Model;
import com.obsidian.core.database.orm.model.Table;
import com.obsidian.core.database.orm.model.relation.BelongsTo;
import com.obsidian.core.database.orm.model.relation.HasOne;
import fr.kainovaii.sypercraft.app.domain.faction.models.FactionPlayer;
import fr.kainovaii.sypercraft.app.domain.rank.StaffRank;
import fr.kainovaii.sypercraft.app.domain.rank.VipRank;

import java.sql.Date;

@Table("game_users")
public class User extends Model
{
    @Override
    public String primaryKey() {
        return "UUID";
    }

    // Relations

    public BelongsTo<StaffRank> staffRank() {
        return belongsTo(StaffRank.class, "StaffRankID");
    }

    public BelongsTo<VipRank> vipRank() {
        return belongsTo(VipRank.class, "VipRankID");
    }

    public HasOne<FactionPlayer> factionPlayer() {
        return hasOne(FactionPlayer.class, "UUID");
    }

    // Accessors

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

    public Date firstConnection() {
        Long timestamp = getLong("first_join");
        return timestamp != null ? new Date(timestamp) : null;
    }

    public Date lastConnection() {
        Long timestamp = getLong("last_logout");
        return timestamp != null ? new Date(timestamp) : null;
    }

    public String getPlaytimeFormatted() {
        long seconds = getPlaytimeSeconds();
        long hours   = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        return hours + "h " + minutes + "m";
    }

    public boolean hasLastConnection() {
        return lastConnection() != null;
    }

    public boolean hasFaction() {
        FactionPlayer fp = factionPlayer().first();
        return fp != null && fp.hasFaction();
    }

    public long getSyscoins() {
        FactionPlayer fp = factionPlayer().first();
        return fp != null ? fp.getSyscoins() : 0;
    }

    public static User findByUUID(String uuid) {
        return Model.query(User.class).where("UUID", uuid).first();
    }

    public static User findByPseudo(String pseudo) {
        return Model.query(User.class).where("Pseudo", pseudo).first();
    }
}