package com.lachesis.mnis.core.liquor.entity;

/**
 * 配液医嘱药物信息
 * 
 * @author lei.lei
 *
 */
public class DrugItem {

	private String orderItemId; // 医嘱项目ID
	private String orderId; // 医嘱ID

	private String drugId; // 药物ID
	private String drugName;// 药物名称
	private float drugDosage;// 药物用量
	private String drugUsage;// 药物用法
	private String drugFreq;// 药物使用频次
	private String drugUnit; // 药物单位

	public String getOrderItemId() {
		return orderItemId;
	}

	public void setOrderItemId(String orderItemId) {
		this.orderItemId = orderItemId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getDrugId() {
		return drugId;
	}

	public void setDrugId(String drugId) {
		this.drugId = drugId;
	}

	public String getDrugName() {
		return drugName;
	}

	public void setDrugName(String drugName) {
		this.drugName = drugName;
	}

	public float getDrugDosage() {
		return drugDosage;
	}

	public void setDrugDosage(float drugDosage) {
		this.drugDosage = drugDosage;
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

	public String getDrugUnit() {
		return drugUnit;
	}

	public void setDrugUnit(String drugUnit) {
		this.drugUnit = drugUnit;
	}

	@Override
	public String toString() {
		return "DrugItem [orderItemId=" + orderItemId + ", orderId=" + orderId
				+ ", drugId=" + drugId + ", drugName=" + drugName
				+ ", drugDosage=" + drugDosage + ", drugUsage=" + drugUsage
				+ ", drugFreq=" + drugFreq + ", drugUnit=" + drugUnit + "]";
	}

}
