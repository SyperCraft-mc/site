package fr.kainovaii.sypercraft.app.domain.faction.models;

import com.obsidian.core.database.orm.model.Model;
import com.obsidian.core.database.orm.model.Table;

@Table("faction_chunks")
public class FactionChunk extends Model
{
    public Object getId() { return getInteger("id"); }

    public int getIdOwner() { return getInteger("idOwner"); }
    public void setIdOwner(int idOwner) { set("idOwner", idOwner); }

    public int getX() { return getInteger("x"); }
    public void setX(int x) { set("x", x); }

    public int getZ() { return getInteger("z"); }
    public void setZ(int z) { set("z", z); }

    public int getType() { return getInteger("type"); }
    public void setType(int type) { set("type", type); }

    public static FactionChunk findByCoords(int x, int z) {
        return Model.query(FactionChunk.class)
                .where("x", x)
                .where("z", z)
                .first();
    }
}