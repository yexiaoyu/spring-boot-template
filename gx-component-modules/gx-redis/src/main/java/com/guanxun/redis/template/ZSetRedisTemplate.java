package com.guanxun.redis.template;

import com.alibaba.fastjson.JSON;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.Tuple;

import java.util.ArrayList;
import java.util.HashSet;
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
 * @since qlchat 1.0
 */
public class ZSetRedisTemplate extends BasicRedisTemplate {

	/**
	 * 加入Sorted set, 如果member在Set里已存在, 只更新score并返回false, 否则返回true.
	 *
	 * @param key
	 * @param member
	 * @param score
	 * @return
	 * @since qlchat 1.0
	 */
	public Boolean zadd(String key, double score, String member) {
		ShardedJedis jedis = null;
		try {
			jedis = pool.getResource();
			return jedis.zadd(key, score, member) == 1 ? true : false;
		} finally {
			this.closeRedis(jedis);
		}
	}
	
	/**
     * 加入Sorted set, 如果member在Set里已存在, 只更新score并返回false, 否则返回true.
     *
     * @param key
     * @param member
     * @param score
     * @param ttl 过期时间，秒
     * @return
     * @since qlchat 1.0
     */
    public Boolean zadd(final String key,final double score,final int ttl,final String member) {
        ShardedJedis jedis = null;
        try {
            jedis = pool.getResource();
            Boolean ret = jedis.zadd(key, score, member) == 1 ? true : false;
            if (ret && ttl > 0) {
                jedis.expire(key, ttl);
            }
            return ret;
        } finally {
            this.closeRedis(jedis);
        }
    }

	/**
	 * For Object, 加入Sorted set, 如果member在Set里已存在, 只更新score并返回false, 否则返回true.
	 *
	 * @param key
	 * @param member
	 * @param score
	 * @return
	 * @since qlchat 1.0
	 */
	public Boolean zadd(String key, double score, Object member) {
		ShardedJedis jedis = null;
		try {
			jedis = pool.getResource();
			// return jedis.zadd(key.getBytes(), score, HessianSerializer.serialize(member)) == 1 ? true : false;
			return jedis.zadd(key.getBytes(), score, toJsonByteArray(member)) == 1 ? true : false;
		} finally {
			this.closeRedis(jedis);
		}
	}

	/**
	 * Return a range of members in a sorted set, by index. Ordered from the lowest to the highest score.
	 *
	 * @param key
	 * @param start
	 * @param end
	 * @return Ordered from the lowest to the highest score.
	 * @since qlchat 1.0
	 */
	public Set<String> zrange(String key, long start, long end) {
		ShardedJedis jedis = null;
		try {
			jedis = pool.getResource();
			return jedis.zrange(key, start, end);
		} finally {
			this.closeRedis(jedis);
		}
	}
	
	/**
     * For Object, Return a range of members in a sorted set, by index.Ordered from the lowest to the highest score.
     *
     * @param key
     * @param start
     * @param end
     * @return Ordered from the lowest to the highest score.
     * @since qlchat 1.0
     */
    public <T> List<T> zrange(String key, long start, long end, Class<T> clazz) {
        List<T> result = new ArrayList<>();
        ShardedJedis jedis = null;
        try {
            jedis = pool.getResource();
            Set<byte[]> tempSet = jedis.zrange(key.getBytes(), start, end);
            if (tempSet != null && tempSet.size() > 0) {
                for (byte[] value : tempSet) {
                    // result.add((T) HessianSerializer.deserialize(value));
                    result.add(fromJsonByteArray(value, clazz));
                }
                return result;
            }
            return null;
        } finally {
            this.closeRedis(jedis);
        }
    }

	/**
	 * Return a range of members in a sorted set, by index. Ordered from the highest to the lowest score.
	 *
	 * @param key
	 * @param start
	 * @param end
	 * @return Ordered from the highest to the lowest score.
	 * @since qlchat 1.0
	 */
	public Set<String> zrevrange(String key, long start, long end) {
		ShardedJedis jedis = null;
		try {
			jedis = pool.getResource();
			return jedis.zrevrange(key, start, end);
		} finally {
			this.closeRedis(jedis);
		}
	}

