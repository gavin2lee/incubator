package com.lachesis.mnis.core.order.entity;
/**
 * 闭环输液药物信息
 * @author ThinkPad
 *
 */
public class OrderInfusionManagerOrderItem {
	private String orderNo;
	private String drugName;
	private String drugId;
	private String drugSpec;
	private String drugDosage;
	private String dosageUnit;
	private String askDropSpeed;
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getDrugName() {
		return drugName;
	}
	public void setDrugName(String drugName) {
		this.drugName = drugName;
	}
	public String getDrugId() {
		return drugId;
	}
	public void setDrugId(String drugId) {
		this.drugId = drugId;
	}
	public String getDrugSpec() {
		return drugSpec;
	}
	public void setDrugSpec(String drugSpec) {
		this.drugSpec = drugSpec;
	}
	public String getDrugDosage() {
		return drugDosage;
	}
	public void setDrugDosage(String drugDosage) {
		this.drugDosage = drugDosage;
	}
	public String getDosageUnit() {
		return dosageUnit;
	}
	public void setDosageUnit(String dosageUnit) {
		this.dosageUnit = dosageUnit;
	}
	public String getAskDropSpeed() {
		return askDropSpeed;
	}
	public void setAskDropSpeed(String askDropSpeed) {
		this.askDropSpeed = askDropSpeed;
	}
	
}
