package com.guanxun.redis;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.serializer.RedisSerializer;

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
    public Long pfadd(final String key, final String elements) {
        Long result = redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                return connection.pfAdd(serializer.serialize(key), serializer.serialize(elements));
            }
        });
        return result;
    }

    /**
     * 统计
     * @param key key
     * @return long
     */
    public long pfcount(final String key) {
        Long result = redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                return connection.pfCount(serializer.serialize(key));
            }
        });
        return result;
    }

}
