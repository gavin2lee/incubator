package com.lachesis.mnis.core;

import com.lachesis.mnis.core.event.entity.DocReportEventEntity;
import com.lachesis.mnis.core.identity.entity.UserInformation;
import com.lachesis.mnis.core.order.entity.*;
import com.lachesis.mnis.core.util.BarCodeVo;

import java.util.Date;
import java.util.List;
import java.util.Map;


public interface OrderService {

	/**
	 * 获取医生为某病人开立的所有医嘱
	 * 
	 * @param patientId
	 * @param orderTypeCode
	 * @return
	 */
	List<OrderExecGroup> getOrderBaseGroupList(String patientId,
			String orderTypeCode,String orderExecTypeCode,String status, Date startDate, Date endDate);

	/**
	 * 获取医生为所选择病人开立的所有医嘱
	 * 
	 * @param patientId
	 * @param orderTypeCode
	 * @param startTime
	 *            医嘱开立时间的开始时间
	 * @param endTime
	 *            医嘱开立时间的结束时间
	 * @return
	 */
	List<OrderExecGroup> getOrderBaseGroupList(List<String> patientIds,
			String orderTypeCode, String state, String startTime, String endTime);

	/**
	 * 医嘱分解
	 * 
	 * @param orderBaseGroups
	 *            待分解的医嘱组
	 * @param patientIds
	 *            病人ID列表
	 * @param groupNoList
	 *            医嘱组id列表
	 * @return
	 */
	public List<OrderExecGroup> decompAndCreateLXOrderExecGroup(
			List<OrderGroup> orderBaseGroups, List<String> patientIds,
			List<String> groupNoList);

	/**
	 * 获取某病人待执行的医嘱列表
	 * @param patientId
	 * @param orderTypeCode		医嘱分类
	 * @param orderExecTypeCode	医嘱执行类型
	 * @return
	 */
	List<OrderExecGroup> getPendingOrderGroups(String patientId,
			String orderTypeCode,String orderExecTypeCode,Date startDate,Date endDate);

	OrderExecGroup getOrderGroupDetail(String orderGroupId,
			String patientId);
	/**
	 * 获取输血信息
	 * @param orderGroupId
	 * @param patientId
	 * @return
	 */
	OrderExecGroup getBloodOrderGroupDetail(String orderGroupId,String secBarcode,
			String patientId);


	/**
	 * 根据执行id，获取与之相关的医嘱组和对应详细执行信息. 因为只有一条执行信息，所以医嘱状态增加已完成。
	 * 
	 * @param execId
	 * @return 医嘱组信息和与执行id对应的执行信息
	 */
	OrderExecGroup getOrderGroupByExecId(String execId, String patientId);

	/**
	 * 用PDA执行医嘱
	 * 
	 * @param orderExecId
	 * @param user
	 * @param 完成已执行输液医嘱
	 * @param 医嘱类型
	 * @isScan 是否扫描
	 * @return 更新了执行信息的相关医嘱组
	 */
	OrderExecGroup execOrderGroupWithPDA(String orderExecId, String patientId,
			UserInformation user, String endOrderExecId, String orderExecType,
			Date execDate, boolean isScan);
	/**
	 * 用PDA执行输血医嘱
	 * 
	 * @param orderExecId
	 * @param user
	 * @param 完成已执行输液医嘱
	 * @param 医嘱类型
	 * @isScan 是否扫描
	 * @return 更新了执行信息的相关医嘱组
	 */
	OrderExecGroup execBloodOrderGroupWithPDA(String orderExecId,String secBarcode, String patientId,
			UserInformation user,String approveNurseCode,String approveNurseName,Date execDate,boolean isScan);

	/**
	 * 批量执行医嘱
	 * 
	 * @param orderExecId
	 *            批量执行的ID
	 * @param userId
	 *            执行护士ID
	 * @param  orderType 医嘱类型           
	 * @return
	 */
	List<OrderExecGroup> batchExecOrderGroupWithPDA(String orderExecId,
			String patientId, UserInformation user,String approveNurseCode,String approveNurseName,
			String orderType,boolean isScan);

