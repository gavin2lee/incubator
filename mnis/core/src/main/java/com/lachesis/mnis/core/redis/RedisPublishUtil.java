package com.lachesis.mnis.core.redis;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;

@Component
public class RedisPublishUtil {
	
	@Autowired
	private RedisService redisService;
	private static Logger LOGGER = LoggerFactory.getLogger(RedisPublishUtil.class);
	public Set<String> getUserIds(String pushUserKey){
		
		Jedis jedis = redisService.getRedis();
		if(jedis == null) {
			LOGGER.error("Cannot get redis connection! Check Redis server!");
			return null;
		}
		// 病房巡视消息推送的频道和频道属性配置key
		Set<String> userIds = jedis.smembers(pushUserKey);
		redisService.returnRedis(jedis);
		
		return userIds;
	}
	
}
