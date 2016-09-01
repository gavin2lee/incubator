package com.lachesis.mnis.core.nursing;

import java.util.List;


public interface NurseItemRepository {

	List<NurseItemCategory> selectNurseItemCategory(String deptCode);

	int insertNurseItemRecord(NurseItemRecord nurseItemRecord);

	List<NurseItemRecord> selectNurseItemRecords(String patientId, String startDate, String endDate);
}
