package fr.kainovaii.sypercraft.app.components;

import com.obsidian.core.database.DB;
import com.obsidian.core.di.annotations.Inject;
import com.obsidian.core.livecomponents.annotations.Action;
import com.obsidian.core.livecomponents.annotations.LiveComponentImpl;
import com.obsidian.core.livecomponents.annotations.State;
import com.obsidian.core.livecomponents.core.LiveComponent;
import fr.kainovaii.sypercraft.app.domain.faction.models.Faction;
import fr.kainovaii.sypercraft.app.domain.faction.FactionRepository;

import java.util.List;
import java.util.stream.Collectors;

@LiveComponentImpl
public class FactionSearch extends LiveComponent
{
    @Inject
    private FactionRepository factionRepository;

    @State private String search = "";
    @State private int    page   = 0;

    private static final int PAGE_SIZE = 8;

    private transient List<Faction> cachedFiltered = null;
    private transient String lastSearch = null;

    @Override
    public void onUpdate()
    {
        if (!search.equals(lastSearch)) {
            page = 0;
            cachedFiltered = null;
            lastSearch = search;
        }
    }

    private List<Faction> filtered()
    {
        if (cachedFiltered != null) return cachedFiltered;

        List<Faction> all = DB.withConnection(() -> factionRepository.findAll());

        cachedFiltered = all.stream()
                .filter(f -> search.isEmpty() || f.getName().toLowerCase().contains(search.toLowerCase()))
                .collect(Collectors.toList());

        return cachedFiltered;
    }

    public List<Faction> getFilteredFactions()
    {
        List<Faction> all = filtered();
        int from = page * PAGE_SIZE;
        if (from >= all.size()) return List.of();
        return all.subList(from, Math.min(from + PAGE_SIZE, all.size()));
    }

    public int getTotalCount()  { return filtered().size(); }
    public int getTotalPages()  { return (int) Math.ceil((double) filtered().size() / PAGE_SIZE); }
    public boolean isHasPrev()  { return page > 0; }
    public boolean isHasNext()  { return (page + 1) * PAGE_SIZE < filtered().size(); }
    public int getPage()        { return page; }
    public String getSearch()   { return search; }

    @Action public void prev() { if (page > 0) page--; }
    @Action public void next() { if (isHasNext()) page++; }

    @Override
    public String template() { return "components/faction-search.html"; }
}