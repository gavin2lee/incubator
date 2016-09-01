/**
 * (C) Copyright. LACHESIS All rights reserved.
 *
 */
package com.lachesis.mnis.web.vo;

import java.io.Serializable;

import com.lachesis.mnis.core.patient.entity.Gender;

/**
 * The class BedPatientInfo.
 * 
 * 病人一览病床明细
 *
 * @author: yanhui.wang
 * @since: 2014-6-17	
 * @version: $Revision$ $Date$ $LastChangedBy$
 *
 */
public class BedPatientInfo implements Serializable {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = -6746734108747037546L;

	private String patientId; //患者ID
	private String inHospitalNo; //住院号
	private String inpNo; //患者编号
	private String patientName; //姓名
	private String deptCode;//部门code
	private String deptName;//部门name
	private String age; //年龄
	private String ageDuration; //年龄段           'M':成人   'Y':儿童 
	private Gender gender; //性别                 M-男，F-女，U-未知
	private Gender sex; //性别显示                 M-男，F-女，U-未知
	private String chargeType; //收费类型
	private String chargeNature; //收费性质  0=自费 1=医保  2 =公费
	private String bedCode; //病床号
	private int tendLevel; //护理级别0-特级,1-一级,2-二级,3-三级
	private String dutyNurseCode; //责任护士ID
	private String dutyNurseName; //责任护士名称
	private String doctor; // 主治医生
	private String diet; //饮食
	private String allergyDrugs; //过敏药物
	private String barcode; //病人条码
	private String admitDate; //入院时间
	private String crisisValue; //病人病危值
	private String admitDiagnosis; //入院诊断

	private boolean isDebit; //是否欠费
	private boolean hasExecOrder; //是否有医嘱
	private boolean isSensitive; //是否过敏 
	private boolean isSeparate; //是否隔离
	private boolean isOperation; //是否手术
	private boolean isHot; //是否发热
	private boolean isNewest; //是否新入院
	private boolean isFall;// 是否跌倒
	private boolean isPressure;//是否压疮
	
	private String showBedCode; //显示在主界面的床号
	
	//腕带是否打印
	private boolean isPrintBarcode;
	//床头卡打印
	private boolean isPrintBed;
	public String getPatientId() {
		return patientId;
	}
	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}
	public String getInHospitalNo() {
		return inHospitalNo;
	}
	public void setInHospitalNo(String inHospitalNo) {
		this.inHospitalNo = inHospitalNo;
	}
	public String getInpNo() {
		return inpNo;
	}
	public void setInpNo(String inpNo) {
		this.inpNo = inpNo;
	}
	public String getPatientName() {
		return patientName;
	}
	public void setPatientName(String patientName) {
		this.patientName = patientName;
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
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getAgeDuration() {
		return ageDuration;
	}
	public void setAgeDuration(String ageDuration) {
		this.ageDuration = ageDuration;
	}
	public Gender getGender() {
		return gender;
	}
	public void setGender(Gender gender) {
		this.gender = gender;
	}
	public Gender getSex() {
		return sex;
	}
	public void setSex(Gender sex) {
		this.sex = sex;
	}
	public String getChargeType() {
		return chargeType;
	}
	public void setChargeType(String chargeType) {
		this.chargeType = chargeType;
	}
	public String getChargeNature() {
		return chargeNature;
	}
	public void setChargeNature(String chargeNature) {
		this.chargeNature = chargeNature;
	}
	public String getBedCode() {
		return bedCode;
	}
	public void setBedCode(String bedCode) {
		this.bedCode = bedCode;
	}
	public int getTendLevel() {
		return tendLevel;
	}
	public void setTendLevel(int tendLevel) {
		this.tendLevel = tendLevel;
	}
	public String getDutyNurseCode() {
		return dutyNurseCode;
	}
	public void setDutyNurseCode(String dutyNurseCode) {
		this.dutyNurseCode = dutyNurseCode;
	}
	public String getDutyNurseName() {
		return dutyNurseName;
	}
	public void setDutyNurseName(String dutyNurseName) {
		this.dutyNurseName = dutyNurseName;
	}
	public String getDiet() {
		return diet;
	}
	public void setDiet(String diet) {
		this.diet = diet;
	}
	public String getAllergyDrugs() {
		return allergyDrugs;
	}
	public void setAllergyDrugs(String allergyDrugs) {
		this.allergyDrugs = allergyDrugs;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public String getAdmitDate() {
		return admitDate;
	}
	public void setAdmitDate(String admitDate) {
		this.admitDate = admitDate;
	}
	public String getCrisisValue() {
		return crisisValue;
	}
	public void setCrisisValue(String crisisValue) {
		this.crisisValue = crisisValue;
	}
	public String getAdmitDiagnosis() {
		return admitDiagnosis;
	}
	public void setAdmitDiagnosis(String admitDiagnosis) {
		this.admitDiagnosis = admitDiagnosis;
	}
	public boolean isDebit() {
		return isDebit;
	}
	public void setDebit(boolean isDebit) {
		this.isDebit = isDebit;
	}
	public boolean isHasExecOrder() {
		return hasExecOrder;
	}
	public void setHasExecOrder(boolean hasExecOrder) {
		this.hasExecOrder = hasExecOrder;
	}
	public boolean isSensitive() {
		return isSensitive;
	}
	public void setSensitive(boolean isSensitive) {
		this.isSensitive = isSensitive;
	}
	public boolean isSeparate() {
		return isSeparate;
	}
	public void setSeparate(boolean isSeparate) {
		this.isSeparate = isSeparate;
	}
	public boolean isOperation() {
		return isOperation;
	}
	public void setOperation(boolean isOperation) {
		this.isOperation = isOperation;
	}
	public boolean isHot() {
		return isHot;
	}
	public void setHot(boolean isHot) {
		this.isHot = isHot;
	}
	public boolean isNewest() {
		return isNewest;
	}
	public void setNewest(boolean isNewest) {
		this.isNewest = isNewest;
	}
	public boolean isFall() {
		return isFall;
	}
	public void setFall(boolean isFall) {
		this.isFall = isFall;
	}
	public boolean isPressure() {
		return isPressure;
	}
	public void setPressure(boolean isPressure) {
		this.isPressure = isPressure;
	}
	public String getShowBedCode() {
		return showBedCode;
	}
	public void setShowBedCode(String showBedCode) {
		this.showBedCode = showBedCode;
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
	public String getDoctor() {
		return doctor;
	}
	public void setDoctor(String doctor) {
		this.doctor = doctor;
	}
}
