package com.lachesis.mnis.core.patient;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lachesis.mnis.core.IdentityService;
import com.lachesis.mnis.core.PatientService;
import com.lachesis.mnis.core.exception.AlertException;
import com.lachesis.mnis.core.exception.MnisException;
import com.lachesis.mnis.core.patient.entity.Bed;
import com.lachesis.mnis.core.patient.entity.Patient;
import com.lachesis.mnis.core.patient.entity.PatientAllergy;
import com.lachesis.mnis.core.patient.entity.PatientBaseInfo;
import com.lachesis.mnis.core.patient.entity.PatientDiagnosis;
import com.lachesis.mnis.core.patient.entity.PatientPrint;
import com.lachesis.mnis.core.patient.entity.WorkUnitStat;
import com.lachesis.mnis.core.patient.reposity.PatientRepository;

@Service("patientService")
public class PatientServiceImpl implements PatientService{
	
	private static Logger LOGGER = LoggerFactory.getLogger(PatientServiceImpl.class);
	
	@Autowired
	private PatientRepository patientRepository;
	@Autowired
	private IdentityService identityService;
	
	@Override
	public Patient getPatientByPatId(String patId) {
		if(StringUtils.isBlank(patId)){
			return null;
		}
		Patient patient = patientRepository.getPatientByPatId(patId);
		//设置过敏信息
		StringBuffer drugBuffer = new StringBuffer();
		if( null != patient && null!= patient.getPatientAllergies()){
			for (int i=0; i<patient.getPatientAllergies().size(); i++) {
				if(StringUtils.isNotBlank(patient.getPatientAllergies().get(i).getDrugName())){
					if( i == 0){
						drugBuffer.append(patient.getPatientAllergies().get(i).getDrugName());
					}else{
						drugBuffer.append(",").append(patient.getPatientAllergies().get(i).getDrugName());
					}
				}
			}
			patient.setAllergen(drugBuffer.toString());
		}
		
		/*if(""SuperCacheUtil.getSYSTEM_CONFIGS().get("isSyncDocrReport")){
			
		}*/
		//取入院诊断、主诉
		
		if(identityService.isSyncDocReport()){
			List<PatientDiagnosis> diaList = patientRepository.getDiagnosis(patId);
			if(diaList!=null
					&&diaList.size()>0
					&& patient!=null){
				for (PatientDiagnosis diagnosis : diaList) {
					if("01".equals(diagnosis.getDataType())){	//入院诊断
						patient.setInDiag(diagnosis.getInfo());
					}else if("02".equals(diagnosis.getDataType())){	  //主诉
						patient.setAppeal(diagnosis.getInfo());
					}
				}
			}
		}
		
		return patient;
	}
	
	
	@Override
	public Patient getPatientByDeptCodeAndBedCode(String deptCode, String bedCode) {
		if(StringUtils.isBlank(deptCode) || StringUtils.isBlank(bedCode)){
			return null;
		}
		
		return patientRepository.getPatientByDeptCodeAndBedCode(deptCode, bedCode);	 
	}
	
	@Override
	public List<Patient> getWardPatientList(int workUnitType, String workUnitCode) {
		if(StringUtils.isBlank(workUnitCode)){
			return null;
		}
		
		List<Patient> patList = patientRepository.getWardPatientList(workUnitType, workUnitCode);
		
		
		if(identityService.isSyncDocReport()){//文书
			if(patList!=null
					&& patList.size()>0){
				
				//取诊断数据
				List<PatientDiagnosis> diaList = patientRepository.getWardDiagnosis(workUnitCode);
				if(diaList!=null
						&& diaList.size()>0){
					boolean zd = false;
					boolean zs = false;
					for (Patient patient : patList) {
						zd = false;
						zs = false;
						for (PatientDiagnosis patientDiagnosis : diaList) {
							if(patient.getPatId()!=null
									&& patient.getPatId().equals(patientDiagnosis.getPatId())){
								if("01".equals(patientDiagnosis.getDataType())){  //入院诊断
									patient.setInDiag(patientDiagnosis.getInfo());
								}else if("02".equals(patientDiagnosis.getDataType())){
									patient.setAppeal(patientDiagnosis.getInfo());
								}
							}
							
							if(zd&&zs){
								break;
							}
						}
					}
				}
				
			}
		}
		
		return patList;
	}
	
	@Override
	public List<Bed> getWardBedList(String wardCode){
		return patientRepository.getWardBedList(wardCode);
	}
	
	@Override
	@Transactional
	public void updatePatientInfo(Patient patient, PatientAllergy[] allergyArray, PatientDiagnosis[] diagArray, String emplCode){
		
		patientRepository.updateInPatientInfo(patient);
		
		patientRepository.deleteAllergyByPatientId(patient.getPatId());
		patientRepository.deleteDiagByPatientId(patient.getPatId());
		
		Date recordDate = new Date();
		List<PatientAllergy> patientAllergies = new ArrayList<PatientAllergy>();
		if ( allergyArray != null && allergyArray.length > 0) {
			for( PatientAllergy item : allergyArray) {
				item.setAllergyDate(recordDate);
				item.setPatientId(patient.getPatId());
//				item.setRecordNurseCode(emplCode);
				patientAllergies.add(item);
			}
			patientRepository.saveAllergyRecordList(patientAllergies);
		}
		
		if ( diagArray != null && diagArray.length > 0) {
			for( PatientDiagnosis item : diagArray) {
				item.setDate(recordDate);
				item.setRecordDate(recordDate);
				item.setPatId(patient.getPatId());
				item.setRecordUser(emplCode);
			}
			patientRepository.saveDiagRecordList(diagArray);
		}
	}
	
