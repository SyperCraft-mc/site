package fr.kainovaii.obsidian.app.components;

import fr.kainovaii.obsidian.app.domain.faction.FactionDTO;
import fr.kainovaii.obsidian.app.domain.faction.FactionService;
import fr.kainovaii.obsidian.database.DB;
import fr.kainovaii.obsidian.di.annotations.Inject;
import fr.kainovaii.obsidian.livecomponents.annotations.LiveComponentImpl;
import fr.kainovaii.obsidian.livecomponents.annotations.State;
import fr.kainovaii.obsidian.livecomponents.core.LiveComponent;

import java.util.List;
import java.util.stream.Collectors;

@LiveComponentImpl
public class FactionSearch extends LiveComponent
{
    @Inject
    private FactionService factionService;

    @State
    private String search = "";

    @State
    private int page = 0;

    private static final int PAGE_SIZE = 8;

    private transient List<FactionDTO> cachedFiltered = null;

    @Override
    public void updateField(String fieldName, Object value)
    {
        super.updateField(fieldName, value);
        if (!fieldName.equals("page")) {
            page = 0;
            cachedFiltered = null;
        }
    }

    private List<FactionDTO> filtered()
    {
        if (cachedFiltered != null) return cachedFiltered;

        List<FactionDTO> all = DB.withConnection(() -> factionService.findAll().stream().toList());

        cachedFiltered = all.stream()
                .filter(f -> search.isEmpty() ||
                        f.getName().toLowerCase().contains(search.toLowerCase()))
                .collect(Collectors.toList());

        return cachedFiltered;
    }

    public List<FactionDTO> getFilteredFactions()
    {
        List<FactionDTO> all = filtered();
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

    public void prev() { if (page > 0) page--; }
    public void next() { if (isHasNext()) page++; }

    @Override
    public String template() { return "components/faction-search.html"; }
}