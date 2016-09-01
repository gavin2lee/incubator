package com.lachesis.mnis.core.redis;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;

/***
 * 
 * Redis服务提供者 实现类
 *
 * @author yuliang.xu
 * @date 2015年5月6日 下午5:06:19 
 *
 */
@Component("redisService")
public class RedisServiceImpl implements RedisService{
	private static final Logger LOGGER = LoggerFactory.getLogger(RedisServiceImpl.class);
	
	@Autowired
	private RedisUtil redisUtil;

	private boolean setPubSub(String value) {
		Jedis jedis = redisUtil.getConnection();
		String result = jedis.set(RedisConstants.KEY_ENABLE_PUBSUB, value);
		redisUtil.closeConnection(jedis);
		if(result.equalsIgnoreCase(RedisConstants.SUCCESS)){
			return true;
		}else{
			LOGGER.error(result);
			return false;
		}
	}

	@Override
	public boolean enablePubSub() {
		return setPubSub(RedisConstants.ENABLE);
	}

	@Override
	public boolean closePubSub() {
		return setPubSub(RedisConstants.DISABLE);
	}

	@Override
	public boolean isEnabledPubSub() {
		Jedis jedis = redisUtil.getConnection();
		String value = jedis.get(RedisConstants.KEY_ENABLE_PUBSUB);
		redisUtil.closeConnection(jedis);
		if(value.equals(RedisConstants.ENABLE)){
			return true;
		}
		return false;
	}

	@Override
	public void subscribe(Subscribe subscribe, String... channel) {
		Jedis jedis = redisUtil.getConnection();
		jedis.subscribe(subscribe, channel);
	}

	@Override
	public void psubscribe(Subscribe subscribe,String... pattern) {
		Jedis jedis = redisUtil.getConnection();
		jedis.psubscribe(subscribe, pattern);
	}

	@Override
	public void subscribe(BinarySubscribe binarySubscribe,byte[]... channel) {
		Jedis jedis = redisUtil.getConnection();
		jedis.subscribe(binarySubscribe, channel);
	}

	@Override
	public void psubscribe(BinarySubscribe binarySubscribe,byte[]... pattern) {
		Jedis jedis = redisUtil.getConnection();
		jedis.psubscribe(binarySubscribe, pattern);
	}

	@Override
	public Long publish(String channel, String message) {
		Jedis jedis = redisUtil.getConnection();
		Long result = jedis.publish(channel, message);
		redisUtil.closeConnection(jedis);
		return result;
	}

	@Override
	public Long publish(byte[] channel, byte[] message) {
		Jedis jedis = redisUtil.getConnection();
		Long result = jedis.publish(channel, message);
		redisUtil.closeConnection(jedis);
		return result;
	}

	@Override
	public Response<Long> publishPipeline(String channel, String message) {
		Jedis jedis = redisUtil.getConnection();
		Pipeline pipeline = jedis.pipelined();
		Response<Long> response = pipeline.publish(channel, message);
		pipeline.sync();
		redisUtil.closeConnection(jedis);
		return response;
	}

	@Override
	public Response<Long> publishPipeline(byte[] channel, byte[] message) {
		Jedis jedis = redisUtil.getConnection();
		Pipeline pipeline = jedis.pipelined();
		Response<Long> response = pipeline.publish(channel, message);
		pipeline.sync();
		redisUtil.closeConnection(jedis);
		return response;
	}

	@Override
	public String savePubSubUserConfig(String userId,String patientId, String channel,
			Map<String, String> map) {
		Jedis jedis = redisUtil.getConnection();
		String result = jedis.hmset(joinPubSubUserConfigKey(userId,patientId, channel), map);
		redisUtil.closeConnection(jedis);
		return result;
	}
	/**
	 * 拼接Pub/Sub 用户配置key
	 * @param userId
	 * @param channel
	 * @return
	 */
	private String joinPubSubUserConfigKey(String userId, String patientId, String channel) {
		StringBuffer key = new StringBuffer(RedisConstants.KEY_PUBSUB_USER_CONFIG);
		key.append(channel).append(userId);
		if(patientId != null) {
			key.append(".");
			key.append(patientId);	
		}
		return key.toString();
	}
	
	@Override
	public String getPubSubUserConfig(String userId,String patientId,
			String channel, String field) {
		Jedis jedis = redisUtil.getConnection();
		String result = jedis.hget(joinPubSubUserConfigKey(userId,patientId, channel), field);
		redisUtil.closeConnection(jedis);
		return result;
	}

	@Override
	public Long setPubSubUserConfig(String userId, String channel,String patientId,
			String field, String value) {
		Jedis jedis = redisUtil.getConnection();
		Long result = jedis.hset(joinPubSubUserConfigKey(userId,patientId, channel), field, value);
		if(result >=0 ){
			jedis.save();	
		}
		redisUtil.closeConnection(jedis);
		return result;
	}

	@Override
	public Map<String, String> getPubSubUserConfigAll(String userId,String patientId,
			String channel) {
		Jedis jedis = redisUtil.getConnection();
		if(null == jedis){
			return null;
		}
		Map<String, String> result = jedis.hgetAll(joinPubSubUserConfigKey(userId, patientId, channel));
		redisUtil.closeConnection(jedis);
		return result;
	}

	@Override
	public Long deletePubSubUserConfig(String userId,String patientId, String channel,
			String... fields) {
		Jedis jedis = redisUtil.getConnection();
		Long result = jedis.hdel(joinPubSubUserConfigKey(userId,patientId, channel), fields);
		redisUtil.closeConnection(jedis);
		return result;
	}

	@Override
	public Jedis getRedis() {
		return redisUtil.getConnection();
	}

	@Override
	public Map<String, String> getAndSavePubSubConfig(String channel,
			String userId, String patientId) {
		Map<String,String> mapValue = getPubSubUserConfigAll(userId, patientId, channel);
		if(null == mapValue){
			return new HashMap<String, String>();
		}
		if(mapValue.keySet().isEmpty()){
			String[] keys = RedisConstants.MAP_CHANNEL_PROPS.get(channel);
			Map<String,String> mapConfig = new HashMap<String,String>();
			for(String key : keys){
				if(key.equals(RedisConstants.FIELD_ENABLE)){
					mapConfig.put(key, "1");
				}
				if(key.equals(RedisConstants.FIELD_BEFORETIME_MINUTE)){
					mapConfig.put(key, "5");
				}
			}
			savePubSubUserConfig(userId, patientId, channel, mapConfig);
			mapValue = getPubSubUserConfigAll(userId,patientId, channel);
		}
		return mapValue;
	}
	
	/**
	 * 关闭应用时的处理：比如持久化redis数据等。
	 */
	@PreDestroy
	public void shutDown() {
		Jedis jedis = redisUtil.getConnection();
		jedis.save();
		redisUtil.closeConnection(jedis);
	}

	@Override
	public void returnRedis(Jedis jedis) {
		if(jedis != null) {
			redisUtil.closeConnection(jedis);
		}
	}
}
