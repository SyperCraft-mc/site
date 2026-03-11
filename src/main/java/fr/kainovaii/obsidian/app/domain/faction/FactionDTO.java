package fr.kainovaii.obsidian.app.domain.faction;

import fr.kainovaii.obsidian.app.domain.faction.models.Faction;
import fr.kainovaii.obsidian.app.domain.faction.models.FactionPlayer;
import fr.kainovaii.obsidian.app.domain.faction.models.FactionRank;

import java.util.List;

public class FactionDTO
{
    private final int id;
    private final String name;
    private final String description;
    private final int level;
    private final int xp;
    private final int points;
    private final long balance;
    private final int memberCount;
    private final List<FactionRankDTO> ranks;
    private final List<FactionPlayerDTO> members;

    private FactionDTO(Builder builder)
    {
        this.id = builder.id;
        this.name = builder.name;
        this.description = builder.description;
        this.level = builder.level;
        this.xp = builder.xp;
        this.points = builder.points;
        this.balance = builder.balance;
        this.memberCount = builder.memberCount;
        this.ranks = builder.ranks;
        this.members = builder.members;
    }

    public static FactionDTO from(Faction faction, int memberCount, List<FactionRank> ranks, List<FactionPlayerDTO> members)
    {
        return new Builder()
                .id((Integer) faction.getId())
                .name(faction.getName())
                .description(faction.getDescription())
                .level(faction.getLevel())
                .xp(faction.getXp())
                .points(faction.getPoint())
                .balance(faction.getBalance())
                .memberCount(memberCount)
                .ranks(ranks.stream().map(FactionRankDTO::from).toList())
                .members(members)
                .build();
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getLevel() { return level; }
    public int getXp() { return xp; }
    public int getPoints() { return points; }
    public long getBalance() { return balance; }
    public int getMemberCount() { return memberCount; }
    public List<FactionRankDTO> getRanks() { return ranks; }
    public List<FactionPlayerDTO> getMembers() { return members; }

    // --- Nested DTOs ---

    public record FactionRankDTO(
            int id,
            String name,
            int hierarchy,
            int taxe,
            String permissions
    ) {
        public static FactionRankDTO from(FactionRank rank) {
            return new FactionRankDTO(
                    (Integer) rank.getId(),
                    rank.getName(),
                    rank.getHierarchy(),
                    rank.getTaxe(),
                    rank.getPermissions()
            );
        }
    }

    public record FactionPlayerDTO(
            String uuid,
            String pseudo,
            String factionRankName,
            int factionRankId,
            int playerRankId,
            long syscoins,
            int power,
            int maxPower
    ) {
        public static FactionPlayerDTO from(FactionPlayer fp, String pseudo, String factionRankName) {
            return new FactionPlayerDTO(
                    fp.getUUID(),
                    pseudo,
                    factionRankName,
                    fp.getFactionRankId(),
                    fp.getPlayerRankId(),
                    fp.getSyscoins(),
                    fp.getPower(),
                    fp.getMaxPower()
            );
        }
    }

    // --- Builder ---

    public static class Builder
    {
        private int id;
        private String name;
        private String description;
        private int level;
        private int xp;
        private int points;
        private long balance;
        private int memberCount;
        private List<FactionRankDTO> ranks;
        private List<FactionPlayerDTO> members;

        public Builder id(int id) { this.id = id; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder description(String description) { this.description = description; return this; }
        public Builder level(int level) { this.level = level; return this; }
        public Builder xp(int xp) { this.xp = xp; return this; }
        public Builder points(int points) { this.points = points; return this; }
        public Builder balance(long balance) { this.balance = balance; return this; }
        public Builder memberCount(int memberCount) { this.memberCount = memberCount; return this; }
        public Builder ranks(List<FactionRankDTO> ranks) { this.ranks = ranks; return this; }
        public Builder members(List<FactionPlayerDTO> members) { this.members = members; return this; }

        public FactionDTO build() { return new FactionDTO(this); }
    }
}