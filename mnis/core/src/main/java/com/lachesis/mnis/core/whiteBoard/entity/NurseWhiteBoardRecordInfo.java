package com.lachesis.mnis.core.whiteBoard.entity;

import java.util.Date;
import java.util.List;
/**
 * 小白板记录信息(记录详情信息和子项详情记录信息)
 * @author ThinkPad
 *
 */
public class NurseWhiteBoardRecordInfo {
	/**
	 * 记录id
	 */
	private int recordId;
	/**
	 * 部门Code
	 */
	private String deptCode;
	/**
	 * 记录Code
	 */
	private String recordCode;
	/**
	 * 记录name
	 */
	private String recordName;
	
	private String randomId;
	/**
	 * 记录值
	 */
	private String recordValue;
	/**
	 * 记录排序
	 */
	private int orderNo;
	/**
	 * 记录中患者id
	 */
	private String patId;
	/**
	 * 患者信息
	 */
	private String patInfo;
	/**
	 * 执行计划
	 */
	private String performSchedule;
	/**
	 * 记录时间
	 */
	private Date recordDate;
	/**
	 * 创建记录用户code
	 */
	private String recordNurseCode;
	/**
	 * 创建记录name
	 */
	private String recordNurseName;
	
	private boolean isValid;
	/**
	 * 记录子项
	 */
	private List<NurseWhiteBoardRecordItemInfo> nurseWhiteBoardRecordItemInfos;

	public int getRecordId() {
		return recordId;
	}

	public void setRecordId(int recordId) {
		this.recordId = recordId;
	}

	public String getRecordCode() {
		return recordCode;
	}

	public void setRecordCode(String recordCode) {
		this.recordCode = recordCode;
	}

	public String getRecordName() {
		return recordName;
	}

	public void setRecordName(String recordName) {
		this.recordName = recordName;
	}

	public String getRecordValue() {
		return recordValue;
	}

	public void setRecordValue(String recordValue) {
		this.recordValue = recordValue;
	}

	public String getPatId() {
		return patId;
	}

	public void setPatId(String patId) {
		this.patId = patId;
	}

	public Date getRecordDate() {
		return recordDate;
	}

	public void setRecordDate(Date recordDate) {
		this.recordDate = recordDate;
	}

	public String getRecordNurseCode() {
		return recordNurseCode;
	}

	public void setRecordNurseCode(String recordNurseCode) {
		this.recordNurseCode = recordNurseCode;
	}

	public String getRecordNurseName() {
		return recordNurseName;
	}

	public void setRecordNurseName(String recordNurseName) {
		this.recordNurseName = recordNurseName;
	}

	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public int getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(int orderNo) {
		this.orderNo = orderNo;
	}

	public String getPatInfo() {
		return patInfo;
	}

	public void setPatInfo(String patInfo) {
		this.patInfo = patInfo;
	}

	public String getPerformSchedule() {
		return performSchedule;
	}

	public void setPerformSchedule(String performSchedule) {
		this.performSchedule = performSchedule;
	}

	public List<NurseWhiteBoardRecordItemInfo> getNurseWhiteBoardRecordItemInfos() {
		return nurseWhiteBoardRecordItemInfos;
	}

	public void setNurseWhiteBoardRecordItemInfos(
			List<NurseWhiteBoardRecordItemInfo> nurseWhiteBoardRecordItemInfos) {
		this.nurseWhiteBoardRecordItemInfos = nurseWhiteBoardRecordItemInfos;
	}

	public String getRandomId() {
		return randomId;
	}

	public void setRandomId(String randomId) {
		this.randomId = randomId;
	}

	public boolean isValid() {
		return isValid;
	}

	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}
	
}
