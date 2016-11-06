package com.harmazing.openbridge.mod.operations.elasticsearch.service;

import java.util.Map;

import com.harmazing.framework.common.service.IBaseService;
import com.harmazing.openbridge.mod.operations.elasticsearch.bean.AppMonitorForm;
import com.harmazing.openbridge.mod.operations.elasticsearch.util.LogType;

public interface IMonitorService extends IBaseService{
	
	/**
	 * 最耗时的URL,访问最多的URL<br>
	 * @param type 统计类型 <br>
	 * 	1.time 统计最耗时的URL <br>
	 * 	2.frequency <br>
	 * @param body 查询条件 格式如下<br>
	 * {<br>
	 * 		appId : '',         //应用应用ID<br>
	 * 		containId : '',  //容器ID<br>
	 * 		beginDate : '',  //开始时间　yyyy-MM-dd hh24:mi:ss.SSS 精确到毫秒<br>
	 * 		endDate : '',  //结束时间   yyyy-MM-dd hh24:mi:ss.SSS 精确到毫秒<br>
	 * 		top : '',          //  显示排名N之内数据<br>
	 * 		filter : [    //过滤条件<br>
	 * 			{k : v},<br>
	 * 			{k : v}<br>
	 * 		]<br>
	 * }<br>
	 */
	public Map<String,Object> cost(AppMonitorForm form,String type);
	
	/**
	 * 最耗时的URL排行,访问最多的URL排行<br>
	 * @param type 统计类型 <br>
	 * 	1.time 统计最耗时的URL <br>
	 * 	2.frequency 统计访问最多的url <br>
	 * @param body 查询条件 格式如下<br>
	 * {<br>
	 * 		appId : '',         //应用应用ID<br>
	 * 		containId : '',  //容器ID<br>
	 * 		beginDate : '',  //开始时间　yyyy-MM-dd hh24:mi:ss.SSS 精确到毫秒<br>
	 * 		endDate : '',  //结束时间   yyyy-MM-dd hh24:mi:ss.SSS 精确到毫秒<br>
	 * 		top : '',          //  显示排名N之内数据<br>
	 * 		filter : [    //过滤条件<br>
	 * 			{k : v},<br>
	 * 			{k : v}<br>
	 * 		]<br>
	 * }<br>
	 */
	public Map<String,Object> top(AppMonitorForm form,String type);
	

	public Map<String,Object> top(AppMonitorForm form,String type,LogType logType);
}
