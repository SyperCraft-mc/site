package fr.kainovaii.sypercraft.app.components;

import fr.kainovaii.sypercraft.app.domain.user.UserDTO;
import fr.kainovaii.sypercraft.app.domain.user.UserService;
import com.obsidian.core.database.DB;
import com.obsidian.core.di.annotations.Inject;
import com.obsidian.core.livecomponents.annotations.Action;
import com.obsidian.core.livecomponents.annotations.LiveComponentImpl;
import com.obsidian.core.livecomponents.annotations.State;
import com.obsidian.core.livecomponents.core.LiveComponent;

import java.util.List;
import java.util.stream.Collectors;

@LiveComponentImpl
public class PlayerSearchAdmin extends LiveComponent
{
    @Inject
    private UserService userService;

    @State private String search        = "";
    @State private String factionFilter = "";
    @State private String rankFilter    = "";
    @State private String statusFilter  = "";
    @State private int    page          = 0;

    private static final int PAGE_SIZE = 12;

    private transient List<UserDTO> cachedFiltered = null;
    private transient String lastSearch        = null;
    private transient String lastFactionFilter = null;
    private transient String lastRankFilter    = null;
    private transient String lastStatusFilter  = null;

    @Override
    public void onUpdate()
    {
        if (!search.equals(lastSearch)
                || !factionFilter.equals(lastFactionFilter)
                || !rankFilter.equals(lastRankFilter)
                || !statusFilter.equals(lastStatusFilter)) {
            page = 0;
            cachedFiltered = null;
            lastSearch        = search;
            lastFactionFilter = factionFilter;
            lastRankFilter    = rankFilter;
            lastStatusFilter  = statusFilter;
        }
    }

    private List<UserDTO> filtered()
    {
        if (cachedFiltered != null) return cachedFiltered;

        List<UserDTO> all = DB.withConnection(() -> userService.findAll().stream().toList());

        cachedFiltered = all.stream()
                .filter(p -> search.isEmpty() ||
                        p.getPseudo().toLowerCase().contains(search.toLowerCase()))
                .filter(p -> factionFilter.isEmpty() ||
                        (p.hasFaction() && p.getFaction().name().equalsIgnoreCase(factionFilter)))
                .filter(p -> rankFilter.isEmpty() ||
                        (p.getStaffRank() != null && p.getStaffRank().label().equalsIgnoreCase(rankFilter)))
                .filter(p -> statusFilter.isEmpty() ||
                        (statusFilter.equals("online") && p.isOnline()) ||
                        (statusFilter.equals("offline") && !p.isOnline()))
                .collect(Collectors.toList());

        return cachedFiltered;
    }

    public List<UserDTO> getFilteredPlayers()
    {
        List<UserDTO> all = filtered();
        int from = page * PAGE_SIZE;
        if (from >= all.size()) return List.of();
        return all.subList(from, Math.min(from + PAGE_SIZE, all.size()));
    }

    public int getTotalCount()       { return filtered().size(); }
    public int getTotalPages()       { return (int) Math.ceil((double) filtered().size() / PAGE_SIZE); }
    public boolean isHasPrev()      { return page > 0; }
    public boolean isHasNext()      { return (page + 1) * PAGE_SIZE < filtered().size(); }
    public int getPage()             { return page; }
    public String getSearch()        { return search; }
    public String getFactionFilter() { return factionFilter; }
    public String getRankFilter()    { return rankFilter; }
    public String getStatusFilter()  { return statusFilter; }

    @Action public void prev() { if (page > 0) page--; }
    @Action public void next() { if (isHasNext()) page++; }

    @Override
    public String template() { return "components/player-search-admin.html"; }
}