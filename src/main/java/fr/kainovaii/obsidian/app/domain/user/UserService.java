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

import java.util.*;
import java.util.stream.Collectors;

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
        return toDTOSingle(user);
    }

    public UserDTO findByPseudo(String pseudo) {
        User user = userRepository.findByPseudo(pseudo);
        if (user == null) return null;
        return toDTOSingle(user);
    }

    public List<UserDTO> findAll() {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) return Collections.emptyList();

        // --- SQL : 1 requête par table rank ---
        Map<Integer, StaffRank> staffRanks = staffRankRepository.findAll()
                .stream()
                .collect(Collectors.toMap(r -> (Integer) r.getId(), r -> r));

        Map<Integer, VipRank> vipRanks = vipRankRepository.findAll()
                .stream()
                .collect(Collectors.toMap(r -> (Integer) r.getId(), r -> r));

        // --- Redis : tous les serveurs en une passe ---
        Map<String, String> onlineMap = buildOnlineMap();

        // --- Redis : tous les FPlayers en une passe ---
        Set<String> uuids = users.stream().map(User::getUUID).collect(Collectors.toSet());
        Map<String, FPlayer> fplayers = batchFPlayers(uuids);

        // --- Redis : toutes les factions nécessaires en une passe ---
        Set<Integer> factionIds = fplayers.values().stream()
                .filter(fp -> fp != null && fp.hasFaction())
                .map(FPlayer::getFactionId)
                .collect(Collectors.toSet());
        Map<Integer, Faction> factions = batchFactions(factionIds);

        // --- Assembly ---
        return users.stream()
                .map(u -> toDTOBatch(u, staffRanks, vipRanks, onlineMap, fplayers, factions))
                .toList();
    }

    private Map<String, String> buildOnlineMap() {
        Set<String> keys = Main.loadRedis().keys("SERVER:*");
        Map<String, String> map = new HashMap<>();
        for (String key : keys) {
            String uuid = key.replace("SERVER:", "");
            String server = Main.loadRedis().get(key);
            if (server != null) map.put(uuid, server);
        }
        return map;
    }

    private Map<String, FPlayer> batchFPlayers(Set<String> uuids) {
        Map<String, FPlayer> result = new HashMap<>();
        for (String uuid : uuids) {
            FPlayer fp = factionRepository.findFPlayer(uuid);
            result.put(uuid, fp); // null si pas de faction, géré à l'assembly
        }
        return result;
    }

    private Map<Integer, Faction> batchFactions(Set<Integer> ids) {
        Map<Integer, Faction> result = new HashMap<>();
        for (int id : ids) {
            Faction f = factionRepository.findById(id);
            if (f != null) result.put(id, f);
        }
        return result;
    }

    private UserDTO toDTOBatch(
            User user,
            Map<Integer, StaffRank> staffRanks,
            Map<Integer, VipRank> vipRanks,
            Map<String, String> onlineMap,
            Map<String, FPlayer> fplayers,
            Map<Integer, Faction> factions)
    {
        StaffRank staffRank = staffRanks.get(user.getStaffRankID());
        VipRank vipRank = vipRanks.get(user.getVipRankID());
        FPlayer fplayer = fplayers.get(user.getUUID());
        Faction faction = (fplayer != null && fplayer.hasFaction())
                ? factions.get(fplayer.getFactionId())
                : null;

        String server = onlineMap.get(user.getUUID());
        boolean online = server != null;

        return UserDTO.fromBatch(user, staffRank, vipRank, fplayer, faction, online, server);
    }

    private UserDTO toDTOSingle(User user) {
        StaffRank staffRank = staffRankRepository.findById(user.getStaffRankID());
        VipRank vipRank = vipRankRepository.findById(user.getVipRankID());
        FPlayer fplayer = factionRepository.findFPlayer(user.getUUID());
        Faction faction = (fplayer != null && fplayer.hasFaction())
                ? factionRepository.findById(fplayer.getFactionId())
                : null;
        return UserDTO.from(user, staffRank, vipRank, playerRepository, fplayer, faction);
    }

    public int allPlayerOnline() {
        Set<String> keys = Main.loadRedis().keys("SERVER:*");
        return keys.size();
    }
}