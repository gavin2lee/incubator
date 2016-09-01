package com.lachesis.mnis.core.scan.entity;

import java.util.Date;
/**
 * 护士扫描信息记录
 * @author ThinkPad
 *
 */
public class NurseScanInfo {
	private int id;
	private String deptCode;
	private String nurseCode;
	private String nurseName;
	private String patId;
	private String barcode;
	private Date scanDate;
	/**
	 * 扫描类型:双核执行医嘱等
	 */
	private String type;
	/**
	 * 扫描状态：0失败，1成功
	 */
	private int status;
	/**
	 * 错误类型
	 */
	private String errorType;
	/**
	 * 失败原因
	 */
	private String error;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public Date getScanDate() {
		return scanDate;
	}
	public void setScanDate(Date scanDate) {
		this.scanDate = scanDate;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getPatId() {
		return patId;
	}
	public void setPatId(String patId) {
		this.patId = patId;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public String getErrorType() {
		return errorType;
	}
	public void setErrorType(String errorType) {
		this.errorType = errorType;
	}
}
