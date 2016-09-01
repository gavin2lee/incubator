package com.lachesis.mnis.core.inoutmanager.repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lachesis.mnis.core.inoutmanager.entity.InOutManager;
import com.lachesis.mnis.core.inoutmanager.entity.InOutManagerStatistic;

public interface InOutManagerRepository {
	/**
	 * 获取出入量
	 * @param patId
	 * @param deptCode
	 * @return
	 */
	public List<InOutManager> getInOutManagers(String patId,String deptCode,Date startDate,Date endDate);
	/**
	 * 统计总入液量和出液量
	 * @param patId
	 * @param deptCode
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public InOutManagerStatistic getInOutManagerStatistic(String patId,String deptCode,Date startDate,Date endDate);

	
	/**
	 * 根据主键获取出入量
	 * @param id
	 * @return
	 */
	public InOutManager getInOutManagerById(int id);
	
	/**
	 * 新增出入量
	 * @param inOutManager
	 * @return
	 */
	public int insertInOutManager(InOutManager inOutManager);
	
	/**
	 * 修改出入量
	 * @param inOutManager
	 * @return
	 */
	public int updateInOutManager(InOutManager inOutManager);

	/**
	 * 删除出入量
	 * @param inOutManager
	 * @return
	 */
	public int deleteById(int id);
	
	/**
	 * 获取性状和颜色的编号
	 * @param dicType 0:性状,1：颜色
	 * @return
	 */
	List<Map<String, String>> getOutDics(HashMap<String, Object> params);
	
}
