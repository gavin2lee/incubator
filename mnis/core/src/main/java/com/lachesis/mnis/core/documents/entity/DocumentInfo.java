package com.lachesis.mnis.core.documents.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 护理单据：输液单
 * 
 * @author lei.lei
 *
 */
public class DocumentInfo implements Serializable {

	private static final long serialVersionUID = -7727986427080867055L;

	private String execOrderId; // 医嘱ID
	private String orderId; // 医嘱ID

	private List<DocumentDrugItem> drugItems; // 医嘱药物信息

	// 患者信息
	private String patientId; // 患者住院号
	private String patientName; // 患者姓名
	private String bedNo; // 床号

	// 护理信息
	private String createName; // 医嘱创建人
	private Date createTime; // 创建时间
	

	private String execName; // 医嘱执行护士姓名
	private Date execTime; // 执行时间

	private String prepareName; // 备药护士姓名
	private Date prepareTime; // 备药时间
	
	private String verifyName; // 审核护士姓名
	private Date verifyTime; // 审核时间
	
	private String liquorName; // 配液护士姓名
	private Date liquorTime; // 配液时间


	public String getExecOrderId() {
		return execOrderId;
	}

	public void setExecOrderId(String execOrderId) {
		this.execOrderId = execOrderId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	

	public List<DocumentDrugItem> getDrugItems() {
		return drugItems;
	}

	public void setDrugItems(List<DocumentDrugItem> drugItems) {
		this.drugItems = drugItems;
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

	public String getBedNo() {
		return bedNo;
	}

	public void setBedNo(String bedNo) {
		this.bedNo = bedNo;
	}

	public String getCreateName() {
		return createName;
	}

	public void setCreateName(String createName) {
		this.createName = createName;
	}


	public String getPrepareName() {
		return prepareName;
	}

	public void setPrepareName(String prepareName) {
		this.prepareName = prepareName;
	}


	public String getLiquorName() {
		return liquorName;
	}

	public void setLiquorName(String liquorName) {
		this.liquorName = liquorName;
	}


	public String getExecName() {
		return execName;
	}

	public void setExecName(String execName) {
		this.execName = execName;
	}


	public String getVerifyName() {
		return verifyName;
	}

	public void setVerifyName(String verifyName) {
		this.verifyName = verifyName;
	}


	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getExecTime() {
		return execTime;
	}

	public void setExecTime(Date execTime) {
		this.execTime = execTime;
	}

	public Date getPrepareTime() {
		return prepareTime;
	}

	public void setPrepareTime(Date prepareTime) {
		this.prepareTime = prepareTime;
	}

	public Date getVerifyTime() {
		return verifyTime;
	}

	public void setVerifyTime(Date verifyTime) {
		this.verifyTime = verifyTime;
	}

	public Date getLiquorTime() {
		return liquorTime;
	}

	public void setLiquorTime(Date liquorTime) {
		this.liquorTime = liquorTime;
	}

	@Override
	public String toString() {
		return "DocumentInfo [execOrderId=" + execOrderId + ", orderId="
				+ orderId + ", drugItems=" + drugItems + ", patientId="
				+ patientId + ", patientName=" + patientName + ", bedNo="
				+ bedNo + ", createName=" + createName + ", createTime="
				+ createTime + ", prepareName=" + prepareName
				+ ", prepareTime=" + prepareTime + ", verifyName=" + verifyName
				+ ", verifyTime=" + verifyTime + ", liquorName=" + liquorName
				+ ", liquorTime=" + liquorTime + ", execName=" + execName
				+ ", execTime=" + execTime + "]";
	}

}
