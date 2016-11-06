package com.harmazing.openbridge.paas.util;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.harmazing.framework.util.ConfigUtil;

import mousio.etcd4j.EtcdClient;
import mousio.etcd4j.promises.EtcdResponsePromise;
import mousio.etcd4j.requests.EtcdKeyGetRequest;
import mousio.etcd4j.responses.EtcdAuthenticationException;
import mousio.etcd4j.responses.EtcdException;
import mousio.etcd4j.responses.EtcdKeysResponse;

public class EtcdUtil {
	
	private static Log logger = LogFactory.getLog(EtcdUtil.class);
	
	private static EtcdClient client;
	
	private static Object lock = new Object();
	
	private EtcdUtil(){
		
	}
	
	public static EtcdClient getClient(){
		if(client!=null){
			return client;
		}
		synchronized (lock) {
			if(client!=null){
				return client;
			}
			try{
				String etcd = ConfigUtil.getConfigString("paasos.k8s.etcdserver");
				if(!StringUtils.hasText(etcd)){
					throw new NoSupportEtcdException("无法获取paasos.k8s.etcdserver配置项目");
				}
//				String etcd = "http://192.168.1.72:4012";
				String[] etcds = etcd.split(",");
				URI[] baseUri = new URI[etcds.length];
				for(int i=0;i<etcds.length;i++){
					baseUri[i] = URI.create(etcds[i]);
				}
				client = new EtcdClient(baseUri);
				return client;
			}
			catch(NoSupportEtcdException e1){
				throw e1;
			}
			catch(Exception e){
				logger.error("创建etcd客户端出错",e);
			}
		}
		return client;
	}
	
	public static String getKey(String directory,String key) {
		return getKey(directory+""+key);
	}
	
	public static String getKey(String path) {
		EtcdClient c = EtcdUtil.getClient();
		EtcdKeysResponse r = null;
		try{
			r = c.get(path).consistent().send().get();
		}
		catch(EtcdException e1){
			//这里估计查询 不到key失败 默认给null
			return null;
		}
		catch(Exception e){
			logger.error("查询失败", e);
			throw new RuntimeException("查询失败",e);
		}
		if(r==null){
			return null;
		}
		return r.getNode().getValue();
	}
	
	public static String createKey(String key,String value){
		EtcdClient c = EtcdUtil.getClient();
		EtcdKeysResponse response = null;
		try {
			response = c.put(key, value).send().get();
		} catch (Exception  e) {
			logger.error("更新失败", e);
			throw new RuntimeException("更新失败",e);
		}
		return response.getAction().name();
	}
	
	public static void delete(String path){
		EtcdClient c = EtcdUtil.getClient();
		EtcdKeysResponse response = null;
		try {
			response = c.delete(path).send().get();
		} catch (Exception  e) {
			//删除失败就不要管了
			logger.error("删除失败", e);
//			throw new RuntimeException("删除失败",e);
		}
//		return response.getAction().name();
	}
	
	/**
	 * 
	 * @param containId 容器ID
	 * @param url  域名
	 * @param address  192.168.31.210:8080
	 */
	public static void updateDNS(String containId,List<String> urls,String ...address){
		//把上一次设置的域名给删掉
		String k = "/container/"+containId;
		String oldUrlPath=null;
		try{
			oldUrlPath = EtcdUtil.getKey(k);
		}
		catch(NoSupportEtcdException e){
			logger.debug(e.getMessage());
//			return ;
		}
		if(StringUtils.hasText(oldUrlPath)){
			String[] oldPath = oldUrlPath.split(";");
			for(String op : oldPath){
				EtcdUtil.delete(op);
			}
		}
		EtcdUtil.delete(k);
		//清除skydns 的时候传递 urls或在address位null就可以了
		if(urls==null || urls.size()==0){
			logger.debug("urls 为空");
			return ;
		}
		if(address==null || address.length==0 || address[0]==null){
			logger.debug("address 为空");
			return ;
		}
		List<String> u = new ArrayList<String>();
		for(String url : urls){
			String[] paths = url.split("\\.");
			StringBuffer path = new StringBuffer();
			path.append("/skydns");
			for(int i=(paths.length-1);i>=0;i--){
				path.append("/").append(paths[i]);
			}
			//好像没有支持代理多个ip
			String[] ms = address[0].split(":");
			JSONObject jo = new JSONObject();
			if(ms.length==1){
				String ip = ms[0];
				jo.put("host", ip);
			}
			else{
				String ip = ms[0];
				String port = ms[1];
				jo.put("host", ip);
				jo.put("port", port);
			}
			EtcdUtil.createKey(path.toString(), jo.toJSONString());
			logger.debug("add "+path.toString()+" ---->"+jo.toJSONString());
			u.add(path.toString());
		}
		
		String us = StringUtils.collectionToDelimitedString(u, ";");
		EtcdUtil.createKey(k, us);
		logger.debug("add "+k+" ---->"+us);
	}
	
//	public static void updateDNS(String containId,String url,String ...address){
//		
//		
//		String k = "/container/"+containId;
//		String oldUrlPath=null;
//		try{
//			oldUrlPath = EtcdUtil.getKey(k);
//		}
//		catch(NoSupportEtcdException e){
//			logger.debug(e.getMessage());
//			return ;
//		}
//		
//		String[] paths = url.split("\\.");
//		StringBuffer path = new StringBuffer();
//		path.append("/skydns");
//		for(int i=(paths.length-1);i>=0;i--){
//			path.append("/").append(paths[i]);
//		}
//		
//		//好像没有支持代理多个ip
//		String[] ms = address[0].split(":");
//		JSONObject jo = new JSONObject();
//		if(ms.length==1){
//			String ip = ms[0];
//			jo.put("host", ip);
//		}
//		else{
//			String ip = ms[0];
//			String port = ms[1];
//			jo.put("host", ip);
//			jo.put("port", port);
//		}
//		if(StringUtils.hasText(oldUrlPath)){
//			//把上一次设置的域名给删掉
//			EtcdUtil.delete(oldUrlPath);
//		}
//		//设置域名
//		EtcdUtil.createKey(path.toString(), jo.toJSONString());
//		//缓存容器对应的域名 方便下次查找删除
//		EtcdUtil.createKey(k, path.toString());
//	}
	
	public static void main(String[] args){
//		EtcdUtil.updateDNS("123132132","yihecloud.base.com", "192.168.31.213:8080");
	}

}
