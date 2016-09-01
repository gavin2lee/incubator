package com.anyi.report.util;

import java.io.*;
import java.util.Iterator;
import java.util.Properties;
import java.util.Map.Entry;

import com.lachesis.mnis.core.constants.MnisConstants;
/**
 * 对属性文件操作的工具类
 * 获取，新增，修改
 * 注意：	以下方法读取属性文件会缓存问题,在修改属性文件时，不起作用，
 *　InputStream in = PropertiesUtils.class.getResourceAsStream("/config.properties");
 *　解决办法：
 *　String savePath = PropertiesUtils.class.getResource("/config.properties").getPath();
 */
public class PropertiesUtils {
	/**
	 * 获取属性文件的数据 根据key获取值
	 * @param fileName 文件名　(注意：加载的是src下的文件,如果在某个包下．请把包名加上)
	 * @param key
	 * @return
	 */
	public static String findPropertiesKey(String fileName,String key) {
		
		try {
			Properties prop = getProperties(fileName);
			return prop.getProperty(key);
		} catch (Exception e) {
			return "";
		}
		
	}

	public static void main(String[] args) {
		Properties prop = new Properties();
		InputStream in = PropertiesUtils.class
				.getResourceAsStream(MnisConstants.PROP_PATH);
		try {
			prop.load(in);
			Iterator<Entry<Object, Object>> itr = prop.entrySet().iterator();
			while (itr.hasNext()) {
				Entry<Object, Object> e = (Entry<Object, Object>) itr.next();
				System.err.println((e.getKey().toString() + "" + e.getValue()
						.toString()));
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		
		System.err.println(findPropertiesKey("111","infusionManager.saveExecOrderInfo.url"));
	}

	/**
	 * 返回　Properties
	 * @param fileName 文件名　(注意：加载的是src下的文件,如果在某个包下．请把包名加上)
	 * @param 
	 * @return
	 */
	public static Properties getProperties(String fileName){
		Properties prop = new Properties();
//		String savePath = PropertiesUtils.class.getResource(fileName).getPath();
		//以下方法读取属性文件会缓存问题
//		InputStream in = PropertiesUtils.class
//				.getResourceAsStream(MnisConstants.PROP_PATH);
		try {
//			InputStream in =new BufferedInputStream(new FileInputStream(savePath));  
			InputStream in = PropertiesUtils.class
					.getResourceAsStream(fileName);
			prop.load(in);
		} catch (Exception e) {
			return null;
		}
		return prop;
	}
	/**
	 * 写入properties信息
	 * 
	 * @param key
	 *            名称
	 * @param value
	 *            值
	 */
	public static void modifyProperties(String fileName,String key, String value) {
		try {
			// 从输入流中读取属性列表（键和元素对）
			Properties prop = getProperties(fileName);
			prop.setProperty(key, value);
			String path = PropertiesUtils.class.getResource(MnisConstants.PROP_PATH).getPath();
			FileOutputStream outputFile = new FileOutputStream(path);
			prop.store(outputFile, "modify");
			outputFile.close();
			outputFile.flush();
		} catch (Exception e) {
		}
	}
}
