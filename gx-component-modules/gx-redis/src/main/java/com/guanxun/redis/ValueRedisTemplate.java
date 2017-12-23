package com.guanxun.redis;

import com.guanxun.redis.utils.RedisJsonUtil;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 
 * <b><code>ValueRedisTemplate</code></b>
 * <p>
 * Strings 数据结构的常用方法封装，支持Object 类型
 * </p>
 * <b>Creation Time:</b> 2016年10月12日 下午5:53:22
 * @author abin.yao
 * @since qlchat 1.0
 */
@Component
public class ValueRedisTemplate extends BasicRedisTemplate {

	public boolean set(final String key, final String value) {
		boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
				connection.set(serializer.serialize(key), serializer.serialize(value));
				return true;
			}
		});
		return result;
	}

	public boolean set(final String key, final Object value) {
		boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
				connection.set(serializer.serialize(key), serializer.serialize(RedisJsonUtil.toJson(value)));
				return true;
			}
		});
		return result;
	}

	public String get(final String key){
		String result = redisTemplate.execute(new RedisCallback<String>() {
			@Override
			public String doInRedis(RedisConnection connection) throws DataAccessException {
				RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
				byte[] value =  connection.get(serializer.serialize(key));
				return serializer.deserialize(value);
			}
		});
		return result;
	}

	public <T> T get(final String key , final Class<T> clazz){
		T result = redisTemplate.execute(new RedisCallback<T>() {
			@Override
			public T doInRedis(RedisConnection connection) throws DataAccessException {
				RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
				byte[] value =  connection.get(serializer.serialize(key));
				return RedisJsonUtil.toBean(serializer.deserialize(value), clazz);
			}
		});
		return result;
	}


	public <T> boolean setList(String key, List<T> list) {
		String value = RedisJsonUtil.toJson(list);
		return set(key,value);
	}

	public <T> List<T> getList(String key, Class<T> clz) {
		String json = get(key);
		if(json!=null){
			List<T> list = RedisJsonUtil.toList(json, clz);
			return list;
		}
		return null;
	}

	/**
	 * 将值 value 关联到 key ，并将 key 的生存时间设为 seconds (以秒为单位)。
	 *  SETEX 是一个原子性(atomic)操作，关联值和设置生存时间两个动作会在同一时间内完成，该命令在 Redis 用作缓存时，非常实用。
	 *
	 * @param key
	 * @param ttl
	 * @param value
	 * @return
	 * @since qlchat 1.0
	 */
	public Boolean setEx(final String key, final long ttl, final String value) {
		boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
				connection.setEx(serializer.serialize(key), ttl, serializer.serialize(value));
				return true;
			}
		});
		return result;
	}

	/**
	 * 如果key还不存在则进行设置，返回true，否则返回false.
	 *
	 * @param key
	 * @param value
	 * @return 
	 * @since qlchat 1.0
	 */
	public boolean setNx(final String key, final String value, final int ttl) {
		boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
				boolean result = connection.setNX(serializer.serialize(key), serializer.serialize(value));
				if(result){
					connection.expire(serializer.serialize(key), ttl);
				}
				return result;
			}
		});
		return result;
	}

	/**
	 * 自增 +1
	 * 改方法不会修改ttl
	 * @param key
	 * @return 返回自增后结果
	 * @since qlchat 1.0
	 */
	public Long incr(final String key) {
		Long result = redisTemplate.execute(new RedisCallback<Long>() {
			@Override
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
				return connection.incr(serializer.serialize(key));
			}
		});
		return result;
	}

	/**
	 * 自增 +1
	 *
	 * @param key key
	 * @param integer 起始值
	 * @return 返回自增后结果
	 * @since qlchat 1.0
	 */
	public Long incrBy(final String key, final long integer) {
		Long result = redisTemplate.execute(new RedisCallback<Long>() {
			@Override
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
				return connection.incrBy(serializer.serialize(key), integer);
			}
		});
		return result;
	}

	/**
	 * 自减 -1
	 *
	 * @param key
	 * @return 返回自减后结果
	 * @since qlchat 1.0
	 */
	public Long decr(final String key) {
		Long result = redisTemplate.execute(new RedisCallback<Long>() {
			@Override
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
				return connection.decr(serializer.serialize(key));
			}
		});
		return result;
	}

}