	/**
	 * 批量执行医嘱，多病人
	 * 
	 * @param orderExecId
	 *            批量执行的ID
	 * @param userId
	 *            执行护士ID
	 * @return
	 */
	void batchExecMultPatOrderGroupWithPDA(Map<String, String> records,
			UserInformation user,String approveNurseCode,String approveNurseName);

	/**
	 * 获取病人开立的医嘱,提供任务清单的分类
	 * 
	 * @param patientId
	 * @param orderTypeCode
	 * @return
	 */
	List<OrderExecGroup> getOrderBaseGroupForTask(List<String> patientIdList,
			String day, String orderExecType);

	/**
	 * 查询交班和病区统计相关医嘱
	 * @param patients
	 * @param startDate
	 * @param endDate
	 */
	List<OrderExecGroup> getShiftOrderList(List<String> patients, Date startDate, Date endDate);

	/**
	 * 获取医嘱床位信息
	 * @param patientIds
	 * @param orderTypeCode
	 * @param orderExecTypeCode
	 * @param status
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	List<OrderBedInfo> getOrderBedInfoList(List<String> patientIds,String deptCode,
			String orderTypeCode,String orderExecTypeCode, Date startTime, Date endTime,boolean isOriginal);
	
	/**
	 * @param newOrderGroupId 待排序医嘱
	 * @param oldOrderGroupId 被排序医嘱
	 * @return
	 */
	int updateOrderNoByOrderGroupIds(String newOrderGroupId, String oldOrderGroupId);
	
	List<HisOrderGroup> getOriginalOrderList(String patientId,
			String orderTypeCode,String orderExecTypeCode,Date startDate, Date endDate);
	/**
	 * 根据医嘱id和医嘱执行类型获取对应的患者id
	 * @param orderGroupId
	 * @param orderExecType
	 * @return
	 */
	String	getPatientIdByOrderGroupId(String orderGroupId,String type);
	
	/**
	 * 统计医嘱未打印的信息
	 * @param deptCode
	 * @param orderTypeCode
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	List<OrderUnprintStatistic> getUnprintOrderStatisticList(
			String deptCode,String orderTypeCode,Date startDate,Date endDate);
	/**
	 * 获取瓶签打印条码
	 * @param params
	 * @return
	 */
	List<OrderPrintInfo> getOrderPrintInfos(String deptCode,String orderTypeCode,String orderExecTypeCode,Date startDate,Date endDate,String orderPrinted,List<String> patIds);
	/**
	 * 判断执行医嘱状态
	 * @param orderGroupId
	 * @param type ：过滤类型
	 * @param isBarFileter:是否条码处理
	 * @return
	 */
	String getOrderStatusByOrderGroupId(String orderGroupId,String type,String patId);
	
	/**
	 * 发布 文书非医嘱类型
	 * 针对患者，护士需求通过手工录入一些信息，这些信息需要自动转抄到文书
	 * 比如：口腔护理
	 * 
	 * @param orderExecLog
	 * @param inTakeValue
	 */
	public void publishDocReportByManual(DocReportEventEntity docReportEventEntity);
	
	/**
	 * 判断患者当天是否有24小时出入量
	 */
	public boolean has24InAndOutOrder(String patientId, Date beginDate, Date endDate);
	
	boolean has24UrineOrder(String patientId, Date beginDate, Date endDate);

