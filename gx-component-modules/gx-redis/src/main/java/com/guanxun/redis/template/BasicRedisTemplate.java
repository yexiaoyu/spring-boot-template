package com.guanxun.redis.template;

import com.guanxun.redis.*;
import com.guanxun.redis.utils.*;
import org.slf4j.*;
import redis.clients.jedis.*;

import javax.annotation.*;

/**
 * 
 * <b><code>BasicRedisTemplate</code></b>
 * <p>
 * redis 基础方法
 * </p>
 * <b>Creation Time:</b> 2016年10月12日 下午5:46:43
 * 
 * @author abin.yao
 * @since qlchat 1.0
 */
public abstract class BasicRedisTemplate {

    private static Logger logger = LoggerFactory.getLogger(BasicRedisTemplate.class);

    protected RedisConnectionFactory connectionFactory;

    public void setConnectionFactory(RedisConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    protected ShardedJedisPool pool;

    @PostConstruct
    protected void initPool() {
        this.pool = connectionFactory.getConnectionPool();
    }

    @SuppressWarnings("deprecation")
    protected void closeRedis(ShardedJedis jedis) {
        if (jedis != null) {
            try {
                pool.returnResource(jedis);
            } catch (Exception e) {
                logger.error("Error happen when return jedis to pool, try to close it directly.", e);
                if (jedis != null) {
                    try {
                        jedis.disconnect();
                    } catch (Exception e1) {
                    }
                }
            }
        }
    }

    /**
     * 删除key, 如果key存在返回true, 否则返回false。
     *
     * @param key
     * @return
     * @since qlchat 1.0
     */
    public boolean del(String key) {
        ShardedJedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.del(key) == 1 ? true : false;
        } finally {
            this.closeRedis(jedis);
        }
    }

    /**
     * true if the key exists, otherwise false
     *
     * @param key
     * @return
     * @since qlchat 1.0
     */
    public Boolean exists(String key) {
        ShardedJedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.exists(key);
        } finally {
            this.closeRedis(jedis);
        }
    }

    /**
     * set key expired time
     *
     * @param key
     * @param seconds
     * @return
     * @since qlchat 1.0
     */
    public Boolean expire(String key, int seconds) {
        ShardedJedis jedis = null;
        if (seconds == 0) {
            return true;
        }
        try {
            jedis = pool.getResource();
            return jedis.expire(key, seconds) == 1 ? true : false;
        } finally {
            this.closeRedis(jedis);
        }
    }

    /**
     * 
     * 把object转换为json byte array
     *
     * @param o
     * @return
     */
    protected byte[] toJsonByteArray(Object o) {
        String json = JsonUtil.toJsonString(o) != null ? JsonUtil.toJsonString(o) : "";
        return json.getBytes();
    }

    /**
     * 
     * 把json byte array转换为T类型object
     *
     * @param b
     * @param clazz
     * @return
     */
    protected <T> T fromJsonByteArray(byte[] b, Class<T> clazz) {
        if (b == null || b.length == 0) {
            return null;
        }
        return JsonUtil.parseJson(new String(b), clazz);
    }

    public Long ttl(final String key) {
        ShardedJedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.ttl(key);
        } finally {
            this.closeRedis(jedis);
        }
    }
}
