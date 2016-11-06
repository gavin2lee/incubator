package com.harmazing.openbridge.mod.operations.elasticsearch.util;

public enum LogType {
	API_LOG("api_log"),
	API_DUBBO_LOG("api_log"),
	APP_LOG("app_log"),
	APP_GC_LOG("app_gc_log"),
	API_GC_LOG("api_gc_log"),
	ALL("all");
	private String type;
	public String getType(){
		return this.type;
	}
	private LogType(String type){
		this.type = type;
	}
};
