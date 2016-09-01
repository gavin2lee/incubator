package com.lachesis.mnis.core.nursing;

import java.text.ParseException;
import java.util.List;


public interface NurseItemService {
	List<NurseItemCategory> getNurseItemConfig(String deptCode);
	
	int saveNurseItemRecord(NurseItemRecord nurseItemRecord);
	
	List<NurseItemRecord> getNurseItemRecords(String patientId, String day) throws ParseException;
}
