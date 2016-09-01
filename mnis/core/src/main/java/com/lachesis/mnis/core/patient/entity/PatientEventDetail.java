package com.lachesis.mnis.core.patient.entity;

import java.util.Date;

import com.lachesis.mnis.core.identity.entity.AgeEntity;
import com.lachesis.mnis.core.util.DateUtil;

public class PatientEventDetail extends PatientEvent {
	
	private String bedNo;
	
	private String age;
	
	private String sex;
	
	private String phone;

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

}
