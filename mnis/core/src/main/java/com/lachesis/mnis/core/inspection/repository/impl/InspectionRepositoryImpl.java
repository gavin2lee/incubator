package com.lachesis.mnis.core.inspection.repository.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.lachesis.mnis.core.inspection.entity.InspectionRecord;
import com.lachesis.mnis.core.inspection.repository.InspectionRepository;
import com.lachesis.mnis.core.mybatis.mapper.InspectionServiceMapper;

@Repository("inspectionSypacsRepository")
public class InspectionRepositoryImpl implements InspectionRepository {
	@Autowired
	private InspectionServiceMapper inspectionSypacsServiceMapper;
	
	@Override
	public int updateInspectionPriFlag(Map<String, Object> paramMap) {
		return inspectionSypacsServiceMapper.updateInspectionPriFlag(paramMap);
	}

	@Override
	public List<InspectionRecord> getInspectionRecordByPatients(Map<String, Object> paramMap) {
		return inspectionSypacsServiceMapper.getInspectionRecordByPatients(paramMap);
	}

	@Override
	public List<InspectionRecord> getInspectionRecord(Map<String, Object> paramMap) {
		return inspectionSypacsServiceMapper.getInspectionRecord(paramMap);
	}

	@Override
	public List<InspectionRecord> getPublishInspections(
			Map<String, Object> paramMap) {
		return inspectionSypacsServiceMapper.getPublishInspections(paramMap);
	}
}
