package fr.kainovaii.obsidian.app.components;

import fr.kainovaii.obsidian.app.domain.user.UserDTO;
import fr.kainovaii.obsidian.app.domain.user.UserService;
import fr.kainovaii.obsidian.database.DB;
import fr.kainovaii.obsidian.di.annotations.Inject;
import fr.kainovaii.obsidian.livecomponents.annotations.LiveComponentImpl;
import fr.kainovaii.obsidian.livecomponents.annotations.State;
import fr.kainovaii.obsidian.livecomponents.core.LiveComponent;

import java.util.List;
import java.util.stream.Collectors;

@LiveComponentImpl
public class PlayerSearch extends LiveComponent
{
    @Inject
    private UserService userService;

    @State
    private String search = "";

    @State
    private String factionFilter = "";

    @State
    private String rankFilter = "";

    @State
    private String statusFilter = "";

    @State
    private int page = 0;

    private static final int PAGE_SIZE = 12;

    private transient List<UserDTO> cachedFiltered = null;

    @Override
    public void updateField(String fieldName, Object value)
    {
        super.updateField(fieldName, value);
        if (!fieldName.equals("page")) {
            page = 0;
            cachedFiltered = null;
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

    public int getTotalCount() { return filtered().size(); }
    public int getTotalPages() { return (int) Math.ceil((double) filtered().size() / PAGE_SIZE); }
    public boolean isHasPrev() { return page > 0; }
    public boolean isHasNext() { return (page + 1) * PAGE_SIZE < filtered().size(); }
    public int getPage() { return page; }
    public String getSearch() { return search; }
    public String getFactionFilter() { return factionFilter; }
    public String getRankFilter() { return rankFilter; }
    public String getStatusFilter() { return statusFilter; }

    public void prev() { if (page > 0) page--; }
    public void next() { if (isHasNext()) page++; }

    @Override
    public String template() { return "components/player-search.html"; }
}