package com.lachesis.mnis.core.patient.entity;

import java.util.Date;

/***
 * 
 * 病人皮试信息
 *
 * @author yuliang.xu
 * @date 2015年6月16日 下午3:06:45
 *
 */
public class PatientSkinTest {

	/** 皮试i项Id */
	private int id;
	/** 住院流水号 */
	private String patientId;
	private String patientName;
	/** 医嘱执行id */
	private String orderExecId;
	/** 皮试状态(0:未皮试,1:已皮试) */
	private boolean status;
	/** 皮试结果(0:阴性,1:阳性) */
	private String testResult;
	/** 皮试录入护士code */
	private String testNurseId;
	/** 皮试录入护士name */
	private String testNurseName;
	/** 皮试确认护士code */
	private String approveNurseId;
	/** 皮试确认护士name */
	private String approveNurseName;
	/** 皮试确认时间 */
	private Date approveDate;
	/** 皮试执行护士code */
	private String execNurseId;
	/** 皮试执行护士name */
	private String execNurseName;
	/** 皮试执行时间 */
	private Date execDate;
	/** 药品批号 */
	private String drugBatchNo;
	/** 药品代码 */
	private String drugCode;
	/** 药品名称 */
	private String drugName;

	private String masterRecordId;

	private int index;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public String getOrderExecId() {
		return orderExecId;
	}

	public void setOrderExecId(String orderExecId) {
		this.orderExecId = orderExecId;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getTestResult() {
		return testResult;
	}

	public void setTestResult(String testResult) {
		this.testResult = testResult;
	}

	public String getTestNurseId() {
		return testNurseId;
	}

	public void setTestNurseId(String testNurseId) {
		this.testNurseId = testNurseId;
	}

	public String getTestNurseName() {
		return testNurseName;
	}

	public void setTestNurseName(String testNurseName) {
		this.testNurseName = testNurseName;
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

	public Date getApproveDate() {
		return approveDate;
	}

	public void setApproveDate(Date approveDate) {
		this.approveDate = approveDate;
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

	public Date getExecDate() {
		return execDate;
	}

	public void setExecDate(Date execDate) {
		this.execDate = execDate;
	}

	public String getDrugBatchNo() {
		return drugBatchNo;
	}

	public void setDrugBatchNo(String drugBatchNo) {
		this.drugBatchNo = drugBatchNo;
	}

	public String getDrugCode() {
		return drugCode;
	}

	public void setDrugCode(String drugCode) {
		this.drugCode = drugCode;
	}

	public String getDrugName() {
		return drugName;
	}

	public void setDrugName(String drugName) {
		this.drugName = drugName;
	}

	public String getMasterRecordId() {
		return masterRecordId;
	}

	public void setMasterRecordId(String masterRecordId) {
		this.masterRecordId = masterRecordId;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

}
