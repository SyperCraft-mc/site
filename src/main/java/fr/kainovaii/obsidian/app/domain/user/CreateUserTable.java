package fr.kainovaii.obsidian.app.domain.user;

import fr.kainovaii.obsidian.database.Migration;

public class CreateUserTable extends Migration
{
    @Override
    public void up()
    {
        createTable("users", table -> {
            table.id();
            table.string("username").notNull();
            table.string("minecraft_uuid").notNull().unique();
            table.string("role").notNull();
            table.timestamps();
        });
    }

    @Override
    public void down() {
        dropTable("users");
    }
}