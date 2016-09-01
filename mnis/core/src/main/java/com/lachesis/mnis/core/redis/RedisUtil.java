package com.lachesis.mnis.core.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;

/**
 * redis 工具类，管理redis连接
 * 
 * @ClassName: RedisUtil
 * @author yuliang.xu
 * @date 2015年5月6日 下午5:01:21
 *
 */
@Component("redisUtil")
public final class RedisUtil {
	static final Logger LOGGER = LoggerFactory.getLogger(RedisUtil.class);

	@Autowired
	private JedisPool jedisPool;

	/**
	 * 获取连接
	 * 
	 * @return
	 */
	@Lazy
	public Jedis getConnection() {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error(e.toString());
			jedisPool.returnBrokenResource(jedis);
		}
		return jedis;
	}

	/**
	 * 关闭连接
	 * 
	 * @param jedis
	 */
	public void closeConnection(final Jedis jedis) {
		if (null != jedis) {
			try {
				jedisPool.returnResource(jedis);
			} catch (Exception e) {
				jedisPool.returnBrokenResource(jedis);
				LOGGER.error(e.toString());
			} finally {
				if (jedis.isConnected()) {
					jedis.quit();
					jedis.disconnect();
				}
			}
		}
	}

	/**
	 * 获取批处理
	 * 
	 * @return
	 */
	public Pipeline getPipeline() {
		Pipeline pipeline = null;
		if (getConnection() != null) {
			pipeline = getConnection().pipelined();
		}
		return pipeline;
	}
	
	public void setJedisPool(JedisPool jedisPool) {
		this.jedisPool = jedisPool;
	}
}
