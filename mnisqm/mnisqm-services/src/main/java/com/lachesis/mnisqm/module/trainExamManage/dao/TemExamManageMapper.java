package com.lachesis.mnisqm.module.trainExamManage.dao;

import com.lachesis.mnisqm.module.trainExamManage.domain.TemExamManage;

import java.util.List;
import java.util.Map;

public interface TemExamManageMapper {
    int deleteByPrimaryKey(Long seqId);

    int insert(TemExamManage record);

    TemExamManage selectByPrimaryKey(Long seqId);

    List<TemExamManage> selectAll();

    int updateByPrimaryKey(TemExamManage record);
    
    List<TemExamManage> queryByTOrDCOrENOrUCOrUN(Map<String, Object> map);
}