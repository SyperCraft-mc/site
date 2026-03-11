package fr.kainovaii.obsidian.app.domain.user;

import fr.kainovaii.obsidian.di.annotations.Repository;

import java.util.List;
import java.util.Optional;

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
        return User.findAll().load();
    }

    public List<User> findByVipRank(int vipRankId) {
        return User.<User>where("VipRankID = ?", vipRankId).load();
    }

    public List<User> findByStaffRank(int staffRankId) {
        return User.<User>where("StaffRankID = ?", staffRankId).load();
    }

    public List<User> findInCombat() {
        return User.<User>where("combat = ?", true).load();
    }

    public static boolean userExist(String pseudo) {
        return User.findFirst("Pseudo = ?", pseudo) != null;
    }

    public boolean save(User user) {
        return user.saveIt();
    }

    public boolean delete(String uuid) {
        User user = User.findByUUID(uuid);
        if (user == null) return false;
        return user.delete();
    }
}