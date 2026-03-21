package fr.kainovaii.sypercraft.app.domain.faction.models;

import com.obsidian.core.database.orm.model.Model;
import com.obsidian.core.database.orm.model.Table;

@Table("faction_playerrank")
public class FactionPlayerRank extends Model
{
    public Object getId() { return getInteger("id"); }

    public String getLabel() { return getString("label"); }
    public void setLabel(String label) { set("label", label); }

    public int getHierarchy() { return getInteger("hierarchy"); }
    public void setHierarchy(int hierarchy) { set("hierarchy", hierarchy); }

    public String getColorRank() { return getString("colorRank"); }
    public void setColorRank(String color) { set("colorRank", color); }

    public String getColorPlayerName() { return getString("colorPlayerName"); }
    public void setColorPlayerName(String color) { set("colorPlayerName", color); }

    public String getColorMessage() { return getString("colorMessage"); }
    public void setColorMessage(String color) { set("colorMessage", color); }

    public String getPermissions() { return getString("permissions"); }
    public void setPermissions(String permissions) { set("permissions", permissions); }

    public String getAvantages() { return getString("avantages"); }
    public void setAvantages(String avantages) { set("avantages", avantages); }
}