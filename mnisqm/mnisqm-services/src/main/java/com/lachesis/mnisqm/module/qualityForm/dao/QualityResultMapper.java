package com.lachesis.mnisqm.module.qualityForm.dao;

import com.lachesis.mnisqm.module.qualityForm.domain.QualityResult;

import java.util.List;
import java.util.Map;

public interface QualityResultMapper {
    int deleteByPrimaryKey(Long seqId);

    int insert(QualityResult record);

    QualityResult selectByPrimaryKey(Long seqId);

    List<QualityResult> selectAll();

    int updateByPrimaryKey(QualityResult record);
    
    QualityResult getByTaskCode(String taskCode);
    
    int updateForDelete(QualityResult record);
    
    List<QualityResult> queryByTimeAndFormNameAndDeptCode(Map<String,Object> map);
}