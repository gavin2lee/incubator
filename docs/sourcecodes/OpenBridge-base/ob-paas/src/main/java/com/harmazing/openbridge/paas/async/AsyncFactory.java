package com.harmazing.openbridge.paas.async;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class AsyncFactory {
	private final static ExecutorService executorService = Executors
			.newFixedThreadPool(42, new AsyncThreadFactory(
					"AsyncThreadFactory", true));

	public static void execute(AsyncRunnable ar) {
		executorService.execute(ar);
	}
}
