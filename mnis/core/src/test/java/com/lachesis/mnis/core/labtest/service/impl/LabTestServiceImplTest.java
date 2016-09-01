package com.lachesis.mnis.core.labtest.service.impl;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.lachesis.mnis.core.LabTestService;
import com.lachesis.mnis.core.SpringTest;

public class LabTestServiceImplTest extends SpringTest {
	@Autowired LabTestService labTestService;
	
	@Test
	public void testGetLabTestRecord(){
		//assertNull(labTestService.getLabTestRecord("", "").getMidException().getSysErr());
		System.out.println("system.out" + labTestService.getLabTestRecord("", "1",null,null));
	}
}
