package fr.kainovaii.obsidian.app.domain.user;

import fr.kainovaii.obsidian.app.domain.staffrank.StaffRank;
import fr.kainovaii.obsidian.app.domain.viprank.VipRank;
import fr.kainovaii.obsidian.app.redis.models.Faction;
import fr.kainovaii.obsidian.app.redis.models.FPlayer;
import fr.kainovaii.obsidian.app.redis.repositories.PlayerRepository;

import java.sql.Date;

public class UserDTO
{
    private final String uuid;
    private final String pseudo;
    private final String discordId;
    private final long playtimeSeconds;
    private final int pointBoutique;
    private final boolean combat;
    private final Date firstConnection;
    private final Date lastConnection;
    private final boolean online;
    private final String currentServer;
    private final StaffRankDTO staffRank;
    private final VipRankDTO vipRank;
    private final FactionDTO faction;

    private UserDTO(Builder builder)
    {
        this.uuid = builder.uuid;
        this.pseudo = builder.pseudo;
        this.discordId = builder.discordId;
        this.playtimeSeconds = builder.playtimeSeconds;
        this.pointBoutique = builder.pointBoutique;
        this.combat = builder.combat;
        this.firstConnection = builder.firstConnection;
        this.lastConnection = builder.lastConnection;
        this.online = builder.online;
        this.currentServer = builder.currentServer;
        this.staffRank = builder.staffRank;
        this.vipRank = builder.vipRank;
        this.faction = builder.faction;
    }

    public static UserDTO from(User user, StaffRank staffRank, VipRank vipRank, PlayerRepository playerRepository, FPlayer fplayer, Faction faction)
    {
        return new Builder()
                .uuid(user.getUUID())
                .pseudo(user.getPseudo())
                .discordId(user.getDiscordId())
                .playtimeSeconds(user.getPlaytimeSeconds())
                .pointBoutique(user.getPointBoutique())
                .combat(user.isInCombat())
                .firstConnection(user.firstConnection())
                .lastConnection(user.lastConnection())
                .online(playerRepository.isOnline(user.getUUID()))
                .currentServer(playerRepository.getCurrentServer(user.getUUID()))
                .staffRank(staffRank != null ? StaffRankDTO.from(staffRank) : null)
                .vipRank(vipRank != null ? VipRankDTO.from(vipRank) : null)
                .faction(fplayer != null && fplayer.hasFaction() && faction != null ? FactionDTO.from(faction, fplayer) : null)
                .build();
    }

    public String getUuid() { return uuid; }
    public String getPseudo() { return pseudo; }
    public String getDiscordId() { return discordId; }
    public long getPlaytimeSeconds() { return playtimeSeconds; }
    public int getPointBoutique() { return pointBoutique; }
    public boolean isCombat() { return combat; }
    public Date getFirstConnection() { return firstConnection; }
    public Date getLastConnection() { return lastConnection; }
    public boolean isOnline() { return online; }
    public String getCurrentServer() { return currentServer; }
    public StaffRankDTO getStaffRank() { return staffRank; }
    public VipRankDTO getVipRank() { return vipRank; }
    public FactionDTO getFaction() { return faction; }
    public boolean hasFaction() { return faction != null; }

    public String getPlaytimeFormatted() {
        long hours = playtimeSeconds / 3600;
        long minutes = (playtimeSeconds % 3600) / 60;
        return hours + "h " + minutes + "m";
    }

    public boolean hasLastConnection() {
        return lastConnection != null;
    }

    // --- Nested DTOs ---

    public record StaffRankDTO(
            int id,
            String label,
            int hierarchy,
            String colorRank,
            String colorPlayerName,
            String colorMessage,
            String color,
            String permissions,
            String discordId
    ) {
        public static StaffRankDTO from(StaffRank rank) {
            return new StaffRankDTO(
                    (Integer) rank.getId(),
                    rank.getLabel(),
                    rank.getHierarchy(),
                    rank.getColorRank(),
                    rank.getColorPlayerName(),
                    rank.getColorMessage(),
                    rank.getColor(),
                    rank.getPermissions(),
                    rank.getDiscordId()
            );
        }
    }

    public record VipRankDTO(
            int id,
            String label,
            String color,
            String avantages,
            int priority
    ) {
        public static VipRankDTO from(VipRank rank) {
            return new VipRankDTO(
                    (Integer) rank.getId(),
                    rank.getLabel(),
                    rank.getColor(),
                    rank.getAvantages(),
                    rank.getPriority()
            );
        }
    }

    public record FactionDTO(
            int id,
            String name,
            String description,
            int power,
            int maxPower,
            int factionRankId,
            String joinedAt
    ) {
        public static FactionDTO from(Faction faction, FPlayer fplayer) {
            return new FactionDTO(
                    faction.getId(),
                    faction.getName(),
                    faction.getDescription(),
                    faction.getPower(),
                    faction.getMaxPower(),
                    fplayer.getFactionRankId(),
                    fplayer.getJoinedAt()
            );
        }
    }

    // --- Builder ---

    public static class Builder
    {
        private String uuid;
        private String pseudo;
        private String discordId;
        private long playtimeSeconds;
        private int pointBoutique;
        private boolean combat;
        private Date firstConnection;
        private Date lastConnection;
        private boolean online;
        private String currentServer;
        private StaffRankDTO staffRank;
        private VipRankDTO vipRank;
        private FactionDTO faction;

        public Builder uuid(String uuid) { this.uuid = uuid; return this; }
        public Builder pseudo(String pseudo) { this.pseudo = pseudo; return this; }
        public Builder discordId(String discordId) { this.discordId = discordId; return this; }
        public Builder playtimeSeconds(long playtimeSeconds) { this.playtimeSeconds = playtimeSeconds; return this; }
        public Builder pointBoutique(int pointBoutique) { this.pointBoutique = pointBoutique; return this; }
        public Builder combat(boolean combat) { this.combat = combat; return this; }
        public Builder firstConnection(Date firstConnection) { this.firstConnection = firstConnection; return this; }
        public Builder lastConnection(Date lastConnection) { this.lastConnection = lastConnection; return this; }
        public Builder online(boolean online) { this.online = online; return this; }
        public Builder currentServer(String currentServer) { this.currentServer = currentServer; return this; }
        public Builder staffRank(StaffRankDTO staffRank) { this.staffRank = staffRank; return this; }
        public Builder vipRank(VipRankDTO vipRank) { this.vipRank = vipRank; return this; }
        public Builder faction(FactionDTO faction) { this.faction = faction; return this; }

        public UserDTO build() { return new UserDTO(this); }
    }
}