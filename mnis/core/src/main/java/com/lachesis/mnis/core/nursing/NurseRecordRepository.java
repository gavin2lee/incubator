package com.lachesis.mnis.core.nursing;

import java.util.List;


public interface NurseRecordRepository {
	int save(NurseRecord nurseRecord);

	List<NurseRecordSpecItem> selectNurseRecordSpecItems(Object object);

	List<NurseRecordSpecItem> selectNurseRecordTreeItems(Object object);

	int insertNurseRecord(NurseRecordEntity nrEntity);

	List<NurseRecordEntity> selectNurseRecord(String patientId, String string,
			String string2);

	void deleteNurseRecord(String masRecordId);
}
