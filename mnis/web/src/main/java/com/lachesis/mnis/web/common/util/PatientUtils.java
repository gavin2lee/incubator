package com.lachesis.mnis.web.common.util;

import org.apache.commons.lang.StringUtils;

import com.lachesis.mnis.core.patient.entity.ChargeType;
import com.lachesis.mnis.core.patient.entity.Patient;
import com.lachesis.mnis.core.util.DateUtil;
import com.lachesis.mnis.core.util.DateUtil.DateFormat;
import com.lachesis.mnis.web.vo.BedPatientInfo;
/**
 * 患者信息帮助类
 * @author ThinkPad
 *
 */
public class PatientUtils {
	/**
	 * 患者实体转换
	 * @param source
	 * @param currentMinDate
	 * @param currentMaxDate
	 * @return
	 */
	public static BedPatientInfo copyBedPatientInfoProperties(
			Patient source, String currentMinDate, String currentMaxDate){
		
		BedPatientInfo dest = new BedPatientInfo();
		if(null == source){
			return dest;
		}
		dest.setBedCode(source.getBedCode());
		dest.setShowBedCode(source.getBedCode());
		dest.setInHospitalNo(source.getInHospNo());
		dest.setInpNo(source.getInpNo());
		dest.setChargeType(source.getChargeType());
		dest.setTendLevel(source.getTendLevel());
		dest.setDutyNurseCode(source.getDutyNurseCode());
		dest.setDutyNurseName(source.getDutyNurseName());
		dest.setDoctor(source.getDoctorName());
		dest.setDiet(source.getDiet());
		dest.setBarcode(source.getBarCode());
		/**
		 * 设置过敏信息
		 */
		StringBuffer drugBuffer = new StringBuffer();
		if( null == source.getPatientAllergies()){
			drugBuffer.append(source.getAllergen());
		}else{
			for (int i=0; i<source.getPatientAllergies().size(); i++) {
				if(StringUtils.isNotBlank(source.getPatientAllergies().get(i).getDrugName())){
					if( i == 0){
						drugBuffer.append(source.getPatientAllergies().get(i).getDrugName());
					}else{
						drugBuffer.append(",").append(source.getPatientAllergies().get(i).getDrugName());
					}
				}
			}
		}
		dest.setAllergyDrugs(drugBuffer.toString());
		dest.setAdmitDiagnosis(source.getInDiag());
		
		String admitDate = DateUtil.format(source.getInDate(), DateFormat.YMD);
		dest.setAdmitDate(admitDate);
		if (admitDate != null && admitDate.trim().length() > 0
				&& admitDate.compareTo(currentMinDate) >= 0
				&& currentMaxDate.compareTo(admitDate) >= 0) {
			dest.setNewest(true);
		}
		
		dest.setDebit((source.getPrepayCost() - source.getOwnCost()) < 0? true:false);
		dest.setHasExecOrder(source.getUnexecutedOrderCount() > 0 ? true
				: false);
		dest.setSensitive(source.getAllergen() != null && source.getAllergen().length() > 0 ? true
				: false);

		dest.setPatientId(source.getPatId());
		dest.setPatientName(source.getName());
		dest.setDeptCode(source.getDeptCode());
		dest.setDeptName(source.getDeptName());
		dest.setAge(source.getAge());
		dest.setAgeDuration(source.getAgeDuration());
		dest.setGender(source.getGender());
		dest.setSex(dest.getGender());
		dest.setChargeType(dest.getChargeType());
		dest.setChargeNature(ChargeType.getDisplay(dest.getChargeType()));
		
		dest.setSeparate(source.isSeparate());
		dest.setOperation(source.isOperation());
		dest.setHot(source.isHot());
		dest.setFall(source.isFall());
		dest.setPressure(source.isPressure());
		
		dest.setPrintBarcode(source.isPrintBarcode());
		dest.setPrintBed(source.isPrintBed());
		return dest;
	}
}
