package fr.kainovaii.sypercraft.app.services;

import fr.kainovaii.sypercraft.Main;
import com.obsidian.core.di.annotations.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;
import redis.clients.jedis.params.ScanParams;
import redis.clients.jedis.resps.ScanResult;

import java.util.*;

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

    /**
     * Scans keys matching the given pattern using cursor-based iteration.
     * Safer than KEYS for production use.
     */
    public Set<String> scan(String pattern)
    {
        try (Jedis jedis = pool.getResource()) {
            Set<String> result = new HashSet<>();
            String cursor = "0";

            do {
                ScanResult<String> scanResult = jedis.scan(cursor, new ScanParams().match(pattern).count(100));
                result.addAll(scanResult.getResult());
                cursor = scanResult.getCursor();
            } while (!cursor.equals("0"));

            return result;
        }
    }

    public Map<String, Object> getAllKitMapData()
    {
        Map<String, Object> data = new HashMap<>();

        Set<String> keys = keys("KITMAP_*");

        for (String key : keys) {
            if (key.startsWith("KITMAP_FACTION:") && !key.contains("_")) {
                data.put(key, getAll(key));
            } else if (key.startsWith("KITMAP_FACTION_MEMBERS:")) {
                data.put(key, smembers(key));
            } else if (key.startsWith("KITMAP_FACTION_CHUNKS:")) {
                data.put(key, smembers(key));
            } else if (key.startsWith("KITMAP_FPLAYER:")) {
                data.put(key, getAll(key));
            } else if (key.startsWith("KITMAP_FCHUNK:")) {
                data.put(key, getAll(key));
            } else if (key.equals("KITMAP_FACTION_IDS")) {
                data.put(key, smembers(key));
            } else if (key.startsWith("KITMAP_FACTION_NAME_IDX:")) {
                data.put(key, get(key));
            }
        }

        return data;
    }
}