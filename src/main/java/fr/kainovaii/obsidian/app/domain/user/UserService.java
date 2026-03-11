package fr.kainovaii.obsidian.app.domain.user;

import fr.kainovaii.obsidian.Main;
import fr.kainovaii.obsidian.app.domain.staffrank.StaffRank;
import fr.kainovaii.obsidian.app.domain.staffrank.StaffRankRepository;
import fr.kainovaii.obsidian.app.domain.viprank.VipRank;
import fr.kainovaii.obsidian.app.domain.viprank.VipRankRepository;
import fr.kainovaii.obsidian.app.redis.models.Faction;
import fr.kainovaii.obsidian.app.redis.models.FPlayer;
import fr.kainovaii.obsidian.app.redis.repositories.FactionRepository;
import fr.kainovaii.obsidian.app.redis.repositories.PlayerRepository;
import fr.kainovaii.obsidian.di.annotations.Inject;
import fr.kainovaii.obsidian.di.annotations.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService
{
    @Inject
    private UserRepository userRepository;

    @Inject
    private StaffRankRepository staffRankRepository;

    @Inject
    private VipRankRepository vipRankRepository;

    @Inject
    private PlayerRepository playerRepository;

    @Inject
    private FactionRepository factionRepository;

    public UserDTO findByUUID(String uuid) {
        User user = userRepository.findByUUID(uuid);
        if (user == null) return null;
        return toDTO(user);
    }
    public UserDTO findByPseudo(String pseudo) {
        User user = userRepository.findByPseudo(pseudo);
        if (user == null) return null;
        return toDTO(user);
    }

    public List<UserDTO> findAll() {
        return userRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    private UserDTO toDTO(User user)
    {
        StaffRank staffRank = staffRankRepository.findById(user.getStaffRankID());
        VipRank vipRank = vipRankRepository.findById(user.getVipRankID());
        FPlayer fplayer = factionRepository.findFPlayer(user.getUUID());
        Faction faction = (fplayer != null && fplayer.hasFaction()) ? factionRepository.findById(fplayer.getFactionId()) : null;
        return UserDTO.from(user, staffRank, vipRank, playerRepository, fplayer, faction);
    }

    public int allPlayerOnline()
    {
        Set<String> servers = Main.loadRedis().smembers("NETWORK_SERVERS");
        Set<String> allPlayers = new HashSet<>();
        for (String server : servers) {
            allPlayers.addAll(Main.loadRedis().smembers("PLAYERS:" + server));
        }

        return allPlayers.size();
    }
}