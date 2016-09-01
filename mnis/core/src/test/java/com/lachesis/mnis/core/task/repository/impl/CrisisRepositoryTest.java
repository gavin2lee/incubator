package com.lachesis.mnis.core.task.repository.impl;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.lachesis.mnis.core.SpringTest;
import com.lachesis.mnis.core.critical.entity.CriticalValue;
import com.lachesis.mnis.core.critical.repository.CriticalRepository;
import com.lachesis.mnis.core.util.DateUtil;
import com.lachesis.mnis.core.util.DateUtil.DateFormat;

public class CrisisRepositoryTest extends SpringTest {
	
	@Autowired
	private CriticalRepository criticalRepository;
	
	@Test
	public void testGetCrisisValue() throws ParseException {
		Map<String, Object> paramMap = new HashMap<String, Object>();
//		paramMap.put("patientId", patientId);
		paramMap.put("deptno", "3001");
		paramMap.put("startTime", DateUtil.parse("2014-11-11 13:59:31",DateFormat.FULL));
		paramMap.put("endTime", DateUtil.parse("2014-12-01 13:59:59",DateFormat.FULL));
		List<CriticalValue> values = criticalRepository.getCriticalValue(paramMap);
		Assert.notEmpty(values);
		
	}
}
