package com.lachesis.mnis.core.bodysign;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.lachesis.mnis.core.bodysign.entity.BodySignDict;
import com.lachesis.mnis.core.bodysign.entity.BodySignItem;
import com.lachesis.mnis.core.bodysign.entity.BodySignItemCodeCount;
import com.lachesis.mnis.core.bodysign.entity.BodySignRecord;
import com.lachesis.mnis.core.bodysign.entity.BodySignItemConfig;
import com.lachesis.mnis.core.identity.entity.UserInformation;
import com.lachesis.mnis.core.mybatis.mapper.BodySignMapper;
import com.lachesis.mnis.core.mybatis.mapper.IdentityMapper;
import com.lachesis.mnis.core.mybatis.mapper.PatientMapper;
import com.lachesis.mnis.core.patient.entity.Patient;
import com.lachesis.mnis.core.patient.entity.PatientEvent;
import com.lachesis.mnis.core.patient.entity.PatientSkinTest;
import com.lachesis.mnis.core.util.GsonUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")

public class BodySignMapperTest extends AbstractJUnit4SpringContextTests {

	@Autowired
	BodySignMapper bodySignMapper;

	@Autowired
	private PatientMapper patientMapper;
	
	@Autowired
	private IdentityMapper identityMapper;
	
	private Patient patient = null;
	private UserInformation nurse = null;
	private BodySignRecord record = null;
	@Before
	public void before(){
		patient = patientMapper.getPatientByPatId("YB0155213_2");
		nurse = identityMapper.getUserByCode("1478");
		
		record = new BodySignRecord();
		record.setPatientId(patient.getPatId());
		record.setPatientName(patient.getName());
		record.setDeptCode(patient.getDeptCode());
		record.setBedCode(patient.getBedCode());
		record.setFullDateTime(new Date());
		record.setRecordNurseCode(nurse.getCode());
		record.setRecordNurseName(nurse.getName());
		record.setRemark("test");
	}
			
	@Test
	public void testBodySignMapper(){
		Assert.assertNotNull(bodySignMapper);
	}
	
	@Test
	public void testGetBodysignDicts() {
		List<BodySignDict> list = bodySignMapper.getBodysignDicts();
		Assert.assertTrue(list.size() > 0);
	}
	

	@Test
	public void testSave() {
		bodySignMapper.save(record);
	}
	
	@Test
	public void testSaveEvent() {
		PatientEvent event = new PatientEvent();
		event.setPatientId(patient.getPatId());
		event.setPatientName(patient.getName());
		event.setRecordDate("09:10");
		event.setChineseEventDate("九时十分");;
		event.setRecorderId(nurse.getCode());
		event.setRecorderName(nurse.getName());
		event.setEventCode("ry");
		event.setEventName("入院");
		
		record.setEvent(event);
		bodySignMapper.save(record);
	}
	
	@Test
	public void testSaveSkinTest() {
		PatientSkinTest test = new PatientSkinTest();
		test.setPatientId(patient.getPatId());
		test.setPatientName(patient.getName());
		test.setTestResult("p");
		test.setExecNurseId(nurse.getCode());
		test.setExecNurseName(nurse.getName());
		test.setExecDate(new Date());
		test.setDrugName("青霉素");

		record.setSkinTestInfo(test);
		bodySignMapper.save(record);
	}
	
	@Test
	public void testSaveSignItem() {
		List<BodySignDict> list = bodySignMapper.getBodysignDicts();
		List<BodySignItem> bodySignItemList = new ArrayList<>();
		BodySignItem item1 = new BodySignItem();
		item1.setBodySignDict(list.get(4));
		item1.setItemValue("12");
		item1.setMeasureNoteCode("moren");
		item1.setMeasureNoteName("默认");
		item1.setSpecMark(true);
		
		BodySignItem item2 = new BodySignItem();
		item2.setBodySignDict(list.get(3));
		item2.setItemValue("112/98");
		item2.setMeasureNoteCode("sz");
		item1.setMeasureNoteName("上肢");
		item2.setSpecMark(true);
		
		bodySignItemList.add(item1);
		bodySignItemList.add(item2);
		record.addToBodySignItems(bodySignItemList);
		bodySignMapper.save(record);
	}
	
	
	@Test
	public void testSaveBodySign() {
		PatientEvent event = new PatientEvent();
		event.setPatientId(patient.getPatId());
		event.setPatientName(patient.getName());
		event.setRecordDate("12:17");
		event.setChineseEventDate("十三时十七分");;
		event.setRecorderId(nurse.getCode());
		event.setRecorderName(nurse.getName());
		event.setEventCode("ss");
		event.setEventName("手术");
		
		record.setEvent(event);
		
		PatientSkinTest test = new PatientSkinTest();
		test.setPatientId(patient.getPatId());
		test.setPatientName(patient.getName());
		test.setTestResult("p");
		test.setExecNurseId(nurse.getCode());
		test.setExecNurseName(nurse.getName());
		test.setExecDate(new Date());
		test.setDrugName("青霉素");

		record.setSkinTestInfo(test);
		List<BodySignDict> list = bodySignMapper.getBodysignDicts();
		List<BodySignItem> bodySignItemList = new ArrayList<>();
		BodySignItem item1 = new BodySignItem();
		item1.setBodySignDict(list.get(4));
		item1.setItemValue("12");
		item1.setMeasureNoteCode("moren");
		item1.setMeasureNoteName("默认");
		item1.setSpecMark(true);
		
		BodySignItem item2 = new BodySignItem();
		item2.setBodySignDict(list.get(3));
		item2.setItemValue("112/98");
		item2.setMeasureNoteCode("sz");
		item1.setMeasureNoteName("上肢");
		item2.setSpecMark(true);
		
		bodySignItemList.add(item1);
		bodySignItemList.add(item2);
		record.addToBodySignItems(bodySignItemList);
		bodySignMapper.save(record);
	}
	
	@Test
	public void testDelete() {
		bodySignMapper.delete(26);
	}

	@Test
	public void testGetBodySignRecords() {
		List<BodySignRecord> list = bodySignMapper.getBodySignRecords(new Date(), new String[]{ patient.getPatId()});
		System.out.println(list.size());
	}

	@Test
	public void testGetEventInfosByPatIdAndEventCode() {
		List<PatientEvent> list = bodySignMapper.getEventInfosByPatIdAndEventCode(patient.getPatId(), "ss",null);
		Assert.assertEquals(list.size(), 1);
	}

	@Test
	public void testGetBodySignRecordById() {
		BodySignRecord record =  bodySignMapper.getBodySignRecordById(27);
		Assert.assertNotNull(record);
		Assert.assertEquals(record.getBodySignItemList().size(), 2);
	}

	@Test
	public void testGetBodySignItemsByTimeAndPatient() {
		List<BodySignItem> list = bodySignMapper.getBodySignItemsByTimeAndPatient(new Date(), patient.getPatId());
		Assert.assertNotNull(list);
	}

	@Test
	public void testGetBodySignItemInDayByItemCode() {
		List<BodySignItemCodeCount> list = bodySignMapper.getBodySignItemInDayByItemCode(new Date(), patient.getPatId());
		Assert.assertNotNull(list);
		System.out.println(list.get(1).getItemCode() + " : " + list.get(1).getTimes());
	}
}
