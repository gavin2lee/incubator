package com.lachesis.mnisqm.module.remote.user.dao;

import com.lachesis.mnisqm.module.user.domain.DeptSchedulInfo;

import java.util.List;
import java.util.Map;

public interface DeptSchedulInfoMapper {
    int insert(DeptSchedulInfo record);

    List<DeptSchedulInfo> queryDeptSchedulInfo(Map<String, Object> conditionMap);
    
    List<DeptSchedulInfo> selectAll();
}