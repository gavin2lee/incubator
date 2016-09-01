package com.lachesis.mnis.core.order.repository;

import com.lachesis.mnis.core.order.entity.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public interface OrderRepository {

	List<OrderExecGroup> selectExecutedOrderGroups(
			String patientId,String orderTypeCode,String orderExecTypeCode, Date startDate, Date endDate);

	/**
	 * 获取患者未执行医嘱
	 * @param patientId
	 * @param orderTypeCode
	 * @param orderExecTypeCode
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	List<OrderExecGroup> selectUnExecutedOrderGroups(
			String patientId,String orderTypeCode,String orderExecTypeCode, Date startDate, Date endDate);
	
	/**
	 * 扫描医嘱获取医嘱信息
	 * @param orderGroupId
	 * @return
	 */
	OrderExecGroup getOrderListScanGroupId(String orderGroupId,String patientId,String stopType);

	int insertSingleOrderExecution(OrderExecLog execInfo);

	List<OrderExecGroup> selectOrderBaseGroupByPatId(String patientId,
			String orderTypeCode,String orderExecTypeCode, String status,Date startDate, Date endDate);

	int updateOrderExecution(List<OrderExecLog> orderExecInfos, String execNurseCode, String execDate);

	List<OrderExecGroup> getShiftOrderList(List<String> patients,
			Date startDate, Date endDate);
	
	List<OrderBedInfo> getOrderLongBedInfoList(List<String> patientIds,String deptCode,
			String orderTypeCode,String orderExecTypeCode,Date startTime, Date endTime);
	List<OrderBedInfo> getOrderTempBedInfoList(List<String> patientIds,String deptCode,
			String orderTypeCode,String orderExecTypeCode,Date startTime, Date endTime);
	/**
	 * 获取原始医嘱长嘱床位信息
	 * @param params
	 * @return
	 */
	List<OrderBedInfo> getOrderOriginalLongBedInfoList(List<String> patientIds,String deptCode,
			String orderTypeCode,String orderExecTypeCode,Date startDate,Date endDate);
	/**
	 * 获取原始医嘱临嘱床位信息
	 * @param params
	 * @return
	 */
	List<OrderBedInfo> getOrderOriginalTempBedInfoList(List<String> patientIds,String deptCode,
			String orderTypeCode,String orderExecTypeCode,Date startDate,Date endDate);
	/**
	 * 医嘱排序
	 */
	
	int updateOrderNoByOrderGroupId(String orderGroupId,String newOrderNo);
	
	List<HisOrderGroup> getOriginalOrderList(List<String> patientIds,
			String orderTypeCode,String orderExecTypeCode,Date startDate, Date endDate);
	
	/**
	 * 内江资中没有静配中心，没有包药机数据都从条码保存在pat_order_group表中  暂时添加方法  
	 *    现有口服药、检验单独查询  长期医嘱不需要查询检验等问题需要解决，如果解决可以屏蔽该方法
	 * @param patientIds
	 * @param orderTypeCode
	 * @param orderExecTypeCode
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	List<HisOrderGroup> getOriginalOrderListOne(List<String> patientIds,
			String orderTypeCode,String orderExecTypeCode,Date startDate, Date endDate);
	/**
	 * 根据医嘱id和医嘱执行类型获取对应的患者id
	 * @param orderGroupId
	 * @param orderExecType
	 * @return
	 */
	String	getPatientIdByOrderGroupId(String orderGroupId,String type);
	/**
	 * 根据包药机条码获取包药机内部医嘱
	 * @param barcode
	 * @return
	 */
	List<String> getOralOrderIdByBarcode(String barcode);
	
	
	/**
	 * 获取口服药条码
	 * @param barcode  条码编号
	 * @param pat_id   患者编号
	 * @return
	 */
	OrderExecGroup getORALOrderGroupDetail(String barcode,String pat_id);
	
	
	/**
	 * 根据条码查找检验项目
	 * @param barcode     条码号
	 * @param pat_id  患者id
	 * @return
	 */
	OrderExecGroup getLABOrderGroupDetailByBarcode(String barcode,String pat_id);
	
	/**
	 * 根据双条码获取输血信息
	 * @param barcode
	 * @param secBarcode
	 * @param patId
	 * @return
	 */
	OrderExecGroup getBloodOrderGroupDetailByBarcode(String barcode,String secBarcode,String patId);
	/**
	 * 统计医嘱已执行总数，当天需要执行总数
	 * @param pat_id
	 * @param order_type
	 * @return
	 */
	OrderCount getOrderCount(String patId,String orderExecType,String orderType,Date queryDate);
	/**
	 * 获取输血统计量
	 * @param params
	 * @return
	 */
	OrderCount getBloodOrderCount(String patId,Date queryDate);
	
	/**
	 * 统计医嘱未打印的患者
	 * @param params
	 * @return
	 */
	List<OrderUnprintStatistic> getUnprintOrderStatisticList(Map<String, Object> params);
	
	/**
	 * 获取瓶签打印医嘱信息
	 * @param params
	 * @return
	 */
	List<OrderPrintInfo> getOrderPrintInfos(Map<String, Object> params);
	
	/**
	 * 判断执行医嘱状态
	 * @param orderGroupId
	 * @param type ：过滤类型
	 * @return
	 */
	String getOrderStatusByOrderGroupId(String orderGroupId,String type,String patId);
	/**
	 * 判断原始医嘱状态
	 * @param orderGroupId
	 * @return
	 */
	String getOriginalOrderStatusByOrderGroupId(String orderGroupId,String patId);
	/**
	 * 获取医嘱执行数量
	 * @param barcode
	 * @param patId
	 * @return
	 */
	int getOrderExecCountById(String barcode,String secBarcode,String patId);

	/**
	 * 查询患者当天的24小时出入量医嘱
	 * @param patientId
	 * @return
	 */
	int getInAndOutOrder(String patientId, Date beginDate, Date endDate);
	int getUrineOrder(String patientId, Date beginDate, Date endDate);
	String getCreateDateTimeOfPatInoutOrder(String patId, Date beginDate, Date endDate);
	/**
	 * 获取医嘱执行类型
	 * @return
	 */
	List<Map<String,Object>> getOrderExecTypes();
	/**
	 * 获取医嘱用法
	 * @return
	 */
	List<Map<String,Object>> getOrderUsageTypes();
	
	/**
	 * 获取转抄到文书医嘱用法
	 * @return
	 */
	List<Map<String,Object>> getCopyOrderExecTypes(HashMap<String, Object> params);
	/**
	 * 医嘱执行单数据--执行单
	 * @param params
	 * @return
	 */
	List<OrderExecDocumentInfo> getOrdExecDocInfos(HashMap<String, Object> params);
	/**
	 * 医嘱执行单数据--执行单,瓶签
	 * @param params
	 * @return
	 */
	List<OrderExecDocumentInfo> getOrdExecDocInfosToOral(HashMap<String, Object> params);
	/**
	 * 医嘱执行单数据--执行单,瓶签
	 * @param params
	 * @return
	 */
	List<OrderExecDocumentInfo> getOrdExecDocInfosToLab(HashMap<String, Object> params);
	/**
	 * 医嘱执行单数据--输液卡
	 * @param params
	 * @return
	 */
	List<OrderExecDocumentInfo> getOrdExecDocInfosOnInfuCard(HashMap<String, Object> params);
	/**
	 * 医嘱执行单数据--瓶签
	 * @param params
	 * @return
	 */
	List<OrderExecDocumentInfo> getOrdExecDocInfosOnLabel(HashMap<String, Object> params);
	String saveOrdExecDocPrtInf(OrderExecDocumentPrintInfo orderExecDocumentPrintInfo);
	int updateOrdExecDocPrtInf(OrderExecDocumentPrintInfo orderExecDocumentPrintInfo);
	int getOrdExecDocPrtInfCount(String id);
	/**
	 * 获取包药机对应的医嘱编号
	 * @param params
	 * @return
	 */
	List<String> getOrderSubNoFromDrugBag(HashMap<String, Object> params);
	/**
	 * 获取输血对应的医嘱编号
	 * @param params
	 * @return
	 */
	List<String> getOrderIdFromBlood(HashMap<String, Object> params);
	
	/**
	 * 根据group_no判断原始医嘱是否停止
	 * @param groupNo
	 * @param patId
	 * @return
	 */
	boolean isStopOrderOriginal(String groupNo,String patId);
	/**
	 * 修改备注信息
	 * @param remark
	 * @param barcode
	 * @return
	 */
	int updatePatOrderRemarkByBarcode(String value,String barcode,int type);
	/**
	 * 插入备注信息
	 * @param remark
	 * @param barcode
	 * @return
	 */
	int insertPatOrderRemark(String value,String barcode,int type); 
	/**
	 * 根据条码判断是否有备注
	 * @param barcode
	 * @return
	 */
	boolean isExistOrderRemark(String barcode,int type);
	
	/**
	 * 根据条码获取医嘱条码类型(普通医嘱,检验,包药机)
	 * @param barcode
	 * @param patId
	 * @return
	 */
	String getOrderBarcodeType(String barcode,String patId);
	/**
	 * 根据条码获取药物基本信息
	 * @param barcode
	 * @return
	 */
	List<OrderItem> getOrderItemsByBarcode(String barcode);
	/**
	 * 根据条码获取包药机药物基本信息
	 * @param barcode
	 * @return
	 */
	List<OrderItem> getOralOrderItemsByBarcode(String barcode);
	/**
	 * 根据条码获取检验项目基本信息
	 * @param barcode
	 * @return
	 */
	List<OrderItem> getLabOrderItemsByBarcode(String barcode);
	/**
	 * NDA获取执行单
	 * @param params
	 * @return
	 */
	List<OrderExecGroup> getOrdExecDocInfosToNda(HashMap<String, Object> params);
	/**
	 * NDA获取执行单--包药机
	 * @param params
	 * @return
	 */
	List<OrderExecGroup> getOrdExecDocInfosToNdaOral(HashMap<String, Object> params);
	/**
	 * NDA获取执行单--检验
	 * @param params
	 * @return
	 */
	List<OrderExecGroup> getOrdExecDocInfosToNdaLab(HashMap<String, Object> params);
	/**
	 * NDA获取执行单--输血
	 * @param params
	 * @return
	 */
	List<OrderExecGroup> getOrdExecDocInfosToNdaBlood(HashMap<String, Object> params);
	/**
	 * 
	 * 医嘱改变推送信息
	 * @param params
	 * @return
	 */
	List<HisOrderGroup> getPublishOriginalOrders(Map<String, Object> params);
	
	/**
	 * 医嘱核对
	 * @param orderApprove
	 */
	void insertOrderApprove(OrderApprove orderApprove);
	void updateOrderApprove(OrderApprove orderApprove);
	Integer getOrderApproveCount(String patId,String orderNo);
	
	/**
	 * 医嘱执行单输血
	 * @param params
	 * @return
	 */
	List<OrderExecDocumentInfo> getOrdExecDocInfosToBlood(HashMap<String, Object> params);
}
