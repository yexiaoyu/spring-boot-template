package com.guanxun.redis.template;

import java.util.ArrayList;
import java.util.List;

import redis.clients.jedis.ShardedJedis;


/**
 * 
 * <b><code>ListRedisTemplate</code></b>
 * <p>
 * Lists 数据结构的常用方法封装，支持Object 类型
 * </p>
 * <b>Creation Time:</b> 2016年10月12日 下午5:50:31
 * 
 * @author abin.yao
 * @since qlchat 1.0
 */
public class ListRedisTemplate extends BasicRedisTemplate {

    /**
     * Prepend one or multiple values to a list
     *
     * @param key
     * @param values
     * @since qlchat 1.0
     */
    public void lpush(String key, String... values) {
        ShardedJedis jedis = null;
        if (values == null) {
            return;
        }
        try {
            jedis = pool.getResource();
            jedis.lpush(key, values);
        } finally {
            this.closeRedis(jedis);
        }
    }

    /**
     * For Object,Prepend one or multiple values to a list
     *
     * @param key
     * @param values
     * @since qlchat 1.0
     */
    public void lpush(String key, Object... values) {
        ShardedJedis jedis = null;
        if (values == null) {
            return;
        }
        try {
            jedis = pool.getResource();
            byte[][] strings = new byte[values.length][];
            for (int j = 0; j < values.length; j++) {
                // strings[j] = HessianSerializer.serialize(values[j]);
                strings[j] = toJsonByteArray(values[j]);
            }
            jedis.lpush(key.getBytes(), strings);
        } finally {
            this.closeRedis(jedis);
        }
    }

    /**
     * For single Object,Prepend one or multiple values to a list
     *
     * @param key
     * @param values
     * @since qlchat 1.0
     */
    public void lpushForObject(String key, Object value) {
        ShardedJedis jedis = null;
        if (value == null) {
            return;
        }
        try {
            jedis = pool.getResource();
            // jedis.lpush(key.getBytes(), HessianSerializer.serialize(value));
            jedis.lpush(key.getBytes(), toJsonByteArray(value));
        } finally {
            this.closeRedis(jedis);
        }
    }

    /**
     * For single Object,Prepend one or multiple values to a list
     *
     * @param key
     * @param values
     * @since qlchat 1.0
     */
    public void rpushForObject(String key, Object value) {
        ShardedJedis jedis = null;
        if (value == null) {
            return;
        }
        try {
            jedis = pool.getResource();
            // jedis.rpush(key.getBytes(), HessianSerializer.serialize(value));
            jedis.rpush(key.getBytes(), toJsonByteArray(value));
        } finally {
            this.closeRedis(jedis);
        }
    }

    /**
     * Append one or multiple values to a list
     *
     * @param key
     * @param values
     * @since qlchat 1.0
     */
    public void rpush(String key, String... values) {
        ShardedJedis jedis = null;
        if (values == null) {
            return;
        }
        try {
            jedis = pool.getResource();
            jedis.rpush(key, values);
        } finally {
            this.closeRedis(jedis);
        }
    }

    /**
     * For Object, Append one or multiple values to a list
     *
     * @param key
     * @param values
     * @since qlchat 1.0
     */
    public void rpush(String key, Object... values) {
        ShardedJedis jedis = null;
        if (values == null) {
            return;
        }
        try {
            jedis = pool.getResource();
            byte[][] strings = new byte[values.length][];
            for (int j = 0; j < values.length; j++) {
                // strings[j] = HessianSerializer.serialize(values[j]);
                strings[j] = toJsonByteArray(values[j]);
            }
            jedis.rpush(key.getBytes(), strings);
        } finally {
            this.closeRedis(jedis);
        }
    }

