package com.lachesis.mnis.core.common.redis;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.lachesis.mnis.core.SpringTest;
import com.lachesis.mnis.core.redis.RedisService;

/**
 *@author xin.chen
 **/
public class RedisServiceImplTest extends SpringTest {
	
	@Autowired RedisService redisService;
	
	@Test
	public void testSetRedisUtil() {
		
	}

	@Test
	public void testSetPubSub() {
		
	}

	@Test
	public void testEnablePubSub() {
		
	}

	@Test
	public void testClosePubSub() {
		
	}

	@Test
	public void testIsEnabledPubSub() {
		
	}

	@Test
	public void testSubscribeSubscribeStringArray() {
		
	}

	@Test
	public void testPsubscribeSubscribeStringArray() {
		
	}

	@Test
	public void testSubscribeBinarySubscribeByteArrayArray() {
		
	}

	@Test
	public void testPsubscribeBinarySubscribeByteArrayArray() {
		
	}

	@Test
	public void testPublishStringString() {
		
	}

	@Test
	public void testPublishByteArrayByteArray() {
		
	}

	@Test
	public void testPublishPipelineStringString() {
		
	}

	@Test
	public void testPublishPipelineByteArrayByteArray() {
		
	}

	@Test
	public void testSavePubSubUserConfig() {
		
	}

	@Test
	public void testGetPubSubUserConfig() {
		
	}

	@Test
	public void testSetPubSubUserConfig() {
		
	}

	@Test
	public void testGetPubSubUserConfigAll() {
		
	}

	@Test
	public void testDeletePubSubUserConfig() {
		
	}

	@Test
	public void testGetRedis() {
		assertNotNull(redisService.getRedis());
	}

	@Test
	public void testGetAndSavePubSubConfig() {
	}
}
