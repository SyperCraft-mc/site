package fr.kainovaii.sypercraft.app.redis.repositories;

import fr.kainovaii.sypercraft.Main;
import fr.kainovaii.sypercraft.app.redis.models.Player;
import fr.kainovaii.sypercraft.app.redis.models.StaffRank;
import fr.kainovaii.sypercraft.app.redis.models.VipRank;
import fr.kainovaii.sypercraft.app.services.RedisService;
import fr.kainovaii.sypercraft.app.utils.ApiUtile;
import fr.kainovaii.obsidian.di.annotations.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Repository
public class PlayerRepository
{
    private RedisService redis() { return Main.loadRedis(); }

    /** Returns all players from Redis (via PLAYER:* keys). */
    public List<Player> findAll()
    {
        Set<String> keys = redis().keys("PLAYER:*");
        List<Player> players = new ArrayList<>();
        for (String key : keys) {
            String uuid = key.replace("PLAYER:", "");
            Map<String, String> data = redis().getAll(key);
            if (data != null && !data.isEmpty())
                players.add(Player.fromRedis(uuid, data));
        }
        return players;
    }

    /** Returns a Player by UUID, or null if not found. */
    public Player findByUuid(String uuid)
    {
        Map<String, String> data = redis().getAll("PLAYER:" + uuid);
        if (data == null || data.isEmpty()) return null;
        return Player.fromRedis(uuid, data);
    }

    /** Returns true if the player exists in Redis. */
    public boolean exists(String uuid)
    {
        return redis().exists("PLAYER:" + uuid);
    }

    /** Returns all staff ranks from Redis (via STAFFRANK_IDS set). */
    public List<StaffRank> findAllStaffRanks()
    {
        Set<String> ids = redis().smembers("STAFFRANK_IDS");
        List<StaffRank> ranks = new ArrayList<>();
        for (String id : ids) {
            StaffRank rank = findStaffRankById(ApiUtile.parseIntOrZero(id));
            if (rank != null) ranks.add(rank);
        }
        return ranks;
    }

    /** Returns the StaffRank of a player, or null if they have none. */
    public StaffRank getStaffRank(Player player)
    {
        if (!player.hasStaffRank()) return null;
        return findStaffRankById(player.getStaffRankId());
    }

    /** Returns a StaffRank by ID, or null if not found. */
    public StaffRank findStaffRankById(int id)
    {
        Map<String, String> data = redis().getAll("STAFFRANK:" + id);
        if (data == null || data.isEmpty()) return null;
        return StaffRank.fromRedis(data);
    }

    /** Returns all VIP ranks from Redis (via VIPRANK_IDS set). */
    public List<VipRank> findAllVipRanks()
    {
        Set<String> ids = redis().smembers("VIPRANK_IDS");
        List<VipRank> ranks = new ArrayList<>();
        for (String id : ids) {
            VipRank rank = findVipRankById(ApiUtile.parseIntOrZero(id));
            if (rank != null) ranks.add(rank);
        }
        return ranks;
    }

    /** Returns the VipRank of a player, or null if they have none. */
    public VipRank getVipRank(Player player)
    {
        if (!player.hasVipRank()) return null;
        return findVipRankById(player.getVipRankId());
    }

    /** Returns a VipRank by ID, or null if not found. */
    public VipRank findVipRankById(int id)
    {
        Map<String, String> data = redis().getAll("VIPRANK:" + id);
        if (data == null || data.isEmpty()) return null;
        return VipRank.fromRedis(data);
    }

    /** Returns true if the player is currently online on the network. */
    public boolean isOnline(String uuid)
    {
        return redis().exists("SERVER:" + uuid);
    }

    /** Returns the server name the player is on, or null if offline. */
    public String getCurrentServer(String uuid)
    {
        return redis().get("SERVER:" + uuid);
    }

    /** Returns all players currently online on the network. */
    public List<Player> findAllOnline()
    {
        Set<String> keys = redis().keys("SERVER:*");
        List<Player> players = new ArrayList<>();
        for (String key : keys) {
            String uuid = key.replace("SERVER:", "");
            Player player = findByUuid(uuid);
            if (player != null) players.add(player);
        }
        return players;
    }
}