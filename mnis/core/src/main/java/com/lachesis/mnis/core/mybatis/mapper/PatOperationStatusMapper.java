package com.lachesis.mnis.core.mybatis.mapper;

import java.util.List;

import com.lachesis.mnis.core.patientManage.entity.PatOperationStatus;

public interface PatOperationStatusMapper {
    int deleteByPrimaryKey(Long seqId);

    int insert(PatOperationStatus record);

    PatOperationStatus selectByPrimaryKey(Long seqId);
    
    PatOperationStatus getRecentlyRecordByPatId(String patId);

    List<PatOperationStatus> selectAll();

    int updateByPrimaryKey(PatOperationStatus record);
}