	@Override
	public List<String> getAttention(String userCode, String deptCode) {
		return patientRepository.getAttention(userCode, deptCode);
	}
	
	@Override
	@Transactional
	public void addAttention(String userCode, String deptCode, String bedCode) {
		if (!patientRepository.isExistAttention(userCode, deptCode, bedCode)) {
			patientRepository.addAttention(userCode, deptCode, bedCode);
		}
	}

	@Override
	@Transactional
	public void delAttention(String userCode, String deptCode, String bedCode) {
		patientRepository.delAttention(userCode, deptCode, bedCode);
	}

	@Override
	public List<String> getPatientByDeptCodeOrUserCode(String userCode, String deptCode) {
		List<String> result = null;
		List<Patient> list = null;
		if(StringUtils.isNotBlank(userCode)){
			list = patientRepository.getWardPatientList(0, userCode);
		}else{
			list = patientRepository.getWardPatientList(2, deptCode);
		}
		
		if(list != null &&  list.size() > 0){
			result = new ArrayList<String>(list.size());
			for(Patient p : list){
				result.add(p.getPatId());
			}
		}
		
		return result;
	}
	
	@Override
	public List<PatientDiagnosis> queryDiagRecord(String patientId) {
		return patientRepository.getDiagRecord(patientId);
	}

	@Override
	public List<PatientAllergy> getPatAllergenByPatId(String patientId) {
		return patientRepository.getPatAllergenByPatId(patientId);
	}
	
	@Override
	public PatientAllergy getAllergyByPatIdAndDrugName(String patId,
			String drugName) {
		return patientRepository.getAllergyByPatIdAndDrugName(patId, drugName);
	}


//	@Override
//	public String getAllergyByPatId(String patId) {
//		return patientRepository.getAllergyByPatId(patId);
//	}
//
//
//	@Override
//	public void updateAllergyToPat(String patId, String allergy) {
//		patientRepository.updateAllergyToPat(patId, allergy);
//	}
//
//
	@Override
	public void saveAllergyRecord(List<PatientAllergy> patientAllergies ) {
		patientRepository.saveAllergyRecordList(patientAllergies);
	}


	@Override
	public int savePatientPrints(List<PatientPrint> patientPrints,boolean isBarcode) {
		if(null == patientPrints){
			throw new MnisException("打印信息信息出错!");
		}
		
		int count = 0;
		PatientPrint existPatientPrint = null;
		for (PatientPrint patientPrint : patientPrints) {
			if(StringUtils.isEmpty(patientPrint.getPrintDataId())){
				continue;
			}
			
			existPatientPrint =  patientRepository.seletePatientPrintByPrintDataId(patientPrint.getPrintDataId());
			if(null == existPatientPrint){
				//新增
				if(null == patientPrint.getPrintDate()){
					patientPrint.setPrintDate(new Date());
				}
				count = patientRepository.insertPatientPrint(patientPrint);
			}else{
				//修改
				if(isBarcode){
					patientPrint.setPrintBarcode(true);
					patientPrint.setPrintBed(existPatientPrint.isPrintBed());
				}else{
					patientPrint.setPrintBarcode(existPatientPrint.isPrintBarcode());
					patientPrint.setPrintBed(true);
				}
				count = patientRepository.updatePatientPrintByPrintDataId(patientPrint);
			}
		}
		
		
		return count;
	}


	@Override
	public List<Patient> getTransferPatientList(Integer workUnitType,
			String workUnitCode) {
		if(StringUtils.isBlank(workUnitCode)){
			return null;
		}
		return patientRepository.getTransferPatientList(workUnitType, workUnitCode);
	}


	@Override
	public int getPatientCount(String deptCode) {
		if(StringUtils.isBlank(deptCode)){
			return 0;
		}
		return patientRepository.getPatientCount(deptCode);
	}


	@Override
	public List<PatientBaseInfo> getPatientBaseInfoByDeptCode(String deptCode,String patId) {
		if(StringUtils.isBlank(deptCode)){
			return null;
		}
		return patientRepository.getPatientBaseInfoByDeptCode(deptCode,patId);
	}


	@Override
	public WorkUnitStat getPatientStatisticByDeptCode(String deptCode) {
		if(StringUtils.isBlank(deptCode)){
			return null;
		}
		return patientRepository.getPatientStatisticByDeptCode(deptCode);
	}


	@Override
	public List<String> getPatIdsByInHospNo(String deptCode, String inHospNo) {
		return patientRepository.getPatIdsByInHospNo(deptCode, inHospNo);
	}


	@Override
	public List<PatientBaseInfo> getPatientBaseInfoByPatInfo(String patId,
			String patName, String inHospNo, String deptCode) {
		if(StringUtils.isBlank(deptCode)){
			LOGGER.error("PatientServiceImpl getPatientBaseInfoByPatInfo deptCode is null");
			throw new AlertException("科室参数为空!");
		}
		
		List<PatientBaseInfo> patientBaseInfos = patientRepository.getPatientBaseInfoByPatInfo(patId, patName, inHospNo, deptCode);
		return patientBaseInfos;
	}


}
