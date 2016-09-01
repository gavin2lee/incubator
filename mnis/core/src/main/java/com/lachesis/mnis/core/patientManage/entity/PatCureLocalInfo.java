package com.lachesis.mnis.core.patientManage.entity;

import java.util.Date;
import java.util.List;

import com.lachesis.mnis.core.identity.entity.AgeEntity;
import com.lachesis.mnis.core.util.DateUtil;

public class PatCureLocalInfo {
    private Long seqId;

    private String patId;  //患者id

    private Integer status;  //状态(在院状态)  1 在院 0 出院

    private Date leaveHospitalTime;  //出院时间

    private String remark;  //备注

    private Date createTime;  

    private String createPerson;

    private Date updateTime;

    private String updatePerson;
    
    private List<PatLeaveGoout> patLeaveGooutList;
    
    private String bedNo;
	
	private String age;
	
	private String sex;
	
	private String phone;
	
	private String patientName;

    public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getBedNo() {
		return bedNo;
	}

	public void setBedNo(String bedNo) {
		this.bedNo = bedNo;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = AgeEntity.calculateAge(new Date(), DateUtil.parse(age.substring(0, 19))).getAge()+"";
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Long getSeqId() {
        return seqId;
    }

    public void setSeqId(Long seqId) {
        this.seqId = seqId;
    }

    public String getPatId() {
        return patId;
    }

    public void setPatId(String patId) {
        this.patId = patId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getLeaveHospitalTime() {
        return leaveHospitalTime;
    }

    public void setLeaveHospitalTime(Date leaveHospitalTime) {
        this.leaveHospitalTime = leaveHospitalTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

	public List<PatLeaveGoout> getPatLeaveGooutList() {
		return patLeaveGooutList;
	}

	public void setPatLeaveGooutList(List<PatLeaveGoout> patLeaveGooutList) {
		this.patLeaveGooutList = patLeaveGooutList;
	}
}
