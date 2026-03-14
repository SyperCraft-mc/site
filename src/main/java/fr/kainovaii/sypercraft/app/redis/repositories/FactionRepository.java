package fr.kainovaii.sypercraft.app.redis.repositories;

import fr.kainovaii.sypercraft.Main;
import fr.kainovaii.sypercraft.app.redis.models.Faction;
import fr.kainovaii.sypercraft.app.redis.models.FactionRank;
import fr.kainovaii.sypercraft.app.redis.models.FPlayer;
import fr.kainovaii.sypercraft.app.services.RedisService;
import fr.kainovaii.sypercraft.app.utils.ApiUtile;
import fr.kainovaii.obsidian.di.annotations.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Repository
public class FactionRepository
{
    private RedisService redis() { return Main.loadRedis(); }

    /** Returns all factions from Redis (via FACTION_IDS set). */
    public List<Faction> findAll()
    {
        Set<String> ids = redis().smembers("FACTION_IDS");
        List<Faction> factions = new ArrayList<>();
        for (String id : ids) {
            Faction f = findById(ApiUtile.parseIntOrZero(id));
            if (f != null) factions.add(f);
        }
        return factions;
    }

    /** Returns a Faction by ID, or null if not found. */
    public Faction findById(int id)
    {
        Map<String, String> data = redis().getAll("FACTION:" + id);
        if (data == null || data.isEmpty()) return null;
        return Faction.fromRedis(data);
    }

    /** Returns a Faction by name using the FACTION_NAME_IDX:{name} index, or null if not found. */
    public Faction findByName(String name)
    {
        String idStr = redis().get("FACTION_NAME_IDX:" + name.toLowerCase());
        if (idStr == null || idStr.isBlank()) return null;
        return findById(ApiUtile.parseIntOrZero(idStr));
    }

    /** Returns all faction IDs from the FACTION_IDS set. */
    public List<Integer> getAllIds()
    {
        Set<String> ids = redis().smembers("FACTION_IDS");
        List<Integer> result = new ArrayList<>();
        for (String id : ids) {
            int parsed = ApiUtile.parseIntOrZero(id);
            if (parsed > 0) result.add(parsed);
        }
        return result;
    }

    /** Returns the FPlayer entry for a given UUID, or null if not found. */
    public FPlayer findFPlayer(String uuid)
    {
        Map<String, String> data = redis().getAll("FPLAYER:" + uuid);
        if (data == null || data.isEmpty()) return null;
        return FPlayer.fromRedis(uuid, data);
    }

    /** Returns all members of a faction using the FACTION_MEMBERS:{factionId} set. */
    public List<FPlayer> getMembers(int factionId)
    {
        Set<String> uuids = redis().smembers("FACTION_MEMBERS:" + factionId);
        List<FPlayer> members = new ArrayList<>();
        for (String uuid : uuids) {
            FPlayer fp = findFPlayer(uuid);
            if (fp != null) members.add(fp);
        }
        return members;
    }

    /** Returns a FactionRank by ID, or null if not found. */
    public FactionRank findRankById(int id)
    {
        Map<String, String> data = redis().getAll("FACTIONRANK:" + id);
        if (data == null || data.isEmpty()) return null;
        return FactionRank.fromRedis(data);
    }

    /** Returns all ranks of a faction using the FACTION_RANKS_IDX:{factionId} set. */
    public List<FactionRank> getRanks(int factionId)
    {
        Set<String> ids = redis().smembers("FACTION_RANKS_IDX:" + factionId);
        List<FactionRank> ranks = new ArrayList<>();
        for (String id : ids) {
            FactionRank rank = findRankById(ApiUtile.parseIntOrZero(id));
            if (rank != null) ranks.add(rank);
        }
        return ranks;
    }


}