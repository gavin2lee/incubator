package com.lachesis.mnis.core.patientManage.entity;

import java.util.Date;

public class PatOperationStatus {
    private Long seqId;

    private String operationId;   //手术id

    private String patId;		//患者id

    private Integer status;		//状态：1 手术申请，2 手术通知，3 科室送出，4 手术室接收，5 麻醉，6 手术中，7 苏醒
    
    private String statusName;	//状态名称

    private Date createTime;   

    private String createPerson;

    private Date updateTime;

    private String updatePerson;
    
    private String execNurseId;  //执行护士id
    
    private String phone;  //执行护士电话

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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

	public String getExecNurseId() {
		return execNurseId;
	}

	public void setExecNurseId(String execNurseId) {
		this.execNurseId = execNurseId;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	
}
