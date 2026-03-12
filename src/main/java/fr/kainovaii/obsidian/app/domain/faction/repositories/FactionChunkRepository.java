package fr.kainovaii.obsidian.app.domain.faction.repositories;

import fr.kainovaii.obsidian.app.domain.faction.models.FactionChunk;
import fr.kainovaii.obsidian.di.annotations.Repository;

import java.util.List;

@Repository
public class FactionChunkRepository
{
    public FactionChunk findById(int id) {
        return FactionChunk.findById(id);
    }

    public List<FactionChunk> findByFactionId(int factionId) {
        return FactionChunk.<FactionChunk>where("idOwner = ?", factionId).load();
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