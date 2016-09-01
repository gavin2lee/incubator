package com.lachesis.mnis.core.order.entity;

import java.util.Date;

import com.google.gson.annotations.SerializedName;

/**
 * 医嘱核对
 * @author ThinkPad
 *
 */
public class OrderApprove {
	private int id;
	private String orderNo;
	private String patId;
	@SerializedName("deptCod")
	private String deptCode;
	private String approveNurseCode;
	private String approveNurseName;
	private Date approveDate;
	/**
	 * 生成时间
	 */
	private Date createDate;
	/**
	 * INFUSION:输液,ORAL:口服药
	 */
	private String approveType;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getApproveNurseCode() {
		return approveNurseCode;
	}
	public void setApproveNurseCode(String approveNurseCode) {
		this.approveNurseCode = approveNurseCode;
	}
	public String getApproveNurseName() {
		return approveNurseName;
	}
	public void setApproveNurseName(String approveNurseName) {
		this.approveNurseName = approveNurseName;
	}
	public Date getApproveDate() {
		return approveDate;
	}
	public void setApproveDate(Date approveDate) {
		this.approveDate = approveDate;
	}
	public String getApproveType() {
		return approveType;
	}
	public void setApproveType(String approveType) {
		this.approveType = approveType;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
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
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
}