	public String getCreateDateTimeOfPatInoutOrder(String patId, String recordDay);
	/**
	 * 获取医嘱执行类型
	 * @return
	 */
	public Map<String,Object> getOrderExecTypes();
	/**
	 * 获取医嘱用法
	 * @return
	 */
	Map<String, Object> getOrderUsageTypes();
	/**
	 * 医嘱执行单--执行单
	 * @param deptCode
	 * @param patIds
	 * @param queryDate
	 * @param execType
	 * @param docType
	 * @return
	 */
	List<OrderExecDocumentInfo> getOrdExecDocInfos(String deptCode,List<String> patIds,String startDate,String endDate,int execType,int docType);
	/**
	 * 医嘱执行单 口服--执行单,瓶签
	 * @param deptCode
	 * @param patIds
	 * @param queryDate
	 * @param execType
	 * @param docType
	 * @return
	 */
	List<OrderExecDocumentInfo> getOrdExecDocInfosToOral(String deptCode,List<String> patIds,String startDate,String endDate,int execType,int docType);
	/**
	 * 医嘱执行单 抽血单--执行单,瓶签
	 * @param deptCode
	 * @param patIds
	 * @param queryDate
	 * @param execType 0全部:1已执行:2未执行
	 * @param docType  样式类型：0：NORMAL,1:EXECUTION
	 * @return
	 */
	List<OrderExecDocumentInfo> getOrdExecDocInfosToLab(String deptCode,List<String> patIds,String startDate,String endDate,int execType,int docType);
	/**
	 * 医嘱执行单 输血--执行单,瓶签
	 * @param deptCode
	 * @param patIds
	 * @param queryDate
	 * @param execType 0全部:1已执行:2未执行
	 * @param docType  样式类型：0：NORMAL,1:EXECUTION
	 * @return
	 */
	List<OrderExecDocumentInfo> getOrdExecDocInfosToBlood(String deptCode,List<String> patIds,String startDate,String endDate,int execType,int docType);
	/**
	 * 医嘱执行单--输液卡
	 * @param deptCode
	 * @param patIds
	 * @param queryDate
	 * @param ordDocType :医嘱单据类型：INFUSION-执行单,LABEL-瓶签
	 * @return
	 */
	List<OrderExecDocumentInfo> getOrdExecDocInfosOnInfuCard(String deptCode,List<String> patIds,String startDate,String endDate);
	/**
	 * 医嘱执行单--瓶签
	 * @param deptCode
	 * @param patIds
	 * @param queryDate
	 * @return
	 */
	List<OrderExecDocumentInfo> getOrdExecDocInfosOnLabel(String deptCode,List<String> patIds, String startDate,String endDate);
	
	List<String> saveOrdExecDocPrtInfs(List<OrderExecDocumentPrintInfo> orderExecDocumentPrintInfos) throws Exception;

	/**
	 * 获取包药机对应的医嘱编号
	 * @param params
	 * @return
	 */
	List<String> getOrderSubNoFromDrugBag(String detpCode,List<String> patIds,Date startDate,Date endDate);
	
	Map<String, Object> getCopyOrderExecTypes(String type);
	
	int savePatOrderRemark(String barcode,String value ,int type);
	/**
	 * 获取医嘱执行状态()
	 * @param barcode
	 * @param patId
	 * @param isQuery是否需要查询
	 * @return 普通：NORMAL-patId,LAB-patId,ORAL-patId
	 */
	public BarCodeVo getOrderBarcodeType(String barcode,String patId,boolean isQuery);
	
	int getOrderExecCountById(String barcode, String secBarcode,String patId);
	/**
	 * 根据条码获取药物基本信息
	 * @param barcode
	 * @param barType
	 * @return
	 */
	List<OrderItem> getOrderItemsByBarcode(String barcode,String barType);
	/**
	 * NDA获取执行单信息
	 * @param deptCode
	 * @param patIds
	 * @param startDate
	 * @param endDate
	 * @param execType
	 * @param docType
	 * @return
	 */
	List<OrderExecGroup> getOrdExecDocInfosToNda(String deptCode,List<String> patIds,String startDate,String endDate,int execType);
	
	List<HisOrderGroup> getPublishOriginalOrders(String deptCode,Date startDate,Date endDate);
	
	void batchSaveOrderApprove(List<OrderApprove> orderApproves);
}
