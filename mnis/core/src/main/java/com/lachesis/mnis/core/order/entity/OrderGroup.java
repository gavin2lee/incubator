package com.lachesis.mnis.core.order.entity;

import java.util.Date;
import java.util.List;

import com.lachesis.mnis.core.liquor.entity.OrderLiquorItem;

/**
 * 医嘱组，按组执行的医嘱项。规定了该组医嘱的用法，频次等信息
 * 对应一组打包的医嘱单元
 * 
 * 用例1： 浏览医生对病人开过的的医嘱列表, List<OrderBaseGroup>
 * @author wenhuan.cui
 *
 */
public class OrderGroup {
	private String orderGroupNo; 
	/**病人Id*/
	private String patientId; 
	/**病人姓名*/
	private String patientName; 
	/**病人床号*/
	private String patientBedCode; 
	/**患者所在护理单元号*/
	private String workUnitCode; 
	/**患者住院号*/
	private String inHospNo; 
	/**医嘱类型码： CZ，LZ等*/
	private String orderTypeCode; 
	/**医嘱类型名称：长期医嘱，临时医嘱等*/
	private String orderTypeName; 
	/**医嘱执行类型码：INFUSION, ORAL 等*/
	private String orderExecTypeCode; 
	/**医嘱执行类型名称：输液，口服药等*/
	private String orderExecTypeName; 
	/**开立日期*/
	private Date createDate; 
	/**开立医生编号*/
	private String createDoctorId; 
	/**开立医生姓名*/
	private String createDoctorName; 
	/**停止日期*/
	private Date stopDate; 
	/**停止医生编号*/
	private String stopDoctorId; 
	/**停止医生姓名*/
	private String stopDoctorName; 
	/**医嘱状态编码:参照Anyi常量类中的ORDER_STATUS_开头的常量*/
	private String orderStatusCode; 
	/**医嘱状态名称*/
	private String orderStatusName; 
	/**用法编码*/
	private String usageCode; 
	/**用法名称*/
	private String usageName; 
	/**医嘱执行频率（可能为非药物医嘱）*/
	private String deliverFreq;
	private String deliverFreqCode;
	/**紧急： true，false*/
	private boolean emergent; 
	/**基本医嘱信息列表（如药物列表）*/
	private List<OrderItem> orderItems; 
	/**
	 * 是否是皮试医嘱：true, false。皮试医嘱关联一条药物医嘱
	 **/
	private boolean skinTestOrder; 
	/**医嘱计划执行时间，初始值由deliverFreq决定*/
	private Date planExecTime; 
	/**医嘱计划首次执行时间*/
	private String planFirstExecTime;
	/**医嘱开始时间*/
	private Date beginDate;
	/**医嘱结束时间*/
	private Date endDate;
	/**医嘱配液状态*/
	private String orderLiquorStatus;
	/**医嘱备注 */
	private String mark;
	/**
	 * 医嘱组序号
	 */
	private String orderNo;
	/**
	 * 医嘱组排序号
	 */
	private String orderSortNo;
	/**
	 * 是否打印瓶签
	 */
	private boolean isPrintLabel;
	/**
	 * 执行计划
	 */
	private String performSchedule;
	
	/**
	 * 试管颜色
	 */
	private String tubecolor;
	
	private OrderLiquorItem orderLiquorItem; // 配液医嘱执行信息
	
