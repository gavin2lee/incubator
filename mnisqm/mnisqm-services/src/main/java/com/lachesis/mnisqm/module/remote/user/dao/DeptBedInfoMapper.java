package com.lachesis.mnisqm.module.remote.user.dao;

import java.util.List;

import com.lachesis.mnisqm.module.user.domain.DeptBedInfo;

public interface DeptBedInfoMapper {

    List<DeptBedInfo> queryDeptManageBedInfo(String deptCode);
    
    List<String> queryDutyNames(String deptCode);
    
    List<DeptBedInfo> selectAll();
}