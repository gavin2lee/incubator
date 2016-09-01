package com.lachesis.mnis.core.labtest.repository.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.lachesis.mnis.core.labtest.entity.LabTestRecord;
import com.lachesis.mnis.core.labtest.entity.LabTestSendRecord;
import com.lachesis.mnis.core.labtest.entity.LabTestSendRecordDetail;
import com.lachesis.mnis.core.labtest.entity.LabTestSendStatistic;
import com.lachesis.mnis.core.labtest.repository.LabTestRepository;
import com.lachesis.mnis.core.mybatis.mapper.LabTestServiceMapper;

@Repository("labTestRepository")
public class LabTestRepositoryImpl implements LabTestRepository {
	@Autowired
	private LabTestServiceMapper labTestMapper;
	
	@Override
	public int updateLabTestRecordPriFlag(Map<String, Object> paramMap) {
		return labTestMapper.updateLabTestRecordPriFlag(paramMap);
	}

	@Override
	public List<LabTestRecord> getLabTestRecord(Map<String, Object> paramMap) {
//		paramMap.put("patientId", ((String)paramMap.get("patientId")).substring(4));
		return labTestMapper.getLabTestRecord(paramMap);
	}

	@Override
	public List<LabTestRecord> getLabTestRecordById(String barCode) {
		return labTestMapper.getLabTestRecordById(barCode);
	}

	@Override
	public List<LabTestSendStatistic> getLabTestSendStatistic(String deptCode,
			String startDate, String endDate,String allType) {
		return labTestMapper.getLabTestSendStatistic(deptCode, startDate, endDate,allType);
	}

	@Override
	public List<LabTestSendRecord> getLabTestSendRecords(String deptCode,String patId,
			String startDate, String endDate,String allType) {
		return labTestMapper.getLabTestSendRecords(deptCode,patId, startDate, endDate,allType);
	}


	@Override
	public String getLabTestSendPatIdByBarcode(String barcode) {
		return labTestMapper.getLabTestSendPatIdByBarcode(barcode);
	}

	@Override
	public int insertLabTestSend(LabTestSendRecordDetail labTestSendRecordDetail) {
		return labTestMapper.insertLabTestSend(labTestSendRecordDetail);
	}

	@Override
	public Integer getLabTestSendCountByBarcode(String barcode) {
		return labTestMapper.getLabTestSendCountByBarcode(barcode);
	}

	@Override
	public List<LabTestRecord> getPublishLabTests(HashMap<String, Object> params) {
		return labTestMapper.getPublishLabTests(params);
	}

	
}
