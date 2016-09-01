package com.lachesis.mnis.core.patient.entity;

import java.util.Date;

/**
 * 病人条码，床头卡，瓶签打印信息
 * @author ThinkPad
 *
 */
public class PatientPrint {
	/**
	 * 打印主键id
	 */
	private int printId;
	/**
	 * 待打印数据识别id
	 */
	private String printDataId;
	/**
	 * 打印数据类型
	 */
	private String printType;
	/**
	 * 条码是否打印
	 */
	private boolean isPrintBarcode;
	/**
	 * 床头卡是否打印
	 */
	private boolean isPrintBed;
	/**
	 * 瓶签是否打印
	 */
	private boolean isPrintLabel;
	/**
	 * 打印时间
	 */
	private Date printDate;
	public int getPrintId() {
		return printId;
	}
	public void setPrintId(int printId) {
		this.printId = printId;
	}
	public String getPrintDataId() {
		return printDataId;
	}
	public void setPrintDataId(String printDataId) {
		this.printDataId = printDataId;
	}
	public String getPrintType() {
		return printType;
	}
	public void setPrintType(String printType) {
		this.printType = printType;
	}
	public boolean isPrintBarcode() {
		return isPrintBarcode;
	}
	public void setPrintBarcode(boolean isPrintBarcode) {
		this.isPrintBarcode = isPrintBarcode;
	}
	public boolean isPrintBed() {
		return isPrintBed;
	}
	public void setPrintBed(boolean isPrintBed) {
		this.isPrintBed = isPrintBed;
	}
	public boolean isPrintLabel() {
		return isPrintLabel;
	}
	public void setPrintLabel(boolean isPrintLabel) {
		this.isPrintLabel = isPrintLabel;
	}
	public Date getPrintDate() {
		return printDate;
	}
	public void setPrintDate(Date printDate) {
		this.printDate = printDate;
	}
}
