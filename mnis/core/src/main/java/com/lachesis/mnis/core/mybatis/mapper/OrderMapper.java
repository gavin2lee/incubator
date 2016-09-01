package com.lachesis.mnis.core.mybatis.mapper;

import com.lachesis.mnis.core.order.entity.*;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface OrderMapper {
	/**
	 * 扫描医嘱获取医嘱信息
	 * @param orderGroupId
	 * @return
	 */
	OrderExecGroup getOrderListScanGroupId(String orderGroupId,String patientId,String stopType,String hospitcalCode);
	
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
	 * 插入医嘱执行计划列表（分解医嘱）
	 * 
	 * @param orderExecList
	 * @return
	 */
	int insertOrderExecution(List<OrderExecLog> orderExecList);

	int insertSingleOrderExecution(OrderExecLog orderExecInfo);

	/**
	 * 更新医嘱执行（HIS执行医嘱，与PDA执行区分开）
	 * 
	 * @param orderExecList
	 * @param execNurseId
	 * @param execDate
	 * @return
	 */
	int updateOrderExecution(List<OrderExecLog> orderExecList,
			String execNurseId, String execDate);

	/**
	 * 为选择病人获取某种类型的医生所开立的医嘱
	 * 
	 * @param patientIds
	 *            病人列表
	 * @param orderTypeCode
	 * @return
	 */
	List<OrderExecGroup> selectOrderBaseGroupByPatId(
			HashMap<String, Object> params);
	/**
	 * 获取口服药单(包药机)医嘱
	 * @param params
	 * @return
	 */
	List<OrderExecGroup> selectDrugBagByPatId(
			HashMap<String, Object> params);
	/**
	 * 获取检验单医嘱
	 * @param params
	 * @return
	 */
	List<OrderExecGroup> selectLabByPatId(
			HashMap<String, Object> params);

	List<OrderExecGroup> getDischargeOrderList(List<String> patients,
			Date startDate, Date endDate);

	List<OrderExecGroup> getNursingOrderList(List<String> patients,
			Date startDate, Date endDate);

	List<OrderExecGroup> getBloodsugarOrderList(List<String> patients,
			Date startDate, Date endDate);

	/**
	 * 为某病人获取某种类型的护士待用PDA辅助执行的医嘱(HIS已执行) 条件：没有停止时间或当前未到停止时间的医嘱组；HIS已执行；PDA未执行；
	 * 
	 * @param patientId
	 * @param orderTypeCode
	 * @return
	 */
	// List<OrderExecGroup> selectPendingOrderGroupByPatientId(String patientId,
	// Date today, Date tomorrow, String orderTypeCode);

	/**
	 * 获取指定病人、指定时间段、指定类型的医嘱已执行记录
	 * 
	 * @param patientId
	 * @param orderTypeCode
	 * @param orderExecTypeCode
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	List<OrderExecGroup> selectExecutedOrderGroups(
			HashMap<String, Object> params);

	/**
	 * 获取患者的未执行医嘱
	 * 
	 * @param patientId
	 * @param orderTypeCode
	 * @param orderExecTypeCode
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	List<OrderExecGroup> selectUnExecutedOrderGroups(
			HashMap<String, Object> params);
	/**
	 * 获取医嘱长嘱床位信息
	 * @param params
	 * @return
	 */
	List<OrderBedInfo> getOrderLongBedInfoList(HashMap<String, Object> params);
	/**
	 * 获取医嘱临嘱床位信息
	 * @param params
	 * @return
	 */
	List<OrderBedInfo> getOrderTempBedInfoList(HashMap<String, Object> params);
	
	/**
	 * 获取原始医嘱长嘱床位信息
	 * @param params
	 * @return
	 */
	List<OrderBedInfo> getOrderOriginalLongBedInfoList(HashMap<String, Object> params);
	/**
	 * 获取原始医嘱临嘱床位信息
	 * @param params
	 * @return
	 */
	List<OrderBedInfo> getOrderOriginalTempBedInfoList(HashMap<String, Object> params);
	
	/**
	 * 医嘱排序
	 */
	
	int updateOrderNoByOrderGroupId(String orderGroupId,String newOrderNo);
	
	/**
	 * 获取原始医嘱和其分解后的执行信息-不包括口服医嘱和检验医嘱
	 * @param params
	 * @return
	 */
	List<HisOrderGroup> getOriginalOrderList(HashMap<String, Object> params);
	
	/**
	 * 获取原始医嘱和其分解后的执行信息-只包括口服医嘱
	 * @param params
	 * @return
	 */
	List<HisOrderGroup> getOriginalOrderListToORAL(HashMap<String, Object> params);
	
	/**
	 * 获取原始医嘱和其分解后的执行信息-只包括检验医嘱
	 * @param params
	 * @return
	 */
	List<HisOrderGroup> getOriginalOrderListToLAB(HashMap<String, Object> params);
	
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
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	List<OrderExecGroup> getBloodOrderGroupDetail(String barcode,String secBarcode,
			String patId,String startDate,String endDate);
	/**
	 * 统计医嘱已执行总数，扫描医嘱需要执行总数
	 * 
	 */
	OrderCount getOrderCount(Map<String, Object> params);
	/**
	 * 获取输血统计量
	 * @param params
	 * @return
	 */
	OrderCount getBloodOrderCount(Map<String, Object> params);
	
	List<OrderUnprintStatistic> getUnprintOrderStatisticList(Map<String, Object> params);
	
	List<OrderPrintInfo> getOrderPrintInfos(Map<String, Object> params);
	
	/**
	 * 通过条码获取医嘱执行信息
	 * @param orderBar
	 * @return
	 */
	OrderExecLog selectOrderExecInfoByOrderBar(String orderBar);
	
	/**
	 * 获取医嘱执行数量
	 * @param barcode
	 * @param patId
	 * @return
	 */
	int getOrderExecCountById(String barcode,String secBarcode,String patId);

	List<OrderExecLog> getBracodeRelation(String orderGroupId);

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
	 * 医嘱执行单输血
	 * @param params
	 * @return
	 */
	List<OrderExecDocumentInfo> getOrdExecDocInfosToBlood(HashMap<String, Object> params);
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
	
	int saveOrdExecDocPrtInf(OrderExecDocumentPrintInfo orderExecDocumentPrintInfo);
	
	int getOrdExecDocPrtInfCount(String id);
	
	/**
	 * 执行单，打印信息更新
	 * @param printInfo
	 * @return
	 */
	int updateOrdExecDocPrtInf(OrderExecDocumentPrintInfo printInfo);
	
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
	String isStopOrderOriginal(String groupNo,String patId);
	
	/**
	 * 修改备注信息
	 * @param value
	 * @param barcode
	 * @return
	 */
	int updatePatOrderRemarkByBarcode(Map<String, Object> params);
	
	/**
	 * 插入备注信息
	 * @param value
	 * @param barcode
	 * @return
	 */
	int insertPatOrderRemark(Map<String, Object> params); 
	/**
	 * 根据条码判断是否有备注
	 * @param barcode
	 * @return
	 */
	Integer getCountByRemarkBarcode(Map<String, Object> params);
	
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
	
	List<HisOrderGroup> getPublishOriginalOrders(Map<String, Object> params);
	/**
	 * 医嘱核对
	 * @param orderApprove
	 */
	void insertOrderApprove(OrderApprove orderApprove);
	void updateOrderApprove(OrderApprove orderApprove);
	Integer getOrderApproveCount(@Param("patId") String patId,@Param("orderNo") String orderNo);
}
