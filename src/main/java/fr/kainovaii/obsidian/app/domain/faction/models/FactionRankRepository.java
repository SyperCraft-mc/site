package fr.kainovaii.obsidian.app.domain.faction.models;

import fr.kainovaii.obsidian.di.annotations.Repository;

import java.util.List;

@Repository
public class FactionRankRepository
{
    public FactionRank findById(int id) {
        return FactionRank.findById(id);
    }

    public List<FactionRank> findByFactionId(int factionId) {
        return FactionRank.<FactionRank>where("factionID = ?", factionId).load();
    }

    public boolean save(FactionRank rank) {
        return rank.saveIt();
    }

    public boolean delete(int id) {
        FactionRank rank = FactionRank.findById(id);
        if (rank == null) return false;
        return rank.delete();
    }
}