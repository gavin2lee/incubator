package com.lachesis.mnisqm.module.qualityForm.dao;

import java.util.List;
import java.util.Map;

import com.lachesis.mnisqm.module.qualityForm.domain.QualityTask;

public interface QualityTaskMapper {
    int deleteByPrimaryKey(Long seqId);

    int insert(QualityTask record);

    QualityTask selectByPrimaryKey(Long seqId);

    List<QualityTask> selectAll();

    int updateByPrimaryKey(QualityTask record);
    
    int updateForDelete(QualityTask record);
    
    List<QualityTask> queryByTimeAndTypeAndDeptCode(Map<String,Object> map);
    
    List<QualityTask> queryByHandUserName(String handUserName);
    
    /**
     * 通过计划id查询计划内的任务
     * @param planId
     * @return
     */
    List<QualityTask> queryByPlanId(Long planId);
}