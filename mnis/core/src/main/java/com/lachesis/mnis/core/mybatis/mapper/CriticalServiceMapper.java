package com.lachesis.mnis.core.mybatis.mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.lachesis.mnis.core.critical.entity.CriticalOperRecord;
import com.lachesis.mnis.core.critical.entity.CriticalValue;
import com.lachesis.mnis.core.critical.entity.CriticalValueRecord;

public interface CriticalServiceMapper {

	 List<CriticalValue> getCriticalValue(Map<String, Object> paramMap);

	CriticalValue getCriticalValueById(String criticalId);

	int updateCritical(String criticalId, String nurseId, String nurseName, Date date);
	/**
	 * 判断危急值状态
	 * @param idbarcode
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
