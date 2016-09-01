package com.lachesis.mnis.core.util;

import java.lang.reflect.Type;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

public class GsonUtils {
	private GsonUtils() {}
	
	private static final Logger LOG = LoggerFactory.getLogger(GsonUtils.class);
	
	/***
	 * 创建默认Gson对象
	 *
	 * @return Gson 返回类型 
	 */
	public static Gson createDefaultGson() {
		return new GsonBuilder().disableHtmlEscaping()
				//.excludeFieldsWithoutExposeAnnotation()
				//.setDateFormat("yyyy.MM.dd HH:mm:ss")
				.setDateFormat("yyyy-MM-dd HH:mm:ss")
				.create();
	}
	
	/***
	 * 将源对象序列号成json字符串
	 *
	 * @param src        待序列化对象
	 * @return String    json字符串
	 */
	public static String toJson(Object src){
		return createDefaultGson().toJson(src);
	}
	
	/***
	 * 将json字符串转成对象
	 *
	 * @param src        待序列号对象 
	 * @param classOfT 需要转换的类型
	 * @return String    json字符串
	 */
	  public static <T> T fromJson(String json, Class<T> classOfT){
		  T obj = null;
		  try {
			  obj = createDefaultGson().fromJson(json, classOfT);
		  }catch(JsonSyntaxException e){
			  //throw  new JsonException(e);
			  LOG.error("", e);
			  obj = null;
		  }
		  
		  return obj;
	 }
	  
	/***
	 * 将json字符串转成对象
	 *
	 * @param src        待序列号对象 
	 * @param typeOfT 需要转换的类型
	 * @return String    json字符串
	 */
	  public static <T> T fromJson(String json, Type typeOfT){
		  T obj = null;
		  try {
			  obj = createDefaultGson().fromJson(json, typeOfT);
		  }catch(JsonSyntaxException e){
			  //throw  new JsonException(e);
			  LOG.error("", e);
			  obj = null;
		  }
		  
		  return obj;
	  }
}
