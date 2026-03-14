package fr.kainovaii.sypercraft.app.domain.faction.repositories;

import fr.kainovaii.sypercraft.app.domain.faction.models.FactionRank;
import fr.kainovaii.obsidian.di.annotations.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class FactionRankRepository
{
    public FactionRank findById(int id) {
        return FactionRank.findById(id);
    }

    public List<FactionRank> findByFactionId(int factionId) {
        return FactionRank.<FactionRank>where("factionID = ?", factionId).load();
    }

    public List<FactionRank> findByFactionIds(List<Integer> factionIds) {
        if (factionIds.isEmpty()) return List.of();
        String placeholders = factionIds.stream().map(i -> "?").collect(Collectors.joining(", "));
        return FactionRank.<FactionRank>where("factionID IN (" + placeholders + ")", factionIds.toArray()).load();
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