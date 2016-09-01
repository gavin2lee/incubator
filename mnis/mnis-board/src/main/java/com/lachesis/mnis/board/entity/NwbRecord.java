package com.lachesis.mnis.board.entity;

import java.util.Date;

import javax.validation.constraints.NotNull;

/**
 * 小白板剂量
 * @author ThinkPad
 *
 */
public class NwbRecord {
	private String recordId;
	@NotNull(message="项目编号不为空!")
	private String code;
	private String name;
	@NotNull(message="部门编号不为空!")
	private String deptCode;
	/**
	 * 记录对应的模板
	 */
	@NotNull(message="模板编号不为空!")
	private String templateId;
	/**
	 * 文本数据值
	 */
	private String value;
	private String patId;
	private String bedCode;
	private String nurseCode;
	private String nurseName;
	private Date createTime;
	
	public String getRecordId() {
		return recordId;
	}
	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}
	public String getNurseCode() {
		return nurseCode;
	}
	public void setNurseCode(String nurseCode) {
		this.nurseCode = nurseCode;
	}
	public String getNurseName() {
		return nurseName;
	}
	public void setNurseName(String nurseName) {
		this.nurseName = nurseName;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getPatId() {
		return patId;
	}
	public void setPatId(String patId) {
		this.patId = patId;
	}
	public String getBedCode() {
		return bedCode;
	}
	public void setBedCode(String bedCode) {
		this.bedCode = bedCode;
	}
	public String getTemplateId() {
		return templateId;
	}
	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}
}
