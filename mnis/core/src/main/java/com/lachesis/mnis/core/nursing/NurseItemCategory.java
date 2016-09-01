package com.lachesis.mnis.core.nursing;

import java.io.Serializable;
import java.util.List;

public class NurseItemCategory  implements Serializable{
    public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public List<NurseItem> getNurseItemList() {
		return nurseItemList;
	}
	public void setNurseItemList(List<NurseItem> nurseItemList) {
		this.nurseItemList = nurseItemList;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 8212095680553419121L;
	private String name; // required
    private String code; // required
    private List<NurseItem> nurseItemList; // required
}
