package fr.kainovaii.sypercraft.app.domain.faction;

import com.obsidian.core.database.orm.repository.BaseRepository;
import com.obsidian.core.di.annotations.Repository;
import fr.kainovaii.sypercraft.app.domain.faction.models.Faction;
import fr.kainovaii.sypercraft.app.domain.faction.models.FactionPlayer;

import java.util.List;

@Repository
public class FactionRepository extends BaseRepository<Faction>
{
    public FactionRepository() {
        super(Faction.class);
    }

    public Faction findById(int id) {
        return query()
                .with("players", "ranks", "chunks")
                .where("id", id)
                .first();
    }

    public List<Faction> findAll() {
        return query()
                .with("players", "ranks", "chunks")
                .get();
    }

    public Faction findByName(String name) {
        return query()
                .with("players", "ranks", "chunks")
                .where("name", name)
                .first();
    }

    public Faction findByUser(String uuid) {
        FactionPlayer fp = FactionPlayer.findByUUID(uuid);
        if (fp == null || !fp.hasFaction()) return null;
        return findById(fp.getFactionId());
    }

    public List<Faction> findByLevel(int level) {
        return query()
                .with("players", "ranks", "chunks")
                .where("level", level)
                .get();
    }

    public List<Faction> findByMinBalance(long minBalance) {
        return query()
                .with("players", "ranks", "chunks")
                .where("balance", ">=", minBalance)
                .get();
    }
}