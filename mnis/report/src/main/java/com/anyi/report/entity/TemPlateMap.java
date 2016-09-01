package com.anyi.report.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class TemPlateMap {

	private static HashMap<String, Object> tamplateMap=new HashMap<String, Object>();//用于存放模板，不用每次都获取
	
	public static List<DocConfig> list_docConfig=new ArrayList<DocConfig>();//文书配置信息，床位展示或频次提醒
	
	private static HashMap<String ,String[]> metadata_code_map=new HashMap<String, String[]>();//护理文书（一般护理所有的列表节点，用于打印）

	//创建私有构造器，不允许该类进行实例化，仅做数据缓存容器使用
	private TemPlateMap() {
		throw new AssertionError();
	}

	public static HashMap<String, Object> getTamplateMap() {
		return tamplateMap;
	}

	public static HashMap<String, String[]> getMetadata_code_map() {
		return metadata_code_map;
	}

	public static synchronized void addTemplateInMap(String templateID, Object template) {
		tamplateMap.put(templateID, template);
	}

	public static synchronized void addMetadataInMap(String templateID,String[] strList) {
		metadata_code_map.put(templateID, strList);
	}

	public static synchronized void clearTemplateInMap(String templateID) {
		tamplateMap.remove(templateID);
	}

	public static synchronized void clearMetadataInMap(String templateID) {
		metadata_code_map.remove(templateID);
	}

	public static synchronized void clearAllTemplateMap(){
		tamplateMap.clear();
	}

	public static synchronized void clearAllMetadataMap(){
		metadata_code_map.clear();
	}
}
