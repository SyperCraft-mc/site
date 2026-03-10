package fr.kainovaii.obsidian.app.domain.user;

import fr.kainovaii.obsidian.di.annotations.Repository;
import org.javalite.activejdbc.LazyList;

@Repository
public class UserRepository
{
    public LazyList<User> findAll() {
        return User.findAll();
    }

    public User findByUsername(String username) {
        return User.findFirst("username = ?", username);
    }

    public User findByMinecraftUuid(String uuid) {
        return User.findFirst("minecraft_uuid = ?", uuid);
    }

    public User findOrCreateByMinecraftProfile(String uuid, String username)
    {
        User user = findByMinecraftUuid(uuid);
        if (user == null) {
            user = new User();
            user.setUsername(username);
            user.setMinecraftUuid(uuid);
            user.setRole("DEFAULT");
            user.saveIt();
        }
        return user;
    }

    public static boolean userExist(String username) {
        return User.findFirst("username = ?", username) != null;
    }

    public boolean delete(String username)
    {
        User user = findByUsername(username);
        return user.delete();
    }

    public Boolean create(String username, String minecraftUuid, String role)
    {
        User user = new User();
        user.setUsername(username);
        user.setMinecraftUuid(minecraftUuid);
        user.setRole(role);
        return user.saveIt();
    }

    public Boolean update(String usernameCurrent, String username, String role)
    {
        User user = findByUsername(usernameCurrent);
        user.setUsername(username);
        user.setRole(role);
        return user.saveIt();
    }
}