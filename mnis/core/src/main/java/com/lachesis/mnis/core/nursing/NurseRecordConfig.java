package com.lachesis.mnis.core.nursing;

import java.util.List;

import com.lachesis.mnis.core.bodysign.entity.BodySignDict;

public class NurseRecordConfig {
    private List<NurseRecordSpecItem> nurseRecordSpecItemList; // required
    private List<BodySignDict> inputItemList; // required
    private List<BodySignDict> outputItemList; // required
    
	public List<NurseRecordSpecItem> getNurseRecordSpecItemList() {
		return nurseRecordSpecItemList;
	}
	public void setNurseRecordSpecItemList(
			List<NurseRecordSpecItem> nurseRecordSpecItemList) {
		this.nurseRecordSpecItemList = nurseRecordSpecItemList;
	}
	public List<BodySignDict> getInputItemList() {
		return inputItemList;
	}
	public void setInputItemList(List<BodySignDict> inputItemList) {
		this.inputItemList = inputItemList;
	}
	public List<BodySignDict> getOutputItemList() {
		return outputItemList;
	}
	public void setOutputItemList(List<BodySignDict> outputItemList) {
		this.outputItemList = outputItemList;
	}
}
