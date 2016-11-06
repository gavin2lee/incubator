package com.harmazing.openbridge.mod.operations.elasticsearch.service;

import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;

import com.harmazing.openbridge.mod.operations.elasticsearch.bean.AppMonitorForm;
import com.harmazing.openbridge.mod.operations.elasticsearch.util.LogType;

public interface IElasticSearchService {
	
	/**
	 * 用于监控模块服务数据获取
	 * @param form
	 * @param type
	 * @return
	 */
	public List<Map<String,Object>>  cost(AppMonitorForm form, LogType logType) throws UnknownHostException ;
	
	/**
	 * 用于监控模块排行
	 * @param form
	 * @param type
	 * @return
	 */
	public List<Map<String,Object>>  top(AppMonitorForm form, String type,LogType logType) throws UnknownHostException ;

	/**
	 * 
	 * appState: 获取APP总访问次数 和 总iP数 <br/>
	 *
	 * @author dengxq
	 * @param appIds
	 * @return
	 * @since JDK 1.6
	 */
	public List<Map<String,Object>> appState(List<String> appIds,LogType logType) throws UnknownHostException;
}
