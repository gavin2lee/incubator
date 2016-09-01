package com.lachesis.mnis.core.nursing.nurseitem.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.lachesis.mnis.core.SpringTest;
import com.lachesis.mnis.core.nursing.NurseItem;
import com.lachesis.mnis.core.nursing.NurseItemRecord;
import com.lachesis.mnis.core.nursing.NurseItemService;

public class NurseItemServiceImplTest extends SpringTest {
	@Autowired NurseItemService nurseItemService;
	
	private static NurseItemRecord nurseItemRecordToTest;
	private static String day = "2013-03-30 ";
	private static String patientId = "1";
	private static String nurseCode = "000058";
	
	@BeforeClass
	public static void init() {
		nurseItemRecordToTest = new NurseItemRecord();
		nurseItemRecordToTest.setPatientId(patientId);
		nurseItemRecordToTest.setRecordDate(day +"12:00");
		nurseItemRecordToTest.setRecordNurseCode(nurseCode);
		List<NurseItem> nurseItemList = new ArrayList<NurseItem>();
		NurseItem item = new NurseItem();
		item.setItemCode("kqhl");
		nurseItemList.add(item);
		item = new NurseItem();
		item.setItemCode("picchl");
		nurseItemRecordToTest.setNurseItemList( nurseItemList );	
	}
	
	@Test
	public void testGetNurseItemConfig() {
		assertNotNull( nurseItemService.getNurseItemConfig(null) );
		assertTrue( nurseItemService.getNurseItemConfig(null).size()>0 );
	}

	@Test
	public void testSaveNurseItemRecord() {
		assertEquals(-1, nurseItemService.saveNurseItemRecord(null) );
		NurseItemRecord niRecord = new NurseItemRecord();
		assertEquals(-1, nurseItemService.saveNurseItemRecord(niRecord) );
		niRecord.setPatientId("122");
		niRecord.setRecordDate("2013-03-23 14:31:20");
		niRecord.setRecordNurseCode("000059");
		assertEquals(-1, nurseItemService.saveNurseItemRecord(niRecord) ); // Fail FOR 没有子项目
		List<NurseItem> nurseItemList = new ArrayList<NurseItem>();
		niRecord.setNurseItemList(nurseItemList);
		assertEquals(-1, nurseItemService.saveNurseItemRecord(niRecord) ); // FAIL FOR 空子项目
		
		assertTrue( nurseItemService.saveNurseItemRecord( nurseItemRecordToTest ) >=1 );
	}

	@Test
	public void testGetNurseItemRecords() throws ParseException {
		assertNull( nurseItemService.getNurseItemRecords(null, null) );
		assertTrue( nurseItemService.saveNurseItemRecord( nurseItemRecordToTest ) >=1 );
		assertNotNull( nurseItemService.getNurseItemRecords( patientId, null ) );
		assertEquals( true, nurseItemService.getNurseItemRecords(patientId, null ).size()>0 );
		assertEquals( true, nurseItemService.getNurseItemRecords(patientId, null ).get(0).getNurseItemList().size()>0 );
	}

}
