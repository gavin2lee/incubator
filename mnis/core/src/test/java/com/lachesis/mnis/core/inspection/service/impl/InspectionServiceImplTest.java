package com.lachesis.mnis.core.inspection.service.impl;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.lachesis.mnis.core.InspectionService;
import com.lachesis.mnis.core.SpringTest;

public class InspectionServiceImplTest extends SpringTest {
	
	@Autowired InspectionService inspectionService;

	@Test
	public void testGetInspectionRecord(){
		assertNotNull(inspectionService.getInspectionRecords("", "1"));
	}

}
