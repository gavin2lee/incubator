package com.lachesis.mnisqm.module.patientManage.domain;

import java.math.BigDecimal;
import java.util.Date;

public class PatientInfo {
    private Long id;

    private String patId;

    private String inHospNo;

    private String bedCode;

    private String wardCode;
    
    private String wardName;
    
    private String deptCode;
    
    private String deptName;

    private String barcode;

    private String name;

    private String gender;

    private String personId;

    private Date birthDate;

    private String contactPerson;

    private String contactPhone;

    private String contactAddress;

    private String isBaby;

    private String dangerLevel;

    private String nurseLevel;

    private String chargeType;

    private String chargeTypeName;

    private String doctorCode;

    private String doctorName;

    private String dutyNurseCode;

    private String dutyNurseName;

    private Date inDate;

    private String inDiag;

    private Date outDate;

    private String outDiag;

    private String status;

    private String dietName;

    private BigDecimal prepayCost;

    private BigDecimal ownCost;

    private BigDecimal balance;

    private String remark;

    private String allergen;

    private String adverseReactionDrugs;

    private Date syncCreate;

    private Date syncUpdate;

    private Integer isseparate;

    private String maritalStatus;

    private String occupation;

    private String education;

    private String hometown;

    private String religion;

    private String source;

    private String dailycaregivers;

    private String admissionmode;

    private String inpNo;

    private String appeal;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWardName() {
		return wardName;
	}

	public void setWardName(String wardName) {
		this.wardName = wardName;
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

    public String getBedCode() {
        return bedCode;
    }

    public void setBedCode(String bedCode) {
        this.bedCode = bedCode;
    }

    public String getWardCode() {
        return wardCode;
    }

    public void setWardCode(String wardCode) {
        this.wardCode = wardCode;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
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

    public String getIsBaby() {
        return isBaby;
    }

    public void setIsBaby(String isBaby) {
        this.isBaby = isBaby;
    }

    public String getDangerLevel() {
        return dangerLevel;
    }

    public void setDangerLevel(String dangerLevel) {
        this.dangerLevel = dangerLevel;
    }

    public String getNurseLevel() {
        return nurseLevel;
    }

    public void setNurseLevel(String nurseLevel) {
        this.nurseLevel = nurseLevel;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDietName() {
        return dietName;
    }

    public void setDietName(String dietName) {
        this.dietName = dietName;
    }

    public BigDecimal getPrepayCost() {
        return prepayCost;
    }

    public void setPrepayCost(BigDecimal prepayCost) {
        this.prepayCost = prepayCost;
    }

    public BigDecimal getOwnCost() {
        return ownCost;
    }

    public void setOwnCost(BigDecimal ownCost) {
        this.ownCost = ownCost;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
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

    public Date getSyncCreate() {
        return syncCreate;
    }

    public void setSyncCreate(Date syncCreate) {
        this.syncCreate = syncCreate;
    }

    public Date getSyncUpdate() {
        return syncUpdate;
    }

    public void setSyncUpdate(Date syncUpdate) {
        this.syncUpdate = syncUpdate;
    }

    public Integer getIsseparate() {
        return isseparate;
    }

    public void setIsseparate(Integer isseparate) {
        this.isseparate = isseparate;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getHometown() {
        return hometown;
    }

    public void setHometown(String hometown) {
        this.hometown = hometown;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDailycaregivers() {
        return dailycaregivers;
    }

    public void setDailycaregivers(String dailycaregivers) {
        this.dailycaregivers = dailycaregivers;
    }

    public String getAdmissionmode() {
        return admissionmode;
    }

    public void setAdmissionmode(String admissionmode) {
        this.admissionmode = admissionmode;
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