package com.lachesis.mnis.core.labtest.entity;

import java.util.Date;

/**
 * 送检信息详情
 * @author ThinkPad
 *
 */
public class LabTestSendRecordDetail {
	private String barcode;
	private String patId;
	/**
	 * 项目显示颜色
	 */
	private String tubeColor;
	private String testName;
	/**
	 * status源：1：未采集,2:已采集
	 */
	private int status;
	/**
	 * 送检
	 */
	private String sendNurseCode;
	private String sendNurseName;
	private Date sendDate;
	/**
	 * 采集
	 */
	private String execNurseCode;
	private String execNurseName;
	private Date execDate;
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public String getPatId() {
		return patId;
	}
	public void setPatId(String patId) {
		this.patId = patId;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getSendNurseCode() {
		return sendNurseCode;
	}
	public void setSendNurseCode(String sendNurseCode) {
		this.sendNurseCode = sendNurseCode;
	}
	public String getSendNurseName() {
		return sendNurseName;
	}
	public void setSendNurseName(String sendNurseName) {
		this.sendNurseName = sendNurseName;
	}
	public String getTubeColor() {
		return tubeColor;
	}
	public void setTubeColor(String tubeColor) {
		this.tubeColor = tubeColor;
	}
	public Date getSendDate() {
		return sendDate;
	}
	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}
	public String getTestName() {
		return testName;
	}
	public void setTestName(String testName) {
		this.testName = testName;
	}
	public String getExecNurseCode() {
		return execNurseCode;
	}
	public void setExecNurseCode(String execNurseCode) {
		this.execNurseCode = execNurseCode;
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
}
