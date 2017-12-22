package com.guanxun.redis.template;

import redis.clients.jedis.ShardedJedis;

/**
 * HyperLogLog模板
 * Created by liujunjie on 16-12-15.
 */
public class HyperLogLogTemplate extends BasicRedisTemplate {

    /**
     * 添加
     * @param key key
     * @param elements 元素
     */
    public Long pfadd(String key, String... elements) {
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.pfadd(key, elements);
        } finally {
            this.closeRedis(jedis);
        }
    }

    /**
     * 统计
     * @param key key
     * @return long
     */
    public long pfcount(String key) {
        ShardedJedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.pfcount(key);
        } finally {
            this.closeRedis(jedis);
        }
    }

}
