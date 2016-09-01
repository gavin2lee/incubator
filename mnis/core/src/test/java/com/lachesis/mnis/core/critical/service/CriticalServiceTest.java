package com.lachesis.mnis.core.critical.service;

import com.lachesis.mnis.core.CriticalValueService;
import com.lachesis.mnis.core.SpringTest;
import com.lachesis.mnis.core.critical.entity.CriticalValueRecord;
import com.lachesis.mnis.core.util.GsonUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertNotNull;

public class CriticalServiceTest extends SpringTest {
	@Autowired
	private CriticalValueService criticalValueService;
	
	@Test
	public void testPropertySet() {
		assertNotNull(criticalValueService);
	}
	
	@Test
	public void testGetCriticalValueRecord(){
		List<CriticalValueRecord> records = null;
		try {
			records = criticalValueService.getCriticalValueRecord("5042",
					null, "2016-04-23", "2016-05-05", true);
			String gson = GsonUtils.toJson(records);
			System.out.println(gson);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Assert.assertNotNull(records);
	}

}
