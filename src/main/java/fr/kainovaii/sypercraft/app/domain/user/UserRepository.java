package fr.kainovaii.sypercraft.app.domain.user;

import com.obsidian.core.database.orm.model.Model;
import com.obsidian.core.di.annotations.Repository;

import java.util.List;
import java.util.Set;

@Repository
public class UserRepository
{
    public User findByUUID(String uuid) {
        return User.findByUUID(uuid);
    }

    public User findByPseudo(String pseudo) {
        return User.findByPseudo(pseudo);
    }

    public List<User> findAll() {
        return Model.all(User.class);
    }

    public List<User> findByUUIDs(Set<String> uuids) {
        if (uuids.isEmpty()) return List.of();
        return Model.query(User.class)
                .whereIn("UUID", List.copyOf(uuids))
                .get();
    }

    public List<User> findByVipRank(int vipRankId) {
        return Model.query(User.class)
                .where("VipRankID", vipRankId)
                .get();
    }

    public List<User> findByStaffRank(int staffRankId) {
        return Model.query(User.class)
                .where("StaffRankID", staffRankId)
                .get();
    }

    public List<User> findInCombat() {
        return Model.query(User.class)
                .where("combat", true)
                .get();
    }

    public boolean userExists(String pseudo) {
        return Model.query(User.class)
                .where("Pseudo", pseudo)
                .exists();
    }

    public boolean save(User user) {
        return user.save();
    }

    public boolean delete(String uuid) {
        User user = User.findByUUID(uuid);
        if (user == null) return false;
        return user.delete();
    }
}