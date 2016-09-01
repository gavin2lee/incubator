package com.lachesis.mnis.core.skintest.entity;

import java.io.Serializable;
import java.util.Date;


/**
 * 皮试前后，图片保存
 * 
 * @author liangming.deng
 * 
 */
public class SkinTestItem implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String skinTestItemId;// 皮试图片id
	private String skinTestId;// 皮试医嘱Id
	private byte[] skinTestItemImgBefore;// 皮试前图片
	private byte[] skinTestItemImgAfter;// 皮试后图片
	private String skinTestItemStatus;// 皮试状态('0'待皮试'1'批示中'2'已皮试(阴性和阳性))
	private String skinTestItemResult;// 皮试结果(p,n)(阴性,阳性)
	private String skinTestItemDrugBatchNo; // 药物批号
	private String skinTestItemInputNurseId;// 录入护士id
	private String skinTestItemInputNurseName;
	private String skinTestItemExecNurseId; // 执行医生号
	private String skinTestItemExecNurseName; // 执行医生姓名
	private Date skinTestItemExecDate;// 皮试执行时间

	private String skinTestItemApproveNurseId;// 核对护士id
	private String skinTestItemApproveNurseName;
	private Date skinTestItemApproveDate;
	private String patId;
	private String deptCode;
	private String hospNo;
	private String drugCode;
	private String drugName;
	public String getSkinTestItemId() {
		return skinTestItemId;
	}
	public void setSkinTestItemId(String skinTestItemId) {
		this.skinTestItemId = skinTestItemId;
	}
	public String getSkinTestId() {
		return skinTestId;
	}
	public void setSkinTestId(String skinTestId) {
		this.skinTestId = skinTestId;
	}
	public byte[] getSkinTestItemImgBefore() {
		return skinTestItemImgBefore;
	}
	public void setSkinTestItemImgBefore(byte[] skinTestItemImgBefore) {
		this.skinTestItemImgBefore = skinTestItemImgBefore;
	}
	public byte[] getSkinTestItemImgAfter() {
		return skinTestItemImgAfter;
	}
	public void setSkinTestItemImgAfter(byte[] skinTestItemImgAfter) {
		this.skinTestItemImgAfter = skinTestItemImgAfter;
	}
	public String getSkinTestItemStatus() {
		return skinTestItemStatus;
	}
	public void setSkinTestItemStatus(String skinTestItemStatus) {
		this.skinTestItemStatus = skinTestItemStatus;
	}
	public String getSkinTestItemResult() {
		return skinTestItemResult;
	}
	public void setSkinTestItemResult(String skinTestItemResult) {
		this.skinTestItemResult = skinTestItemResult;
	}
	public String getSkinTestItemDrugBatchNo() {
		return skinTestItemDrugBatchNo;
	}
	public void setSkinTestItemDrugBatchNo(String skinTestItemDrugBatchNo) {
		this.skinTestItemDrugBatchNo = skinTestItemDrugBatchNo;
	}
	public String getSkinTestItemInputNurseId() {
		return skinTestItemInputNurseId;
	}
	public void setSkinTestItemInputNurseId(String skinTestItemInputNurseId) {
		this.skinTestItemInputNurseId = skinTestItemInputNurseId;
	}
	public String getSkinTestItemInputNurseName() {
		return skinTestItemInputNurseName;
	}
	public void setSkinTestItemInputNurseName(String skinTestItemInputNurseName) {
		this.skinTestItemInputNurseName = skinTestItemInputNurseName;
	}
	public String getSkinTestItemExecNurseId() {
		return skinTestItemExecNurseId;
	}
	public void setSkinTestItemExecNurseId(String skinTestItemExecNurseId) {
		this.skinTestItemExecNurseId = skinTestItemExecNurseId;
	}
	public String getSkinTestItemExecNurseName() {
		return skinTestItemExecNurseName;
	}
	public void setSkinTestItemExecNurseName(String skinTestItemExecNurseName) {
		this.skinTestItemExecNurseName = skinTestItemExecNurseName;
	}
	public Date getSkinTestItemExecDate() {
		return skinTestItemExecDate;
	}
	public void setSkinTestItemExecDate(Date skinTestItemExecDate) {
		this.skinTestItemExecDate = skinTestItemExecDate;
	}
	public String getSkinTestItemApproveNurseId() {
		return skinTestItemApproveNurseId;
	}
	public void setSkinTestItemApproveNurseId(String skinTestItemApproveNurseId) {
		this.skinTestItemApproveNurseId = skinTestItemApproveNurseId;
	}
	public String getSkinTestItemApproveNurseName() {
		return skinTestItemApproveNurseName;
	}
	public void setSkinTestItemApproveNurseName(String skinTestItemApproveNurseName) {
		this.skinTestItemApproveNurseName = skinTestItemApproveNurseName;
	}
	public Date getSkinTestItemApproveDate() {
		return skinTestItemApproveDate;
	}
	public void setSkinTestItemApproveDate(Date skinTestItemApproveDate) {
		this.skinTestItemApproveDate = skinTestItemApproveDate;
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
	public String getHospNo() {
		return hospNo;
	}
	public void setHospNo(String hospNo) {
		this.hospNo = hospNo;
	}
	
	public String getDrugCode() {
		return drugCode;
	}
	public void setDrugCode(String drugCode) {
		this.drugCode = drugCode;
	}
	public String getDrugName() {
		return drugName;
	}
	public void setDrugName(String drugName) {
		this.drugName = drugName;
	}
	@Override
	public String toString() {
		return "SkinTestItem [skinTestItemId=" + skinTestItemId
				+ ", skinTestId=" + skinTestId 
				+ ", skinTestItemStatus=" + skinTestItemStatus
				+ ", skinTestItemResult=" + skinTestItemResult
				+ ", skinTestItemDrugBatchNo=" + skinTestItemDrugBatchNo
				+ ", skinTestItemInputNurseId=" + skinTestItemInputNurseId
				+ ", skinTestItemInputNurseName=" + skinTestItemInputNurseName
				+ ", skinTestItemExecNurseId=" + skinTestItemExecNurseId
				+ ", skinTestItemExecNurseName=" + skinTestItemExecNurseName
				+ ", skinTestItemExecDate=" + skinTestItemExecDate
				+ ", skinTestItemApproveNurseId=" + skinTestItemApproveNurseId
				+ ", skinTestItemApproveNurseName="
				+ skinTestItemApproveNurseName + "]";
	}
}
