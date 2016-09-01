package com.lachesis.mnis.core.nursing;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class TemplateBean implements Serializable {

	private static final long serialVersionUID = -1151947300323514013L;
	private Integer templateId;
	private Integer recordId;
	private String deptId;
	private String templateName;
	private String templateType;
	private String docType;
	private String docFormat; // 0为病例 2为表格 1为病程（未使用)
	private String content;
	private String value;
	private String nurseId;
	private String commitNurseId;
	private Date commitTime;
	private Date recordTime;
	private String recordName;
	private String patientId;
	private String showTempId; // 杭创用来显示的模版ID
	private Integer recordRows;
	private Integer recordStatus; // 记录状态:0书写 1完成 2封存(归档) 9删除
	private Integer pdaFlag;
	private List<TemplateItemBean> itemList;
	private byte[] byteData;

	public Integer getRecordId() {
		return recordId;
	}

	public void setRecordId(Integer recordId) {
		this.recordId = recordId;
	}

	public Integer getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Integer templateId) {
		this.templateId = templateId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getTemplateType() {
		return templateType;
	}

	public void setTemplateType(String templateType) {
		this.templateType = templateType;
	}

	public List<TemplateItemBean> getItemList() {
		return itemList;
	}

	public void setItemList(List<TemplateItemBean> itemList) {
		this.itemList = itemList;
	}

	public String getNurseId() {
		return nurseId;
	}

	public void setNurseId(String nurseId) {
		this.nurseId = nurseId;
	}

	public Date getRecordTime() {
		return recordTime;
	}

	public void setRecordTime(Date recordTime) {
		this.recordTime = recordTime;
	}

	public String getPatientId() {
		return patientId;
	}

	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}

	public String getDocType() {
		return docType;
	}

	public void setDocType(String docType) {
		this.docType = docType;
	}

	public String getRecordName() {
		return recordName;
	}

	public void setRecordName(String recordName) {
		this.recordName = recordName;
	}

	public String getDocFormat() {
		return docFormat;
	}

	public void setDocFormat(String docFormat) {
		this.docFormat = docFormat;
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getShowTempId() {
		return showTempId;
	}

	public void setShowTempId(String showTempId) {
		this.showTempId = showTempId;
	}

	public Integer getRecordRows() {
		return recordRows;
	}

	public void setRecordRows(Integer recordRows) {
		this.recordRows = recordRows;
	}

	public Integer getRecordStatus() {
		return recordStatus;
	}

	public void setRecordStatus(Integer recordStatus) {
		this.recordStatus = recordStatus;
	}

	public Integer getPdaFlag() {
		return pdaFlag;
	}

	public void setPdaFlag(Integer pdaFlag) {
		this.pdaFlag = pdaFlag;
	}

	public byte[] getByteData() {
		return byteData;
	}

	public void setByteData(byte[] byteData) {
		this.byteData = byteData;
	}

	public String getCommitNurseId() {
		return commitNurseId;
	}

	public void setCommitNurseId(String commitNurseId) {
		this.commitNurseId = commitNurseId;
	}

	public Date getCommitTime() {
		return commitTime;
	}

	public void setCommitTime(Date commitTime) {
		this.commitTime = commitTime;
	}

}
