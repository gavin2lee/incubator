package com.lachesis.mnis.core.order.entity;

import java.util.Date;
import java.util.List;

import com.google.gson.annotations.SerializedName;
/**
 * 执行单打印信息
 * @author ThinkPad
 *
 */
	
public class OrderExecDocumentPrintInfo {
	
	/**
	 * 打印护士code
	 */
	@SerializedName("nurCod")
	private String printNurseCode;
	@SerializedName("nurNm")
	private String printNurseName;
	@SerializedName("prtDate")
	private Date printDate;
	/**
	 * 打印审核护士code
	 */
	@SerializedName("chkNurCod")
	private String checkNurseCode;
	@SerializedName("chkNurNm")
	private String checkNurseName;
	@SerializedName("chkDate")
	private Date checkDate;
	/**
	 * 医嘱执行单类型
	 * 执行单：EXECUTION
	 * 输液卡：INFUSION
	 * 瓶签：LABEL
	 */
	private String ordDocType;
	/**
	 * 用法
	 */
	private String ordUsgType;
	/**
	 * 长嘱或临嘱
	 */
	private String ordType;
	/**
	 * 操作时间
	 */
	private Date operDate;
	/**
	 * 打印主键
	 */
	private String printId;
	/**
	 * 患者id
	 */
	private String patId;
	@SerializedName("deptCod")
	private String deptCode;
	
	private List<String> orderNos;
	public String getPrintNurseCode() {
		return printNurseCode;
	}
	public void setPrintNurseCode(String printNurseCode) {
		this.printNurseCode = printNurseCode;
	}
	public String getPrintNurseName() {
		return printNurseName;
	}
	public void setPrintNurseName(String printNurseName) {
		this.printNurseName = printNurseName;
	}
	public Date getPrintDate() {
		return printDate;
	}
	public void setPrintDate(Date printDate) {
		this.printDate = printDate;
	}
	public String getPrintId() {
		return printId;
	}
	public void setPrintId(String printId) {
		this.printId = printId;
	}
	public String getPatId() {
		return patId;
	}
	public void setPatId(String patId) {
		this.patId = patId;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getCheckNurseCode() {
		return checkNurseCode;
	}
	public void setCheckNurseCode(String checkNurseCode) {
		this.checkNurseCode = checkNurseCode;
	}
	public String getCheckNurseName() {
		return checkNurseName;
	}
	public void setCheckNurseName(String checkNurseName) {
		this.checkNurseName = checkNurseName;
	}
	public Date getCheckDate() {
		return checkDate;
	}
	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}
	public String getOrdDocType() {
		return ordDocType;
	}
	public void setOrdDocType(String ordDocType) {
		this.ordDocType = ordDocType;
	}
	public Date getOperDate() {
		return operDate;
	}
	public void setOperDate(Date operDate) {
		this.operDate = operDate;
	}
	public String getOrdUsgType() {
		return ordUsgType;
	}
	public void setOrdUsgType(String ordUsgType) {
		this.ordUsgType = ordUsgType;
	}
	public String getOrdType() {
		return ordType;
	}
	public void setOrdType(String ordType) {
		this.ordType = ordType;
	}
	public List<String> getOrderNos() {
		return orderNos;
	}
	public void setOrderNos(List<String> orderNos) {
		this.orderNos = orderNos;
	}
}
