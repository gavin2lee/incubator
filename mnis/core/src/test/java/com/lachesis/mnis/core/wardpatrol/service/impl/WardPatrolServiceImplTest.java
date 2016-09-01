package com.lachesis.mnis.core.wardpatrol.service.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.lachesis.mnis.core.PatientService;
import com.lachesis.mnis.core.SpringTest;
import com.lachesis.mnis.core.WardPatrolService;
import com.lachesis.mnis.core.patient.PatientServiceImpl;
import com.lachesis.mnis.core.patient.entity.Patient;
import com.lachesis.mnis.core.patrol.WardPatrolServiceImpl;
import com.lachesis.mnis.core.patrol.entity.WardPatrolInfo;
import com.lachesis.mnis.core.util.DateUtil;
import com.lachesis.mnis.core.util.DateUtil.DateFormat;

public class WardPatrolServiceImplTest extends SpringTest{
	@Autowired WardPatrolService wardPatrolService;
	// Patient must have been admitted into hospital
	@Autowired PatientService patientService;
	private static Patient inpatientInfo;	
	
	@BeforeClass
	public static void init() {
		inpatientInfo = new Patient();
		inpatientInfo.setName("JUnit");
		inpatientInfo.setTendLevel(0);
		inpatientInfo.setDutyNurseCode("000058");
		inpatientInfo.setDeptCode("1005");
	}
	

	
	@Test
	public void testGetWardPatrolPlan() {
		List<WardPatrolInfo> wResult = wardPatrolService.getWardPatrolPlan("5042","468","1","2015-10-24");
		assertNotNull(wResult);
	}

	@Ignore("Next")
	@Test
	public void testGetWardPatrolPlanForPatient() {
		fail("Not yet implemented");
	}

	@Test(expected=Exception.class) // 抛出病人信息无效的异常
	public void testSaveWardPatrolInfoForPatientError() throws Exception {
			wardPatrolService.saveWardPatrolInfoForPatient(null, null, null); 
			wardPatrolService.saveWardPatrolInfoForPatient(new Patient(), null, null);
	}

	@Test
	public void testSaveWardPatrolInfoForPatient() throws Exception {	// 正常保存
		assertNotNull( wardPatrolService.saveWardPatrolInfoForPatient(inpatientInfo, 
				inpatientInfo.getDutyNurseCode(), 
				inpatientInfo.getDeptCode()) );
	}
	
	@Test
	public void testGetWardPatrolLogByPatId() throws Exception {
		//保存成功后应能获取记录	
		assertNull( wardPatrolService.getWardPatrolLogByPatId(null, null) );
		// 
		assertNotNull( wardPatrolService.saveWardPatrolInfoForPatient(inpatientInfo, 
				inpatientInfo.getDutyNurseCode(), 
				inpatientInfo.getDeptCode()) );

		String today =   DateUtil.format(DateFormat.YMD);
		// 病人id为null时返回null
		assertNull( wardPatrolService.getWardPatrolLogByPatId(null, today ) );  
		assertNotNull( wardPatrolService.getWardPatrolLogByPatId("999999999", null ) ); // 随便输入病人id都返回列表，即使是空列表

		assertTrue( wardPatrolService.getWardPatrolLogByPatId( // 刚插入的记录
				inpatientInfo.getPatId(), today
				).size()>0 );
	}

}
