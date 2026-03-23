package fr.kainovaii.sypercraft.app.domain.faction.models;

import com.obsidian.core.database.orm.model.Model;
import com.obsidian.core.database.orm.model.Table;
import com.obsidian.core.database.orm.model.relation.BelongsTo;
import fr.kainovaii.sypercraft.app.domain.user.User;

@Table("faction_players")
public class FactionPlayer extends Model
{
    @Override
    public String primaryKey() {
        return "UUID";
    }

    public BelongsTo<Faction> faction() {
        return belongsTo(Faction.class, "FactionID");
    }

    public BelongsTo<User> user() {
        return belongsTo(User.class, "UUID");
    }

    public BelongsTo<FactionRank> factionRank() {
        return belongsTo(FactionRank.class, "FactionRankID");
    }

    public String getUUID()               { return getString("UUID"); }

    public int getFactionId()             { return getInteger("FactionID"); }
    public void setFactionId(int id)      { set("FactionID", id); }

    public int getFactionRankId()         { return getInteger("FactionRankID"); }
    public void setFactionRankId(int id)  { set("FactionRankID", id); }

    public int getPlayerRankId()          { return getInteger("PlayerRankID"); }
    public void setPlayerRankId(int id)   { set("PlayerRankID", id); }

    public long getSyscoins()             { return getLong("Syscoins"); }
    public void setSyscoins(long s)       { set("Syscoins", s); }

    public int getPower()                 { return getInteger("power"); }
    public void setPower(int power)       { set("power", power); }

    public int getMaxPower()              { return getInteger("maxpower"); }
    public void setMaxPower(int maxPower) { set("maxpower", maxPower); }

    public boolean hasFaction()           { return getFactionId() > 0; }

    public static FactionPlayer findByUUID(String uuid) {
        return Model.query(FactionPlayer.class).where("UUID", uuid).first();
    }
}