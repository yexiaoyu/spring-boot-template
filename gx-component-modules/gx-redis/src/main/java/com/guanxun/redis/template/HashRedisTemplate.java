package com.guanxun.redis.template;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import redis.clients.jedis.ShardedJedis;

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
	 * @return
	 * @since qlchat 1.0
	 */
	public Boolean hset(final String key, final String field, final String value) {
		return hset(key, field, value, 0);
	}

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
	public Boolean hset(final String key, final String field, final String value, final int ttl) {
		ShardedJedis jedis = null;
		try {
			jedis = pool.getResource();
			Long reply = jedis.hset(key, field, value);
			if (ttl > 0) {
				jedis.expire(key, ttl);
			}
			return reply == 1;
		} finally {
			this.closeRedis(jedis);
		}
	}

	/**
	 * Set the Object value of a hash field
	 *
	 * @param key
	 * @param field
	 * @param value
	 * @return
	 * @since qlchat 1.0
	 */
	public Boolean hset(final String key, final String field, final Object value) {
		return hset(key, field, value, 0);
	}

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
	public Boolean hset(final String key, final String field, final Object value, final int ttl) {
		ShardedJedis jedis = null;
		try {
			jedis = pool.getResource();
			Long reply = jedis.hset(key.getBytes(), field.getBytes(), toJsonByteArray(value));
			if (ttl > 0) {
				jedis.expire(key, ttl);
			}
			return reply == 1;
		} finally {
			this.closeRedis(jedis);
		}
	}

	/**
	 * Get the value of a hash field
	 *
	 * @param key
	 * @param field
	 * @return
	 * @since qlchat 1.0
	 */
	public String hget(final String key, final String field) {
		return hget(key, field, 0);
	}

	/**
	 * Get the value of a hash field
	 *
	 * @param key
	 * @param field
	 * @return
	 * @since qlchat 1.0
	 */
	public String hget(final String key, final String field, int ttl) {
		ShardedJedis jedis = null;
		try {
			jedis = pool.getResource();
			String res = jedis.hget(key, field);
			if (ttl > 0) {
				jedis.expire(key, ttl);
			}
			return res;
		} finally {
			this.closeRedis(jedis);
		}
	}

	/**
	 * Get the value of a hash field
	 *
	 * @param key
	 * @param field
	 * @return
	 * @since qlchat 1.0
	 */
	public <T> T hget(final String key, final String field, final Class<T> clazz) {
		return hget(key, field, clazz, 0);
	}

	/**
	 * Get the value of a hash field
	 *
	 * @param key
	 * @param field
	 * @return
	 * @since qlchat 1.0
	 */
	public <T> T hget(final String key, final String field, final Class<T> clazz, final int ttl) {
		ShardedJedis jedis = null;
		try {
			jedis = pool.getResource();
			T res = fromJsonByteArray(jedis.hget(key.getBytes(), field.getBytes()), clazz);
			if (ttl > 0) {
				this.expire(key, ttl);
			}
			return res;
		} finally {
			this.closeRedis(jedis);
		}
	}

	/**
	 * Delete one or more hash fields
	 *
	 * @param key
	 * @param fields
	 * @return
	 * @since qlchat 1.0
	 */
	public Boolean hdel(final String key, final String... fields) {
		ShardedJedis jedis = null;
		try {
			jedis = pool.getResource();
			return jedis.hdel(key, fields) == 1 ? true : false;
		} finally {
			this.closeRedis(jedis);
		}
	}

	/**
	 * Check if a hash field exists
	 *
	 * @param key
	 * @param field
	 * @return
	 * @since qlchat 1.0
	 */
	public Boolean hexists(final String key, final String field) {
		ShardedJedis jedis = null;
		try {
			jedis = pool.getResource();
			return jedis.hexists(key, field);
		} finally {
			this.closeRedis(jedis);
		}
	}

	/**
	 * Get all the fields and values in a hash
	 * 当Hash较大时候，慎用！
	 * @param key
	 * @return
	 * @since qlchat 1.0
	 */
	public Map<String, String> hgetAll(final String key) {
		ShardedJedis jedis = null;
		try {
			jedis = pool.getResource();
			return jedis.hgetAll(key);
		} finally {
			this.closeRedis(jedis);
		}
	}

	/**
	 * Get all the fields and values in a hash
	 * 当Hash较大时候，慎用！
	 * @param key
	 * @return
	 * @since qlchat 1.0
	 */
	public Map<String, Object> hgetAllObject(final String key) {
		ShardedJedis jedis = null;
		try {
			jedis = pool.getResource();
			Map<byte[], byte[]> byteMap = jedis.hgetAll(key.getBytes());
			if (byteMap != null && byteMap.size() > 0) {
				Map<String, Object> map = new HashMap<String, Object>();
				for (Entry<byte[], byte[]> e : byteMap.entrySet()) {
					map.put(new String(e.getKey()), fromJsonByteArray(e.getValue(), Object.class));
				}
				return map;
			}
			return null;
		} finally {
			this.closeRedis(jedis);
		}
	}

	/**
	 * Get all the fields and values in a hash
	 * 当Hash较大时候，慎用！
	 * @param key
	 * @return
	 * @since qlchat 1.0
	 */
	public <T> Map<String, T> hgetAllObject(final String key, Class<T> clazz) {
		ShardedJedis jedis = null;
		try {
			jedis = pool.getResource();
			Map<byte[], byte[]> byteMap = jedis.hgetAll(key.getBytes());
			if (byteMap != null && byteMap.size() > 0) {
				Map<String, T> map = new HashMap<String, T>();
				for (Entry<byte[], byte[]> e : byteMap.entrySet()) {
					map.put(new String(e.getKey()), fromJsonByteArray(e.getValue(), clazz));
				}
				return map;
			}
			return null;
		} finally {
			this.closeRedis(jedis);
		}
	}

	/**
	 * Get the values of all the given hash fields.
	 *
	 * @param key
	 * @param fields
	 * @return
	 * @since qlchat 1.0
	 */
	public List<String> hmget(final String key, final String... fields) {
		ShardedJedis jedis = null;
		try {
			jedis = pool.getResource();
			return jedis.hmget(key, fields);
		} finally {
			this.closeRedis(jedis);
		}
	}

	/**
	 * 
	 * Get the value of a mulit fields
	 *
	 * @param key
	 * @param ttl
	 * @param fields
	 * @return
	 * @since Wifi 1.0
	 */
	public Map<String, Object> hmgetObject(final String key, final int ttl, final String... fields) {
		ShardedJedis jedis = null;
		try {
			if (null == fields) {
				return null;
			}
			jedis = pool.getResource();
			List<byte[]> byteList = new ArrayList<byte[]>();
			for (String field : fields) {
				byteList.add(field.getBytes());
			}
			List<byte[]> resBytes = jedis.hmget(key.getBytes(), byteList.toArray(new byte[byteList.size()][]));
			Map<String, Object> resMap = null;
			if (null != resBytes) {
				resMap = new HashMap<String, Object>();
				for (int i = 0; i < resBytes.size(); i++) {
					resMap.put(fields[i], fromJsonByteArray(resBytes.get(i), Object.class));
				}
			}
			if (ttl > 0) {
				this.expire(key, ttl);
			}
			return resMap;
		} finally {
			this.closeRedis(jedis);
		}
	}

	/**
	 * 
	 * Get the value of a mulit fields
	 *
	 * @param key
	 * @param ttl
	 * @param fields
	 * @return
	 * @since Wifi 1.0
	 */
	public Map<String, Object> hmgetObject(final String key, final String... fields) {
		return hmgetObject(key, 0, fields);
	}

	/**
	 * Set multiple hash fields to multiple values.
	 *
	 * @param key
	 * @param hash
	 * @return
	 * @since qlchat 1.0
	 */
	public String hmset(final String key, final Map<String, String> hash) {
		ShardedJedis jedis = null;
		try {
			jedis = pool.getResource();
			return jedis.hmset(key, hash);
		} finally {
			this.closeRedis(jedis);
		}
	}

	/**
	 * Set multiple hash fields to multiple values.
	 *
	 * @param key
	 * @param hash
	 * @return
	 * @since qlchat 1.0
	 */
	public String hmsetObject(final String key, final Map<String, Object> hash) {
		ShardedJedis jedis = null;
		try {
			jedis = pool.getResource();
			Map<byte[], byte[]> byteMap = new HashMap<byte[], byte[]>(hash.size());
			for (Entry<String, Object> e : hash.entrySet()) {
				byteMap.put(e.getKey().getBytes(), toJsonByteArray(e.getValue()));
			}
			return jedis.hmset(key.getBytes(), byteMap);
		} finally {
			this.closeRedis(jedis);
		}
	}

	/**
	 * Increment the integer value of a hash field by the given number.
	 *
	 * @param key
	 * @param field
	 * @param value
	 * @return
	 * @since qlchat 1.0
	 */
	public Long hincrBy(final String key, final String field, final long value) {
		ShardedJedis jedis = null;
		try {
			jedis = pool.getResource();
			return jedis.hincrBy(key, field, value);
		} finally {
			this.closeRedis(jedis);
		}
	}

	/**
	 * Get all the fields in a hash.
	 *
	 * @param key
	 * @return
	 * @since qlchat 1.0
	 */
	public Set<String> hkeys(final String key) {
		ShardedJedis jedis = null;
		try {
			jedis = pool.getResource();
			return jedis.hkeys(key);
		} finally {
			this.closeRedis(jedis);
		}
	}

	/**
	 * Get all the fields in a hash.
	 *
	 * @param key
	 * @return
	 * @since qlchat 1.0
	 */
	public <T> Set<T> hkeys(final String key, final Class<T> clazz) {
		ShardedJedis jedis = null;
		try {
			jedis = pool.getResource();
			Set<byte[]> set = jedis.hkeys(key.getBytes());
			Set<T> objectSet = new HashSet<T>();
			if (set != null && set.size() != 0) {
				for (byte[] b : set) {
					objectSet.add(fromJsonByteArray(b, clazz));
				}
			}
			return objectSet;
		} finally {
			this.closeRedis(jedis);
		}
	}

	/**
	 * Get the number of fields in a hash.
	 *
	 * @param key
	 * @return
	 * @since qlchat 1.0
	 */
	public Long hlen(final String key) {
		ShardedJedis jedis = null;
		try {
			jedis = pool.getResource();
			return jedis.hlen(key);
		} finally {
			this.closeRedis(jedis);
		}
	}

	public Long hsetnx(final String key, final String field, final String value) {
		ShardedJedis jedis = null;
		try {
			jedis = pool.getResource();
			return jedis.hsetnx(key, field, value);
		} finally {
			this.closeRedis(jedis);
		}

	}
}
