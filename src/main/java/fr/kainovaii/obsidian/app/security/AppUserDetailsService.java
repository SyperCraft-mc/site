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
            User user = userRepository.findByUsername(username);
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

    private AppUserDetails adapt(User user) {
        if (user == null) return null;
        return new AppUserDetails() {
            public Object getId() { return user.getId(); }
            public String getUsername() { return user.getUsername(); }
            public String getPassword() { return user.getPassword(); }
            public String getRole() { return user.getRole(); }
        };
    }
}