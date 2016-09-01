package com.lachesis.mnis.core.nursing.nurserecord.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.lachesis.mnis.core.SpringTest;
import com.lachesis.mnis.core.bodysign.entity.BodySignDict;
import com.lachesis.mnis.core.bodysign.entity.BodySignItem;
import com.lachesis.mnis.core.nursing.NurseRecord;
import com.lachesis.mnis.core.nursing.NurseRecordService;
import com.lachesis.mnis.core.nursing.NurseRecordSpecItem;
import com.lachesis.mnis.core.util.DateUtil;
import com.lachesis.mnis.core.util.DateUtil.DateFormat;

public class NurseRecordServiceImplTest extends SpringTest {
	@Autowired NurseRecordService nurseService;
	
	private NurseRecord nrRecordTest;
	private String testDay = "2013-03-22";
	@Before 
	public void init() {
		nrRecordTest  = new NurseRecord();
		nrRecordTest.setRecordDate( DateUtil.parse((testDay + " 12:00"),DateFormat.FULL));
		nrRecordTest.setPatientId("111");
		nrRecordTest.setRecordNurseCode("000058");
		nrRecordTest.setCooled(1);
		
		List<BodySignItem> bsItemList = new ArrayList<BodySignItem>();
		BodySignItem bodySignItem = new BodySignItem();
		bodySignItem.setItemValue("60");
		bodySignItem.setBodySignDict(BodySignDict.create("heartRate",null,null) );
		bsItemList.add( bodySignItem );
		nrRecordTest.setBodySignItemList(bsItemList);
				
		List<NurseRecordSpecItem> nrItemList = new ArrayList<NurseRecordSpecItem>();
		NurseRecordSpecItem spcItem = new NurseRecordSpecItem();
		spcItem.setItemCode("bqgcjcs");
		spcItem.setItemValue("病人高热给予退热");
		nrItemList.add(spcItem);
		nrRecordTest.setNurseRecordSpecItemList(nrItemList);
	}
	
	@Test
	public void testSaveNurseRecord() {
		assertEquals(-1, nurseService.saveNurseRecord(null, false));
		NurseRecord nrRecord = new NurseRecord();
		assertEquals(-1, nurseService.saveNurseRecord(nrRecord, false) );
		List<BodySignItem> bsItemList = new ArrayList<BodySignItem>();
		nrRecord.setBodySignItemList(bsItemList);
		assertEquals(-1, nurseService.saveNurseRecord(nrRecord, false) );
		List<NurseRecordSpecItem> nrItemList = new ArrayList<NurseRecordSpecItem>();
		nrRecord.setNurseRecordSpecItemList(nrItemList);
		assertEquals(-1, nurseService.saveNurseRecord(nrRecord, false) );
	
		BodySignItem bodySignItem = new BodySignItem();
		bodySignItem.setItemValue("60");
		bodySignItem.setBodySignDict(BodySignDict.create("heartRate",null,null) );
		bsItemList.add( bodySignItem );
		assertEquals(-1, nurseService.saveNurseRecord(nrRecord, false) ); // 必须填入记录时间、记录人、病人id，否则无效

		assertEquals(true, nurseService.saveNurseRecord(nrRecordTest, false)>=1 );
	}
	
	@Test
	public void testGetNurseRecord() throws Exception {
		assertEquals(true, nurseService.saveNurseRecord(nrRecordTest, false)>=1 ); // 先成功插入数据
		assertNotNull( nurseService.getNurseRecord(null, null));
		assertEquals(true, nurseService.getNurseRecord(null, null).size()==0);
		assertEquals(true, nurseService.getNurseRecord(testDay, nrRecordTest.getPatientId()).size()>=0);
		assertNotNull( nurseService.getNurseRecord(testDay, nrRecordTest.getPatientId()).get(0).getNurseRecordSpecItemList().size()>0);
	}

	@Test
	public void testGetNurseRecordConfig() {
		assertNotNull( nurseService.getNurseRecordConfig(null) );
	}

}
