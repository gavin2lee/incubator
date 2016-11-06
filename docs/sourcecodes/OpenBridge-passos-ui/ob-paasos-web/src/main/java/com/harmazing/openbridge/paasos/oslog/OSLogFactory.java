package com.harmazing.openbridge.paasos.oslog;


import java.util.Date;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.springframework.util.StringUtils;

import com.harmazing.framework.util.StringUtil;
import com.harmazing.openbridge.paasos.oslog.model.PaasProjectLog;


public class OSLogFactory {
	
	public static final String SAVE_OPERATOR = "保存";
	
	public static final String MODIFY_OPERATOR = "修改";
	
	public static final String STOP_OPERATOR = "停止";
	
	public static final String DELETE_OPERATOR = "删除";
	
	public static final String DEPLOY_OPERATOR = "部署";
	
	public static final String KUORONG_OPERATOR = "扩容";
	
	private static ThreadLocal<String> session = new ThreadLocal<String>();
	
	private static OSLogFactory factory = new OSLogFactory();
	
	private LogDispatcher dispatcher = new LogDispatcher(factory);
	
	private OSLogFactory(){
		
	}
	
	public static OSLogFactory get(){
		return factory;
	}
	
	public static void add(String key,String type,String message,String user,String isBegin){
		PaasProjectLog ol = new PaasProjectLog();
		ol.setId(StringUtil.getUUID());
		ol.setKey(key);
		ol.setType(type);
		ol.setMessage(message);
		ol.setCreateUser(user);
		ol.setCreateDate(new Date().getTime());
		ol.setBegin(isBegin);
		
		System.out.println("---------------"+Thread.currentThread().getName());
		
		if(StringUtils.hasText(isBegin) && "1".equals(isBegin)){
			//初始化
			session.remove();
			session.set(StringUtil.getUUID());
		}
		ol.setSessionId(session.get());
		
		factory.getDispatcher().log(ol);
	}
	public static void add(String key,String type,String message,String user,boolean isBegin){
		if(isBegin){
			add(key,type,message,user,"1");
		}
		else{
			add(key,type,message,user,null);
		}
	}
	
	public static void add(String key,String type,String message){
		add(key,type,message,null,false);
	}
	
	public static void add(String key,String type,String message,boolean isEnd){
		if(isEnd){
			add(key,type,message,null,"2");
		}else{
			add(key,type,message,null,false);
		}
		
	}

	public LogDispatcher getDispatcher() {
		return dispatcher;
	}
	
	public static void bindSessionId(String sessionId){
		session.set(sessionId);
	}
	
	public static void unBindSessionId(){
		session.remove();
	}

	public static String getSessionValue() {
		String v = session.get();
		if(StringUtils.isEmpty(v)){
			session.set(StringUtil.getUUID());
		}
		return session.get();
	}
	
	

}
