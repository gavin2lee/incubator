package com.lachesis.mnis.core.nursing.nurseshift.repository.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.lachesis.mnis.core.PatientService;
import com.lachesis.mnis.core.SpringTest;
import com.lachesis.mnis.core.nursing.NurseShiftEntity;
import com.lachesis.mnis.core.nursing.NurseShiftRecordEntity;
import com.lachesis.mnis.core.nursing.NurseShiftRepository;
import com.lachesis.mnis.core.nursing.NurseShiftServiceImpl;
import com.lachesis.mnis.core.task.TaskService;
import com.lachesis.mnis.core.util.DateUtil;
import com.lachesis.mnis.core.util.DateUtil.DateFormat;
import com.lachesis.mnis.core.util.StringUtil;

public class TestNurseShiftRepositoryImpl extends SpringTest {

	private static final String TEST_PATID = "ZY010000078480";
	private static final String QUERY_PATID_STRING = "ZY0100001281242014122108000020141221160000";
	private static final String TEST_NURSE_SHIFT_ID = "ZY010000078476";
	private static String recordId = "11";
	private static final String deptCode = "3001";

	@Autowired
	NurseShiftRepository nurseShiftRepository;

	@Autowired
	PatientService patientService;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(TestNurseShiftRepositoryImpl.class);

	private Map<String, Object> paramsMap = new HashMap<String, Object>();
	private NurseShiftEntity nurseShiftEntity;
	private NurseShiftRecordEntity nurseShiftRecordEntity;

	private String[] currentShiftTimeRange = new String[2];
	private String[] preShiftTimeRange = new String[2];

	@Before
	public void init() {
		recordId = StringUtil.getUUID();
		// paramsMap.put("patients", patients);
		paramsMap.put("deptCode", deptCode);

		nurseShiftEntity = new NurseShiftEntity();
		nurseShiftEntity.setPatientId(TEST_PATID);
		nurseShiftEntity.setNurseShiftId(TEST_PATID);
		nurseShiftEntity.setDeptCode(deptCode);

		nurseShiftRecordEntity = new NurseShiftRecordEntity();
		nurseShiftRecordEntity.setPatientId(TEST_PATID);
		nurseShiftRecordEntity.setDeptCode(deptCode);
		nurseShiftRecordEntity.setNurseShiftId(TEST_PATID);
		nurseShiftRecordEntity.setShiftRecordId(recordId);
		nurseShiftRecordEntity.setShiftRecordData("1212122212");

		currentShiftTimeRange = new NurseShiftServiceImpl()
				.calcLastShiftTimeRange(true);
		preShiftTimeRange = new NurseShiftServiceImpl()
				.calcLastShiftTimeRange(false);

		paramsMap.put("startDate", DateUtil.parse("2014-12-21 00:00:00", DateFormat.FULL));
		paramsMap.put("endDate", DateUtil.parse("2014-12-22 16:00:00", DateFormat.FULL));
		// paramsMap.put("startDate", preShiftTimeRange[0]);
		// paramsMap.put("endDate", preShiftTimeRange[1]);
	}

	@Test
	public void testInit() {
		Assert.assertNotNull(nurseShiftRepository);
	}

	@Test
	public void testGetNurseShifts() {
		paramsMap.put("rangeType", "1");
		List<NurseShiftEntity> nurseShiftEntities = this.nurseShiftRepository
				.getNurseShifts(paramsMap);
		LOGGER.info("testGetNurseShifts():nurseShiftEntities->"
				+ nurseShiftEntities.size());
		patientService.getPatientByPatId(TEST_PATID);
		Assert.assertNotNull(nurseShiftEntities);
	}

	@Test
	public void testGetNurseShiftEntityById() {
		NurseShiftEntity entity = this.nurseShiftRepository
				.getNurseShiftEntityById(QUERY_PATID_STRING);
		Assert.assertNotNull(entity);
	}

	@Test
	public void testInsertNurseShift() {
		Assert.assertEquals(1,
				this.nurseShiftRepository.insertNurseShift(nurseShiftEntity));

	}

	@Test
	public void testUpdateNurseShift() {
		this.nurseShiftRepository.insertNurseShift(nurseShiftEntity);
		nurseShiftEntity.setShiftNurseId("1111111");
		Assert.assertEquals(1,
				this.nurseShiftRepository.updateNurseShift(nurseShiftEntity));

	}

	@Test
	public void testDeleteNurseShiftById() {
		Assert.assertEquals(1, this.nurseShiftRepository
				.deleteNurseShiftById(QUERY_PATID_STRING));
	}

	@Test
	public void testGetShiftRecordsByNurseShiftId() {
		List<NurseShiftRecordEntity> nurseShiftRecordEntities = this.nurseShiftRepository
				.getShiftRecordsByNurseShiftId(
						QUERY_PATID_STRING);
		Assert.assertNotNull(nurseShiftRecordEntities);
	}

	@Test
	public void testGetShiftRecordById() {
		NurseShiftRecordEntity entity = this.nurseShiftRepository
				.getShiftRecordById(recordId);
		Assert.assertNotNull(entity);
	}

	@Test
	public void testInsertShiftRecord() {
		int flag = this.nurseShiftRepository
				.insertShiftRecord(nurseShiftRecordEntity);
		Assert.assertEquals(1,flag);
	}

	@Test
	public void testUpdateShiftRecord() {
		this.nurseShiftRepository.insertShiftRecord(nurseShiftRecordEntity);
		nurseShiftRecordEntity.setShiftRecordData("222222");
		Assert.assertEquals(1, this.nurseShiftRepository
				.updateShiftRecord(nurseShiftRecordEntity));
	}

}
