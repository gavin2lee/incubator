package com.lachesis.mnis.core.wardpatrol.repository.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.lachesis.mnis.core.SpringTest;
import com.lachesis.mnis.core.patrol.entity.WardPatrolInfo;
import com.lachesis.mnis.core.patrol.repository.WardPatrolRepository;

public class WardPatrolResitoryImplTest extends SpringTest {
	private WardPatrolInfo wardPatrolInfo = null;
	@Autowired
	private WardPatrolRepository wardPatrolRepository;
	
	private static String deptCode = null;
	private static String patientId = null;
	
	@Before
	public void init() {
		deptCode = "H250004";
		patientId = "ZA1001803_1";
		wardPatrolInfo = new WardPatrolInfo();
		wardPatrolInfo.setBedCode("111");
		wardPatrolInfo.setDeptId(deptCode);
		wardPatrolInfo.setPatientId(patientId);
		wardPatrolInfo.setTendLevel(2);
		wardPatrolInfo.setNurseEmplCode("1070");
		wardPatrolInfo.setPatrolDate(new Date());
		wardPatrolInfo.setNextPatrolDate(new Date(Calendar.getInstance().getTimeInMillis() + 1000*60*60));
	}
	
	@Test
	public void testSaveWardPatrolInfo(){
		 int count = wardPatrolRepository.saveWardPatrolInfo(wardPatrolInfo);
		 
		 Assert.assertEquals(1, count);
	}
	
	@Test
	public void testGetWardPatrolPlan(){
		wardPatrolRepository.saveWardPatrolInfo(wardPatrolInfo);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("deptCode", deptCode);
		
		List<WardPatrolInfo> wardPatrolInfos = wardPatrolRepository.getWardPatrolPlan(params);
		Assert.assertNotNull(wardPatrolInfos);
	}
	
	@Test
	public void testSelectWardPatrolByPatId(){
		wardPatrolRepository.saveWardPatrolInfo(wardPatrolInfo);
		List<String> patientIds = new ArrayList<>();
		patientIds.add(patientId);
		
		List<WardPatrolInfo> wardPatrolInfos = wardPatrolRepository.selectWardPatrolByPatId(patientIds, null, null);
		Assert.assertNotNull(wardPatrolInfos);
	}
}
