package com.harmazing.framework.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisUtil {
	// 访问密码
	private static String AUTH = "";
	// 可用连接实例的最大数目，默认值为8；
	// 如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
	private static int MAX_TOATL = 512;
	// 控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值也是8。
	private static int MAX_IDLE = 256;
	// 等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时。如果超过等待时间，则直接抛出JedisConnectionException；
	private static int MAX_WAIT = 5120;
	private static int TIMEOUT = 3000;
	// 在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
	private static boolean TEST_ON_BORROW = true;
	private static JedisPool jedisPool = null;

	static {
		try {
			String _ADDR = ConfigUtil.getConfigString("redis.host");
			if (StringUtil.isNotNull(_ADDR)) {
				Integer _PORT = ConfigUtil.getConfigInt("redis.port", 6379);
				String _AUTH = ConfigUtil.getOrElse("redis.auth", AUTH);
				Integer _TIMEOUT = ConfigUtil.getConfigInt("redis.timeout",
						TIMEOUT);
				JedisPoolConfig config = new JedisPoolConfig();
				config.setMaxTotal(MAX_TOATL);
				config.setMaxIdle(MAX_IDLE);
				config.setMaxWaitMillis(MAX_WAIT);
				config.setTestOnBorrow(TEST_ON_BORROW);
				if (StringUtil.isNotNull(_AUTH)) {
					jedisPool = new JedisPool(config, _ADDR, _PORT, _TIMEOUT,
							_AUTH);
				} else {
					jedisPool = new JedisPool(config, _ADDR, _PORT, _TIMEOUT);
				}
			}
		} catch (Exception e) {
			LogUtil.error("redis 链接错误", e);
		}
	}

	public synchronized static Jedis getJedis() {
		try {
			if (jedisPool != null) {
				return jedisPool.getResource();
			}
		} catch (Exception e) {
			LogUtil.error("redis 链接错误", e);
		}
		return null;
	}

	public static abstract interface JedisCallback {
		public abstract Object execute(String key) throws Exception;
	}

	public static void setObject(String key, byte[] obj) {
		Jedis jedis = getJedis();
		try {
			jedis.set(key.getBytes(), obj);
		} catch (Exception e) {
			LogUtil.error(e);
			throw new RuntimeException("Redis setObject exception", e);
		} finally {
			if (jedis != null)
				jedis.close();
		}
	}

	/**
	 * 
	 * @param key
	 * @param obj
	 * @param expire
	 *            单位秒钟
	 */
	public static void setObject(String key, byte[] obj, int expire) {
		Jedis jedis = getJedis();
		try {
			jedis.set(key.getBytes(), obj);
			jedis.expire(key.getBytes(), expire);
		} catch (Exception e) {
			LogUtil.error(e);
			throw new RuntimeException("Redis setObject exception", e);
		} finally {
			if (jedis != null)
				jedis.close();
		}
	}

	public static void setObject(String key, Object obj) {
		setObject(key, serialize(obj));
	}

	/**
	 * 
	 * @param key
	 * @param obj
	 * @param expire
	 *            单位秒钟
	 */
	public static void setObject(String key, Object obj, int expire) {
		setObject(key, serialize(obj), expire);
	}

	public static Object getObject(final String key) {
		return getObjectByCallback(key, new JedisCallback() {
			@Override
			public Object execute(String key) throws Exception {
				return null;
			}
		});
	}

	public static Object getObjectByCallback(final String key,
			JedisCallback callback) {
		Jedis jedis = getJedis();
		try {
			byte[] bytes = jedis.get(key.getBytes());
			if (bytes != null) {
				return unserialize(bytes);
			} else {
				return callback.execute(key);
			}
		} catch (Exception e) {
			LogUtil.error(e);
			throw new RuntimeException("Redis getObject exception", e);
		} finally {
			if (jedis != null)
				jedis.close();
		}
	}

	public static byte[] serialize(Object object) {
		ObjectOutputStream oos = null;
		ByteArrayOutputStream baos = null;
		try {
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			oos.writeObject(object);
			return baos.toByteArray();
		} catch (Exception e) {
		}
		return null;
	}

	public static Object unserialize(byte[] bytes) {
		ByteArrayInputStream bais = null;
		try {
			bais = new ByteArrayInputStream(bytes);
			ObjectInputStream ois = new ObjectInputStream(bais);
			return ois.readObject();
		} catch (Exception e) {
		}
		return null;
	}

	public static void delete(String key) {
		Jedis jedis = getJedis();
		try {
			jedis.del(key);
		} catch (Exception e) {
			LogUtil.error(e);
			throw new RuntimeException("Redis delete exception", e);
		} finally {
			if (jedis != null)
				jedis.close();
		}
	}
}
