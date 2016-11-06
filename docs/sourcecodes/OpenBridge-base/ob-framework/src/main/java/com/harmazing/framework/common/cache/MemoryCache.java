package com.harmazing.framework.common.cache;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class MemoryCache implements ICache {
//	private Map<String, Object> cache = Collections.synchronizedMap(new HashMap<String,Object>());
	private ConcurrentMap<String, Object> cache = new ConcurrentHashMap<String, Object>();
	@Override
	public void add(String key, Object obj, int timeout) {
		this.cache.put(key, obj);
	}
	@Override
	public void add(String key, Object obj) {
		this.cache.put(key, obj);
	}

	@Override
	public void clearAll() {
		this.cache.clear();
	}

	@Override
	public boolean containKey(String key) {
		return this.cache.containsKey(key);
	}

	@Override
	public void delete(String key) {
		cache.remove(key);
	}

	@Override
	public Object get(String key) {
		return cache.get(key);
	}
}