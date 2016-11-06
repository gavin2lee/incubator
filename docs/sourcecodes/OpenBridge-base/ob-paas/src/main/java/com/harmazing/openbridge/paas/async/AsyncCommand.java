package com.harmazing.openbridge.paas.async;

public interface AsyncCommand {
	public abstract void execute(AsyncTask task) throws AsyncException;
}