	/**
	 * For Object, Return a range of members in a sorted set, by index. Ordered from the highest to the lowest score.
	 *
	 * @param key
	 * @param start
	 * @param end
	 * @param clazz
	 * @return Ordered from the highest to the lowest score.
	 * @since qlchat 1.0
	 */
	public <T> List<T> zrevrange(String key, long start, long end, Class<T> clazz) {
		ShardedJedis jedis = null;
		try {
			jedis = pool.getResource();
			Set<byte[]> tempSet = jedis.zrevrange(key.getBytes(), start, end);
			if (tempSet != null && tempSet.size() > 0) {
			    List<T> result = new ArrayList<>();
				for (byte[] value : tempSet) {
					// result.add((T) HessianSerializer.deserialize(value));
					result.add(fromJsonByteArray(value, clazz));
				}
				return result;
			}
			return null;
		} finally {
			this.closeRedis(jedis);
		}
	}

	/**
	 * Return the all the elements in the sorted set at key with a score between
	 * min and max (including elements with score equal to min or max).
	 * 
	 * @param key
	 * @param min
	 * @param max
	 * @return Ordered from the lowest to the highest score.
	 * @since qlchat 1.0
	 */
	public Set<String> zrangeByScore(final String key, final double min, final double max) {
		ShardedJedis jedis = null;
		try {
			jedis = pool.getResource();
			return jedis.zrangeByScore(key, min, max);
		} finally {
			this.closeRedis(jedis);
		}
	}

	/**
	 * For Object, Return the all the elements in the sorted set at key with a score between
	 * min and max (including elements with score equal to min or max).
	 *
	 * @param key
	 * @param min
	 * @param max
	 * @return Ordered from the lowest to the highest score.
	 * @since qlchat 1.0
	 */
	public <T> Set<T> zrangeHashSetByScore(final String key, final double min, final double max, Class<T> clazz) {
		ShardedJedis jedis = null;
		try {
			jedis = pool.getResource();
			Set<byte[]> tempSet = jedis.zrangeByScore(key.getBytes(), min, max);
			if (tempSet != null && tempSet.size() > 0) {
				HashSet<T> result = new HashSet<T>();
				for (byte[] value : tempSet) {
					// result.add((T) HessianSerializer.deserialize(value));
					result.add(fromJsonByteArray(value, clazz));
				}
				return result;
			}
			return null;
		} finally {
			this.closeRedis(jedis);
		}
	}

	/**
	 * Return the all the elements in the sorted set at key with a score between
	 * min and max (including elements with score equal to min or max).
	 * @param key
	 * @param min
	 * @param max
	 * @return Ordered from the highest to the lowest score.
	 * @since qlchat 1.0
	 */
	public Set<String> zrevrangeByScore(final String key, final double min, final double max) {
		ShardedJedis jedis = null;
		try {
			jedis = pool.getResource();
			return jedis.zrevrangeByScore(key, max, min);
		} finally {
			this.closeRedis(jedis);
		}
	}
	
	/**
     * For Object, Return the all the elements in the sorted set at key with a score between
     * min and max (including elements with score equal to min or max).
     *
     * @param key
     * @param min
     * @param max
     * @return Ordered from the lowest to the highest score.
     * @since qlchat 1.0
     */
	public <T> List<T> zrangeByScore(final String key, final double min, final double max, Class<T> clazz) {
        ShardedJedis jedis = null;
        try {
            jedis = pool.getResource();
            Set<byte[]> tempSet = jedis.zrangeByScore(key.getBytes(), min, max);
            if (tempSet != null && tempSet.size() > 0) {
                List<T> result = new ArrayList<>();
                for (byte[] value : tempSet) {
                    result.add(fromJsonByteArray(value, clazz));
                }
                return result;
            }
            return null;
        } finally {
            this.closeRedis(jedis);
        }
    }

	public <T> List<T> zrangeByScore(final String key, final double min, final double max, final int offset, final int count, Class<T> clazz) {
		ShardedJedis jedis = null;
		try {
			jedis = pool.getResource();
			Set<byte[]> tempSet = jedis.zrangeByScore(key.getBytes(), min, max, offset, count);
			if (tempSet != null && tempSet.size() > 0) {
				List<T> result = new ArrayList<>();
				for (byte[] value : tempSet) {
					result.add(fromJsonByteArray(value, clazz));
				}
				return result;
			}
			return null;
		} finally {
			this.closeRedis(jedis);
		}
	}

