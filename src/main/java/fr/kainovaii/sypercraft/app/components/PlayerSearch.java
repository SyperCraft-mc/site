package fr.kainovaii.sypercraft.app.components;

import com.obsidian.core.cache.Cache;
import com.obsidian.core.di.annotations.Inject;
import com.obsidian.core.livecomponents.annotations.Action;
import com.obsidian.core.livecomponents.annotations.LiveComponentImpl;
import com.obsidian.core.livecomponents.annotations.State;
import com.obsidian.core.livecomponents.core.LiveComponent;
import fr.kainovaii.sypercraft.app.domain.faction.models.Faction;
import fr.kainovaii.sypercraft.app.domain.faction.models.FactionPlayer;
import fr.kainovaii.sypercraft.app.domain.user.User;
import fr.kainovaii.sypercraft.app.domain.user.UserRepository;
import fr.kainovaii.sypercraft.Main;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@LiveComponentImpl
public class PlayerSearch extends LiveComponent
{
    @Inject
    private UserRepository userRepository;

    @State private String search       = "";
    @State private String rankFilter   = "";
    @State private String statusFilter = "";
    @State private int    page         = 0;

    private static final int PAGE_SIZE = 12;

    public Set<String> getOnlineUuids() {
        return Cache.remember("players:online", 5, () ->
            Main.loadRedis().keys("SERVER:*")
                .stream()
                .map(k -> k.replace("SERVER:", ""))
                .collect(Collectors.toSet())
        );
    }

    public Map<String, String> getFactionNames()
    {
        return Cache.remember("players:faction-names", 30, () ->
        {
            Map<String, String> map = new HashMap<>();
            for (User u : userRepository.findAll()) {
                FactionPlayer fp = u.factionPlayer().first();
                if (fp != null && fp.hasFaction()) {
                    Faction f = fp.faction().first();
                    if (f != null) map.put(u.getUUID(), f.getName());
                }
            }
            return map;
        });
    }

    private List<User> filtered()
    {
        Set<String> onlineUuids = getOnlineUuids();

        return Cache.remember("users:all", 30, () -> userRepository.findAll())
                .stream()
                .filter(p -> search.isEmpty() || p.getPseudo().toLowerCase().contains(search.toLowerCase()))
                .filter(p -> {
                    if (rankFilter.isEmpty()) return true;
                    var rank = p.staffRank().first();
                    return rank != null && rank.getLabel().equalsIgnoreCase(rankFilter);
                })
                .filter(p -> {
                    if (statusFilter.isEmpty()) return true;
                    boolean online = onlineUuids.contains(p.getUUID());
                    return (statusFilter.equals("online") && online) || (statusFilter.equals("offline") && !online);
                })
                .collect(Collectors.toList());
    }

    public List<User> getFilteredPlayers()
    {
        List<User> all = filtered();
        int from = page * PAGE_SIZE;
        if (from >= all.size()) return List.of();
        return all.subList(from, Math.min(from + PAGE_SIZE, all.size()));
    }

    public int getTotalCount()       { return filtered().size(); }
    public int getTotalPages()       { return (int) Math.ceil((double) filtered().size() / PAGE_SIZE); }
    public boolean isHasPrev()       { return page > 0; }
    public boolean isHasNext()       { return (page + 1) * PAGE_SIZE < filtered().size(); }
    public int getPage()             { return page; }
    public String getSearch()        { return search; }
    public String getRankFilter()    { return rankFilter; }
    public String getStatusFilter()  { return statusFilter; }

    @Action public void prev() { if (page > 0) page--; }
    @Action public void next() { if (isHasNext()) page++; }

    @Override
    public String template() { return "components/player-search.html"; }
}