	public String getOrderGroupNo() {
		return orderGroupNo;
	}
	public void setOrderGroupNo(String orderGroupNo) {
		this.orderGroupNo = orderGroupNo;
	}
	public String getPatientId() {
		return patientId;
	}
	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}
	public String getPatientName() {
		return patientName;
	}
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	public String getPatientBedCode() {
		return patientBedCode;
	}
	public void setPatientBedCode(String patientBedCode) {
		this.patientBedCode = patientBedCode;
	}
	public String getOrderTypeCode() {
		return orderTypeCode;
	}
	public void setOrderTypeCode(String orderTypeCode) {
		this.orderTypeCode = orderTypeCode;
	}
	public String getOrderTypeName() {
		return orderTypeName;
	}
	public void setOrderTypeName(String orderTypeName) {
		this.orderTypeName = orderTypeName;
	}
	public String getOrderExecTypeCode() {
		return orderExecTypeCode;
	}
	public void setOrderExecTypeCode(String orderExecTypeCode) {
		this.orderExecTypeCode = orderExecTypeCode;
	}
	public String getOrderExecTypeName() {
		return orderExecTypeName;
	}
	public void setOrderExecTypeName(String orderExecTypeName) {
		this.orderExecTypeName = orderExecTypeName;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getCreateDoctorId() {
		return createDoctorId;
	}
	public void setCreateDoctorId(String createDoctorId) {
		this.createDoctorId = createDoctorId;
	}
	public String getCreateDoctorName() {
		return createDoctorName;
	}
	public void setCreateDoctorName(String createDoctorName) {
		this.createDoctorName = createDoctorName;
	}
	public Date getStopDate() {
		return stopDate;
	}
	public void setStopDate(Date stopDate) {
		this.stopDate = stopDate;
	}
	public String getStopDoctorId() {
		return stopDoctorId;
	}
	public void setStopDoctorId(String stopDoctorId) {
		this.stopDoctorId = stopDoctorId;
	}
	public String getStopDoctorName() {
		return stopDoctorName;
	}
	public void setStopDoctorName(String stopDoctorName) {
		this.stopDoctorName = stopDoctorName;
	}
	public String getOrderStatusCode() {
		return orderStatusCode;
	}
	public void setOrderStatusCode(String orderStatusCode) {
		this.orderStatusCode = orderStatusCode;
	}
	public String getOrderStatusName() {
		return orderStatusName;
	}
	public void setOrderStatusName(String orderStatusName) {
		this.orderStatusName = orderStatusName;
	}
	public String getUsageCode() {
		return usageCode;
	}
	public void setUsageCode(String usageCode) {
		this.usageCode = usageCode;
	}
	public String getUsageName() {
		return usageName;
	}
	public void setUsageName(String usageName) {
		this.usageName = usageName;
	}
	public String getDeliverFreq() {
		return deliverFreq;
	}
	public void setDeliverFreq(String deliverFreq) {
		this.deliverFreq = deliverFreq;
	}
	public boolean isEmergent() {
		return emergent;
	}
	public void setEmergent(boolean emergent) {
		this.emergent = emergent;
	}
	public List<OrderItem> getOrderItems() {
		return orderItems;
	}
	public int getOrderItemsSize() {
		return orderItems == null ? 0 : orderItems.size();
	}
	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}
	public String getWorkUnitCode() {
		return workUnitCode;
	}
	public void setWorkUnitCode(String workUnitCode) {
		this.workUnitCode = workUnitCode;
	}
	public String getInHospNo() {
		return inHospNo;
	}
	public void setInHospNo(String inHospNo) {
		this.inHospNo = inHospNo;
	}
	public boolean isSkinTestOrder() {
		return skinTestOrder;
	}
	public void setSkinTestOrder(boolean skinTestOrder) {
		this.skinTestOrder = skinTestOrder;
	}
	public Date getPlanExecTime() {
		return planExecTime;
	}
	public void setPlanExecTime(Date planExecTime) {
		this.planExecTime = planExecTime;
	}
	public String getPlanFirstExecTime() {
		return planFirstExecTime;
	}
	public void setPlanFirstExecTime(String planFirstExecTime) {
		this.planFirstExecTime = planFirstExecTime;
	}
	public String getMark() {
		return mark;
	}
	public void setMark(String mark) {
		this.mark = mark;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	
	public String getOrderSortNo() {
		return orderSortNo;
	}
	public void setOrderSortNo(String orderSortNo) {
		this.orderSortNo = orderSortNo;
	}
	public Date getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getOrderLiquorStatus() {
		return orderLiquorStatus;
	}
	public void setOrderLiquorStatus(String orderLiquorStatus) {
		this.orderLiquorStatus = orderLiquorStatus;
	}
	public boolean isPrintLabel() {
		return isPrintLabel;
	}
	public void setPrintLabel(boolean isPrintLabel) {
		this.isPrintLabel = isPrintLabel;
	}
	public String getPerformSchedule() {
		return performSchedule;
	}
	public void setPerformSchedule(String performSchedule) {
		this.performSchedule = performSchedule;
	}
	public String getTubecolor() {
		return tubecolor;
	}
	public void setTubecolor(String tubecolor) {
		this.tubecolor = tubecolor;
	}
	public OrderLiquorItem getOrderLiquorItem() {
		return orderLiquorItem;
	}
	public void setOrderLiquorItem(OrderLiquorItem orderLiquorItem) {
		this.orderLiquorItem = orderLiquorItem;
	}
	public String getDeliverFreqCode() {
		return deliverFreqCode;
	}
	public void setDeliverFreqCode(String deliverFreqCode) {
		this.deliverFreqCode = deliverFreqCode;
	}
	
}