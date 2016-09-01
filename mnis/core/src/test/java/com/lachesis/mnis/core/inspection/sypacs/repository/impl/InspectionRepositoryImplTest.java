package com.lachesis.mnis.core.inspection.sypacs.repository.impl;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.lachesis.mnis.core.SpringTest;
import com.lachesis.mnis.core.inspection.entity.InspectionRecord;
import com.lachesis.mnis.core.inspection.repository.InspectionRepository;

public class InspectionRepositoryImplTest extends SpringTest {
	@Autowired
	InspectionRepository inspectionRepository;
	private Map<String, Object> paramMap = new HashMap<String,Object>();
	private static final Logger LOGGER = LoggerFactory.getLogger(InspectionRepositoryImplTest.class);
	@Before
	public void init(){
		paramMap.put("patientId", "0000077846");
	}
	
	@Test
	public void testInit() {
		assertNotNull(inspectionRepository);
	}
	
	@Test
	public void testGetInspectionRecord(){
		List<InspectionRecord> records = inspectionRepository.getInspectionRecord(paramMap);
	
		LOGGER.info("testGetInspectionRecord records size:" + records.size());
		assertNotNull(records);
	}
	
	@Test
	public void testGetInspectionRecordByPatients(){
		paramMap.clear();
		List<String> patientIdsList = new ArrayList<String>();
		patientIdsList.add("0000077846");
		paramMap.put("patientIds", patientIdsList);
		List<InspectionRecord> records = inspectionRepository.getInspectionRecordByPatients(paramMap);
	
		LOGGER.info("testGetInspectionRecordByPatients records size:" + records.size());
		assertNotNull(records);
	}

}