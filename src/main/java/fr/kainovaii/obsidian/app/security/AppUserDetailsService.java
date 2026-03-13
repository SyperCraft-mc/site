package fr.kainovaii.obsidian.app.security;

import fr.kainovaii.obsidian.app.domain.user.User;
import fr.kainovaii.obsidian.app.domain.user.UserRepository;
import fr.kainovaii.obsidian.database.DB;
import fr.kainovaii.obsidian.security.user.UserDetailsService;
import fr.kainovaii.obsidian.security.user.UserDetailsServiceImpl;

@UserDetailsServiceImpl
public class AppUserDetailsService implements UserDetailsService
{
    private final UserRepository userRepository;

    public AppUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public AppUserDetails loadByUsername(String username)
    {
        return DB.withConnection(() -> {
            if (!UserRepository.userExist(username)) return null;
            User user = userRepository.findByPseudo(username);
            return adapt(user);
        });
    }

    @Override
    public AppUserDetails loadById(Object id)
    {
        return DB.withConnection(() -> {
            User user = User.findById(id);
            return adapt(user);
        });
    }

    private AppUserDetails adapt(User player) {
        if (player == null) return null;
        return new AppUserDetails()
        {
            // Obsidian input
            public String getPassword() { return ""; }

            public String getRole()
            {
                if (player.getPseudo().equalsIgnoreCase("KainoVaii") || player.getPseudo().equalsIgnoreCase("Nam2Sy")) {
                    return "ADMIN";
                } else {
                    return "DEFAULT";
                }
            }

            // SyperCraft input
            public Object getId() { return player.getId(); }
            public String getUsername() { return player.getPseudo(); }
            public int getStaffRank() { return player.getStaffRankID(); }
            public int getVipRank() { return player.getVipRankID(); }
            public String getUUID() { return player.getUUID(); }
            public long getPlaytimeSeconds() { return player.getPlaytimeSeconds(); }
        };
    }
}