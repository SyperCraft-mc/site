package fr.kainovaii.sypercraft.app.domain.viprank;

import com.obsidian.core.di.annotations.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class VipRankRepository
{
    public VipRank findById(int id) { return VipRank.findById(id); }

    public Optional<VipRank> findByLabel(String label) {
        return Optional.ofNullable(VipRank.findByLabel(label));
    }

    public List<VipRank> findAll() {
        return VipRank.findAll().load();
    }

    public List<VipRank> findAllOrderByPriority() {
        return VipRank.<VipRank>where("1=1 ORDER BY priority DESC").load();
    }

    public boolean save(VipRank rank) {
        return rank.saveIt();
    }

    public boolean delete(int id) {
        VipRank rank = VipRank.findById(id);
        if (rank == null) return false;
        return rank.delete();
    }
}