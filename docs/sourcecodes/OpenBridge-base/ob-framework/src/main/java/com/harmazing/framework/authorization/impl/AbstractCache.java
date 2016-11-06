package com.harmazing.framework.authorization.impl;

import java.util.Date;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AbstractCache<K, V> {
	public static ScheduledExecutorService service = Executors
			.newSingleThreadScheduledExecutor();

	public static class CacheObject<V> {
		private final V value;
		private Date date;

		public CacheObject(V value) {
			this.value = value;
			touch();
		}

		public V getValue() {
			return value;
		}

		/**
		 * 单位毫秒
		 *
		 * @return
		 */
		public long getTouchTime() {
			return (new Date()).getTime() - date.getTime();
		}

		public void touch() {
			date = new Date();
		}
	}

	@SuppressWarnings("rawtypes")
	public static class CacheRelease implements Runnable {
		private AbstractCache cr;

		public CacheRelease(AbstractCache cr) {
			this.cr = cr;
		}

		@Override
		public void run() {
			this.cr.clearInvalid();
		}

	}

	private final ConcurrentHashMap<K, CacheObject<V>> map = new ConcurrentHashMap<K, AbstractCache.CacheObject<V>>();
	// 读取一次后有效期顺延，默认为 false
	private final boolean isLimitLife;
	// 缓存有效期
	private final long lifeCircle; // 单位毫秒

	public AbstractCache() {
		this(false);
	}

	public AbstractCache(boolean isLimitLife) {
		this(isLimitLife, 0);
	}

	public AbstractCache(long lifeCircle) {
		this(false, lifeCircle);
	}

	public AbstractCache(boolean isLimitLife, long lifeCircle) {
		this.isLimitLife = isLimitLife;
		this.lifeCircle = lifeCircle;
		service.scheduleAtFixedRate(new CacheRelease(this), this.lifeCircle,
				this.lifeCircle, TimeUnit.MILLISECONDS);
	}

	public V get(K k) {
		if (k == null) {
			return null;
		}
		CacheObject<V> cacheObject = map.get(k);
		if (cacheObject == null) {
			return null;
		}
		// 有效期为负数 一直有效
		if (lifeCircle <= 0) {
			return cacheObject.getValue();
		}
		// 如果已经过期放回空
		if (cacheObject.getTouchTime() > lifeCircle) {
			map.remove(k);
			return null;
		}
		// 读取一次顺延有效期
		if (isLimitLife) {
			cacheObject.touch();
		}
		return cacheObject.getValue();
	}

	public V get(K k, V defaultValue) {
		V v = get(k);
		if (v != null) {
			return v;
		}
		set(k, defaultValue);
		return defaultValue;
	}

	public void set(K k, V v) {
		if (k == null || v == null) {
			return;
		}
		CacheObject<V> cacheObject = new CacheObject<V>(v);
		map.put(k, cacheObject);
	}

	public void remove(K k) {
		if (k == null) {
			return;
		}
		map.remove(k);
	}

	public void clearInvalid() {
		for (Entry<K, CacheObject<V>> entry : map.entrySet()) {
			if (entry.getValue().getTouchTime() > lifeCircle) {
				map.remove(entry.getKey());
			}
		}
	}

	public void clear() {
		map.clear();
	}

	public int size() {
		return map.size();
	}

	public boolean isLimitLife() {
		return isLimitLife;
	}

	public long getLifeCircle() {
		return lifeCircle;
	}
}
