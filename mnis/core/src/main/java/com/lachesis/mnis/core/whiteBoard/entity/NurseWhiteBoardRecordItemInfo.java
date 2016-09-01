package com.lachesis.mnis.core.whiteBoard.entity;

import java.util.Date;
/**
 * 小白板子项记录详情
 * @author ThinkPad
 *
 */
public class NurseWhiteBoardRecordItemInfo {
	/*
	 * 记录子项id
	 */
	private int recordItemId;
	/**
	 * 记录id
	 */
	private int recordId;
	/**
	 * 记录子项Code
	 */
	private String recordItemCode;
	/**
	 * 记录子项name
	 */
	private String recordItemName;
	/**
	 * 记录子项值
	 */
	private String recordItemValue;
	/**
	 * 记录子项创建日期
	 */
	private Date recordItemDate;
	/**
	 * 执行子项日期(2015-10-23 7:00:00)
	 */
	private Date execItemDate;
	/**
	 * 开始任务的时间
	 */
	private Date startRecordItemDate;
	/**
	 * 结束任务的时间
	 */
	private Date endRecordItemDate;
	/**
	 * 患者id
	 */
	private String itemPatId;
	/**
	 * 患者信息床位-姓名
	 */
	private String itemPatInfo;
	/**
	 * 小白板任务状态
	 */
	private String status;
	
	public String getRecordItemCode() {
		return recordItemCode;
	}
	public void setRecordItemCode(String recordItemCode) {
		this.recordItemCode = recordItemCode;
	}
	public String getRecordItemName() {
		return recordItemName;
	}
	public void setRecordItemName(String recordItemName) {
		this.recordItemName = recordItemName;
	}
	public String getRecordItemValue() {
		return recordItemValue;
	}
	public void setRecordItemValue(String recordItemValue) {
		this.recordItemValue = recordItemValue;
	}
	public Date getRecordItemDate() {
		return recordItemDate;
	}
	public void setRecordItemDate(Date recordItemDate) {
		this.recordItemDate = recordItemDate;
	}
	public String getItemPatId() {
		return itemPatId;
	}
	public void setItemPatId(String itemPatId) {
		this.itemPatId = itemPatId;
	}
	public String getItemPatInfo() {
		return itemPatInfo;
	}
	public void setItemPatInfo(String itemPatInfo) {
		this.itemPatInfo = itemPatInfo;
	}
	public int getRecordItemId() {
		return recordItemId;
	}
	public void setRecordItemId(int recordItemId) {
		this.recordItemId = recordItemId;
	}
	public int getRecordId() {
		return recordId;
	}
	public void setRecordId(int recordId) {
		this.recordId = recordId;
	}
	public Date getEndRecordItemDate() {
		return endRecordItemDate;
	}
	public void setEndRecordItemDate(Date endRecordItemDate) {
		this.endRecordItemDate = endRecordItemDate;
	}
	public Date getStartRecordItemDate() {
		return startRecordItemDate;
	}
	public void setStartRecordItemDate(Date startRecordItemDate) {
		this.startRecordItemDate = startRecordItemDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getExecItemDate() {
		return execItemDate;
	}
	public void setExecItemDate(Date execItemDate) {
		this.execItemDate = execItemDate;
	}
}
