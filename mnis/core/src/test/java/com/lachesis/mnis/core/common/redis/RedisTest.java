package com.lachesis.mnis.core.common.redis;

import org.junit.Assert;
import org.junit.Test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisShardInfo;

public class RedisTest {
	@Test
	public void testRedisStarted() {
		JedisShardInfo info = new JedisShardInfo("10.2.2.20");
		info.setPassword("redis102220");
		Jedis jedis = new Jedis(info);
//		Jedis jedis = new Jedis("10.2.2.20");
		Assert.assertNotNull(jedis);
		String ping = jedis.ping();
		System.out.println(ping);
		Assert.assertEquals("PONG", ping);
	}
}
