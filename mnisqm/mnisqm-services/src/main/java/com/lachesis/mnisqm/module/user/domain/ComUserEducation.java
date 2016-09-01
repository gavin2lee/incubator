package com.lachesis.mnisqm.module.user.domain;


public class ComUserEducation {
    private Long seqId;

    private String userCode;

    private String status="01";   //01:有效  09：无效

    private String startDate;

    private String endDate;

    private String educationType;

    private Long leaningTime;

    private String courseTopics;

    private String courseContent;

    private String createPerson;

    private String updatePerson;

    public Long getSeqId() {
		return seqId;
	}

	public void setSeqId(Long seqId) {
		this.seqId = seqId;
	}

	public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getEducationType() {
		return educationType;
	}

	public void setEducationType(String educationType) {
		this.educationType = educationType;
	}

	public Long getLeaningTime() {
		return leaningTime;
	}

	public void setLeaningTime(Long leaningTime) {
		this.leaningTime = leaningTime;
	}

	public String getCourseTopics() {
		return courseTopics;
	}

	public void setCourseTopics(String courseTopics) {
		this.courseTopics = courseTopics;
	}

	public String getCourseContent() {
		return courseContent;
	}

	public void setCourseContent(String courseContent) {
		this.courseContent = courseContent;
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