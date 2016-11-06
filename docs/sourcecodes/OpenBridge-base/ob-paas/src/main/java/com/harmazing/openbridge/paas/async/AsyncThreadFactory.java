package com.harmazing.openbridge.paas.async;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class AsyncThreadFactory implements ThreadFactory {
	private static final AtomicInteger POOL_SEQ = new AtomicInteger(1);

	private final AtomicInteger mThreadNum = new AtomicInteger(1);

	private final String mPrefix;

	private final boolean mDaemo;

	private final ThreadGroup mGroup;

	public AsyncThreadFactory() {
		this("pool-" + POOL_SEQ.getAndIncrement(), false);
	}

	public AsyncThreadFactory(String prefix) {
		this(prefix, false);
	}

	public AsyncThreadFactory(String prefix, boolean daemo) {
		mPrefix = prefix + "-thread-";
		mDaemo = daemo;
		SecurityManager s = System.getSecurityManager();
		mGroup = (s == null) ? Thread.currentThread().getThreadGroup() : s
				.getThreadGroup();
	}

	public Thread newThread(Runnable runnable) {
		String name = mPrefix + mThreadNum.getAndIncrement();
		Thread ret = new Thread(mGroup, runnable, name, 0);
		ret.setDaemon(mDaemo);
		return ret;
	}

	public ThreadGroup getThreadGroup() {
		return mGroup;
	}
}
