package com.harmazing.framework.web;

import javax.servlet.ServletContext;

import org.springframework.context.ApplicationEvent;

import com.harmazing.framework.quartz.ListenerHandler;

public class SystemEvent extends ApplicationEvent {
	public static final String STARTUP = "startup";
	public static final String SHUTDOWN = "shutdown";
	
	public static final String BEAN_HANDLER = "bean_handler";

	private ServletContext servletContext;
	private String type;
	
	private Class clazz;

	public SystemEvent(Object source, String type, ServletContext servletContext) {
		super(source);
		this.type = type;
		this.servletContext = servletContext;
	}
	
	public SystemEvent(Object source, String type, Class<? extends ListenerHandler> clazz) {
		super(source);
		this.type = type;
		this.clazz = clazz;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public ServletContext getServletContext() {
		return servletContext;
	}

	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	public Class getClazz() {
		return clazz;
	}

	public void setClazz(Class clazz) {
		this.clazz = clazz;
	}
	
	

}
