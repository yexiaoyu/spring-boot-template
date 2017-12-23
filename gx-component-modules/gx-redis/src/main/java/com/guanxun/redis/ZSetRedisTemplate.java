package com.guanxun.redis;

import com.guanxun.redis.utils.JSONUtil;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.util.List;
import java.util.Set;

/**
 * 
 * <b><code>ZSetRedisTemplate</code></b>
 * <p>
 * Sorted Sets 数据结构的常用方法封装，支持Object 类型
 * </p>
 * <b>Creation Time:</b> 2016年10月12日 下午5:54:09
 * @author abin.yao
 * 
 */
public class ZSetRedisTemplate extends BasicRedisTemplate {

	/**
	 * 加入Sorted set, 如果member在Set里已存在, 只更新score并返回false, 否则返回true.
	 *
	 * @param key
	 * @param member
	 * @param score
	 * @return
	 * 
	 */
	public Boolean zAdd(final String key, final double score, final String member) {
		Boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
				return connection.zAdd(serializer.serialize(key), score, serializer.serialize(member));
			}
		});
		return result;
	}

	/**
	 * For Object, 加入Sorted set, 如果member在Set里已存在, 只更新score并返回false, 否则返回true.
	 *
	 * @param key
	 * @param member
	 * @param score
	 * @return
	 * 
	 */
	public Boolean zAdd(final String key, final double score, final Object member) {
		Boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
				return connection.zAdd(serializer.serialize(key), score, serializer.serialize(JSONUtil.toJson(member)));
			}
		});
		return result;
	}

	/**
	 * Return a range of members in a sorted set, by index. Ordered from the lowest to the highest score.
	 *
	 * @param key
	 * @param start
	 * @param end
	 * @return Ordered from the lowest to the highest score.
	 * 
	 */
	public Set<String> zRange(final String key, final long start, final long end) {
		Set<String> result = redisTemplate.execute(new RedisCallback<Set<String>>() {
			@Override
			public Set<String> doInRedis(RedisConnection connection) throws DataAccessException {
				RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
				Set<byte[]> result = connection.zRange(serializer.serialize(key), start, end);
				return formatByteToStrSet(result);
			}
		});
		return result;
	}
	
	/**
     * For Object, Return a range of members in a sorted set, by index.Ordered from the lowest to the highest score.
     *
     * @param key
     * @param start
     * @param end
     * @return Ordered from the lowest to the highest score.
     * 
     */
    public <T> Set<T> zRange(final String key, final long start, final long end, final Class<T> clazz) {
		Set<T> result = redisTemplate.execute(new RedisCallback<Set<T>>() {
			@Override
			public Set<T> doInRedis(RedisConnection connection) throws DataAccessException {
				RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
				Set<byte[]> result = connection.zRange(serializer.serialize(key), start, end);
				return formatByteToStrSet(result, clazz);
			}
		});
		return result;
    }

	/**
	 * Return a range of members in a sorted set, by index. Ordered from the highest to the lowest score.
	 *
	 * @param key
	 * @param start
	 * @param end
	 * @return Ordered from the highest to the lowest score.
	 * 
	 */
	public List<String> zRevRange(final String key, final long start, final long end) {
		List<String> result = redisTemplate.execute(new RedisCallback<List<String>>() {
			@Override
			public List<String> doInRedis(RedisConnection connection) throws DataAccessException {
				RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
				Set<byte[]> result = connection.zRevRange(serializer.serialize(key), start, end);
				return formatByteToStrList(result);
			}
		});
		return result;
	}

	/**
	 * For Object, Return a range of members in a sorted set, by index. Ordered from the highest to the lowest score.
	 *
	 * @param key
	 * @param start
	 * @param end
	 * @param clazz
	 * @return Ordered from the highest to the lowest score.
	 * 
	 */
	public <T> List<T> zRevRange(final String key, final long start, final long end, final Class<T> clazz) {
		List<T> result = redisTemplate.execute(new RedisCallback<List<T>>() {
			@Override
			public List<T> doInRedis(RedisConnection connection) throws DataAccessException {
				RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
				Set<byte[]> result = connection.zRevRange(serializer.serialize(key), start, end);
				return formatByteToStrList(result, clazz);
			}
		});
		return result;
	}

	/**
	 * Return the all the elements in the sorted set at key with a score between
	 * min and max (including elements with score equal to min or max).
	 * 
	 * @param key
	 * @param min
	 * @param max
	 * @return Ordered from the lowest to the highest score.
	 * 
	 */
	public List<String> zRangeByScore(final String key, final double min, final double max) {
		List<String> result = redisTemplate.execute(new RedisCallback<List<String>>() {
			@Override
			public List<String> doInRedis(RedisConnection connection) throws DataAccessException {
				RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
				Set<byte[]> result = connection.zRangeByScore(serializer.serialize(key), min, max);
				return formatByteToStrList(result);
			}
		});
		return result;
	}
	
	/**
     * For Object, Return the all the elements in the sorted set at key with a score between
     * min and max (including elements with score equal to min or max).
     *
     * @param key
     * @param min
     * @param max
     * @return Ordered from the lowest to the highest score.
     * 
     */
	public <T> List<T> zRevRangeByScore(final String key, final double min, final double max, final Class<T> clazz) {
		List<T> result = redisTemplate.execute(new RedisCallback<List<T>>() {
			@Override
			public List<T> doInRedis(RedisConnection connection) throws DataAccessException {
				RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
				Set<byte[]> result = connection.zRevRangeByScore(serializer.serialize(key), min, max);
				return formatByteToStrList(result, clazz);
			}
		});
		return result;
    }

	public <T> List<T> zRangeByScore(final String key, final double min, final double max, final int offset, final int count, final Class<T> clazz) {
		List<T> result = redisTemplate.execute(new RedisCallback<List<T>>() {
			@Override
			public List<T> doInRedis(RedisConnection connection) throws DataAccessException {
				RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
				Set<byte[]> result = connection.zRangeByScore(serializer.serialize(key), min, max, offset, count);
				return formatByteToStrList(result, clazz);
			}
		});
		return result;

	}

	/**
	 * Remove one or more members from a sorted set
	 *
	 * @param key
	 * @param members
	 * @return
	 * 
	 */
	public Long zRem(final String key, final String members) {
		Long result = redisTemplate.execute(new RedisCallback<Long>() {
			@Override
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
				return connection.zRem(serializer.serialize(key), serializer.serialize(members));
			}
		});
		return result;
	}

	/**
	 * For Object, Remove one or more members from a sorted set
	 *
	 * @param key
	 * @param members
	 * @return
	 * 
	 */
	public Long zrem(final String key, final Object members) {
		Long result = redisTemplate.execute(new RedisCallback<Long>() {
			@Override
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
				return connection.zRem(serializer.serialize(key), serializer.serialize(JSONUtil.toJson(members)));
			}
		});
		return result;
	}

	/**
	 * Get the score associated with the given member in a sorted set
	 *
	 * @param key
	 * @param member
	 * @return
	 * 
	 */
	public Double zscore(final String key, final String member) {
		Double result = redisTemplate.execute(new RedisCallback<Double>() {
			@Override
			public Double doInRedis(RedisConnection connection) throws DataAccessException {
				RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
				return connection.zScore(serializer.serialize(key), serializer.serialize(member));
			}
		});
		return result;
	}

	/**
	 * For ObjecGet the score associated with the given member in a sorted set
	 *
	 * @param key
	 * @param member
	 * @return
	 * 
	 */
	public Double zscore(final String key, final Object member) {
		Double result = redisTemplate.execute(new RedisCallback<Double>() {
			@Override
			public Double doInRedis(RedisConnection connection) throws DataAccessException {
				RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
				return connection.zScore(serializer.serialize(key), serializer.serialize(JSONUtil.toJson(member)));
			}
		});
		return result;
	}

	/**
	 * Remove all elements in the sorted set at key with rank between start and
	 * end. Start and end are 0-based with rank 0 being the element with the
	 * lowest score. Both start and end can be negative numbers, where they
	 * indicate offsets starting at the element with the highest rank. For
	 * example: -1 is the element with the highest score, -2 the element with
	 * the second highest score and so forth.
	 * <p>
	 * <b>Time complexity:</b> O(log(N))+O(M) with N being the number of
	 * elements in the sorted set and M the number of elements removed by the
	 * operation
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 * 
	 */
	public Long zRemRangeByScore(final String key, final long start, final long end) {
		Long result = redisTemplate.execute(new RedisCallback<Long>() {
			@Override
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
				return connection.zRemRangeByScore(serializer.serialize(key), start, end);
			}
		});
		return result;
	}

	/**
	 * 
	 * Get the length of a sorted set
	 * 
	 * @param key
	 * @param min
	 * @param max
	 * @return
	 * 
	 */
	public Long zCount(final String key, final double min, final double max) {
		Long result = redisTemplate.execute(new RedisCallback<Long>() {
			@Override
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
				return connection.zCount(serializer.serialize(key), min, max);
			}
		});
		return result;
	}

	/**
	 * Get the number of members in a sorted set
	 * @param key key
	 * @return Long
	 */
	public Long zCard(final String key) {
		Long result = redisTemplate.execute(new RedisCallback<Long>() {
			@Override
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
				return connection.zCard(serializer.serialize(key));
			}
		});
		return result;
	}

	public <T> List<T> zRevRangeByScore(final String key, final double max, final double min, final int offset, final int count, final Class<T> clazz) {
		List<T> result = redisTemplate.execute(new RedisCallback<List<T>>() {
			@Override
			public List<T> doInRedis(RedisConnection connection) throws DataAccessException {
				RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
				Set<byte[]> result = connection.zRevRangeByScore(serializer.serialize(key), min, max, offset, count);
				return formatByteToStrList(result, clazz);
			}
		});
		return result;
	}

	public List<String> zRevRangeByScore(final String key, final double max, final double min, final int offset, final int count) {
		List<String> result = redisTemplate.execute(new RedisCallback<List<String>>() {
			@Override
			public List<String> doInRedis(RedisConnection connection) throws DataAccessException {
				RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
				Set<byte[]> result = connection.zRevRangeByScore(serializer.serialize(key), min, max, offset, count);
				return formatByteToStrList(result);
			}
		});
		return result;
	}

	public Long zRemRangeByScore(final String key, final double start, final double end) {
		Long result = redisTemplate.execute(new RedisCallback<Long>() {
			@Override
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
				return connection.zRemRangeByScore(serializer.serialize(key), start, end);
			}
		});
		return result;
	}
}
