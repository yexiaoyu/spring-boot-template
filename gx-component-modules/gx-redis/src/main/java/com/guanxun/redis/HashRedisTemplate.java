package com.guanxun.redis;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.guanxun.redis.utils.RedisJsonUtil;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * 
 * <b><code>HashRedisTemplate</code></b>
 * <p>
 * Hashes 数据结构的常用方法封装，Hashes不宜存放过大数据
 * </p>
 * <b>Creation Time:</b> 2016年10月12日 下午5:49:25
 * @author abin.yao
 * @since qlchat 1.0
 */
public class HashRedisTemplate extends BasicRedisTemplate {

	/**
	 * Set the string value of a hash field
	 *
	 * @param key
	 * @param field
	 * @param value
	 * @param ttl
	 * @return
	 * @since qlchat 1.0
	 */
	public Boolean hSet(final String key, final String field, final String value, final int ttl) {
		boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
				boolean result = connection.hSet(serializer.serialize(key), serializer.serialize(field), serializer.serialize(value));
				if(result){
					expire(key, ttl);
				}
				return result;
			}
		});
		return result;
	}

//	public Boolean hMSet(final String key, final Map<String, String> value, final int ttl) {
//		boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
//			@Override
//			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
//				RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
//				redisTemplate.getHashKeySerializer().serialize(value);
//				boolean result = connection.hSet(serializer.serialize(key), serializer.serialize(field), serializer.serialize(value));
//				if(result){
//					expire(key, ttl);
//				}
//				return result;
//			}
//		});
//		return result;
//	}

	/**
	 * Set the Object value of a hash field
	 *
	 * @param key
	 * @param field
	 * @param value
	 * @param ttl
	 * @return
	 * @since qlchat 1.0
	 */
	public Boolean hSet(final String key, final String field, final Object value, final int ttl) {
		boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
				boolean result = connection.hSet(serializer.serialize(key), serializer.serialize(field), serializer.serialize(RedisJsonUtil.toJson(value)));
				if(result){
					expire(key, ttl);
				}
				return result;
			}
		});
		return result;
	}

	/**
	 * Get the value of a hash field
	 *
	 * @param key
	 * @param field
	 * @return
	 * @since qlchat 1.0
	 */
	public String hGet(final String key, final String field) {
		String result = redisTemplate.execute(new RedisCallback<String>() {
			@Override
			public String doInRedis(RedisConnection connection) throws DataAccessException {
				RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
				byte[] value =  connection.hGet(serializer.serialize(key), serializer.serialize(field));
				return serializer.deserialize(value);
			}
		});
		return result;
	}

	/**
	 * Get the value of a hash field
	 *
	 * @param key
	 * @param field
	 * @return
	 * @since qlchat 1.0
	 */
	public <T> T hGet(final String key, final String field, final Class<T> clazz) {
		T result = redisTemplate.execute(new RedisCallback<T>() {
			@Override
			public T doInRedis(RedisConnection connection) throws DataAccessException {
				RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
				byte[] value =  connection.hGet(serializer.serialize(key), serializer.serialize(field));
				return RedisJsonUtil.toBean(serializer.deserialize(value), clazz);
			}
		});
		return result;
	}



	/**
	 * Delete one or more hash fields
	 *
	 * @param key
	 * @param fields
	 * @return 返回删除成功记录数
	 * @since qlchat 1.0
	 */
	public Long hDel(final String key, final String fields) {
		Long result = redisTemplate.execute(new RedisCallback<Long>() {
			@Override
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
				return connection.hDel(serializer.serialize(key), serializer.serialize(fields));
			}
		});
		return result;
	}

	/**
	 * Check if a hash field exists
	 *
	 * @param key
	 * @param field
	 * @return
	 * @since qlchat 1.0
	 */
	public Boolean hExists(final String key, final String field) {
		boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
				connection.hExists(serializer.serialize(key), serializer.serialize(field));
				return true;
			}
		});
		return result;
	}

	/**
	 * Get all the fields and values in a hash
	 * 当Hash较大时候，慎用！
	 * @param key
	 * @return
	 * @since qlchat 1.0
	 */
	public <T>  Map<String, T> hGetAll(final String key, final Class<T> clazz) {
		Map<String, T> result = redisTemplate.execute(new RedisCallback<Map<String, T>>() {
			@Override
			public Map<String, T> doInRedis(RedisConnection connection) throws DataAccessException {
				RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
				Map<byte[], byte[]> value = connection.hGetAll(serializer.serialize(key));

				if (value != null && value.size() > 0) {
					Map<String, T> newMap = new HashMap<String, T>();
					for (Map.Entry<byte[], byte[]> entry : value.entrySet()) {
						newMap.put(serializer.deserialize(entry.getKey()), RedisJsonUtil.toBean(serializer.deserialize(entry.getValue()), clazz));
					}
					return newMap;
				}

				return null;
			}
		});
		return result;
	}

	/**
	 * Get all the fields and values in a hash
	 * 当Hash较大时候，慎用！
	 * @param key
	 * @return
	 * @since qlchat 1.0
	 */
	public Map<String, String> hGetAll(final String key) {
		Map<String, String> result = redisTemplate.execute(new RedisCallback<Map<String, String>>() {
			@Override
			public Map<String, String> doInRedis(RedisConnection connection) throws DataAccessException {
				RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
				Map<byte[], byte[]> value = connection.hGetAll(serializer.serialize(key));
				if (value != null && value.size() > 0) {
					Map<String, String> newMap = new HashMap<String, String>();
					for (Map.Entry<byte[], byte[]> entry : value.entrySet()) {
						newMap.put(serializer.deserialize(entry.getKey()), serializer.deserialize(entry.getValue()));
					}
					return newMap;
				}

				return null;
			}
		});
		return result;
	}

	/**
	 * Increment the integer value of a hash field by the given number.
	 *
	 * @param key
	 * @param field
	 * @param value
	 * @return 返回增加后的结果
	 * @since qlchat 1.0
	 */
	public Long hIncrBy(final String key, final String field, final long value) {
		Long result = redisTemplate.execute(new RedisCallback<Long>() {
			@Override
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
				return connection.hIncrBy(serializer.serialize(key), serializer.serialize(field), value);
			}
		});
		return result;
	}

	/**
	 * Get all the fields in a hash.
	 *
	 * @param key
	 * @return
	 * @since qlchat 1.0
	 */
	public Set<String> hkeys(final String key) {
		Set<String> result = redisTemplate.execute(new RedisCallback<Set<String>>() {
			@Override
			public Set<String> doInRedis(RedisConnection connection) throws DataAccessException {
				RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
				Set<byte[]> result = connection.hKeys(serializer.serialize(key));
				return formatByteToStrSet(result);
			}
		});
		return result;
	}

	/**
	 * Get the number of fields in a hash.
	 *
	 * @param key
	 * @return
	 * @since qlchat 1.0
	 */
	public Long hLen(final String key) {
		Long result = redisTemplate.execute(new RedisCallback<Long>() {
			@Override
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
				return connection.hLen(serializer.serialize(key));
			}
		});
		return result;
	}

	public boolean hSetNX(final String key, final String field, final String value) {
		boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
				return connection.hSetNX(serializer.serialize(key), serializer.serialize(field), serializer.serialize(value));
			}
		});
		return result;
	}
}
