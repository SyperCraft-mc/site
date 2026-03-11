package fr.kainovaii.obsidian.app.services;

import fr.kainovaii.obsidian.Main;
import fr.kainovaii.obsidian.core.EnvLoader;
import fr.kainovaii.obsidian.core.Obsidian;
import fr.kainovaii.obsidian.di.annotations.Repository;
import fr.kainovaii.obsidian.di.annotations.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Map;
import java.util.Set;

@Service
public class RedisService
{
    private final JedisPool pool;

    public RedisService()
    {
        String host = Main.loadEnv().get("REDIS_HOST");
        int port = Integer.parseInt(Main.loadEnv().get("REDIS_PORT"));
        String password = Main.loadEnv().get("REDIS_PASSWORD");

        JedisPoolConfig config = new JedisPoolConfig();
        this.pool = new JedisPool(config, host, port, 2000, password);
    }

    public String get(String key)
    {
        try (Jedis jedis = pool.getResource()) {
            return jedis.get(key);
        }
    }

    public Map<String, String> getAll(String key) {
        try (Jedis jedis = pool.getResource()) {
            return jedis.hgetAll(key);
        }
    }

    public Set<String> keys(String pattern) {
        try (Jedis jedis = pool.getResource()) {
            return jedis.keys(pattern);
        }
    }

    public boolean exists(String key)
    {
        try (Jedis jedis = pool.getResource()) {
            return jedis.exists(key);
        }
    }

    public Set<String> smembers(String key)
    {
        try (Jedis jedis = pool.getResource()) {
            return jedis.smembers(key);
        }
    }
}
