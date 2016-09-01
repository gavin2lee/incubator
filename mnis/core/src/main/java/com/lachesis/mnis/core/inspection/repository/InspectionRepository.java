package com.lachesis.mnis.core.inspection.repository;

import java.util.List;
import java.util.Map;

import com.lachesis.mnis.core.inspection.entity.InspectionRecord;


public interface InspectionRepository {

	int updateInspectionPriFlag(Map<String, Object> paramMap);

	List<InspectionRecord> getInspectionRecordByPatients(
			Map<String, Object> paramMap);

	List<InspectionRecord> getInspectionRecord(Map<String, Object> paramMap);
	
	/**
	 * 获取推送检查
	 * @param paramMap
	 * @return
	 */
	List<InspectionRecord> getPublishInspections(Map<String, Object> paramMap);

}
