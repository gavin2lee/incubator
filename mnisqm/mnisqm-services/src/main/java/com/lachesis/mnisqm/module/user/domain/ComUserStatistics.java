package com.lachesis.mnisqm.module.user.domain;

import java.util.List;


public class ComUserStatistics {
    private List<Statistics> technicalPost;//职称

    private List<Statistics> duty;//职务

    private List<Statistics> education;//最高学历
    
    private List<Statistics> clinical;//临床层级
    
    private List<Statistics> energyLevel;//能级
    
    private List<Statistics> teachLevel;//代教级别
    
    private List<Statistics> gender;//性别
    
    private List<Statistics> userType;//类型

	public List<Statistics> getTechnicalPost() {
		return technicalPost;
	}

	public void setTechnicalPost(List<Statistics> technicalPost) {
		this.technicalPost = technicalPost;
	}

	public List<Statistics> getDuty() {
		return duty;
	}

	public void setDuty(List<Statistics> duty) {
		this.duty = duty;
	}

	public List<Statistics> getEducation() {
		return education;
	}

	public void setEducation(List<Statistics> education) {
		this.education = education;
	}

	public List<Statistics> getClinical() {
		return clinical;
	}

	public void setClinical(List<Statistics> clinical) {
		this.clinical = clinical;
	}

	public List<Statistics> getEnergyLevel() {
		return energyLevel;
	}

	public void setEnergyLevel(List<Statistics> energyLevel) {
		this.energyLevel = energyLevel;
	}

	public List<Statistics> getTeachLevel() {
		return teachLevel;
	}

	public void setTeachLevel(List<Statistics> teachLevel) {
		this.teachLevel = teachLevel;
	}

	public List<Statistics> getGender() {
		return gender;
	}

	public void setGender(List<Statistics> gender) {
		this.gender = gender;
	}

	public List<Statistics> getUserType() {
		return userType;
	}

	public void setUserType(List<Statistics> userType) {
		this.userType = userType;
	}
}