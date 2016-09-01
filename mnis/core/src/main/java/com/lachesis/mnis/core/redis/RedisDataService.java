package com.lachesis.mnis.core.redis;

import java.util.List;
import java.util.Map;
/**
 * redis操作hset数据(hashmap)
 * @author liangming.deng
 *
 * @param <T>
 */
public interface RedisDataService<T> {
	/**
	 * 保存数据到redis
	 * @param key
	 * @param hashKey
	 * @param t
	 */
	void saveDataToRedis(String key,String hashKey,T t) throws Exception;
	/**
	 * 删除reids中数据
	 * @param key
	 * @param hashKey
	 */
	void delDataToRedis(String key,String hashKey) throws Exception;
	/**
	 * 根据Key获取数据
	 * @param key
	 * @param hashKey
	 * @return
	 */
	T getDataByIdFromRedis(String key,String hashKey) throws Exception;
	/**
	 * 获取所有数据
	 * @return
	 */
	Map<Object, Object> getAllData(String key) throws Exception;
	
	List<T> getAllDataList(String key) throws Exception;
}
