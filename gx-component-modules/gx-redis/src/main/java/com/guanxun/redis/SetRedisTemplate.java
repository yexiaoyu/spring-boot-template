package com.guanxun.redis;

import java.util.Set;

import com.guanxun.redis.utils.JSONUtil;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * 
 * <b><code>SetRedisTemplate</code></b>
 * <p>
 * Set 数据结构的常用方法封装，支持Object 类型
 * </p>
 * <b>Creation Time:</b> 2016年10月12日 下午5:51:46
 * 
 */
public class SetRedisTemplate extends BasicRedisTemplate {

    /**
     * Add one or more members to a set
     *
     * @param key
     * @param members
     * @return
     */
    public Long sAdd(final String key, final String members) {
        Long result = redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                return connection.sAdd(serializer.serialize(key), serializer.serialize(members));
            }
        });
        return result;
    }

    /**
     * For Object, Add one or more members to a set
     *
     * @param key
     * @param members
     * @return
     * 
     */
    public Long sAdd(final String key, final Object members) {
        Long result = redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                return connection.sAdd(serializer.serialize(key), serializer.serialize(JSONUtil.toJson(members)));
            }
        });
        return result;
    }

    /**
     * Remove one or more members from a set
     *
     * @param key
     * @param members
     * @return
     * 
     */
    public Long sRem(final String key, final String members) {
        Long result = redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                return connection.sRem(serializer.serialize(key), serializer.serialize(members));
            }
        });
        return result;
    }

    /**
     * For Object, Remove one or more members from a set
     *
     * @param key
     * @param members
     * @return
     * 
     */
    public Long sRem(final String key, final Object members) {
        Long result = redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                return connection.sRem(serializer.serialize(key), serializer.serialize(JSONUtil.toJson(members)));
            }
        });
        return result;
    }

    /**
     * Get all the members in a set.
     *
     * @param key
     * @return
     * 
     */
    public Set<String> sMembers(final String key) {
        Set<String> result = redisTemplate.execute(new RedisCallback<Set<String>>() {
            @Override
            public Set<String> doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                Set<byte[]> result = connection.sMembers(serializer.serialize(key));
                return formatByteToStrSet(result);
            }
        });
        return result;
    }

    /**
     * For Object, Get all the members in a set.
     *
     * @param key
     * @param clazz
     * @return
     * 
     */
    public <T> Set<T> sMembers(final String key, final Class<T> clazz) {
        Set<T> result = redisTemplate.execute(new RedisCallback<Set<T>>() {
            @Override
            public Set<T> doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                Set<byte[]> result = connection.sMembers(serializer.serialize(key));
                return formatByteToStrSet(result, clazz);
            }
        });
        return result;
    }

    /**
     * Get all the members is number in a set.
     *
     * @param key
     * @return
     * 
     */
    public Long sCard(final String key) {
        Long result = redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                return connection.sCard(serializer.serialize(key));
            }
        });
        return result;
    }

    /**
     * true if the meber exists in a set,else false
     *
     * @param key
     * @param member
     * @return
     * 
     */
    public Boolean sIsMember(final String key, final String member) {
        Boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                return connection.sIsMember(serializer.serialize(key), serializer.serialize(member));
            }
        });
        return result;
    }

    public String sPop(final String key) {
        String result = redisTemplate.execute(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] value =  connection.sPop(serializer.serialize(key));
                return serializer.deserialize(value);
            }
        });
        return result;
    }
    public <T> T sPop(final String key, final Class<T> clazz) {
        T result = redisTemplate.execute(new RedisCallback<T>() {
            @Override
            public T doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] value =  connection.sPop(serializer.serialize(key));
                return JSONUtil.toBean(serializer.deserialize(value), clazz);
            }
        });
        return result;
    }

}
