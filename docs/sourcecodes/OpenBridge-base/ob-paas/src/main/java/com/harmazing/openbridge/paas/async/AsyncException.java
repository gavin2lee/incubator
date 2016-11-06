package com.harmazing.openbridge.paas.async;


public class AsyncException extends RuntimeException {
	private AsyncTask asyncTask;

	public AsyncTask getAsyncTask() {
		return this.asyncTask;
	}

	public AsyncException(AsyncTask asyncTask) {
		super();
		this.asyncTask = asyncTask;
	}

	public AsyncException(AsyncTask asyncTask, Throwable cause) {
		super(cause);
		this.asyncTask = asyncTask;
	}
}
