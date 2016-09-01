package com.lachesis.mnis.core.critical.entity;

import java.io.Serializable;
import java.util.List;

import com.lachesis.mnis.core.labtest.entity.LabTestRecord.LabTestItem;

public class CriticalValueRecord implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String criticalValueId; // ID
	private String deptNo;
	private String patientName; // 病人姓名
	private String bedCode; // 床号
	private String inHospNo; // 住院号
	private String sendPeople; // 发送人
	private String sendTime; // 发送时间
	private String criticalValue; // 危机值内容
	private String dispose; // 处理人
	private String disposeTime; // 处理时间
	private String type; // 危机值类型 T为检验科的 Z为检查的
	private String status; // 危机值状态 为0是为未读，其他暂未定义  (医生处理)
	
	private String itemSubject;
	private List<LabTestItem> labTestItems;
	private CriticalOperRecord criticalOperRecord;
	public String getCriticalValueId() {
		return criticalValueId;
	}

	public void setCriticalValueId(String criticalValueId) {
		this.criticalValueId = criticalValueId;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getBedCode() {
		return bedCode;
	}

	public void setBedCode(String bedCode) {
		this.bedCode = bedCode;
	}

	public String getInHospNo() {
		return inHospNo;
	}

	public void setInHospNo(String inHospNo) {
		this.inHospNo = inHospNo;
	}

	public String getSendPeople() {
		return sendPeople;
	}

	public void setSendPeople(String sendPeople) {
		this.sendPeople = sendPeople;
	}

	public String getSendTime() {
		return sendTime;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}

	public String getCriticalValue() {
		return criticalValue;
	}

	public void setCriticalValue(String criticalValue) {
		this.criticalValue = criticalValue;
	}

	public String getDispose() {
		return dispose;
	}

	public void setDispose(String dispose) {
		this.dispose = dispose;
	}

	public String getDisposeTime() {
		return disposeTime;
	}

	public void setDisposeTime(String disposeTime) {
		this.disposeTime = disposeTime;
	}


	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDeptNo() {
		return deptNo;
	}

	public void setDeptNo(String deptNo) {
		this.deptNo = deptNo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getItemSubject() {
		return itemSubject;
	}

	public void setItemSubject(String itemSubject) {
		this.itemSubject = itemSubject;
	}

	public List<LabTestItem> getLabTestItems() {
		return labTestItems;
	}

	public void setLabTestItems(List<LabTestItem> labTestItems) {
		this.labTestItems = labTestItems;
	}

	public CriticalOperRecord getCriticalOperRecord() {
		return criticalOperRecord;
	}

	public void setCriticalOperRecord(CriticalOperRecord criticalOperRecord) {
		this.criticalOperRecord = criticalOperRecord;
	}

	@Override
	public String toString() {
		return "CriticalValueRecord [criticalValueId=" + criticalValueId
				+ ", deptNo=" + deptNo + ", patientName=" + patientName
				+ ", bedCode=" + bedCode + ", inHospNo=" + inHospNo
				+ ", sendPeople=" + sendPeople + ", sendTime=" + sendTime
				+ ", criticalValue=" + criticalValue + ", dispose=" + dispose
				+ ", disposeTime=" + disposeTime + ", type=" + type
				+ ", status=" + status + ", itemSubject=" + itemSubject
				+ ", labTestItems=" + labTestItems + ", criticalOperRecord="
				+ criticalOperRecord + "]";
	}
	

}
