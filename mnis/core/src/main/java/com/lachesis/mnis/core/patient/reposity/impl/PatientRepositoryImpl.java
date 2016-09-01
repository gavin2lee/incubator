package com.lachesis.mnis.core.patient.reposity.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.lachesis.mnis.core.mybatis.mapper.PatientMapper;
import com.lachesis.mnis.core.patient.entity.Bed;
import com.lachesis.mnis.core.patient.entity.Patient;
import com.lachesis.mnis.core.patient.entity.PatientAllergy;
import com.lachesis.mnis.core.patient.entity.PatientBaseInfo;
import com.lachesis.mnis.core.patient.entity.PatientDiagnosis;
import com.lachesis.mnis.core.patient.entity.PatientPrint;
import com.lachesis.mnis.core.patient.entity.WorkUnitStat;
import com.lachesis.mnis.core.patient.reposity.PatientRepository;
import com.lachesis.mnis.core.util.DateUtil;

@Repository("patientRepository")
public class PatientRepositoryImpl implements PatientRepository {
	
	@Autowired
	private PatientMapper patientMapper;

	@Override
	public Patient getPatientByPatId(String patId) {
		return patientMapper.getPatientByPatId(patId);
	}
	
	@Override
	public Patient getPatientByDeptCodeAndBedCode(String deptCode, String bedCode){
		return patientMapper.getPatientByDeptCodeAndBedCode(deptCode, bedCode);
	}
	
	@Override
	public List<Patient> getWardPatientList(int workUnitType, String workUnitCode) {
		return patientMapper.getWardPatientList(workUnitType, workUnitCode);
	}

	@Override
	public List<Bed> getWardBedList(String wardCode) {
		return patientMapper.getWardBedList(wardCode);
	}
	
	@Override
	public int updateInPatientInfo(Patient patient) {
		return patientMapper.updateInPatientInfo(patient);
	}

	@Override
	public List<String> getAttention(String userCode, String deptCode) {
		return patientMapper.getAttention(userCode, deptCode);
	}

	@Override
	public boolean isExistAttention(String userCode, String deptCode, String bedCode) {
		return patientMapper.isExistAttention(userCode, deptCode, bedCode);
	}
	
	@Override
	public void addAttention(String userCode, String deptCode, String bedCode) {
		patientMapper.addAttention(userCode, deptCode, bedCode);
	}

	@Override
	public void delAttention(String userCode, String deptCode, String bedCode) {
		patientMapper.deleteAttention(userCode, deptCode, bedCode);
	}
	
	@Override
	public List<PatientDiagnosis> getDiagRecord(String patientId) {
		return patientMapper.getDiagRecord(patientId);
	}

	@Override
	public List<PatientAllergy> getPatAllergenByPatId(String patientId) {
		return patientMapper.getPatAllergenByPatId(patientId);
	}
	
	@Override
	public PatientAllergy getAllergyByPatIdAndDrugName(String patId,
			String drugName) {
		return patientMapper.getAllergyByPatIdAndDrugName(patId, drugName);
	}

	@Override
	public void saveAllergyRecordList(List<PatientAllergy> patientAllergies) {
		patientMapper.saveAllergyRecordList(patientAllergies);
	}

	@Override
	public void deleteAllergyByPatientId(String patientId) {
		patientMapper.deleteAllergyByPatientId(patientId);
	}

	@Override
	public void deleteDiagByPatientId(String patientId) {
		patientMapper.deleteDiagByPatientId(patientId);
	}

	@Override
	public void saveDiagRecordList(PatientDiagnosis[] diagArray) {
		patientMapper.saveDiagRecordList(diagArray);
	}

//	@Override
//	public String getAllergyByPatId(String patId) {
//		return patientMapper.getAllergyByPatId(patId);
//	}
//
//	@Override
//	public void updateAllergyToPat(String patId, String allergy) {
//		patientMapper.updateAllergyToPat(patId, allergy);
//	}

	@Override
	public int insertPatientPrint(PatientPrint patientPrint) {
		return patientMapper.insertPatientPrint(patientPrint);
	}

	@Override
	public int updatePatientPrintByPrintDataId(PatientPrint patientPrint) {
		return patientMapper.updatePatientPrintByPrintDataId(patientPrint);
	}

	@Override
	public PatientPrint seletePatientPrintByPrintDataId(String printDataId) {
		return patientMapper.seletePatientPrintByPrintDataId(printDataId);
	}

	@Override
	public List<Patient> getTransferPatientList(Integer workUnitType,
			String workUnitCode) {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_YEAR, -7);
		
		return patientMapper.getTransferPatientList(workUnitType, workUnitCode, DateUtil.getDateWithMinTime(c.getTime()));
	}

	@Override
	public int getPatientCount(String deptCode) {
		return patientMapper.getPatientCount(deptCode);
	}

	@Override
	public List<PatientBaseInfo> getPatientBaseInfoByDeptCode(String deptCode,String patId) {
		return patientMapper.getPatientBaseInfoByDeptCode(deptCode,patId);
	}

	@Override
	public WorkUnitStat getPatientStatisticByDeptCode(String deptCode) {
		return patientMapper.getPatientStatisticByDeptCode(deptCode);
	}

	@Override
	public List<PatientDiagnosis> getDiagnosis(String patId) {
		return patientMapper.getDiagnosis(patId);
	}

	@Override
	public List<PatientDiagnosis> getWardDiagnosis(String wardCode) {
		return patientMapper.getWardDiagnosis(wardCode);
	}

	@Override
	public List<String> getPatIdsByInHospNo(String deptCode, String inHospNo) {
		return patientMapper.getPatIdsByInHospNo(deptCode, inHospNo);
	}

	@Override
	public List<PatientBaseInfo> getPatientBaseInfoByPatInfo(String patId,
			String patName, String inHospNo, String deptCode) {
		return patientMapper.getPatientBaseInfoByPatInfo(patId, patName, inHospNo, deptCode);
	}
}