package fr.kainovaii.obsidian.app.domain.faction.models;


import fr.kainovaii.obsidian.di.annotations.Repository;

import java.util.List;

@Repository
public class FactionRepository
{
    public Faction findById(int id) {
        return Faction.findById(id);
    }

    public Faction findByName(String name) {
        return Faction.findByName(name);
    }

    public List<Faction> findAll() {
        return Faction.findAll().load();
    }

    public List<Faction> findByLevel(int level) {
        return Faction.<Faction>where("level = ?", level).load();
    }

    public List<Faction> findByMinBalance(long minBalance) {
        return Faction.<Faction>where("balance >= ?", minBalance).load();
    }

    public boolean save(Faction faction) {
        return faction.saveIt();
    }

    public boolean delete(int id) {
        Faction faction = Faction.findById(id);
        if (faction == null) return false;
        return faction.delete();
    }
}