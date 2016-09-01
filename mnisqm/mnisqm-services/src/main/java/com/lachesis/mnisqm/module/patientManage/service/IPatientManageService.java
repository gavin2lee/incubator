package com.lachesis.mnisqm.module.patientManage.service;

import com.lachesis.mnisqm.module.patientManage.domain.BedInfo;
import com.lachesis.mnisqm.module.patientManage.domain.PatientInfo;

public interface IPatientManageService {

	int savePatientInfo(PatientInfo patientInfo);
	
	int saveBedInfo(BedInfo bedInfo);
	
	PatientInfo getPatientInfoByInHospNo(String inHospNo);
	
	int updatePatientInfo(PatientInfo record);
	
	int updateBedInfo(BedInfo record);
	
	BedInfo getBedInfoByCode(String code);
}
