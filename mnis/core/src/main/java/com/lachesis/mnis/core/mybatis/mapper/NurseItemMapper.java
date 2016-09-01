package com.lachesis.mnis.core.mybatis.mapper;

import java.util.List;

import com.lachesis.mnis.core.nursing.NurseItemCategory;
import com.lachesis.mnis.core.nursing.NurseItemRecord;

public interface NurseItemMapper {
	List<NurseItemCategory> selectNurseItemCategory(String deptCode);

	int insertNurseItemRecord(NurseItemRecord nurseItemRecord);

	List<NurseItemRecord> selectNurseItemRecords(String patientId,
			String startDate, String endDate);
}
