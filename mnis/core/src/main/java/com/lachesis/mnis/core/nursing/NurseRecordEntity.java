package com.lachesis.mnis.core.nursing;

import java.util.ArrayList;
import java.util.List;

import com.lachesis.mnis.core.bodysign.entity.BodySignDict;
import com.lachesis.mnis.core.bodysign.entity.BodySignItem;

/**
 * 护理记录ORM辅助类
 * 为便于将生命体征项和护理记录特殊项用统一结构
 * @author wenhuan.cui
 *
 */
public class NurseRecordEntity {
	private NurseRecord nurseRecord;
	private List<BodySignItem> itemList; //将所有项目转为生命体征格式进行存储
	private int masterRecordId;
	
	public void setNurseRecord(NurseRecord nurseRecord) {
		this.nurseRecord = nurseRecord;
	}	
	public NurseRecord getNurseRecord() {
		return nurseRecord;
	}
	
	public List<BodySignItem> getItemList() {
		return itemList;
	}

	public void setItemList(List<BodySignItem> itemList) {
		this.itemList = itemList;
	}

	public int getMasterRecordId() {
		return masterRecordId;
	}

	public void setMasterRecordId(int masterRecordId) {
		this.masterRecordId = masterRecordId;
	}
	
	public boolean adapt() {
		if(nurseRecord == null) {
			return false;		
		}
		itemList = nurseRecord.getBodySignItemList();
		if( itemList == null ) {
			itemList = new ArrayList<BodySignItem>();
		}
		
		List<NurseRecordSpecItem> specItemList = nurseRecord.getNurseRecordSpecItemList();
		if(specItemList!=null) {
			for(NurseRecordSpecItem specItem:specItemList ) {
				// 转为生命体征以便于保存
				itemList.add( getFromNurseRecord(specItem) ); 
			}
		}
		
		return itemList.size() == 0 ? false : true;
	}
	
	public boolean validate() {
		if(nurseRecord == null) {
			return false;
		}
		if(	nurseRecord.getPatientId()!=null
				&& nurseRecord.getRecordDate()!=null
				&& nurseRecord.getRecordNurseCode()!=null) {
			return true;
		}
		return false;
	}
	
	
	// 从护理记录特殊项转换为生命体征项
	public BodySignItem getFromNurseRecord(NurseRecordSpecItem specItem) {
		BodySignItem bodySignItem = new BodySignItem();
		// 特殊填写值
		bodySignItem.setMeasureNoteName( specItem.getItemValue() ); 
		// 特殊填写值编码
		bodySignItem.setMeasureNoteCode( specItem.getItemValueCode() ); 
		bodySignItem.setBodySignDict(BodySignDict.create(specItem.getItemCode(),specItem.getItemName(),null) );
		return bodySignItem;
	}
	

}
