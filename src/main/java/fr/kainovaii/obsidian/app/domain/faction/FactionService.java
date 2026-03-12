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

import java.util.List;

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
        return toDTO(faction);
    }

    public FactionDTO findByName(String name)
    {
        Faction faction = factionRepository.findByName(name);
        if (faction == null) return null;
        return toDTO(faction);
    }

    public FactionDTO findByUser(String uuid)
    {
        Faction faction = factionRepository.findByUser(uuid);
        if (faction == null) return null;
        return toDTO(faction);
    }

    public List<FactionDTO> findAll()
    {
        return factionRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    private FactionDTO toDTO(Faction faction)
    {
        int memberCount = factionPlayerRepository.countByFactionId((Integer) faction.getId());
        int chunkCount = factionChunkRepository.countByFactionId((Integer) faction.getId());
        List<FactionRank> ranks = factionRankRepository.findByFactionId((Integer) faction.getId());
        List<FactionChunk> chunks = factionChunkRepository.findByFactionId((Integer) faction.getId());

        List<FactionDTO.FactionPlayerDTO> members = factionPlayerRepository.findByFactionId((Integer) faction.getId())
                .stream()
                .map(fp -> {
                    User user = userRepository.findByUUID(fp.getUUID());
                    String pseudo = user != null ? user.getPseudo() : fp.getUUID();
                    FactionRank factionRank = factionRankRepository.findById(fp.getFactionRankId());
                    String factionRankName = factionRank != null ? factionRank.getName() : "";
                    return FactionDTO.FactionPlayerDTO.from(fp, pseudo, factionRankName);
                })
                .toList();

        return FactionDTO.from(faction, memberCount, chunkCount, ranks, members, chunks);
    }
}