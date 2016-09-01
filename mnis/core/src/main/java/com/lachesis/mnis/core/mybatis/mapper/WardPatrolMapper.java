package com.lachesis.mnis.core.mybatis.mapper;

import java.util.List;
import java.util.Map;

import com.lachesis.mnis.core.patrol.entity.WardPatrolInfo;
/**
 * 病房巡视
 * @author liangming.deng
 *
 */
public interface WardPatrolMapper {
	/**
	 * 获取病房巡视列表信息
	 * @param params
	 * @return
	 */
	List<WardPatrolInfo>  getWardPatrolPlan(Map<String, Object> params);
	/**
	 * 根据病人Id获取病房巡视数据
	 * @param params
	 * @return
	 */
	List<WardPatrolInfo> selectWardPatrolByPatId(Map<String, Object> params);
	/**
	 * 保存病房巡视
	 * @param wardPatrolInfo
	 * @return
	 */
	int saveWardPatrolInfo(WardPatrolInfo wardPatrolInfo);
	
	List<WardPatrolInfo> getPublishWardPatrols(Map<String, Object> params);
	
}
