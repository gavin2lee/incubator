package com.lachesis.mnis.core.liquor.entity;

import java.util.Date;
import java.util.List;
/**
 * 配液信息(临时输液医嘱(当天信息))
 * @author ThinkPad
 *
 */
public class OrderLiquor {
	private String patientNo; // 住院号
	private String patientId; // 患者ID
	private String bedNo; // 患者床号
	private String patientName; // 患者姓名

	private String nurseId; // 护士ID,此患者为该护士负责
	private String nurseName; // 护士姓名

	private List<DrugItem> drugItems; // 包含的药物信息
	private OrderLiquorItem orderLiquorItem; // 配液医嘱执行信息
	private Date plan_time;   //计划执行日期
	private String drugUsage;// 药物用法   备药、审核、配液显示用法时为每组药用法，去除材料、非药物类型
	private String drugFreq;// 药物使用频次

	public String getPatientNo() {
		return patientNo;
	}

	public void setPatientNo(String patientNo) {
		this.patientNo = patientNo;
	}

	public String getPatientId() {
		return patientId;
	}

	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}

	public String getBedNo() {
		return bedNo;
	}

	public void setBedNo(String bedNo) {
		this.bedNo = bedNo;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getNurseId() {
		return nurseId;
	}

	public void setNurseId(String nurseId) {
		this.nurseId = nurseId;
	}

	public String getNurseName() {
		return nurseName;
	}

	public void setNurseName(String nurseName) {
		this.nurseName = nurseName;
	}

	public List<DrugItem> getDrugItems() {
		return drugItems;
	}

	public void setDrugItems(List<DrugItem> drugItems) {
		this.drugItems = drugItems;
	}

	public OrderLiquorItem getOrderLiquorItem() {
		return orderLiquorItem;
	}

	public void setOrderLiquorItem(OrderLiquorItem orderLiquorItem) {
		this.orderLiquorItem = orderLiquorItem;
	}

	public Date getPlan_time() {
		return plan_time;
	}

	public void setPlan_time(Date plan_time) {
		this.plan_time = plan_time;
	}

	public String getDrugUsage() {
		return drugUsage;
	}

	public void setDrugUsage(String drugUsage) {
		this.drugUsage = drugUsage;
	}

	public String getDrugFreq() {
		return drugFreq;
	}

	public void setDrugFreq(String drugFreq) {
		this.drugFreq = drugFreq;
	}

	@Override
	public String toString() {
		return "OrderLiquor [patientId=" + patientId + ", bedNo=" + bedNo
				+ ", patientName=" + patientName + ", nurseId=" + nurseId
				+ ", nurseName=" + nurseName + ", drugItems=" + drugItems
				+ ", orderLiquorItem=" + orderLiquorItem + "]";
	}

}