	/**
	 * Return a range of members with scores in a sorted set, by index. Ordered from the lowest to the highest score.
	 *
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 * @since qlchat 1.0
	 */
	public Set<Tuple> zrangeWithScores(final String key, final long start, final long end) {
		ShardedJedis jedis = null;
		try {
			jedis = pool.getResource();
			return jedis.zrangeWithScores(key, start, end);
		} finally {
			this.closeRedis(jedis);
		}
	}



	/**
	 * Return a range of members with scores in a sorted set, by index. Ordered from the highest  to the lowest score.
	 *
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 * @since qlchat 1.0
	 */
	public Set<Tuple> zrevrangeWithScores(final String key, final long start, final long end) {
		ShardedJedis jedis = null;
		try {
			jedis = pool.getResource();
			return jedis.zrevrangeWithScores(key, start, end);
		} finally {
			this.closeRedis(jedis);
		}
	}

	/**
	 * Return the all the elements in the sorted set at key with a score between
	 * min and max (including elements with score equal to min or max). Ordered from the lowest to the highest score.
	 *
	 * @param key
	 * @param min
	 * @param max
	 * @return Ordered from the lowest to the highest score.
	 * @since qlchat 1.0
	 */
	public Set<Tuple> zrangeByScoreWithScores(final String key, final double min, final double max) {
		ShardedJedis jedis = null;
		try {
			jedis = pool.getResource();
			return jedis.zrangeByScoreWithScores(key, min, max);
		} finally {
			this.closeRedis(jedis);
		}
	}

	public Set<Tuple> zrangeByScoreWithScores(final String key, final double min, final double max, final int offset, final int count) {
		ShardedJedis jedis = null;
		try {
			jedis = pool.getResource();
			return jedis.zrangeByScoreWithScores(key, min, max, offset, count);
		} finally {
			this.closeRedis(jedis);
		}
	}

	/**
	 * Return the all the elements in the sorted set at key with a score between
	 * min and max (including elements with score equal to min or max). Ordered from the highest to the lowest score.
	 *
	 * @param key
	 * @param min
	 * @param max
	 * @return Ordered from the highest to the lowest score.
	 * @since qlchat 1.0
	 */
	public Set<Tuple> zrevrangeByScoreWithScores(final String key, final double min, final double max) {
		ShardedJedis jedis = null;
		try {
			jedis = pool.getResource();
			return jedis.zrevrangeByScoreWithScores(key, max, min);
		} finally {
			this.closeRedis(jedis);
		}
	}

	public Set<Tuple> zrevrangeByScoreWithScores(final String key, final double min, final double max, final int offset, final int count) {
		ShardedJedis jedis = null;
		try {
			jedis = pool.getResource();
			return jedis.zrevrangeByScoreWithScores(key, max, min, offset, count);
		} finally {
			this.closeRedis(jedis);
		}
	}

	/**
	 * Remove one or more members from a sorted set
	 *
	 * @param key
	 * @param members
	 * @return
	 * @since qlchat 1.0
	 */
	public Boolean zrem(final String key, final String... members) {
		ShardedJedis jedis = null;
		try {
			jedis = pool.getResource();
			return jedis.zrem(key, members) == 1 ? true : false;
		} finally {
			this.closeRedis(jedis);
		}
	}

	/**
	 * For Object, Remove one or more members from a sorted set
	 *
	 * @param key
	 * @param members
	 * @return
	 * @since qlchat 1.0
	 */
	public Boolean zrem(final String key, final Object... members) {
		ShardedJedis jedis = null;
		try {
			jedis = pool.getResource();
			byte[][] strings = new byte[members.length][];
			for (int j = 0; j < members.length; j++) {
				// strings[j] = HessianSerializer.serialize(members[j]);
				strings[j] = toJsonByteArray(members[j]);
			}
			return jedis.zrem(key.getBytes(), strings) == 1 ? true : false;
		} finally {
			this.closeRedis(jedis);
		}
	}

