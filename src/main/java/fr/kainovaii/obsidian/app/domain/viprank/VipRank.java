package fr.kainovaii.obsidian.app.domain.viprank;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

@Table("game_viprank")
public class VipRank extends Model
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

    public String getColor() {
        return getString("color");
    }

    public void setColor(String color) {
        set("color", color);
    }

    public String getAvantages() {
        return getString("avantages");
    }

    public void setAvantages(String avantages) {
        set("avantages", avantages);
    }

    public int getPriority() {
        return getInteger("priority");
    }

    public void setPriority(int priority) {
        set("priority", priority);
    }

    public static VipRank findByLabel(String label) {
        return findFirst("label = ?", label);
    }

    public static VipRank findHighestPriority() {
        return findFirst("1=1 ORDER BY priority DESC");
    }
}