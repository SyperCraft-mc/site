package fr.kainovaii.sypercraft.app.domain.faction.repositories;

import fr.kainovaii.sypercraft.app.domain.faction.models.Faction;
import fr.kainovaii.sypercraft.app.domain.faction.models.FactionPlayer;
import com.obsidian.core.database.orm.model.Model;
import com.obsidian.core.di.annotations.Repository;

import java.util.List;

@Repository
public class FactionRepository
{
    public Faction findById(int id) {
        return Model.find(Faction.class, id);
    }

    public Faction findByName(String name) {
        return Faction.findByName(name);
    }

    public List<Faction> findAll() {
        return Model.all(Faction.class);
    }

    public Faction findByUser(String uuid) {
        FactionPlayer fp = FactionPlayer.findByUUID(uuid);
        if (fp == null || !fp.hasFaction()) return null;
        return findById(fp.getFactionId());
    }

    public List<Faction> findByLevel(int level) {
        return Model.query(Faction.class)
                .where("level", level)
                .get();
    }

    public List<Faction> findByMinBalance(long minBalance) {
        return Model.query(Faction.class)
                .where("balance", ">=", minBalance)
                .get();
    }

    public boolean save(Faction faction) {
        return faction.save();
    }

    public boolean delete(int id) {
        Faction faction = Model.find(Faction.class, id);
        if (faction == null) return false;
        return faction.delete();
    }
}