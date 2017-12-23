package com.guanxun.redis;

import com.guanxun.hepler.SpringHelper;
import com.guanxun.redis.utils.*;
import org.slf4j.*;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 
 * <b><code>BasicRedisTemplate</code></b>
 * <p>
 * redis 基础方法
 * </p>
 *
 */
public class BasicRedisTemplate {

    private static Logger logger = LoggerFactory.getLogger(BasicRedisTemplate.class);

    @Resource
    public RedisTemplate<String, ?> redisTemplate;// = SpringHelper.getBean(RedisTemplate.class);

    public boolean expire(final String key, long expire) {
        return redisTemplate.expire(key, expire, TimeUnit.SECONDS);
    }

    public Long ttl(final String key) {
        Long result = redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                return connection.ttl(serializer.serialize(key));
            }
        });
        return result;
    }

    public boolean exists(final String key) {
        boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                return connection.exists(serializer.serialize(key));
            }
        });
        return result;
    }

    public Set<String> formatByteToStrSet(Set<byte[]> result){
        if(result != null && result.size() > 0){
            Set<String> set = new HashSet<>();
            for(byte[] value : result){
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                set.add(serializer.deserialize(value));
            }
            return set;
        }
        return null;
    }

    public <T> Set<T> formatByteToStrSet(Set<byte[]> result, Class<T> clazz){
        if(result != null && result.size() > 0){
            Set<T> set = new HashSet<>();
            for(byte[] value : result){
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                set.add(JSONUtil.toBean(serializer.deserialize(value), clazz));
            }
            return set;
        }
        return null;
    }
    public List<String> formatByteToStrList(Set<byte[]> result){
        if(result != null && result.size() > 0){
            List<String> set = new ArrayList<>();
            for(byte[] value : result){
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                set.add(serializer.deserialize(value));
            }
            return set;
        }
        return null;
    }

    public <T> List<T> formatByteToStrList(Set<byte[]> result, Class<T> clazz){
        if(result != null && result.size() > 0){
            List<T> set = new ArrayList<>();
            for(byte[] value : result){
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                set.add(JSONUtil.toBean(serializer.deserialize(value), clazz));
            }
            return set;
        }
        return null;
    }
}
