package com.lachesis.mnis.core.nursing;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lachesis.mnis.core.mybatis.mapper.NurseItemMapper;

class GrNurseItemRepositoryImpl implements NurseItemRepository {
	@Autowired
	NurseItemMapper nurseItemMapper;

	@Override
	public List<NurseItemCategory> selectNurseItemCategory(String deptCode) {
		return nurseItemMapper.selectNurseItemCategory(deptCode);
	}

	@Override
	public int insertNurseItemRecord(NurseItemRecord nurseItemRecord) {
		return nurseItemMapper.insertNurseItemRecord(nurseItemRecord);
	}

	@Override
	public List<NurseItemRecord> selectNurseItemRecords(String patientId,
			String startDate, String endDate) {
		return nurseItemMapper.selectNurseItemRecords(patientId, startDate, endDate);
	}
}