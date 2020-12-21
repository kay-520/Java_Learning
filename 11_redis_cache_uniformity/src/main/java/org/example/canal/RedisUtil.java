package org.example.canal;

import redis.clients.jedis.Jedis;

/**
 * @author: create by wangmh
 * @projectName: 11_redis_cache_uniformity
 * @description:
 * @date: 2020/12/21
 **/
public class RedisUtil {

    private static Jedis jedis = null;

    public static synchronized Jedis getJedis() {
        if (jedis == null) {
            jedis = new Jedis("10.211.55.5", 6379);
        }
        return jedis;
    }

    public static boolean existKey(String key) {
        return getJedis().exists(key);
    }

    public static void delKey(String key) {
        getJedis().del(key);
    }

    public static String stringGet(String key) {
        return getJedis().get(key);
    }

    public static String stringSet(String key, String value) {
        return getJedis().set(key, value);
    }

    public static void hashSet(String key, String field, String value) {
        getJedis().hset(key, field, value);
    }
}