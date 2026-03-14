package fr.kainovaii.sypercraft.app.domain.staffrank;

import fr.kainovaii.obsidian.di.annotations.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class StaffRankRepository
{
    public StaffRank findById(int id) { return StaffRank.findById(id); }

    public Optional<StaffRank> findByLabel(String label) {
        return Optional.ofNullable(StaffRank.findByLabel(label));
    }

    public List<StaffRank> findAll() {
        return StaffRank.findAll().load();
    }

    public List<StaffRank> findAllOrderByHierarchy() {
        return StaffRank.<StaffRank>where("1=1 ORDER BY hierarchy ASC").load();
    }

    public boolean save(StaffRank rank) {
        return rank.saveIt();
    }

    public boolean delete(int id) {
        StaffRank rank = StaffRank.findById(id);
        if (rank == null) return false;
        return rank.delete();
    }
}