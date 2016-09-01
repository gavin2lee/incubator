package com.lachesis.mnis.core;

import java.util.Date;
import java.util.List;

import com.lachesis.mnis.core.liquor.entity.OrderLiquor;
import com.lachesis.mnis.core.liquor.entity.OrderLiquorItem;
import com.lachesis.mnis.core.liquor.entity.OrderLiquorStatistic;
import com.lachesis.mnis.core.liquor.entity.OrderUnExecRecord;
import com.lachesis.mnis.core.liquor.entity.OrderUnExecRecordItem;


public interface OrderLiquorService {


	/**
	 * 根据部门ID 获取两天内所有的配液医嘱
	 * @param deptId
	 * @return
	 */
	public List<OrderLiquor> getOrderLiquorList(String deptId,String liquorState,boolean isAllStatus);
	
	/**
	 * 根据部门ID 获取两天内该护士所关注的病人所有的配液医嘱
	 * @param deptId
	 * @param nurseId
	 * @return
	 */
	
	public List<OrderLiquor> getOrderLiquorListByNurseId(String deptId,String nurseId);

	/**
	 * 执行医嘱备药
	 * @param execOrderId
	 * @param prepareNurseCode
	 * @param prepareNurseName
	 */
	public void execOrderPrepare(String execOrderId,String prepareNurseCode,String prepareNurseName);
	
	/**
	 * 执行医嘱审核
	 * @param execOrderId
	 * @param verifyNurseCode
	 * @param verifyNurseName
	 */
	public void execOrderVerify(String execOrderId,String verifyNurseCode,String verifyNurseName);
	
	/**
	 * 执行医嘱配液
	 * @param execOrderId
	 * @param liquorNurseCode
	 * @param liquorNurseName
	 */
	public void execOrderLiquor(String execOrderId,String liquorNurseCode,String liquorNurseName);
	
	/**
	 * 执行配药(三个接口合并)
	 * @param execOrderId
	 * @param liquorNurseCode
	 * @param liquorNurseName
	 * @param execType
	 */
	public void execLiquor(String execOrderId,String liquorNurseCode,String liquorNurseName,int execType);
	
	/**
	 * 保存备药信息
	 * @param orderLiquor
	 * @return
	 */
	public int insertOrderLiquorForPrepare(OrderLiquorItem orderLiquor);
	
	/**
	 * 保存配液信息
	 * @param orderLiquor
	 * @return
	 */
	public int insertOrderLiquorForExec(OrderLiquorItem orderLiquor);
	
	/**
	 * 保存审核信息
	 * @param orderLiquor
	 * @return
	 */
	public int insertOrderLiquorForVerify(OrderLiquorItem orderLiquor);
	
	/**
	 * 更新配液信息
	 * @param orderLiquor
	 * @return
	 */
	public int updateOrderLiquor(OrderLiquorItem orderLiquor);
	/**
	 * 更新审核信息
	 * @param orderLiquor
	 * @return
	 */
	public int updateOrderLiquorForVerify(OrderLiquorItem orderLiquor);
	
	/**
	 * 根据执行医嘱ID 查新当前医嘱配液信息
	 * @param orderId
	 * @return
	 */
	public OrderLiquorItem selectOrderLiquorByExecOrderId(String orderId);
	
	/**
	 * 根据医嘱id获取配液信息
	 * @param orderGroupId
	 * @param deptId
	 * @param liquorState
	 * @return
	 */
	public List<OrderLiquor> getLiqourOrderByOrderGroupId(String orderGroupId,
			String deptId,String liquorStatus);
	
	/**
	 * 统计部门配液人数信息
	 * @param deptCode
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public OrderLiquorStatistic getOrderLiquorStatistic(String deptCode,Date startDate,Date endDate);
	
	/**
	 * 获取全科或关注患者的未执行信息
	 * @param deptCode
	 * @param nurseCode
	 * @return
	 */
	List<OrderUnExecRecord> getOrderUnExecRecords(String deptCode,String nurseCode,String recordId,Date startDate,Date endDate);
	
	/**
	 * 插入未执行药物记录
	 * @param barcode
	 * @return
	 */
	int insertOrderUnExecRecordItem(String barcode,String recordUserCode,String recordUserName,String deptCode);
}
