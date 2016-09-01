package com.lachesis.mnis.core.mybatis.mapper;

import java.util.List;
import java.util.Map;

import com.lachesis.mnis.core.patientManage.entity.PatOperationInfo;

public interface PatOperationInfoMapper {
    int deleteByPrimaryKey(Long seqId);

    int insert(PatOperationInfo record);

    PatOperationInfo selectByPrimaryKey(Long seqId);

    List<PatOperationInfo> selectAll();

    int updateByPrimaryKey(PatOperationInfo record);
    
    List<PatOperationInfo> getByPatId(Map<String, Object> conditionMap);
    
    List<PatOperationInfo> queryByStatusOrDate(Map<String, Object> conditionMap);
}