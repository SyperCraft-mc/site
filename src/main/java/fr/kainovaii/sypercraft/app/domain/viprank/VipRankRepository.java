package fr.kainovaii.sypercraft.app.domain.viprank;

import com.obsidian.core.database.orm.model.Model;
import com.obsidian.core.di.annotations.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class VipRankRepository
{
    public VipRank findById(int id) {
        return Model.find(VipRank.class, id);
    }

    public Optional<VipRank> findByLabel(String label) {
        return Optional.ofNullable(VipRank.findByLabel(label));
    }

    public List<VipRank> findAll() {
        return Model.all(VipRank.class);
    }

    public List<VipRank> findAllOrderByPriority() {
        return Model.query(VipRank.class)
                .orderByDesc("priority")
                .get();
    }

    public boolean save(VipRank rank) {
        return rank.save();
    }

    public boolean delete(int id) {
        VipRank rank = Model.find(VipRank.class, id);
        if (rank == null) return false;
        return rank.delete();
    }
}