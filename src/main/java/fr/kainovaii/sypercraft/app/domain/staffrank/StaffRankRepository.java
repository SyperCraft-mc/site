package fr.kainovaii.sypercraft.app.domain.staffrank;

import com.obsidian.core.database.orm.model.Model;
import com.obsidian.core.di.annotations.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class StaffRankRepository
{
    public StaffRank findById(int id) {
        return Model.find(StaffRank.class, id);
    }

    public Optional<StaffRank> findByLabel(String label) {
        return Optional.ofNullable(StaffRank.findByLabel(label));
    }

    public List<StaffRank> findAll() {
        return Model.all(StaffRank.class);
    }

    public List<StaffRank> findAllOrderByHierarchy() {
        return Model.query(StaffRank.class)
                .orderBy("hierarchy")
                .get();
    }

    public boolean save(StaffRank rank) {
        return rank.save();
    }

    public boolean delete(int id) {
        StaffRank rank = Model.find(StaffRank.class, id);
        if (rank == null) return false;
        return rank.delete();
    }
}