    /**
     * Remove and get the last element in a list
     *
     * @param key
     * @return
     * @since qlchat 1.0
     */
    public String rpop(String key) {
        ShardedJedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.rpop(key);
        } finally {
            this.closeRedis(jedis);
        }
    }

    /**
     * For Object, Remove and get the last element in a list
     *
     * @param key
     * @param clazz
     * @return
     * @since qlchat 1.0
     */
    public <T> T rpop(String key, Class<T> clazz) {
        ShardedJedis jedis = null;
        try {
            jedis = pool.getResource();
            // return (T) HessianSerializer.deserialize(jedis.rpop(key.getBytes()));
            return fromJsonByteArray(jedis.rpop(key.getBytes()), clazz);
        } finally {
            this.closeRedis(jedis);
        }
    }

    /**
     * Remove and get the first element in a list
     *
     * @param key
     * @return
     * @since qlchat 1.0
     */
    public String lpop(String key) {
        ShardedJedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.lpop(key);
        } finally {
            this.closeRedis(jedis);
        }
    }

    /**
     * For Object, Remove and get the first element in a list
     *
     * @param key
     * @param clazz
     * @return
     * @since qlchat 1.0
     */
    public <T> T lpop(String key, Class<T> clazz) {
        ShardedJedis jedis = null;
        try {
            jedis = pool.getResource();
            // return (T) HessianSerializer.deserialize(jedis.lpop(key.getBytes()));
            return fromJsonByteArray(jedis.lpop(key.getBytes()), clazz);
        } finally {
            this.closeRedis(jedis);
        }
    }

    /**
     * Get the length of a list
     *
     * @param key
     * @return
     * @since qlchat 1.0
     */
    public Long llen(String key) {
        ShardedJedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.llen(key);
        } finally {
            this.closeRedis(jedis);
        }
    }

    /**
     * 删除List中的等于value的元素
     * 
     * count = 1 :删除第一个； count = 0 :删除所有； count = -1:删除最后一个；
     *
     * @param key
     * @param count
     * @param value
     * @return
     * @since qlchat 1.0
     */
    public Long lrem(String key, long count, String value) {
        ShardedJedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.lrem(key, count, value);
        } finally {
            this.closeRedis(jedis);
        }
    }

    /**
     * FOR Object, 删除List中的等于value的元素
     * 
     * count = 1 :删除第一个； count = 0 :删除所有； count = -1:删除最后一个；
     *
     * @param key
     * @param count
     * @param value
     * @return
     * @since qlchat 1.0
     */
    public Long lrem(String key, long count, Object value) {
        ShardedJedis jedis = null;
        try {
            jedis = pool.getResource();
            // return jedis.lrem(key.getBytes(), count, HessianSerializer.serialize(value));
            return jedis.lrem(key.getBytes(), count, toJsonByteArray(value));
        } finally {
            this.closeRedis(jedis);
        }
    }

    /**
     * Get a range of elements from a list.
     * <P>
     * For example LRANGE foobar 0 2 will return the first three elements of the list.
     * </p>
     * <P>
     * For example LRANGE foobar -1 -2 will return the last two elements of the list.
     * </p>
     * 
     * @param key
     * @param start
     * @param end
     * @return
     * @since qlchat 1.0
     */
    public List<String> lrange(String key, long start, long end) {
        ShardedJedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.lrange(key, start, end);
        } finally {
            this.closeRedis(jedis);
        }
    }

    /**
     * For Object, Get a range of elements from a list.
     * <P>
     * For example LRANGE foobar 0 2 will return the first three elements of the list.
     * </p>
     * <P>
     * For example LRANGE foobar -1 -2 will return the last two elements of the list.
     * </p>
     * 
     * @param key
     * @param start
     * @param end
     * @return
     * @since qlchat 1.0
     */
    public <T> List<T> lrange(String key, long start, long end, Class<T> clazz) {
        ShardedJedis jedis = null;
        try {
            jedis = pool.getResource();
            List<byte[]> list = jedis.lrange(key.getBytes(), start, end);
            if (list != null && list.size() > 0) {
                List<T> results = new ArrayList<>();
                for (byte[] bytes : list) {
                    results.add(fromJsonByteArray(bytes, clazz));
                }
                return results;
            }
            return null;
        } finally {
            this.closeRedis(jedis);
        }
    }
    
    public String ltrim(String key, long start, long end) {
        ShardedJedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.ltrim(key, start, end);
        } finally {
            this.closeRedis(jedis);
        }
    }
}
