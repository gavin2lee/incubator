package com.harmazing.openbridge.paas.async;

import org.apache.log4j.Logger;


public class AsyncRunnable implements Runnable {
	
	private AsyncCommand command;

	private AsyncCallback callback;

	private AsyncTask task;

	private Logger logger = Logger.getLogger(AsyncRunnable.class);

	public AsyncRunnable(AsyncTask task, AsyncCommand command,
			AsyncCallback callback) {
		this.task = task;
		this.command = command;
		this.callback = callback;
	}

	@Override
	public void run() {
		String threadName = Thread.currentThread().getName();
		try {
			command.execute(this.task);
			if (logger.isDebugEnabled()) {
				logger.debug(threadName + " --command-- execute success "
						+ task.getTaskId());
			}
		} catch (AsyncException e) {
			logger.error(e);
			this.task.setAsyncException(e);
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
			this.task.setAsyncException(new AsyncException(this.task, e));
		}
		if (callback != null) {
			callback.execute(this.task);
			if (logger.isDebugEnabled()) {
				logger.debug(threadName + " --callback-- execute success "
						+ task.getTaskId());
			}
		}
	}
}
