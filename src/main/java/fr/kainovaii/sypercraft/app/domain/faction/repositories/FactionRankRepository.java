package fr.kainovaii.sypercraft.app.domain.faction.repositories;

import fr.kainovaii.sypercraft.app.domain.faction.models.FactionRank;
import com.obsidian.core.database.orm.model.Model;
import com.obsidian.core.di.annotations.Repository;

import java.util.List;

@Repository
public class FactionRankRepository
{
    public FactionRank findById(int id) {
        return Model.find(FactionRank.class, id);
    }

    public List<FactionRank> findByFactionId(int factionId) {
        return Model.query(FactionRank.class)
                .where("factionID", factionId)
                .get();
    }

    public List<FactionRank> findByFactionIds(List<Integer> factionIds) {
        if (factionIds.isEmpty()) return List.of();
        return Model.query(FactionRank.class)
                .whereIn("factionID", List.copyOf(factionIds))
                .get();
    }

    public boolean save(FactionRank rank) {
        return rank.save();
    }

    public boolean delete(int id) {
        FactionRank rank = Model.find(FactionRank.class, id);
        if (rank == null) return false;
        return rank.delete();
    }
}