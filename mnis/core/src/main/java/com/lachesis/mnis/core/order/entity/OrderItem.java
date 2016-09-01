package com.lachesis.mnis.core.order.entity;

/**
 * 医嘱项，比如一个药，一个护理医嘱，一个检查等。一个医嘱组({@link #OrderGroup})包含一个 或多个医嘱项
 * 
 * 用例1：医嘱信息的最小单元
 * 
 * @author wenhuan.cui
 * 
 */
public class OrderItem {
	/** 医嘱项目表Id */
	private String orderId;
	/** 医嘱组号 */
	private String orderGroupNo;
	/**
	 * 医嘱组内序号
	 */
	private String orderSubNo;
	/** 医嘱项目名称 */
	private String orderName;
	/** 用量 */
	private Float dosage;
	/** 用量单位 */
	private String dosageUnit;
	/** 药品规格 */
	private String drugSpec;
	/** 需要皮试： true， false */
	private boolean skinTestRequired;
	/** 医生所下注意事项备注 */
	private String remark;
	/** 检验标本编号 */
	private String specimenCode;
	/** 检验标本名称 */
	private String specimenName;
	/** 检查部位编号 */
	private String examineLocCode;
	/** 检查部位名称 */
	private String examineLocName;
	/** 医嘱项目编码 */
	private String orderItemCode;
	/** 对应皮试医嘱组号（skinTestRequired为true时） */
	private String skinTestOrderGroupNo;
	/** 项目单价 */
	private double unitPrice;
	/** 数量 */
	private String quantity;
	/** 高危药物等级 */
	private String riskLevel;
	/** 高危药物标识 0为非高危，1为高危 */
	private Integer riskFlag;
	
	private String performNo;

	// 医嘱类型(三院由多条医嘱组民组合医嘱，从多条医嘱中判断医嘱类型
	private String orderExecTypeCode;
	private String orderExecTypeName;
	
	//用法 医嘱转抄如果为空，判断为材料，文书不转抄
	private String usageCode;
	private String usageName;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getOrderGroupNo() {
		return orderGroupNo;
	}

	public void setOrderGroupNo(String orderGroupNo) {
		this.orderGroupNo = orderGroupNo;
	}

	public String getOrderName() {
		return orderName;
	}

	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}

	public Float getDosage() {
		return dosage;
	}

	public void setDosage(Float dosage) {
		this.dosage = dosage;
	}

	public String getDosageUnit() {
		return dosageUnit;
	}

	public void setDosageUnit(String dosageUnit) {
		this.dosageUnit = dosageUnit;
	}

	public String getDrugSpec() {
		return drugSpec;
	}

	public void setDrugSpec(String drugSpec) {
		this.drugSpec = drugSpec;
	}

	public boolean isSkinTestRequired() {
		return skinTestRequired;
	}

	public void setSkinTestRequired(boolean skinTestRequired) {
		this.skinTestRequired = skinTestRequired;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getSpecimenName() {
		return specimenName;
	}

	public void setSpecimenName(String specimenName) {
		this.specimenName = specimenName;
	}

	public String getExamineLocName() {
		return examineLocName;
	}

	public void setExamineLocName(String examineLocName) {
		this.examineLocName = examineLocName;
	}

	public String getSkinTestOrderGroupNo() {
		return skinTestOrderGroupNo;
	}

	public void setSkinTestOrderGroupNo(String skinTestOrderGroupNo) {
		this.skinTestOrderGroupNo = skinTestOrderGroupNo;
	}

	public double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getOrderItemCode() {
		return orderItemCode;
	}

	public void setOrderItemCode(String orderItemCode) {
		this.orderItemCode = orderItemCode;
	}

	public String getExamineLocCode() {
		return examineLocCode;
	}

	public void setExamineLocCode(String examineLocCode) {
		this.examineLocCode = examineLocCode;
	}

	public String getSpecimenCode() {
		return specimenCode;
	}

	public void setSpecimenCode(String specimenCode) {
		this.specimenCode = specimenCode;
	}

	public String getRiskLevel() {
		return riskLevel;
	}

	public void setRiskLevel(String riskLevel) {
		this.riskLevel = riskLevel;
	}

	public Integer getRiskFlag() {
		if (riskFlag == null) {
			return 0;
		}
		return riskFlag;
	}

	public void setRiskFlag(Integer riskFlag) {
		if (riskFlag == null) {
			this.riskFlag = 0;
		} else {
			this.riskFlag = riskFlag;
		}
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

	public String getOrderSubNo() {
		return orderSubNo;
	}

	public void setOrderSubNo(String orderSubNo) {
		this.orderSubNo = orderSubNo;
	}

	public String getPerformNo() {
		return performNo;
	}

	public void setPerformNo(String performNo) {
		this.performNo = performNo;
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
	
	

}
