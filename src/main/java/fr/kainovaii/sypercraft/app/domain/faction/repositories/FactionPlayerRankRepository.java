package fr.kainovaii.sypercraft.app.domain.faction.repositories;

import fr.kainovaii.sypercraft.app.domain.faction.models.FactionPlayerRank;
import com.obsidian.core.database.orm.model.Model;
import com.obsidian.core.di.annotations.Repository;

import java.util.List;

@Repository
public class FactionPlayerRankRepository
{
    public FactionPlayerRank findById(int id) {
        return Model.find(FactionPlayerRank.class, id);
    }

    public List<FactionPlayerRank> findAll() {
        return Model.all(FactionPlayerRank.class);
    }

    public List<FactionPlayerRank> findAllOrderByHierarchy() {
        return Model.query(FactionPlayerRank.class)
                .orderBy("hierarchy")
                .get();
    }

    public boolean save(FactionPlayerRank rank) {
        return rank.save();
    }

    public boolean delete(int id) {
        FactionPlayerRank rank = Model.find(FactionPlayerRank.class, id);
        if (rank == null) return false;
        return rank.delete();
    }
}