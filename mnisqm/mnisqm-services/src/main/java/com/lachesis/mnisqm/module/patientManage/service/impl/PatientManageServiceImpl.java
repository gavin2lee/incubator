package com.lachesis.mnisqm.module.patientManage.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lachesis.mnisqm.module.patientManage.dao.BedInfoMapper;
import com.lachesis.mnisqm.module.patientManage.dao.PatientInfoMapper;
import com.lachesis.mnisqm.module.patientManage.domain.BedInfo;
import com.lachesis.mnisqm.module.patientManage.domain.PatientInfo;
import com.lachesis.mnisqm.module.patientManage.service.IPatientManageService;

@Service
public class PatientManageServiceImpl implements IPatientManageService {

	@Autowired
	private PatientInfoMapper patientInfoMapper;
	
	@Autowired
	private BedInfoMapper bedInfoMapper;
	
	@Override
	public int savePatientInfo(PatientInfo patientInfo) {
		return patientInfoMapper.insert(patientInfo);
	}

	@Override
	public int saveBedInfo(BedInfo bedInfo) {
		return bedInfoMapper.insert(bedInfo);
	}

	@Override
	public PatientInfo getPatientInfoByInHospNo(String inHospNo) {
		return patientInfoMapper.getByInHospNo(inHospNo);
	}

	@Override
	public int updatePatientInfo(PatientInfo record) {
		return patientInfoMapper.updateByPatId(record);
	}

	@Override
	public int updateBedInfo(BedInfo record) {
		return bedInfoMapper.updateByCode(record);
	}

	@Override
	public BedInfo getBedInfoByCode(String code) {
		return bedInfoMapper.getByCode(code);
	}

}
