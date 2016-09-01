package com.lachesis.mnis.core.mybatis.mapper;

import java.util.Date;
import java.util.List;

import com.lachesis.mnis.core.patient.entity.Bed;
import com.lachesis.mnis.core.patient.entity.Patient;
import com.lachesis.mnis.core.patient.entity.PatientAllergy;
import com.lachesis.mnis.core.patient.entity.PatientBaseInfo;
import com.lachesis.mnis.core.patient.entity.PatientDiagnosis;
import com.lachesis.mnis.core.patient.entity.PatientPrint;
import com.lachesis.mnis.core.patient.entity.WorkUnitStat;

public interface PatientMapper {
	
	Patient getPatientByPatId(String patId);
	
	Patient getPatientByDeptCodeAndBedCode(String deptCode, String bedCode);
	/**
	 * 根据住院号获取患者条码
	 * @param deptCode
	 * @param inHospNo
	 * @return
	 */
	List<String> getPatIdsByInHospNo(String deptCode,String inHospNo);

	List<Patient> getWardPatientList(int workUnitType, String workUnitCode);
	
	List<Bed> getWardBedList(String wardCode); 

	int updateInPatientInfo(Patient patient);
	
	/*******关注相关********/
	List<String> getAttention(String userCode, String deptCode);

	boolean isExistAttention(String userCode, String deptCode, String bedCode);
	
	void addAttention(String userCode, String deptCode, String bedCode);

	void deleteAttention(String userCode, String deptCode, String bedCode);
	
	/*******诊断相关***********/
	List<PatientDiagnosis> getDiagRecord(String patientId);
	
	void saveDiagRecordList(PatientDiagnosis[] diagArray);
	
	void deleteDiagByPatientId(String patientId);
	
	/********过敏信息相关********/
	List<PatientAllergy> getPatAllergenByPatId(String patientId);
	
	void saveAllergyRecordList(List<PatientAllergy> list);
	
	void deleteAllergyByPatientId(String patientId);
	
	PatientAllergy getAllergyByPatIdAndDrugName(String patId,String drugName);
	//pat_cure_info
//	String getAllergyByPatId(String patId);
//	
//	void updateAllergyToPat(String patId,String allergy);
	/************腕带,床头卡,瓶签打印信息***************/
	int insertPatientPrint(PatientPrint patientPrint);
	
	int updatePatientPrintByPrintDataId(PatientPrint patientPrint);
	
	PatientPrint seletePatientPrintByPrintDataId(String printDataId);

	List<Patient> getTransferPatientList(int workUnitType, String workUnitCode, Date d);
	//获取部门在院换人总数
	int getPatientCount(String deptCode);
	/**
	 * 获取患者基本信息
	 * @param deptCode
	 * @param outDate 出院日期
	 * @return
	 */
	List<PatientBaseInfo> getPatientBaseInfoByDeptCode(String deptCode,String patId);
	
	/**
	 * 根据患者获取患者基本信息
	 * @param patId
	 * @param patName
	 * @param inHospNo
	 * @param deptCode
	 * @return
	 */
	List<PatientBaseInfo> getPatientBaseInfoByPatInfo(String patId,String patName,String inHospNo,String deptCode);
	/**
	 * 患者统计信息
	 * @param deptCode
	 * @return
	 */
	WorkUnitStat getPatientStatisticByDeptCode(String deptCode);
	
	
	/**
	 * 取最新入院診斷，主訴
	 * @param patId
	 * @return
	 */
	List<PatientDiagnosis> getDiagnosis(String patId);
	
	
	/**
	 * 考虑其他医院诊断、主诉没有保存到诊断表中，单独查询诊断，在进行业务处理
	 * @param wardCode  科室编号
	 * @return
	 */
	List<PatientDiagnosis> getWardDiagnosis(String wardCode);
}
