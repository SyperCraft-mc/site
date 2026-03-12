package fr.kainovaii.obsidian.app.domain.faction;

import fr.kainovaii.obsidian.app.domain.faction.models.Faction;
import fr.kainovaii.obsidian.app.domain.faction.models.FactionChunk;
import fr.kainovaii.obsidian.app.domain.faction.models.FactionPlayer;
import fr.kainovaii.obsidian.app.domain.faction.models.FactionRank;
import fr.kainovaii.obsidian.app.domain.faction.repositories.FactionChunkRepository;
import fr.kainovaii.obsidian.app.domain.faction.repositories.FactionPlayerRepository;
import fr.kainovaii.obsidian.app.domain.faction.repositories.FactionRankRepository;
import fr.kainovaii.obsidian.app.domain.faction.repositories.FactionRepository;
import fr.kainovaii.obsidian.app.domain.user.User;
import fr.kainovaii.obsidian.app.domain.user.UserRepository;
import fr.kainovaii.obsidian.di.annotations.Inject;
import fr.kainovaii.obsidian.di.annotations.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FactionService
{
    @Inject
    private FactionRepository factionRepository;

    @Inject
    private FactionRankRepository factionRankRepository;

    @Inject
    private FactionPlayerRepository factionPlayerRepository;

    @Inject
    private FactionChunkRepository factionChunkRepository;

    @Inject
    private UserRepository userRepository;

    public FactionDTO findById(int id)
    {
        Faction faction = factionRepository.findById(id);
        if (faction == null) return null;
        return toDTOSingle(faction);
    }

    public FactionDTO findByName(String name)
    {
        Faction faction = factionRepository.findByName(name);
        if (faction == null) return null;
        return toDTOSingle(faction);
    }

    public FactionDTO findByUser(String uuid)
    {
        Faction faction = factionRepository.findByUser(uuid);
        if (faction == null) return null;
        return toDTOSingle(faction);
    }

    public List<FactionDTO> findAll()
    {
        List<Faction> factions = factionRepository.findAll();
        if (factions.isEmpty()) return Collections.emptyList();

        List<Integer> factionIds = factions.stream()
                .map(f -> (Integer) f.getId())
                .toList();

        Map<Integer, List<FactionPlayer>> playersByFaction = factionPlayerRepository
                .findByFactionIds(factionIds)
                .stream()
                .collect(Collectors.groupingBy(FactionPlayer::getFactionId));

        Map<Integer, List<FactionRank>> ranksByFaction = factionRankRepository
                .findByFactionIds(factionIds)
                .stream()
                .collect(Collectors.groupingBy(fp -> (Integer) fp.getFactionId()));

        Map<Integer, List<FactionChunk>> chunksByFaction = factionChunkRepository
                .findByFactionIds(factionIds)
                .stream()
                .collect(Collectors.groupingBy(fc -> (Integer) fc.getIdOwner()));

        Set<String> allUuids = playersByFaction.values().stream()
                .flatMap(List::stream)
                .map(FactionPlayer::getUUID)
                .collect(Collectors.toSet());

        Map<String, String> pseudoByUuid = userRepository.findByUUIDs(allUuids)
                .stream()
                .collect(Collectors.toMap(User::getUUID, User::getPseudo));

        return factions.stream()
                .map(f -> toDTOBatch(f, playersByFaction, ranksByFaction, chunksByFaction, pseudoByUuid))
                .toList();
    }

    private FactionDTO toDTOBatch(
            Faction faction,
            Map<Integer, List<FactionPlayer>> playersByFaction,
            Map<Integer, List<FactionRank>> ranksByFaction,
            Map<Integer, List<FactionChunk>> chunksByFaction,
            Map<String, String> pseudoByUuid)
    {
        int factionId = (Integer) faction.getId();

        List<FactionPlayer> players = playersByFaction.getOrDefault(factionId, List.of());
        List<FactionRank> ranks = ranksByFaction.getOrDefault(factionId, List.of());
        List<FactionChunk> chunks = chunksByFaction.getOrDefault(factionId, List.of());

        Map<Integer, String> rankNameById = ranks.stream()
                .collect(Collectors.toMap(r -> (Integer) r.getId(), FactionRank::getName));

        List<FactionDTO.FactionPlayerDTO> members = players.stream()
                .map(fp -> {
                    String pseudo = pseudoByUuid.getOrDefault(fp.getUUID(), fp.getUUID());
                    String rankName = rankNameById.getOrDefault(fp.getFactionRankId(), "");
                    return FactionDTO.FactionPlayerDTO.from(fp, pseudo, rankName);
                })
                .toList();

        return FactionDTO.from(faction, players.size(), chunks.size(), ranks, members, chunks);
    }

    private FactionDTO toDTOSingle(Faction faction)
    {
        int factionId = (Integer) faction.getId();

        int memberCount = factionPlayerRepository.countByFactionId(factionId);
        int chunkCount = factionChunkRepository.countByFactionId(factionId);
        List<FactionRank> ranks = factionRankRepository.findByFactionId(factionId);
        List<FactionChunk> chunks = factionChunkRepository.findByFactionId(factionId);

        Map<Integer, String> rankNameById = ranks.stream()
                .collect(Collectors.toMap(r -> (Integer) r.getId(), FactionRank::getName));

        List<FactionDTO.FactionPlayerDTO> members = factionPlayerRepository.findByFactionId(factionId)
                .stream()
                .map(fp -> {
                    User user = userRepository.findByUUID(fp.getUUID());
                    String pseudo = user != null ? user.getPseudo() : fp.getUUID();
                    String rankName = rankNameById.getOrDefault(fp.getFactionRankId(), "");
                    return FactionDTO.FactionPlayerDTO.from(fp, pseudo, rankName);
                })
                .toList();

        return FactionDTO.from(faction, memberCount, chunkCount, ranks, members, chunks);
    }
}