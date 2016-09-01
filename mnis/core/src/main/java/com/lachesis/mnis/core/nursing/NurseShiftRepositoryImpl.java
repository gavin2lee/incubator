package com.lachesis.mnis.core.nursing;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.lachesis.mnis.core.mybatis.mapper.NurseShiftMapper;

@Repository("nurseShiftRepository")
public class NurseShiftRepositoryImpl implements NurseShiftRepository {

	@Autowired
	NurseShiftMapper nurseShiftMapper;
	
	@Override
	public List<NurseShiftEntity> getNurseShifts(Map<String, Object> paramsMap) {
		return this.nurseShiftMapper.getNurseShifts(paramsMap);
	}
	
	@Override
	public int insertNurseShift(NurseShiftEntity nurseShiftEntity) {
		return this.nurseShiftMapper.insertNurseShift(nurseShiftEntity);
	}
	@Override
	public int updateNurseShift(NurseShiftEntity nurseShiftEntity) {
		return this.nurseShiftMapper.updateNurseShift(nurseShiftEntity);
	}
	
	@Override
	public int deleteNurseShiftById(String nurseShiftId) {
		return this.nurseShiftMapper.deleteNurseShiftById(nurseShiftId);
	}
	@Override
	public NurseShiftRecordEntity getShiftRecordById(String shiftRecordId) {
		return this.nurseShiftMapper.getShiftRecordById(shiftRecordId);
	}
	@Override
	public int insertShiftRecord(NurseShiftRecordEntity nurseShiftRecordEntity) {
		return this.nurseShiftMapper.insertShiftRecord(nurseShiftRecordEntity);
	}
	@Override
	public int updateShiftRecord(NurseShiftRecordEntity nurseShiftRecordEntity) {
		return this.nurseShiftMapper.updateShiftRecord(nurseShiftRecordEntity);
	}
	@Override
	public int deleteShiftRecordById(String shiftRecordId) {
		return this.nurseShiftMapper.deleteShiftRecordById(shiftRecordId);
	}
	@Override
	public List<NurseShiftRecordEntity> getShiftRecordsByNurseShiftId(String nurseShiftId) {
		return this.nurseShiftMapper.getShiftRecordsByNurseShiftId(nurseShiftId);
	}

	@Override
	public NurseShiftEntity getNurseShiftEntityById(String nurseShiftId) {
		return this.nurseShiftMapper.getNurseShiftEntityById(nurseShiftId);
	}
}
