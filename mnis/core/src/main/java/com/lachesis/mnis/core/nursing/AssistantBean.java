package com.lachesis.mnis.core.nursing;

import java.io.Serializable;

public class AssistantBean implements Serializable {

	private static final long serialVersionUID = -7223601954519004307L;

	private Integer assClassId;
	private String assClassCode;
	private String assClassName;
	
	private Integer assId;
	private String assName;

	public Integer getAssClassId() {
		return assClassId;
	}

	public void setAssClassId(Integer assClassId) {
		this.assClassId = assClassId;
	}

	public String getAssClassCode() {
		return assClassCode;
	}

	public void setAssClassCode(String assClassCode) {
		this.assClassCode = assClassCode;
	}

	public String getAssClassName() {
		return assClassName;
	}

	public void setAssClassName(String assClassName) {
		this.assClassName = assClassName;
	}

	public Integer getAssId() {
		return assId;
	}

	public void setAssId(Integer assId) {
		this.assId = assId;
	}

	public String getAssName() {
		return assName;
	}

	public void setAssName(String assName) {
		this.assName = assName;
	}

}
