package com.harmazing.framework.quartz;

public interface ListenerHandler<T> {
	
	public T handler(Object o);

}
