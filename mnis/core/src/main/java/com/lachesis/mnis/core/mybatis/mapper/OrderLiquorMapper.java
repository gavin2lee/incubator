package com.lachesis.mnis.core.mybatis.mapper;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.lachesis.mnis.core.liquor.entity.OrderLiquor;
import com.lachesis.mnis.core.liquor.entity.OrderLiquorItem;
import com.lachesis.mnis.core.liquor.entity.OrderLiquorStatistic;
import com.lachesis.mnis.core.liquor.entity.OrderUnExecRecord;
import com.lachesis.mnis.core.liquor.entity.OrderUnExecRecordItem;

public interface OrderLiquorMapper {

	/**
	 * 根据部门ID 获取两天内所有的配液医嘱
	 * 
	 * @param deptId
	 * @return
	 */
	List<OrderLiquor> getOrderLiquorList(HashMap<String, Object> params);
	/**
	 * 获取包药机配液信息
	 * @param params
	 * @return
	 */
	List<OrderLiquor> getDrugBagLiquorList(HashMap<String, Object> params);

	/**
	 * 保存备药信息
	 * 
	 * @param orderLiquor
	 * @return
	 */
	int insertOrderLiquorForPrepare(OrderLiquorItem orderLiquor);

	/**
	 * 审核信息
	 * 
	 * @param orderLiquor
	 * @return
	 */
	int insertOrderLiquorForVerify(OrderLiquorItem orderLiquor);

	/**
	 * 保存配液信息
	 * 
	 * @param orderLiquor
	 * @return
	 */
	int insertOrderLiquorForExec(OrderLiquorItem orderLiquor);

	/**
	 * 更新配液信息
	 * 
	 * @param orderLiquor
	 * @return
	 */
	int updateOrderLiquor(OrderLiquorItem orderLiquor);

	int updateOrderLiquorForVerify(OrderLiquorItem orderLiquor);

	/**
	 * 根据执行医嘱ID 查新当前医嘱配液信息
	 * 
	 * @param execOrderId
	 * @return
	 */
	OrderLiquorItem selectOrderLiquorByExecOrderId(String orderId);
	
	/**
	 * 统计部门配液人数信息
	 * @param deptCode
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	OrderLiquorStatistic getOrderLiquorStatistic(String deptCode,Date startDate,Date endDate,String orderTypeCode,String type);
	
	/**
	 * 获取全科或关注患者的未执行信息
	 * @param deptCode
	 * @param patIds
	 * @return
	 */
	List<OrderUnExecRecord> getOrderUnExecRecords(String deptCode,List<String> patIds,String recordId,Date startDate,Date endDate);
	
	/**
	 * 插入未执行药物记录
	 * @param orderUnExecRecordItem
	 * @return
	 */
	int insertOrderUnExecRecordItem(OrderUnExecRecordItem orderUnExecRecordItem);
}
