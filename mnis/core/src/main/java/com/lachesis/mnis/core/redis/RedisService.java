package com.lachesis.mnis.core.redis;

import java.util.Map;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Response;

/***
 * Redis 服务提供类
 * 
 * @author yuliang.xu
 * @date 2015年5月6日 下午5:02:58
 *
 */
public interface RedisService {
	/**
	 * 启用Pub/Sub
	 * 
	 * @return
	 */
	boolean enablePubSub();

	/**
	 * 关闭Pub/Sub
	 * 
	 * @return
	 */
	boolean closePubSub();

	/**
	 * Pub/Sub是否启用
	 * 
	 * @return
	 */
	boolean isEnabledPubSub();

	/**
	 * 获取Redis连接
	 * 
	 * @return
	 */
	Jedis getRedis();

	/**
	 * 归还Redis连接
	 * 
	 * @param jedis
	 */
	void returnRedis(Jedis jedis);

	/**
	 * 发布
	 * 
	 * @param channel
	 *            频道名称
	 * @param message
	 *            消息
	 * @return
	 */
	Long publish(String channel, String message);

	/**
	 * 发布
	 * 
	 * @param channel
	 *            频道名称
	 * @param message
	 *            消息
	 * @return
	 */
	Long publish(byte[] channel, byte[] message);

	/**
	 * 批量发布
	 * 
	 * @param channel
	 *            频道名称
	 * @param message
	 *            消息
	 * @return
	 */
	Response<Long> publishPipeline(String channel, String message);

	/**
	 * 批量发布
	 * 
	 * @param channel
	 *            频道名称
	 * @param message
	 *            消息
	 * @return
	 */
	Response<Long> publishPipeline(byte[] channel, byte[] message);

	/**
	 * 订阅
	 * 
	 * @param subscribe
	 *            实现一个类，继承自JedisPubSub类并实现Serializable，在该类的覆写方法中处理订阅信息。
	 * @param channel
	 *            频道名称
	 * @return
	 */
	void subscribe(Subscribe subscribe, String... channel);

	/**
	 * 订阅
	 * 
	 * @param subscribe
	 *            实现一个类，继承自JedisPubSub类并实现Serializable，在该类的覆写方法中处理订阅信息。
	 * @param pattern
	 *            频道名称表达式
	 */
	void psubscribe(Subscribe subscribe, String... pattern);

	/**
	 * 订阅
	 * 
	 * @param binarySubscribes
	 *            实现一个类，继承自BinaryJedisPubSub类并实现Serializable，在该类的覆写方法中处理订阅信息。
	 * @param channel
	 *            频道名称
	 */
	void subscribe(BinarySubscribe binarySubscribes, byte[]... channel);

	/**
	 * 订阅
	 * 
	 * @param binarySubscribes
	 *            实现一个类，继承自BinaryJedisPubSub类并实现Serializable，在该类的覆写方法中处理订阅信息。
	 * @param pattern
	 *            频道名称表达式
	 */
	void psubscribe(BinarySubscribe binarySubscribes, byte[]... pattern);

	/**
	 * 保存PubSub用户个人配置
	 * 
	 * @param userId
	 *            用户Id，唯一标识
	 * @param patientId
	 *            病人Id，唯一标识
	 * @param channel
	 *            订阅频道名称
	 * @param map
	 *            配置信息
	 * @return
	 */
	String savePubSubUserConfig(String userId, String patientId,
			String channel, Map<String, String> map);

	/**
	 * 获取指定用户和订阅频道的配置信息
	 * 
	 * @param userId
	 *            用户Id，唯一标识
	 * @param patientId
	 *            病人Id，唯一标识
	 * @param channel
	 *            订阅频道名称
	 * @param field
	 *            属性名称
	 * @return
	 */
	String getPubSubUserConfig(String userId, String patientId, String channel,
			String field);

	/**
	 * 设置指定用户和订阅频道的配置信息
	 * 
	 * @param userId
	 *            用户Id，唯一标识
	 * @param patientId
	 *            病人Id，唯一标识
	 * @param channel
	 *            订阅频道名称
	 * @param field
	 *            属性名称
	 * @param value
	 *            值
	 * @return
	 */
	Long setPubSubUserConfig(String userId, String patientId, String channel,
			String field, String value);

	/**
	 * 获取指定用户和订阅频道的所有配置信息
	 * 
	 * @param userId
	 *            用户Id，唯一标识
	 * @param patientId
	 *            病人Id，唯一标识
	 * @param channel
	 *            订阅频道名称
	 * @return
	 */
	Map<String, String> getPubSubUserConfigAll(String userId, String patientId,
			String channel);

	/**
	 * 删除指定用户和订阅频道的配置信息
	 * 
	 * @param userId
	 *            用户Id，唯一标识
	 * @param patientId
	 *            病人Id，唯一标识
	 * @param channel
	 *            频道名称
	 * @param field
	 *            属性参数数组
	 * @return
	 */
	Long deletePubSubUserConfig(String userId, String patientId,
			String channel, String... fields);

	/**
	 * 获取和保存用户订阅频道配置信息
	 * 
	 * @param channel
	 *            频道名称
	 * @param userId
	 *            用户Id，唯一标识
	 * @param patientId
	 *            病人Id，唯一标识
	 * @return
	 */
	Map<String, String> getAndSavePubSubConfig(String channel, String userId,
			String patientId);
}
