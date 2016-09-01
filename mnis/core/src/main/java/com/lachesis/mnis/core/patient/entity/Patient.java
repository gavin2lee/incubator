package com.lachesis.mnis.core.patient.entity;

import java.util.Date;
import java.util.List;

/***
 * 病人信息
 * 
 * @author yuliang.xu
 * @date 2015年6月11日 上午10:37:19
 */
public class Patient {

	/** 患者记录自增长号 */
	private int id;

	/*********** 核心数据 **************/
	/** 住院流水号 */
	private String patId;
	/** 姓名 */
	private String name;
	/** 住院号 */
	private String inHospNo;
	/**患者编号*/
	private String inpNo;
	/** 床号 */
	private String bedCode;
	/** 病区代码 */
	private String deptCode;
	/** 病区名称 */
	private String deptName;
	/*********** 核心数据 **************/

	/** 患者条码 */
	private String barCode;
	/** 性别(F:女,M:男) */
	private Gender gender;
	/** 身份证 */
	private String personId;
	/** 出生日期 */
	private Date birthday;
	/** 联系人 */
	private String contactPerson;
	/** 联系电话 */
	private String contactPhone;
	/** 联系地址 */
	private String contactAddress;

	/** 是否婴儿(0:否,1:是) */
	private boolean isBaby;
	/** 危险级别(3:N:普通, 2:S:病重, 1:D:重危,4:E死亡) */
	private String dangerLevel;
	/** 护理级别(0:特级,1:一级,2:二级,3:三级护理) */
	private int tendLevel;
	/** 收费类型/付款方式 */
	private String chargeType;
	/** 收费类型名称 */
	private String chargeTypeName;
	/** 主治医生 */
	private String doctorCode;
	/** 主治医生姓名 */
	private String doctorName;
	/** 责任护士 */
	private String dutyNurseCode;
	/** 责任护士姓名 */
	private String dutyNurseName;
	/** 入院日期 */
	private Date inDate;
	/** 入院诊断 */
	private String inDiag;
	/** 出院日期 */
	private Date outDate;
	/** 出院诊断 */
	private String outDiag;
	/**
	 * 状态(0:否,1:是) 住院状态有(未使用)： R-住院登记 I-病房接诊 B-出院登记 O-出院结算 P-预约出院 N-无费退院
	 */
	private boolean status;
	/** 饮食  list? */
	private String diet;

	/** 预交费用 */
	private double prepayCost;
	/** 自费金额 */
	private double ownCost;
	/** 余额 */
	private double balance;
	/** 备注 */
	private String remark;
	/** 药品过敏  list? */
	private String allergen;
	/** 不良反应药物  list? */
	private String adverseReactionDrugs;

	/*********** 目前数据库不存在字段 ***************/
	/** 年龄 */
	private String age;
	/** 年龄段 */
	private String ageDuration;
	/** 未执行医嘱个数 */
	private int unexecutedOrderCount;
	//  手术时间，改为list 关联手术表？
	private String surgeryDate;
	//  关联诊断表
	private List<PatientDiagnosis> diagnosis;
	//  关联转科换床表
	private List<PatientTransfer> transfers;
	
	private List<PatientAllergy> patientAllergies;

	// 是否隔离 1：是
	private boolean isSeparate;
	// 是否手术 1：是
	private boolean isOperation;
	// 是否发热1：是
	private boolean isHot;
	//是否跌倒
	private boolean isFall; 
	//是否压疮
	private boolean isPressure;
	
	//主诉
	private String appeal;
	
	/**
	 * 条码是否打印
	 */
	private boolean isPrintBarcode;
	/**
	 * 床头卡是否打印
	 */
	private boolean isPrintBed;

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getInHospNo() {
		return inHospNo;
	}

	public void setInHospNo(String inHospNo) {
		this.inHospNo = inHospNo;
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

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getContactPerson() {
		return contactPerson;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	public String getContactAddress() {
		return contactAddress;
	}

	public void setContactAddress(String contactAddress) {
		this.contactAddress = contactAddress;
	}

	public boolean isBaby() {
		return isBaby;
	}

	public void setBaby(boolean isBaby) {
		this.isBaby = isBaby;
	}

	public String getDangerLevel() {
		return dangerLevel;
	}

	public void setDangerLevel(String dangerLevel) {
		this.dangerLevel = dangerLevel;
	}

	public int getTendLevel() {
		return tendLevel;
	}

	public void setTendLevel(int tendLevel) {
		this.tendLevel = tendLevel;
	}

	public String getChargeType() {
		return chargeType;
	}

	public void setChargeType(String chargeType) {
		this.chargeType = chargeType;
	}

	public String getChargeTypeName() {
		return chargeTypeName;
	}

	public void setChargeTypeName(String chargeTypeName) {
		this.chargeTypeName = chargeTypeName;
	}

	public String getDoctorCode() {
		return doctorCode;
	}

	public void setDoctorCode(String doctorCode) {
		this.doctorCode = doctorCode;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
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

	public Date getInDate() {
		return inDate;
	}

	public void setInDate(Date inDate) {
		this.inDate = inDate;
	}

	public String getInDiag() {
		return inDiag;
	}

	public void setInDiag(String inDiag) {
		this.inDiag = inDiag;
	}

	public Date getOutDate() {
		return outDate;
	}

	public void setOutDate(Date outDate) {
		this.outDate = outDate;
	}

	public String getOutDiag() {
		return outDiag;
	}

	public void setOutDiag(String outDiag) {
		this.outDiag = outDiag;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getDiet() {
		return diet;
	}

	public void setDiet(String diet) {
		if(null != diet && "null".equals(diet.toLowerCase())){
			this.diet = "";
		}else{
			this.diet = diet;
		}
	}

	public double getPrepayCost() {
		return prepayCost;
	}

	public void setPrepayCost(double prepayCost) {
		this.prepayCost = prepayCost;
	}

	public double getOwnCost() {
		return ownCost;
	}

	public void setOwnCost(double ownCost) {
		this.ownCost = ownCost;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getAllergen() {
		return allergen;
	}

	public void setAllergen(String allergen) {
		this.allergen = allergen;
	}

	public String getAdverseReactionDrugs() {
		return adverseReactionDrugs;
	}

	public void setAdverseReactionDrugs(String adverseReactionDrugs) {
		this.adverseReactionDrugs = adverseReactionDrugs;
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

	public int getUnexecutedOrderCount() {
		return unexecutedOrderCount;
	}

	public void setUnexecutedOrderCount(int unexecutedOrderCount) {
		this.unexecutedOrderCount = unexecutedOrderCount;
	}

	public String getSurgeryDate() {
		return surgeryDate;
	}

	public void setSurgeryDate(String surgeryDate) {
		this.surgeryDate = surgeryDate;
	}

	public List<PatientDiagnosis> getDiagnosis() {
		return diagnosis;
	}

	public void setDiagnosis(List<PatientDiagnosis> diagnosis) {
		this.diagnosis = diagnosis;
	}

	public List<PatientTransfer> getTransfers() {
		return transfers;
	}

	public void setTransfers(List<PatientTransfer> transfers) {
		this.transfers = transfers;
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

	public List<PatientAllergy> getPatientAllergies() {
		return patientAllergies;
	}

	public void setPatientAllergies(List<PatientAllergy> patientAllergies) {
		this.patientAllergies = patientAllergies;
	}

	public String getInpNo() {
		return inpNo;
	}

	public void setInpNo(String inpNo) {
		this.inpNo = inpNo;
	}

	public String getAppeal() {
		return appeal;
	}

	public void setAppeal(String appeal) {
		this.appeal = appeal;
	}
	
}
