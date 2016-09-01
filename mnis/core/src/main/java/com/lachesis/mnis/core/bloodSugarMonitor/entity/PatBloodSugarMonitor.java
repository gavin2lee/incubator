package com.lachesis.mnis.core.bloodSugarMonitor.entity;

import java.util.Date;

public class PatBloodSugarMonitor {
	private Long id;//ID
	private String patId;//患者流水号
	private String name;//患者姓名
	private String itemCode;//项目编号
	private String itemValue;//项目值
	private String recordTime;//记录时间
	private String status;//状态
	private String operateCode;//操作人编号
	private String operateName;//操作人姓名
	private Date operateTime;//操作时间
	private Date createTime;//创建时间
	
	//数据查询专用
	private boolean provisions = false;//是否查询指定点的数据
	private String deptCode;//科室编号
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getPatId() {
		return patId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setPatId(String patId) {
		this.patId = patId;
	}
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public String getItemValue() {
		return itemValue;
	}
	public void setItemValue(String itemValue) {
		this.itemValue = itemValue;
	}
	public String getRecordTime() {
		return recordTime;
	}
	public void setRecordTime(String recordTime) {
		this.recordTime = recordTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getOperateCode() {
		return operateCode;
	}
	public void setOperateCode(String operateCode) {
		this.operateCode = operateCode;
	}
	public String getOperateName() {
		return operateName;
	}
	public void setOperateName(String operateName) {
		this.operateName = operateName;
	}
	public Date getOperateTime() {
		return operateTime;
	}
	public void setOperateTime(Date operateTime) {
		this.operateTime = operateTime;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public boolean isProvisions() {
		return provisions;
	}
	public void setProvisions(boolean provisions) {
		this.provisions = provisions;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
}