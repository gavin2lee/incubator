package com.lachesis.mnisqm.module.qualityForm.domain;

public class QualityModelTrans {
	
	private Long detailId;
	
	private String detailName;
	
	private String descrip;
	
	private String standard;
	
	private Integer points;
	
	private String typeName;
	
	private Long typeId;
	
	private String bigTypeName;
	
	private Long bigTypeId;


	public String getDetailName() {
		return detailName;
	}

	public void setDetailName(String detailName) {
		this.detailName = detailName;
	}

	public String getDescrip() {
		return descrip;
	}

	public void setDescrip(String descrip) {
		this.descrip = descrip;
	}

	public String getStandard() {
		return standard;
	}

	public void setStandard(String standard) {
		this.standard = standard;
	}

	public Integer getPoints() {
		return points;
	}

	public void setPoints(Integer points) {
		this.points = points;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public Long getDetailId() {
		return detailId;
	}

	public void setDetailId(Long detailId) {
		this.detailId = detailId;
	}

	public Long getTypeId() {
		return typeId;
	}

	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}

	public String getBigTypeName() {
		return bigTypeName;
	}

	public void setBigTypeName(String bigTypeName) {
		this.bigTypeName = bigTypeName;
	}

	public Long getBigTypeId() {
		return bigTypeId;
	}

	public void setBigTypeId(Long bigTypeId) {
		this.bigTypeId = bigTypeId;
	}

}
