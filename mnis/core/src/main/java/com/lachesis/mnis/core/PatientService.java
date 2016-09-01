package com.lachesis.mnis.core;

import java.util.List;

import com.lachesis.mnis.core.patient.entity.Bed;
import com.lachesis.mnis.core.patient.entity.Patient;
import com.lachesis.mnis.core.patient.entity.PatientAllergy;
import com.lachesis.mnis.core.patient.entity.PatientBaseInfo;
import com.lachesis.mnis.core.patient.entity.PatientDiagnosis;
import com.lachesis.mnis.core.patient.entity.PatientPrint;
import com.lachesis.mnis.core.patient.entity.WorkUnitStat;

/***
 * 
 * 病人相关的service
 *
 * @author yuliang.xu
 * @date 2015年6月11日 上午10:03:18 
 *
 */
public interface PatientService {
	
	/**
	 * 根据病人id查询病人信息
	 * @param patId
	 * @return
	 */
	Patient getPatientByPatId(String patId);
	
	/**
	 * 根据病床号查询病人信息
	 * @param bedCode
	 * @return
	 */
	Patient getPatientByDeptCodeAndBedCode(String deptCode, String bedCode);
	
	/**
	 * 获取病区病人列表
	 * @param workUnitType -- 0: 责任护士，1:科室，2:病区 (科室和病区其实就是一个)
	 * @param workUnitCode     工作单元编号，根据Type取不同意义
	 *						   type = 0 : userID
	 *						   type = 1 : 科室CODE
	 *						   type = 2 : 病区CODE
	 * @return
	 */
	List<Patient> getWardPatientList(int workUnitType, String workUnitCode);
	
	/**
	 * 
	 * 获取病区床位列表
	 *
	 * @param wardCode
	 * @return
	 */
	List<Bed> getWardBedList(String wardCode);
	
	/**
	 * 修改患者信息
	 * 
	 * @param patient
	 * @param allergyList 过敏药物列表
	 * @param diagList 诊断列表
	 * @param emplCode 记录护士编号
	 * @return
	 */
	void updatePatientInfo(Patient patient, PatientAllergy[] allergyArray, PatientDiagnosis[] diagArray, String emplCode);

	/**
	 * 根据用户与当前科室查询关注病人与责任护士相关病人，返回病人patId列表
	 * @param userCode
	 * @param deptCode
	 * @return 病人ID列表
	 */
	List<String> getAttention(String userCode, String deptCode);
	
	/**
	 * 添加病人关注
	 * @param userCode 护士ID
	 * @param deptCode 科室
	 * @param bedCode 床号
	 */
	void addAttention(String userCode, String deptCode, String bedCode);
	
	/**
	 * 删除关注
	 * @param userCode
	 * @param deptCode
	 * @param bedNo
	 */
	void delAttention(String userCode, String deptCode, String bedNo);

	/**
	 * 根据部门code或护士id 关注获取病人信息
     * @param userCode
	 * @param depcCode
	 * @return
	 */
	List<String> getPatientByDeptCodeOrUserCode(String userCode, String deptCode);
	
	/**
	 * 根据患者编号获取诊断列表
	 * 
	 * @param patientId
	 * @return
	 */
	List<PatientDiagnosis> queryDiagRecord(String patientId);
	
	/**
	 * 根据患者编号获取过敏列表
	 * 
	 * @param patientId
	 * @return
	 */
	List<PatientAllergy> getPatAllergenByPatId(String patientId);
	
	/**
	 * 根据病人id和药物名称获取过敏信息
	 * @param patId
	 * @param drugName
	 * @return
	 */
	PatientAllergy getAllergyByPatIdAndDrugName(String patId,
			String drugName);
	
//	/**
//	 * 根据病人ID获取过敏信息
//	 * @param patId
//	 * @return
//	 */
//	String getAllergyByPatId(String patId);
//	/**
//	 * 修改患者的过敏信息
//	 * @param patId
//	 * @param allergy
//	 */
//	void updateAllergyToPat(String patId,String allergy);
//	
	void saveAllergyRecord(List<PatientAllergy> patientAllergies);
	/**
	 * 操作打印信息
	 * @param patientPrint
	 * @return
	 */
	int savePatientPrints(List<PatientPrint> patientPrints,boolean isBarcode);

	/**
	 * 查询科室转运病人，包括转入，转出和七天内已出院的病人
	 * @param workUnitType
	 * @param workUnitCode
	 * @return
	 */
	List<Patient> getTransferPatientList(Integer workUnitType,
			String workUnitCode);
	
	/**
	 * 获取部门在院换人总数
	 * @param deptCode
	 * @return
	 */
	int getPatientCount(String deptCode);
	
	/**
	 * 根据deptCode获取患者基本信息
	 * @param deptCode
	 * @return
	 */
	List<PatientBaseInfo> getPatientBaseInfoByDeptCode(String deptCode,String patId);
	
	WorkUnitStat getPatientStatisticByDeptCode(String deptCode);
	/**
	 * 根据住院号获取患者条码
	 * @param deptCode
	 * @param inHospNo
	 * @return
	 */
	List<String> getPatIdsByInHospNo(String deptCode,String inHospNo);
	/**
	 * 根据患者获取患者基本信息
	 * @param patId
	 * @param patName
	 * @param inHospNo
	 * @param deptCode
	 * @return
	 */
	List<PatientBaseInfo> getPatientBaseInfoByPatInfo(String patId,String patName,
			String inHospNo,String deptCode) throws Exception;
}