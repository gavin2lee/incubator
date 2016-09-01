package com.lachesis.mnis.core.nursing;

import java.util.List;

import org.springframework.stereotype.Repository;


@Repository("nurseItemRepository")
public class NurseItemRepositoryImpl implements NurseItemRepository {

	@Override
	public List<NurseItemCategory> selectNurseItemCategory(String deptCode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int insertNurseItemRecord(NurseItemRecord nurseItemRecord) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<NurseItemRecord> selectNurseItemRecords(String patientId,
			String string, String string2) {
		// TODO Auto-generated method stub
		return null;
	}

}
