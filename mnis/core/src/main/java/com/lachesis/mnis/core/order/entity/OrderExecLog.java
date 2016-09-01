package com.lachesis.mnis.core.order.entity;

import java.util.Date;

/**
 * 对应护士的一次医嘱执行 用例1： 执行医嘱后返回信息中包含更新内容 用例2： 标记医嘱完成后返回信息中包含更新内容 用例3：
 * 提供待执行医嘱列表中的计划时间等信息 用例4： 提供医嘱详情中的执行历史信息 用例5： 提供输液和口服药执行表的保存和查询信息
 * 
 * @author wenhuan.cui
 * 
 */
public class OrderExecLog {

	/** 对应执行表记录Id（如果HIS不提供，可由orderGroupNo与planDate拼接而成） */
	private String orderExecId;
	/**
	 * 第二条码
	 */
	private String secBarcode;
	/**
	 * 医嘱类型
	 */
	private String orderExecType;
	/**
	 * 病人ID
	 */
	private String patientId;
	/**
	 * 科室ID
	 */
	private String detpId;
	/** 计划执行时间 */
	private Date planDate;
	/** 执行开始时间 */
	private Date execDate;
	/** 执行护士号 */
	private String execNurseId;
	/** 执行护士姓名 */
	private String execNurseName;
	/** 完成时间 */
	private Date finishDate;
	/** 完成护士号 */
	private String finishNurseId;
	/** 完成护士姓名 */
	private String finishNurseName;
	/** 医嘱执行条码 */
	private String orderExecBarcode;
	/** 最近一次巡视的给药速度（输液医嘱） */
	private String deliverSpeed;
	/** 医嘱组号 */
	private String orderExecGroupNo;
	/** 给药速度单位 */
	private String deliverSpeedUnit;
	/** 核对本次执行的护士工号(如输血医嘱需要双人核对执行) */
	private String approveNurseId;
	/** 核对本次执行的护士姓名(如输血医嘱需要双人核对执行) */
	private String approveNurseName;
	/** 医嘱类型 */
	private String orderType;

	public String getOrderExecId() {
		return orderExecId;
	}

	public void setOrderExecId(String orderExecId) {
		this.orderExecId = orderExecId;
	}

	public Date getPlanDate() {
		return planDate;
	}

	public void setPlanDate(Date planDate) {
		this.planDate = planDate;
	}
	
	public Date getExecDate() {
		return execDate;
	}

	public void setExecDate(Date execDate) {
		this.execDate = execDate;
	}

	public String getExecNurseId() {
		return execNurseId;
	}

	public void setExecNurseId(String execNurseId) {
		this.execNurseId = execNurseId;
	}

	public String getExecNurseName() {
		return execNurseName;
	}

	public void setExecNurseName(String execNurseName) {
		this.execNurseName = execNurseName;
	}

	public Date getFinishDate() {
		return finishDate;
	}

	public void setFinishDate(Date finishDate) {
		this.finishDate = finishDate;
	}

	public String getFinishNurseId() {
		return finishNurseId;
	}

	public void setFinishNurseId(String finishNurseId) {
		this.finishNurseId = finishNurseId;
	}

	public String getFinishNurseName() {
		return finishNurseName;
	}

	public void setFinishNurseName(String finishNurseName) {
		this.finishNurseName = finishNurseName;
	}

	public String getOrderExecBarcode() {
		return orderExecBarcode;
	}

	public void setOrderExecBarcode(String orderExecBarcode) {
		this.orderExecBarcode = orderExecBarcode;
	}

	public String getDeliverSpeed() {
		return deliverSpeed;
	}

	public void setDeliverSpeed(String deliverSpeed) {
		this.deliverSpeed = deliverSpeed;
	}

	public String getOrderExecGroupNo() {
		return orderExecGroupNo;
	}

	public void setOrderExecGroupNo(String orderExecGroupNo) {
		this.orderExecGroupNo = orderExecGroupNo;
	}

	public String getDeliverSpeedUnit() {
		return deliverSpeedUnit;
	}

	public void setDeliverSpeedUnit(String deliverSpeedUnit) {
		this.deliverSpeedUnit = deliverSpeedUnit;
	}

	public String getApproveNurseId() {
		return approveNurseId;
	}

	public void setApproveNurseId(String approveNurseId) {
		this.approveNurseId = approveNurseId;
	}

	public String getApproveNurseName() {
		return approveNurseName;
	}

	public void setApproveNurseName(String approveNurseName) {
		this.approveNurseName = approveNurseName;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getOrderExecType() {
		return orderExecType;
	}

	public void setOrderExecType(String orderExecType) {
		this.orderExecType = orderExecType;
	}

	public String getPatientId() {
		return patientId;
	}

	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}

	public String getDetpId() {
		return detpId;
	}

	public void setDetpId(String detpId) {
		this.detpId = detpId;
	}

	public String getSecBarcode() {
		return secBarcode;
	}

	public void setSecBarcode(String secBarcode) {
		this.secBarcode = secBarcode;
	}
}
