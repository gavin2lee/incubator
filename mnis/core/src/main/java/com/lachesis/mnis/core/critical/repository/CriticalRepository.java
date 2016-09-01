package com.lachesis.mnis.core.critical.repository;

import java.util.List;
import java.util.Map;

import com.lachesis.mnis.core.critical.entity.CriticalOperRecord;
import com.lachesis.mnis.core.critical.entity.CriticalValue;
import com.lachesis.mnis.core.critical.entity.CriticalValueRecord;


public interface CriticalRepository {	 
	
	 List<CriticalValue> getCriticalValue(Map<String, Object> paramMap);

	CriticalValue getCriticalValueById(String criticalId);

	int configCritical(String criticalId, String nurseId);
	
	/**
	 * 判断危急值状态
	 * @param barcode
	 * @return
	 */
	Integer getCritiveRecordCount(String barcode);
	/**
	 * 插入危急值
	 * @param criticalOperRecord
	 * @return
	 */
	int insertCriticalOperRecord(CriticalOperRecord criticalOperRecord);
	
	List<CriticalValueRecord> getCriticalValueRecord(Map<String, Object> params);
	
}
