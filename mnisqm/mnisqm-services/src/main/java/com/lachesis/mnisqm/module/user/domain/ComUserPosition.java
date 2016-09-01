package com.lachesis.mnisqm.module.user.domain;


public class ComUserPosition {
    private Long seqId;

    private String status="01";    //01:有效  09：无效

    private String userCode;//员工编号

    private String technicalPost;//职称

    private String startDate;

    private String endDate;

    private String reviewUnit;

    private String createPerson;

    private String updatePerson;

    public Long getSeqId() {
		return seqId;
	}

	public void setSeqId(Long seqId) {
		this.seqId = seqId;
	}

	public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getTechnicalPost() {
		return technicalPost;
	}

	public void setTechnicalPost(String technicalPost) {
		this.technicalPost = technicalPost;
	}

	public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getReviewUnit() {
        return reviewUnit;
    }

    public void setReviewUnit(String reviewUnit) {
        this.reviewUnit = reviewUnit;
    }

	public String getCreatePerson() {
		return createPerson;
	}

	public void setCreatePerson(String createPerson) {
		this.createPerson = createPerson;
	}

	public String getUpdatePerson() {
		return updatePerson;
	}

	public void setUpdatePerson(String updatePerson) {
		this.updatePerson = updatePerson;
	}
}