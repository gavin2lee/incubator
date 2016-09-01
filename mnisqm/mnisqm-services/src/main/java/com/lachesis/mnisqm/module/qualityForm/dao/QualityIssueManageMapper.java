package com.lachesis.mnisqm.module.qualityForm.dao;

import com.lachesis.mnisqm.module.qualityForm.domain.QualityIssueManage;

import java.util.List;
import java.util.Map;

public interface QualityIssueManageMapper {
    int deleteByPrimaryKey(Long seqId);

    int insert(QualityIssueManage record);

    QualityIssueManage selectByPrimaryKey(Long seqId);

    List<QualityIssueManage> selectAll();

    int updateByPrimaryKey(QualityIssueManage record);
    
    QualityIssueManage getByresultCode(String resultCode);
    
    List<QualityIssueManage> queryByTimeAndTypeAndDeptCode(Map<String,Object> map);
}