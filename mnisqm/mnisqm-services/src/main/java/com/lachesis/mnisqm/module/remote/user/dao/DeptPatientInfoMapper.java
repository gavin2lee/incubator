package com.lachesis.mnisqm.module.remote.user.dao;

import com.lachesis.mnisqm.module.user.domain.DeptPatientInfo;
import java.util.List;

public interface DeptPatientInfoMapper {
    int insert(DeptPatientInfo record);

    List<DeptPatientInfo> selectAll();
}