package fr.kainovaii.sypercraft.app.components;

import fr.kainovaii.sypercraft.app.domain.report.ReportDTO;
import fr.kainovaii.sypercraft.app.domain.report.ReportService;
import com.obsidian.core.database.DB;
import com.obsidian.core.di.annotations.Inject;
import com.obsidian.core.livecomponents.annotations.Action;
import com.obsidian.core.livecomponents.annotations.LiveComponentImpl;
import com.obsidian.core.livecomponents.annotations.State;
import com.obsidian.core.livecomponents.core.LiveComponent;

import java.util.List;
import java.util.stream.Collectors;

@LiveComponentImpl
public class ReportSearchAdmin extends LiveComponent
{
    @Inject
    private ReportService reportService;

    @State private String search       = "";
    @State private String statusFilter = "";
    @State private String serverFilter = "";
    @State private int    page         = 0;

    private static final int PAGE_SIZE = 10;

    private transient List<ReportDTO> cachedAll      = null;
    private transient List<ReportDTO> cachedFiltered = null;
    private transient String lastSearch       = null;
    private transient String lastStatusFilter = null;
    private transient String lastServerFilter = null;

    @Override
    public void onUpdate()
    {
        if (!search.equals(lastSearch)
                || !statusFilter.equals(lastStatusFilter)
                || !serverFilter.equals(lastServerFilter)) {
            page = 0;
            cachedFiltered = null;
            lastSearch       = search;
            lastStatusFilter = statusFilter;
            lastServerFilter = serverFilter;
        }
    }

    private List<ReportDTO> filtered()
    {
        if (cachedFiltered != null) return cachedFiltered;

        if (cachedAll == null)
            cachedAll = DB.withConnection(() -> reportService.findAll().stream().toList());

        cachedFiltered = cachedAll.stream()
                .filter(r -> search.isEmpty() ||
                        r.getAuthor().getPseudo().toLowerCase().contains(search.toLowerCase()) ||
                        r.getReporter().getPseudo().toLowerCase().contains(search.toLowerCase()) ||
                        r.getMessage().toLowerCase().contains(search.toLowerCase()))
                .filter(r -> statusFilter.isEmpty() ||
                        r.getStatus().equalsIgnoreCase(statusFilter))
                .filter(r -> serverFilter.isEmpty() ||
                        r.getServer().equalsIgnoreCase(serverFilter))
                .collect(Collectors.toList());

        return cachedFiltered;
    }

    public List<ReportDTO> getFilteredReports()
    {
        List<ReportDTO> all = filtered();
        int from = page * PAGE_SIZE;
        if (from >= all.size()) return List.of();
        return all.subList(from, Math.min(from + PAGE_SIZE, all.size()));
    }

    public int getTotalCount()      { return filtered().size(); }
    public int getTotalPages()      { return (int) Math.ceil((double) filtered().size() / PAGE_SIZE); }
    public boolean isHasPrev()     { return page > 0; }
    public boolean isHasNext()     { return (page + 1) * PAGE_SIZE < filtered().size(); }
    public int getPage()            { return page; }
    public String getSearch()       { return search; }
    public String getStatusFilter() { return statusFilter; }
    public String getServerFilter() { return serverFilter; }

    @Action public void prev() { if (page > 0) page--; }
    @Action public void next() { if (isHasNext()) page++; }

    @Override
    public String template() { return "components/report-search-admin.html"; }
}