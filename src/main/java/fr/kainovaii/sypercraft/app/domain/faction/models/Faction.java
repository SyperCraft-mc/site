package fr.kainovaii.sypercraft.app.domain.faction.models;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

@Table("faction_list")
public class Faction extends Model
{
    public Object getId() {
        return getInteger("id");
    }

    public String getName() {
        return getString("name");
    }

    public void setName(String name) {
        set("name", name);
    }

    public String getDescription() {
        return getString("description");
    }

    public void setDescription(String description) {
        set("description", description);
    }

    public String getRelationList() {
        return getString("relation_list");
    }

    public void setRelationList(String relationList) {
        set("relation_list", relationList);
    }

    public int getLevel() {
        return getInteger("level");
    }

    public void setLevel(int level) {
        set("level", level);
    }

    public String getZonesList() {
        return getString("zones_list");
    }

    public void setZonesList(String zonesList) {
        set("zones_list", zonesList);
    }

    public String getHome() {
        return getString("home");
    }

    public void setHome(String home) {
        set("home", home);
    }

    public long getBalance() {
        return getLong("balance");
    }

    public void setBalance(long balance) {
        set("balance", balance);
    }

    public int getPoint() {
        return getInteger("point");
    }

    public void setPoint(int point) {
        set("point", point);
    }

    public int getXp() {
        return getInteger("xp");
    }

    public void setXp(int xp) {
        set("xp", xp);
    }

    public void addXp(int amount)
    {
        int newXp = getXp() + amount;
        if (newXp >= 100) {
            setLevel(getLevel() + 1);
            setXp(newXp - 100);
        } else {
            setXp(newXp);
        }
    }

    public static Faction findByName(String name) {
        return findFirst("name = ?", name);
    }
}