package fr.kainovaii.obsidian.app.domain.faction;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;
import org.javalite.activejdbc.annotations.IdName;

@Table("faction_players")
@IdName("UUID")
public class FactionPlayer extends Model
{
    public String getUUID() { return getString("UUID"); }

    public int getFactionId() { return getInteger("FactionID"); }
    public void setFactionId(int id) { set("FactionID", id); }

    public int getFactionRankId() { return getInteger("FactionRankID"); }
    public void setFactionRankId(int id) { set("FactionRankID", id); }

    public int getPlayerRankId() { return getInteger("PlayerRankID"); }
    public void setPlayerRankId(int id) { set("PlayerRankID", id); }

    public long getSyscoins() { return getLong("Syscoins"); }
    public void setSyscoins(long syscoins) { set("Syscoins", syscoins); }

    public int getPower() { return getInteger("power"); }
    public void setPower(int power) { set("power", power); }

    public int getMaxPower() { return getInteger("maxpower"); }
    public void setMaxPower(int maxPower) { set("maxpower", maxPower); }

    public boolean hasFaction() { return getFactionId() > 0; }

    public static FactionPlayer findByUUID(String uuid) {
        return findFirst("UUID = ?", uuid);
    }
}