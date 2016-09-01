package com.lachesis.mnis.core.order.entity;

import java.util.Date;
import java.util.List;

import com.google.gson.annotations.SerializedName;

/**
 * 医嘱执行单信息
 * 
 * @author ThinkPad
 *
 */
public class OrderExecDocumentInfo {
	
	/**
	 * 患者信息
	 */
	private String patId;
	private String inHospNo;
	@SerializedName("patNm")
	private String patName;
	@SerializedName("bedCod")
	private String bedCode;
	/**
	 * 部门信息
	 */
	@SerializedName("deptCod")
	private String deptCode;
	@SerializedName("deptNm")
	private String deptName;

	/**
	 * 执行日期
	 */
	private Date execDate;
	/**
	 * 医嘱结合相关信息
	 */
	@SerializedName("ordExecDocOrdInf")
	private List<OrderExecDocOrderInfo> orderExecDocOrderInfos;

	public String getPatId() {
		return patId;
	}

	public void setPatId(String patId) {
		this.patId = patId;
	}

	public String getInHospNo() {
		return inHospNo;
	}

	public void setInHospNo(String inHospNo) {
		this.inHospNo = inHospNo;
	}

	public String getPatName() {
		return patName;
	}

	public void setPatName(String patName) {
		this.patName = patName;
	}

	public String getBedCode() {
		return bedCode;
	}

	public void setBedCode(String bedCode) {
		this.bedCode = bedCode;
	}

	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public Date getExecDate() {
		return execDate;
	}

	public void setExecDate(Date execDate) {
		this.execDate = execDate;
	}

	public List<OrderExecDocOrderInfo> getOrderExecDocOrderInfos() {
		return orderExecDocOrderInfos;
	}

	public void setOrderExecDocOrderInfos(
			List<OrderExecDocOrderInfo> orderExecDocOrderInfos) {
		this.orderExecDocOrderInfos = orderExecDocOrderInfos;
	}
}
