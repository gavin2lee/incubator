package com.lachesis.mnis.core.patient.reposity;

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
 * 病人相关的数据访问层
 *
 * @author yuliang.xu
 * @date 2015年6月11日 上午10:58:06 
 *
 */
public interface PatientRepository {

	/**
	 * 根据病人id查询病人信息
	 * @param patId
	 * @return
	 */
	Patient getPatientByPatId(String patId);
	
	/**
	 * 根据病人床号查询病人信息
	 * @param bedCode
	 * @return
	 */
	Patient getPatientByDeptCodeAndBedCode(String deptCode, String bedCode);
	
	/**
	 * 返回病床列表
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
	
	/***
	 * 
	 * 更新病人信息
	 *
	 * @param patient
	 * @return
	 */
	int updateInPatientInfo(Patient patient);
	
	
	/**
	 * 根据用户与当前科室查询关注病人，返回病人ID列表
	 * @param userCode
	 * @param deptCode
	 * @return 病人ID列表
	 */
	List<String> getAttention(String userCode, String deptCode);
	
	/**
	 * 查看某个床号关注情况
	 * @param userCode 护士ID
	 * @param deptCode 科室
	 * @param bedCode 床号
	 */
	boolean isExistAttention(String userCode, String deptCode, String bedCode);
	
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
	 * @param bedCode
	 */
	void delAttention(String userCode, String deptCode, String bedCode);
	
	/**
	 * 查询诊断记录
	 * @param patientId
	 * @return
	 */
	List<PatientDiagnosis> getDiagRecord(String patientId);
	
	/***
	 * 查询过敏信息
	 * @param patientId
	 * @return
	 */
	List<PatientAllergy> getPatAllergenByPatId(String patientId);
	
	/**
	 * 根据患者id和药物名称获取过敏信息
	 * @param patId
	 * @param drugName
	 * @return
	 */
	PatientAllergy getAllergyByPatIdAndDrugName(String patId,String drugName);

	/**
	 * 保存过敏信息
	 * @param allergyArray
	 */
	void saveAllergyRecordList(List<PatientAllergy> patientAllergies);

	/**
	 * 删除过敏信息
	 * @param patientId
	 */
	void deleteAllergyByPatientId(String patientId);
	
	/***
	 * 删除诊断信息
	 * @param patientId
	 */
	void deleteDiagByPatientId(String patientId);

	/***
	 * 保存诊断信息
	 * @param diagArray
	 */
	void saveDiagRecordList(PatientDiagnosis[] diagArray);
/*	*//**
	 * 根据病人ID获取过敏信息
	 * @param patId
	 * @return
	 *//*
	String getAllergyByPatId(String patId);
	*//**
	 * 修改患者的过敏信息
	 * @param patId
	 * @param allergy
	 *//*
	void updateAllergyToPat(String patId,String allergy);*/
	/**
	 * 插入打印信息
	 * @param patientPrint
	 * @return
	 */
	int insertPatientPrint(PatientPrint patientPrint);
	/**
	 * 根据printDataId修改信息
	 * @param patientPrint
	 * @return
	 */
	int updatePatientPrintByPrintDataId(PatientPrint patientPrint);
	/**
	 * 根据printDataId的个数
	 * @param printDataId
	 * @return
	 */
	PatientPrint seletePatientPrintByPrintDataId(String printDataId);

	/**
	 * 查询科室转运病人，包括转入，转出和七天内出院的病人
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
	 * 根据deptcode获取患者基本信息
	 * @param deptCode
	 * @return
	 */
	List<PatientBaseInfo> getPatientBaseInfoByDeptCode(String deptCode,String patId);
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
	List<PatientBaseInfo> getPatientBaseInfoByPatInfo(String patId,String patName,String inHospNo,String deptCode);
}
