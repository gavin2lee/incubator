package com.lachesis.mnis.core.bodysign;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.lachesis.mnis.core.BodySignService;
import com.lachesis.mnis.core.IdentityService;
import com.lachesis.mnis.core.PatientService;
import com.lachesis.mnis.core.SpringTest;
import com.lachesis.mnis.core.bodysign.entity.BodySignDict;
import com.lachesis.mnis.core.bodysign.entity.BodySignItem;
import com.lachesis.mnis.core.bodysign.entity.BodySignRecord;
import com.lachesis.mnis.core.bodysign.entity.BodySignItemConfig;
import com.lachesis.mnis.core.bodysign.entity.BodyTempSheet;
import com.lachesis.mnis.core.bodysign.repository.BodySignRepository;
import com.lachesis.mnis.core.identity.entity.UserInformation;
import com.lachesis.mnis.core.patient.entity.Patient;
import com.lachesis.mnis.core.patient.entity.PatientEvent;
import com.lachesis.mnis.core.patient.entity.PatientSkinTest;
import com.lachesis.mnis.core.util.GsonUtils;

public class BodySignServiceImplTest extends SpringTest {

	@Autowired
	BodySignService bodySignService;
	
	@Autowired
	BodySignRepository bodySignRepository;

	@Autowired
	IdentityService identityService;
	
	@Autowired
	PatientService patientService;
	
	@Test
	public void testBodySignService() {
		Assert.assertNotNull(bodySignService);
		Map<String, BodySignDict> map = BodySignService.BODY_SIGN_DICT_MAP;
		Assert.assertNotNull(map);
	}

	@Test
	public void testGetBodySignRecord() {
		bodySignService.getBodySignRecord(new Date(), "YB0155213_2",false);
	}

	@Test
	public void testSaveBodySignRecord() {
		Patient patient = patientService.getPatientByPatId("YB0155213_2");
		UserInformation nurse = identityService.queryUserByCode("1478");
		
		BodySignRecord record = new BodySignRecord();
		record.setPatientId(patient.getPatId());
		record.setPatientName(patient.getName());
		record.setDeptCode(patient.getDeptCode());
		record.setBedCode(patient.getBedCode());
		record.setRecordDay("2015-06-18");
		record.setRecordTime("12:00:00");
		record.setRecordNurseCode(nurse.getCode());
		record.setRecordNurseName(nurse.getName());
		record.setRemark("test");
		
		PatientEvent event = new PatientEvent();
		event.setPatientId(patient.getPatId());
		event.setPatientName(patient.getName());
		event.setRecordDate("2015-06-18 17:46:01");
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
		List<BodySignDict> list = bodySignService.getBodySignDicts();
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
		
		bodySignService.saveBodySignRecord(record,false);
	}

	@Test
	public void testGetBodyTempSheet() {
		Patient patient = patientService.getPatientByPatId("YB0155213_2");
		BodyTempSheet sheet = bodySignService.getBodyTempSheet(new Date(), patient, 0);
		Assert.assertNotNull(sheet);
	}

	@Test
	public void testGetBodySignConfigByDeptCode() {
		List<BodySignItemConfig> list = bodySignRepository.getBodySignConfigByDeptCode(null);
		String jsonString = GsonUtils.toJson(list);
		System.err.println(jsonString);
		Assert.assertNotNull(list);
	}
}
