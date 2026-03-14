package fr.kainovaii.sypercraft;

import fr.kainovaii.sypercraft.app.services.RedisService;
import fr.kainovaii.obsidian.core.EnvLoader;
import fr.kainovaii.obsidian.core.Obsidian;

public class Main
{
    private static RedisService redisInstance;

    public static void main(String[] args)
    {
        Obsidian.run(Main.class, args);
    }

    public static EnvLoader loadEnv()
    {
        return Obsidian.loadConfigAndEnv();
    }

    public static RedisService loadRedis()
    {
        if (redisInstance == null) {
            redisInstance = new RedisService();
        }
        return redisInstance;
    }
}