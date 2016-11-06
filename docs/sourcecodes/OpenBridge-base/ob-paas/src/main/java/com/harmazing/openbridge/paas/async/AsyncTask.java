package com.harmazing.openbridge.paas.async;

import java.io.Serializable;


public interface AsyncTask extends Serializable {
	public String getTaskId();

	public void setAsyncException(AsyncException e);

	public AsyncException getAsyncException();
}
