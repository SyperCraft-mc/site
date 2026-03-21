package fr.kainovaii.sypercraft.app.domain.faction.repositories;

import fr.kainovaii.sypercraft.app.domain.faction.models.FactionPlayer;
import com.obsidian.core.database.orm.model.Model;
import com.obsidian.core.di.annotations.Repository;

import java.util.List;

@Repository
public class FactionPlayerRepository
{
    public FactionPlayer findByUUID(String uuid) {
        return FactionPlayer.findByUUID(uuid);
    }

    public List<FactionPlayer> findByFactionId(int factionId) {
        return Model.query(FactionPlayer.class)
                .where("FactionID", factionId)
                .get();
    }

    public List<FactionPlayer> findByFactionIds(List<Integer> factionIds) {
        if (factionIds.isEmpty()) return List.of();
        return Model.query(FactionPlayer.class)
                .whereIn("FactionID", List.copyOf(factionIds))
                .get();
    }

    public int countByFactionId(int factionId) {
        return (int) Model.query(FactionPlayer.class)
                .where("FactionID", factionId)
                .count();
    }

    public boolean save(FactionPlayer fp) {
        return fp.save();
    }

    public boolean delete(String uuid) {
        FactionPlayer fp = FactionPlayer.findByUUID(uuid);
        if (fp == null) return false;
        return fp.delete();
    }
}