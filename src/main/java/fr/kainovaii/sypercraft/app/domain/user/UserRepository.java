package fr.kainovaii.sypercraft.app.domain.user;

import com.obsidian.core.database.orm.repository.BaseRepository;
import com.obsidian.core.di.annotations.Repository;

import java.util.List;
import java.util.Set;

@Repository
public class UserRepository extends BaseRepository<User>
{
    public UserRepository() {
        super(User.class);
    }

    public User findByUUID(String uuid) {
        return query()
                .with("staffRank", "vipRank", "factionPlayer")
                .where("UUID", uuid)
                .first();
    }

    public User findByPseudo(String pseudo) {
        return query()
                .with("staffRank", "vipRank", "factionPlayer")
                .where("Pseudo", pseudo)
                .first();
    }

    public List<User> findAll() {
        return query()
                .with("staffRank", "vipRank", "factionPlayer")
                .get();
    }

    public List<User> findByUUIDs(Set<String> uuids) {
        if (uuids.isEmpty()) return List.of();
        return query()
                .with("staffRank", "vipRank", "factionPlayer")
                .whereIn("UUID", List.copyOf(uuids))
                .get();
    }

    public List<User> findByStaffRank(int staffRankId) {
        return query()
                .with("staffRank", "vipRank", "factionPlayer")
                .where("StaffRankID", staffRankId)
                .get();
    }

    public List<User> findByVipRank(int vipRankId) {
        return query()
                .with("staffRank", "vipRank", "factionPlayer")
                .where("VipRankID", vipRankId)
                .get();
    }

    public List<User> findInCombat() {
        return query()
                .with("staffRank", "vipRank", "factionPlayer")
                .where("combat", true)
                .get();
    }

    public boolean userExists(String pseudo) {
        return existsWhere("Pseudo", pseudo);
    }
}