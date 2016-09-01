package com.lachesis.mnisqm.module.remote.user.dao;

import java.util.List;

import com.lachesis.mnisqm.module.user.domain.DeptHumanResourceInfo;

public interface DeptHumanResourceInfoMapper {
    int insert(DeptHumanResourceInfo record);

    List<DeptHumanResourceInfo> queryDeptHumanResourceInfo();
    
    List<DeptHumanResourceInfo> selectAll();
}
