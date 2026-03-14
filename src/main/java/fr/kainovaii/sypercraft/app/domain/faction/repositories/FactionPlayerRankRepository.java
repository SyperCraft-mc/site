package fr.kainovaii.sypercraft.app.domain.faction.repositories;

import fr.kainovaii.sypercraft.app.domain.faction.models.FactionPlayerRank;
import fr.kainovaii.obsidian.di.annotations.Repository;

import java.util.List;

@Repository
public class FactionPlayerRankRepository
{
    public FactionPlayerRank findById(int id) {
        return FactionPlayerRank.findById(id);
    }

    public List<FactionPlayerRank> findAll() {
        return FactionPlayerRank.findAll().load();
    }

    public List<FactionPlayerRank> findAllOrderByHierarchy() {
        return FactionPlayerRank.<FactionPlayerRank>where("1=1 ORDER BY hierarchy ASC").load();
    }

    public boolean save(FactionPlayerRank rank) {
        return rank.saveIt();
    }

    public boolean delete(int id) {
        FactionPlayerRank rank = FactionPlayerRank.findById(id);
        if (rank == null) return false;
        return rank.delete();
    }
}