package fr.kainovaii.obsidian.app.domain.report;

import fr.kainovaii.obsidian.Main;
import fr.kainovaii.obsidian.app.domain.faction.FactionGrade;
import fr.kainovaii.obsidian.app.domain.staffrank.StaffRank;
import fr.kainovaii.obsidian.app.domain.staffrank.StaffRankRepository;
import fr.kainovaii.obsidian.app.domain.user.User;
import fr.kainovaii.obsidian.app.domain.user.UserDTO;
import fr.kainovaii.obsidian.app.domain.user.UserRepository;
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
import java.util.stream.Stream;

@Service
public class ReportService
{
    @Inject
    private ReportRepository reportRepository;

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

    public List<ReportDTO> findAll() {
        return toDTOBatch(reportRepository.findAll());
    }

    public Optional<ReportDTO> findById(int id) {
        return reportRepository.findById(id).map(r -> toDTOBatch(List.of(r)).get(0));
    }

    public List<ReportDTO> findPending() {
        return toDTOBatch(reportRepository.findPending());
    }

    public List<ReportDTO> findProcessing() {
        return toDTOBatch(reportRepository.findProcessing());
    }

    public Report create(int messageId, String authorUuid, String message, long messageDate, String reporterUuid, String server) {
        return reportRepository.create(messageId, authorUuid, message, messageDate, reporterUuid, server);
    }

    public boolean markAsProcessing(int id) { return reportRepository.markAsProcessing(id); }
    public boolean updateStatus(int id, String status) { return reportRepository.updateStatus(id, status); }
    public boolean delete(int id) { return reportRepository.delete(id); }

    private List<ReportDTO> toDTOBatch(List<Report> reports)
    {
        if (reports.isEmpty()) return Collections.emptyList();

        Set<String> uuids = reports.stream()
                .flatMap(r -> Stream.of(r.getAuthorUuid(), r.getReporterUuid()))
                .collect(Collectors.toSet());

        List<String> uuidList = new ArrayList<>(uuids);

        // Batch DB
        Map<String, User> users = userRepository.findByUUIDs(uuids)
                .stream()
                .collect(Collectors.toMap(User::getUUID, u -> u));

        Map<Integer, StaffRank> staffRanks = staffRankRepository.findAll()
                .stream()
                .collect(Collectors.toMap(r -> (Integer) r.getId(), r -> r));

        Map<Integer, VipRank> vipRanks = vipRankRepository.findAll()
                .stream()
                .collect(Collectors.toMap(r -> (Integer) r.getId(), r -> r));

        // Batch Redis — SERVER:* pour online/server
        Map<String, String> onlineMap = buildOnlineMap();

        // Batch Redis — FPLAYER pipeline
        Map<String, FPlayer> fplayers = batchFPlayers(uuidList);

        // Batch Redis — FACTION pipeline
        Set<Integer> factionIds = fplayers.values().stream()
                .filter(fp -> fp != null && fp.hasFaction())
                .map(FPlayer::getFactionId)
                .collect(Collectors.toSet());
        Map<Integer, Faction> factions = batchFactions(new ArrayList<>(factionIds));

        // Building
        return reports.stream()
                .map(r -> {
                    UserDTO author   = resolveUser(r.getAuthorUuid(),   users, staffRanks, vipRanks, onlineMap, fplayers, factions);
                    UserDTO reporter = resolveUser(r.getReporterUuid(), users, staffRanks, vipRanks, onlineMap, fplayers, factions);
                    return ReportDTO.from(r, author, reporter);
                })
                .toList();
    }

    private UserDTO resolveUser(
            String uuid,
            Map<String, User> users,
            Map<Integer, StaffRank> staffRanks,
            Map<Integer, VipRank> vipRanks,
            Map<String, String> onlineMap,
            Map<String, FPlayer> fplayers,
            Map<Integer, Faction> factions)
    {
        User user = users.get(uuid);
        if (user == null) return null;

        StaffRank staffRank = staffRanks.get(user.getStaffRankID());
        VipRank vipRank     = vipRanks.get(user.getVipRankID());
        FPlayer fplayer     = fplayers.get(uuid);
        Faction faction     = (fplayer != null && fplayer.hasFaction()) ? factions.get(fplayer.getFactionId()) : null;

        String server = onlineMap.get(uuid);
        boolean online = server != null;

        return UserDTO.fromBatch(user, staffRank, vipRank, fplayer, faction, online, server);
    }

    private Map<String, String> buildOnlineMap()
    {
        Set<String> keys = Main.loadRedis().keys("SERVER:*");
        if (keys.isEmpty()) return Collections.emptyMap();

        List<String> keyList = new ArrayList<>(keys);
        List<String> servers = Main.loadRedis().getPipeline(keyList);

        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < keyList.size(); i++) {
            String server = servers.get(i);
            if (server != null) map.put(keyList.get(i).replace("SERVER:", ""), server);
        }
        return map;
    }

    private Map<String, FPlayer> batchFPlayers(List<String> uuids)
    {
        if (uuids.isEmpty()) return Collections.emptyMap();
        List<String> keys = uuids.stream().map(u -> "FPLAYER:" + u).toList();
        List<Map<String, String>> results = Main.loadRedis().hgetAllPipeline(keys);

        Map<String, FPlayer> result = new HashMap<>();
        for (int i = 0; i < uuids.size(); i++) {
            Map<String, String> data = results.get(i);
            if (data != null && !data.isEmpty())
                result.put(uuids.get(i), FPlayer.fromRedis(uuids.get(i), data));
        }
        return result;
    }

    private Map<Integer, Faction> batchFactions(List<Integer> ids)
    {
        if (ids.isEmpty()) return Collections.emptyMap();
        List<String> keys = ids.stream().map(id -> "FACTION:" + id).toList();
        List<Map<String, String>> results = Main.loadRedis().hgetAllPipeline(keys);

        Map<Integer, Faction> result = new HashMap<>();
        for (int i = 0; i < ids.size(); i++) {
            Map<String, String> data = results.get(i);
            if (data != null && !data.isEmpty())
                result.put(ids.get(i), Faction.fromRedis(data));
        }
        return result;
    }
}