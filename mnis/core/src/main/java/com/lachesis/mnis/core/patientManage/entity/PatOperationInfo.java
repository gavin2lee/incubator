package com.lachesis.mnis.core.patientManage.entity;

import java.util.Date;
import java.util.List;

import com.lachesis.mnis.core.identity.entity.AgeEntity;
import com.lachesis.mnis.core.util.DateUtil;

public class PatOperationInfo {
    private Long seqId;

    private String operationId;
    
    private String operationName;

    private String patId;
    
    private String bedNo;
    
    private String patientName;
    
    private String age;
    
    private String sex;
    
    private String deptName;

    private String preOperationDiagnosis;

    private String postOperationDiagnosis;

    private String operationNo;

    private String instrumentNurse;

    private String anesthesiaMethods;

    private String surgeon;

    private String anesthesiaDoctor;

    private String operationOne;

    private String operationTwo;
    
    private Date operationTime;

    private String scrubNurse;

    private String circulatingNurse;

    private Integer isAnesthesiaConsultation;

    private Integer isEmergencyTreatment;

    private Date createTime;

    private String createPerson;

    private Date updateTime;

    private String updatePerson;
    
    private List<PatOperationStatus> patOperationStatusList;

    public Long getSeqId() {
        return seqId;
    }

    public void setSeqId(Long seqId) {
        this.seqId = seqId;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public String getPatId() {
        return patId;
    }

    public void setPatId(String patId) {
        this.patId = patId;
    }

    public String getPreOperationDiagnosis() {
        return preOperationDiagnosis;
    }

    public void setPreOperationDiagnosis(String preOperationDiagnosis) {
        this.preOperationDiagnosis = preOperationDiagnosis;
    }

    public String getPostOperationDiagnosis() {
        return postOperationDiagnosis;
    }

    public void setPostOperationDiagnosis(String postOperationDiagnosis) {
        this.postOperationDiagnosis = postOperationDiagnosis;
    }

    public String getOperationNo() {
        return operationNo;
    }

    public void setOperationNo(String operationNo) {
        this.operationNo = operationNo;
    }

    public String getInstrumentNurse() {
        return instrumentNurse;
    }

    public void setInstrumentNurse(String instrumentNurse) {
        this.instrumentNurse = instrumentNurse;
    }

    public String getAnesthesiaMethods() {
        return anesthesiaMethods;
    }

    public void setAnesthesiaMethods(String anesthesiaMethods) {
        this.anesthesiaMethods = anesthesiaMethods;
    }

    public String getSurgeon() {
        return surgeon;
    }

    public void setSurgeon(String surgeon) {
        this.surgeon = surgeon;
    }

    public String getAnesthesiaDoctor() {
        return anesthesiaDoctor;
    }

    public void setAnesthesiaDoctor(String anesthesiaDoctor) {
        this.anesthesiaDoctor = anesthesiaDoctor;
    }

    public String getOperationOne() {
        return operationOne;
    }

    public void setOperationOne(String operationOne) {
        this.operationOne = operationOne;
    }

    public String getOperationTwo() {
        return operationTwo;
    }

    public void setOperationTwo(String operationTwo) {
        this.operationTwo = operationTwo;
    }

    public String getScrubNurse() {
        return scrubNurse;
    }

    public void setScrubNurse(String scrubNurse) {
        this.scrubNurse = scrubNurse;
    }

    public String getCirculatingNurse() {
        return circulatingNurse;
    }

    public void setCirculatingNurse(String circulatingNurse) {
        this.circulatingNurse = circulatingNurse;
    }

    public Integer getIsAnesthesiaConsultation() {
        return isAnesthesiaConsultation;
    }

    public void setIsAnesthesiaConsultation(Integer isAnesthesiaConsultation) {
        this.isAnesthesiaConsultation = isAnesthesiaConsultation;
    }

    public Integer getIsEmergencyTreatment() {
        return isEmergencyTreatment;
    }

    public void setIsEmergencyTreatment(Integer isEmergencyTreatment) {
        this.isEmergencyTreatment = isEmergencyTreatment;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreatePerson() {
        return createPerson;
    }

    public void setCreatePerson(String createPerson) {
        this.createPerson = createPerson;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdatePerson() {
        return updatePerson;
    }

    public void setUpdatePerson(String updatePerson) {
        this.updatePerson = updatePerson;
    }

	public String getOperationName() {
		return operationName;
	}

	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = AgeEntity.calculateAge(null, DateUtil.parse(age.substring(0,19))).getAge()+"";
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	

	public String getBedNo() {
		return bedNo;
	}

	public void setBedNo(String bedNo) {
		this.bedNo = bedNo;
	}

	public List<PatOperationStatus> getPatOperationStatusList() {
		return patOperationStatusList;
	}

	public void setPatOperationStatusList(
			List<PatOperationStatus> patOperationStatusList) {
		this.patOperationStatusList = patOperationStatusList;
	}

	public Date getOperationTime() {
		return operationTime;
	}

	public void setOperationTime(Date operationTime) {
		this.operationTime = operationTime;
	}
}
