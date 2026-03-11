package fr.kainovaii.obsidian.app.domain.faction.models;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

@Table("faction_ranks")
public class FactionRank extends Model
{
    public Object getId() { return getInteger("id"); }

    public String getName() { return getString("name"); }
    public void setName(String name) { set("name", name); }

    public int getHierarchy() { return getInteger("hierarchy"); }
    public void setHierarchy(int hierarchy) { set("hierarchy", hierarchy); }

    public String getPermissions() { return getString("permissions"); }
    public void setPermissions(String permissions) { set("permissions", permissions); }

    public int getFactionId() { return getInteger("factionID"); }
    public void setFactionId(int id) { set("factionID", id); }

    public int getTaxe() { return getInteger("taxe"); }
    public void setTaxe(int taxe) { set("taxe", taxe); }

    public static FactionRank findByFactionId(int factionId) {
        return findFirst("factionID = ?", factionId);
    }
}