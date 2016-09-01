package com.lachesis.mnis.core.scan.service.impl;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.lachesis.mnis.core.NurseScanService;
import com.lachesis.mnis.core.SpringTest;
import com.lachesis.mnis.core.scan.entity.NurseScanInfo;

public class NurseScanServiceTest extends SpringTest {

	@Autowired
	private NurseScanService nurseScanService;
	
	@Test
	public void testSaveNurseScan(){
		NurseScanInfo nurseScanInfo = new NurseScanInfo();
		nurseScanInfo.setError("1111");
		nurseScanInfo.setNurseCode("000283");
		nurseScanInfo.setNurseName("dddd");
		nurseScanInfo.setStatus(0);
		nurseScanInfo.setType("ORDER_EXEC");
		nurseScanInfo.setScanDate(new Date());
		int count = 0;
		try {
			count = nurseScanService.saveNurseScan(nurseScanInfo);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Assert.assertSame(1, count);
		
	}
}
