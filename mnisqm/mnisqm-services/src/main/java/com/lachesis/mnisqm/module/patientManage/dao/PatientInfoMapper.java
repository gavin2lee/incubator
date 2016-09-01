package com.lachesis.mnisqm.module.patientManage.dao;

import com.lachesis.mnisqm.module.patientManage.domain.PatientInfo;
import java.util.List;

public interface PatientInfoMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PatientInfo record);

    PatientInfo selectByPrimaryKey(Long id);

    List<PatientInfo> selectAll();

    int updateByPatId(PatientInfo record);
    
    PatientInfo getByInHospNo(String inHospNo);
}