package com.harmazing.openbridge.paas.architecture.service;

import java.util.List;
import java.util.Map;

import com.harmazing.openbridge.paas.architecture.vo.GraphParam;
import com.harmazing.openbridge.paas.architecture.vo.GraphVo;


public abstract interface IArchitectureService {
	
	
	/**
	 * 获取系统部署加构图
	 * @param type
	 * @return
	 */
	public List<GraphVo> getGraphByType(String type,String userId);
	
	public List<GraphVo> getPaasNodes(String userId);
	
	public List<GraphVo> getPaasProxys(String userId);
	
	public List<GraphVo> getPaasResources(String userId);

	
	public List<GraphVo> getPaas(String userId);

	public Map<String, Object> getAppAndNodeStatus(GraphParam param);
}
