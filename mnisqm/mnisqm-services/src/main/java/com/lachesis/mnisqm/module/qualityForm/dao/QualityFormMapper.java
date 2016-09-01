package com.lachesis.mnisqm.module.qualityForm.dao;

import java.util.List;
import java.util.Map;

import com.lachesis.mnisqm.module.qualityForm.domain.QualityForm;

public interface QualityFormMapper {
    int deleteByPrimaryKey(Long seqId);

    int insert(QualityForm record);

    QualityForm selectByPrimaryKey(Long seqId);

    List<QualityForm> selectAll();

    int updateByPrimaryKey(QualityForm record);
    
    int updateForDelete(QualityForm record);
    
    List<QualityForm> queryByDeptCodeAndType(Map<String,Object> map);
    
    QualityForm getByFormCode(String formCode);
}