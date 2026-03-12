package fr.kainovaii.obsidian.app.domain.faction.repositories;

import fr.kainovaii.obsidian.app.domain.faction.models.FactionPlayer;
import fr.kainovaii.obsidian.di.annotations.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class FactionPlayerRepository
{
    public FactionPlayer findByUUID(String uuid) {
        return FactionPlayer.findByUUID(uuid);
    }

    public List<FactionPlayer> findByFactionId(int factionId) {
        return FactionPlayer.<FactionPlayer>where("FactionID = ?", factionId).load();
    }

    public List<FactionPlayer> findByFactionIds(List<Integer> factionIds) {
        if (factionIds.isEmpty()) return List.of();
        String placeholders = factionIds.stream().map(i -> "?").collect(Collectors.joining(", "));
        return FactionPlayer.<FactionPlayer>where("FactionID IN (" + placeholders + ")", factionIds.toArray()).load();
    }

    public int countByFactionId(int factionId) {
        return FactionPlayer.count("FactionID = ?", factionId).intValue();
    }

    public boolean save(FactionPlayer fp) {
        return fp.saveIt();
    }

    public boolean delete(String uuid) {
        FactionPlayer fp = FactionPlayer.findByUUID(uuid);
        if (fp == null) return false;
        return fp.delete();
    }
}