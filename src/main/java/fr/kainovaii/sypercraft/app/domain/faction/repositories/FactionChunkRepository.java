package fr.kainovaii.sypercraft.app.domain.faction.repositories;

import com.obsidian.core.database.orm.repository.BaseRepository;
import fr.kainovaii.sypercraft.app.domain.faction.models.FactionChunk;
import com.obsidian.core.database.orm.model.Model;
import com.obsidian.core.di.annotations.Repository;

import java.util.List;

@Repository
public class FactionChunkRepository extends BaseRepository<FactionChunk>
{
    public FactionChunkRepository() {
        super(FactionChunk.class);
    }

    public FactionChunk findById(int id) {
        return findById(id);
    }

    public List<FactionChunk> findByFactionId(int factionId) {
        return Model.query(FactionChunk.class)
                .where("idOwner", factionId)
                .get();
    }

    public List<FactionChunk> findByFactionIds(List<Integer> factionIds) {
        if (factionIds.isEmpty()) return List.of();
        return Model.query(FactionChunk.class)
                .whereIn("idOwner", List.copyOf(factionIds))
                .get();
    }

    public int countByFactionId(int factionId) {
        return (int) Model.query(FactionChunk.class)
                .where("idOwner", factionId)
                .count();
    }

    public FactionChunk findByCoords(int x, int z) {
        return FactionChunk.findByCoords(x, z);
    }

    public boolean save(FactionChunk chunk) {
        return chunk.save();
    }

    public boolean delete(int id) {
        FactionChunk chunk = Model.find(FactionChunk.class, id);
        if (chunk == null) return false;
        return chunk.delete();
    }
}