	/**
	 * Get the score associated with the given member in a sorted set
	 *
	 * @param key
	 * @param member
	 * @return
	 * @since qlchat 1.0
	 */
	public Double zscore(final String key, final String member) {
		ShardedJedis jedis = null;
		try {
			jedis = pool.getResource();
			return jedis.zscore(key, member);
		} finally {
			this.closeRedis(jedis);
		}
	}

	/**
	 * For ObjecGet the score associated with the given member in a sorted set
	 *
	 * @param key
	 * @param member
	 * @return
	 * @since qlchat 1.0
	 */
	public Double zscore(final String key, final Object member) {
		ShardedJedis jedis = null;
		try {
			jedis = pool.getResource();
			// return jedis.zscore(key.getBytes(), HessianSerializer.serialize(member));
			return jedis.zscore(key.getBytes(), toJsonByteArray(member));
		} finally {
			this.closeRedis(jedis);
		}
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
	 * @since qlchat 1.0
	 */
	public Long zremrangeByRank(String key, long start, long end) {
		ShardedJedis jedis = null;
		try {
			jedis = pool.getResource();
			return jedis.zremrangeByRank(key, start, end);
		} finally {
			this.closeRedis(jedis);
		}
	}

	/**
	 * 
	 * Get the length of a sorted set
	 * 
	 * @param key
	 * @param min
	 * @param max
	 * @return
	 * @since qlchat 1.0
	 */
	public Long zcount(final String key, final double min, final double max) {
		ShardedJedis jedis = null;
		try {
			jedis = pool.getResource();
			return jedis.zcount(key, min, max);
		} finally {
			this.closeRedis(jedis);
		}
	}

	/**
	 * Get the number of members in a sorted set
	 * @param key key
	 * @return Long
	 */
	public Long zcard(final String key) {
		ShardedJedis jedis = null;
		try {
			jedis = pool.getResource();
			return jedis.zcard(key);
		} finally {
			this.closeRedis(jedis);
		}
	}
	
	public <T> List<T> zrevrangeByScore(String key, double max, double min, Class<T> clazz) {
        List<T> result = new ArrayList<T>();
        ShardedJedis jedis = null;
        try {
            jedis = pool.getResource();
            Set<String> sets = jedis.zrevrangeByScore(key, max, min);
            if (null == sets || sets.size()  == 0) {
                return result;
            }
            for (String s : sets) {
                result.add(JSON.parseObject(s, clazz));
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if(jedis != null) {jedis.close();}
        }
        return result;
    }

	public <T> List<T> zrevrangeByScore(String key, double max, double min, int offset, int count, Class<T> clazz) {
		List<T> result = new ArrayList<T>();
		ShardedJedis jedis = null;
		try {
			jedis = pool.getResource();
			Set<String> sets = jedis.zrevrangeByScore(key, max, min, offset, count);
			if (null == sets || sets.size()  == 0) {
				return result;
			}
			for (String s : sets) {
				result.add(JSON.parseObject(s, clazz));
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if(jedis != null) {jedis.close();}
		}
		return result;
	}

	public List<String> zrevrangeByScore(String key, double max, double min, int offset, int count) {
		List<String> result = new ArrayList<>();
		ShardedJedis jedis = null;
		try {
			jedis = pool.getResource();
			Set<String> sets = jedis.zrevrangeByScore(key, max, min, offset, count);
			if (null == sets || sets.size()  == 0) {
				return result;
			}
			for (String s : sets) {
				result.add(s);
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if(jedis != null) {jedis.close();}
		}
		return result;
	}

	public Set<String> zrangeByScore(String key, double max, double min, int offset, int count) {
		ShardedJedis jedis = null;
		try {
			jedis = pool.getResource();
			return jedis.zrangeByScore(key, min, max, offset, count);
		} finally {
			this.closeRedis(jedis);
		}
	}

	public Long zremrangeByScore(String key, double start, double end) {
		ShardedJedis jedis = null;
		try {
			jedis = pool.getResource();
			return jedis.zremrangeByScore(key, start, end);
		}finally {
			this.closeRedis(jedis);
		}
	}
}
