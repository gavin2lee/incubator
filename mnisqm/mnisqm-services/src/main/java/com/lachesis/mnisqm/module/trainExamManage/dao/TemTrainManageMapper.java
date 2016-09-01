package com.lachesis.mnisqm.module.trainExamManage.dao;

import com.lachesis.mnisqm.module.trainExamManage.domain.TemTrainManage;

import java.util.List;
import java.util.Map;

public interface TemTrainManageMapper {
    int deleteByPrimaryKey(Long seqId);

    int insert(TemTrainManage record);

    TemTrainManage selectByPrimaryKey(Long seqId);

    List<TemTrainManage> selectAll();

    int updateByPrimaryKey(TemTrainManage record);
    
    List<TemTrainManage> queryByTimeOrDeptCodeOrCourseName(Map<String,Object> map);
}