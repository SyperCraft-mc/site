package fr.kainovaii.sypercraft.app.services;

import fr.kainovaii.sypercraft.Main;
import fr.kainovaii.obsidian.di.annotations.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Service wrapper around Jedis providing pooled Redis access.
 * All methods borrow a connection from the pool and release it automatically.
 */
@Service
public class RedisService
{
    private final JedisPool pool;

    /**
     * Initializes the Jedis connection pool using environment variables:
     * REDIS_HOST, REDIS_PORT, REDIS_PASSWORD.
     */
    public RedisService()
    {
        String host = Main.loadEnv().get("REDIS_HOST");
        int port = Integer.parseInt(Main.loadEnv().get("REDIS_PORT"));
        String password = Main.loadEnv().get("REDIS_PASSWORD");

        JedisPoolConfig config = new JedisPoolConfig();
        this.pool = new JedisPool(config, host, port, 2000, password);
    }

    /**
     * Returns the string value associated with the given key, or null if not found.
     */
    public String get(String key)
    {
        try (Jedis jedis = pool.getResource()) {
            return jedis.get(key);
        }
    }

    /**
     * Returns all fields and values of the hash stored at the given key.
     * Returns an empty map if the key does not exist.
     */
    public Map<String, String> getAll(String key)
    {
        try (Jedis jedis = pool.getResource()) {
            return jedis.hgetAll(key);
        }
    }

    /**
     * Returns all keys matching the given glob-style pattern.
     * Avoid using on large datasets in production — prefer SCAN-based alternatives.
     */
    public Set<String> keys(String pattern)
    {
        try (Jedis jedis = pool.getResource()) {
            return jedis.keys(pattern);
        }
    }

    /**
     * Returns true if the given key exists in Redis.
     */
    public boolean exists(String key)
    {
        try (Jedis jedis = pool.getResource()) {
            return jedis.exists(key);
        }
    }

    /**
     * Returns all members of the set stored at the given key.
     */
    public Set<String> smembers(String key)
    {
        try (Jedis jedis = pool.getResource()) {
            return jedis.smembers(key);
        }
    }

    /**
     * Fetches string values for multiple keys in a single Redis roundtrip using pipelining.
     * Results are returned in the same order as the input keys.
     * If a key does not exist, the corresponding entry will be null.
     *
     * @param keys list of Redis string keys to fetch
     * @return list of values, one per key
     */
    public List<String> getPipeline(List<String> keys)
    {
        try (Jedis jedis = pool.getResource()) {
            Pipeline pipeline = jedis.pipelined();
            List<Response<String>> responses = new ArrayList<>();
            for (String key : keys) {
                responses.add(pipeline.get(key));
            }
            pipeline.sync();
            return responses.stream().map(Response::get).toList();
        }
    }

    /**
     * Fetches all hash fields for multiple keys in a single Redis roundtrip using pipelining.
     * Results are returned in the same order as the input keys.
     * If a key does not exist, the corresponding entry will be an empty map.
     *
     * @param keys list of Redis hash keys to fetch
     * @return list of field-value maps, one per key
     */
    public List<Map<String, String>> hgetAllPipeline(List<String> keys)
    {
        try (Jedis jedis = pool.getResource()) {
            Pipeline pipeline = jedis.pipelined();
            List<Response<Map<String, String>>> responses = new ArrayList<>();
            for (String key : keys) {
                responses.add(pipeline.hgetAll(key));
            }
            pipeline.sync();
            return responses.stream().map(Response::get).toList();
        }
    }
}