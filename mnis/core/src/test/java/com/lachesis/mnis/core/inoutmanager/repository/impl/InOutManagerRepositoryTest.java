package com.lachesis.mnis.core.inoutmanager.repository.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.lachesis.mnis.core.InOutManagerService;
import com.lachesis.mnis.core.SpringTest;
import com.lachesis.mnis.core.inoutmanager.entity.InOutManager;
import com.lachesis.mnis.core.inoutmanager.entity.InOutManagerStatistic;
import com.lachesis.mnis.core.inoutmanager.repository.InOutManagerRepository;

public class InOutManagerRepositoryTest extends SpringTest {
	@Autowired
	private InOutManagerRepository inOutManagerRepository;
	
	@Test
	public void testGetInOutManagers(){
		List<InOutManager> inOutManagers = inOutManagerRepository.getInOutManagers("1", "1",null,null);
		
		Assert.assertNotNull(inOutManagers);
	}
	
	@Test
	public void testGetInOutManagerStatistic(){
		
		InOutManagerStatistic inOutManagerStatistic = inOutManagerRepository.getInOutManagerStatistic(null, "5042", null, null);
		
		Assert.assertNotNull(inOutManagerStatistic);
	}
	
	@Test
	public void testGetInOutManagerById(){
		InOutManager inOutManager = inOutManagerRepository.getInOutManagerById(1);
		
		Assert.assertNotNull(inOutManager);
	}
	
	@Test
	public void testInsertInOutManager(){
		InOutManager inOutManager = new InOutManager();
		inOutManager.setPatId("111");
		inOutManager.setDeptCode("111");
		int count = inOutManagerRepository.insertInOutManager(inOutManager);
		Assert.assertEquals(0, count);
	}
	
	@Test
	public void testUpdateInOutManager(){
		InOutManager inOutManager = new InOutManager();
		inOutManager.setId(1);
		int count = inOutManagerRepository.updateInOutManager(inOutManager);
		Assert.assertEquals(0, count);
	}
	
	@Test
	public void testDeleteById(){
		int count = inOutManagerRepository.deleteById(1);
		Assert.assertEquals(0, count);
	}
	
	@Test
	public void testGetOutDics(){
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("dicType", 1);
		List<Map<String, String>> dics = inOutManagerRepository.getOutDics(params);
		Assert.assertNotNull(dics);
	}
}
