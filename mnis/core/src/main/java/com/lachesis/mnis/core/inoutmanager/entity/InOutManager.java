package com.lachesis.mnis.core.inoutmanager.entity;

import java.util.Date;

/**
 * 出入量管理
 * @author ThinkPad
 *
 */
public class InOutManager {
	//主键
	private int id;
	//患者Id
	private String patId;
	//患者部门
	private String deptCode;
	//入量code
	private String inItemCode;
	//入量name
	private String inItemName;
	//入量值
	private double inItemVal;
	//入量单位
	private String inItemUnit;
	//出量code
	private String outItemCode;
	//出量name
	private String outItemName;
	//出量值
	private double outItemVal;
	//出量单位
	private String outItemUnit;
	//创建时间
	private Date createDate;
	//创建用户
	private String createUserCode;
	//创建用户name
	private String createUserName;
	//修改时间
	private Date updateDate;
	//修改用户
	private String updateUserCode;
	//修改用户name
	private String updateUserName;
	/**
	 * 出量性状编号
	 */
	private String outShapeCode;
	private String outShapeName;
	/**
	 * 出量颜色编号
	 */
	private String outColorCode;
	private String outColorName;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public String getInItemCode() {
		return inItemCode;
	}
	public void setInItemCode(String inItemCode) {
		this.inItemCode = inItemCode;
	}
	public String getInItemName() {
		return inItemName;
	}
	public void setInItemName(String inItemName) {
		this.inItemName = inItemName;
	}
	public String getInItemUnit() {
		return inItemUnit;
	}
	public void setInItemUnit(String inItemUnit) {
		this.inItemUnit = inItemUnit;
	}
	public String getOutItemCode() {
		return outItemCode;
	}
	public void setOutItemCode(String outItemCode) {
		this.outItemCode = outItemCode;
	}
	public String getOutItemName() {
		return outItemName;
	}
	public void setOutItemName(String outItemName) {
		this.outItemName = outItemName;
	}
	public String getOutItemUnit() {
		return outItemUnit;
	}
	public void setOutItemUnit(String outItemUnit) {
		this.outItemUnit = outItemUnit;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getCreateUserCode() {
		return createUserCode;
	}
	public void setCreateUserCode(String createUserCode) {
		this.createUserCode = createUserCode;
	}
	public String getCreateUserName() {
		return createUserName;
	}
	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public String getUpdateUserCode() {
		return updateUserCode;
	}
	public void setUpdateUserCode(String updateUserCode) {
		this.updateUserCode = updateUserCode;
	}
	public String getUpdateUserName() {
		return updateUserName;
	}
	public void setUpdateUserName(String updateUserName) {
		this.updateUserName = updateUserName;
	}
	public double getInItemVal() {
		return inItemVal;
	}
	public void setInItemVal(double inItemVal) {
		this.inItemVal = inItemVal;
	}
	public double getOutItemVal() {
		return outItemVal;
	}
	public void setOutItemVal(double outItemVal) {
		this.outItemVal = outItemVal;
	}
	public String getOutShapeCode() {
		return outShapeCode;
	}
	public void setOutShapeCode(String outShapeCode) {
		this.outShapeCode = outShapeCode;
	}
	public String getOutColorCode() {
		return outColorCode;
	}
	public void setOutColorCode(String outColorCode) {
		this.outColorCode = outColorCode;
	}
	public String getOutShapeName() {
		return outShapeName;
	}
	public void setOutShapeName(String outShapeName) {
		this.outShapeName = outShapeName;
	}
	public String getOutColorName() {
		return outColorName;
	}
	public void setOutColorName(String outColorName) {
		this.outColorName = outColorName;
	}
}
