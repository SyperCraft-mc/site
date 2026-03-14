package fr.kainovaii.sypercraft.app.domain.faction.repositories;

import fr.kainovaii.sypercraft.app.domain.faction.models.FactionChunk;
import fr.kainovaii.obsidian.di.annotations.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class FactionChunkRepository
{
    public FactionChunk findById(int id) {
        return FactionChunk.findById(id);
    }

    public List<FactionChunk> findByFactionId(int factionId) {
        return FactionChunk.<FactionChunk>where("idOwner = ?", factionId).load();
    }

    public List<FactionChunk> findByFactionIds(List<Integer> factionIds) {
        if (factionIds.isEmpty()) return List.of();
        String placeholders = factionIds.stream().map(i -> "?").collect(Collectors.joining(", "));
        return FactionChunk.<FactionChunk>where("idOwner IN (" + placeholders + ")", factionIds.toArray()).load();
    }

    public int countByFactionId(int factionId) {
        return FactionChunk.count("idOwner = ?", factionId).intValue();
    }

    public FactionChunk findByCoords(int x, int z) {
        return FactionChunk.findByCoords(x, z);
    }

    public boolean save(FactionChunk chunk) {
        return chunk.saveIt();
    }

    public boolean delete(int id) {
        FactionChunk chunk = FactionChunk.findById(id);
        if (chunk == null) return false;
        return chunk.delete();
    }
}