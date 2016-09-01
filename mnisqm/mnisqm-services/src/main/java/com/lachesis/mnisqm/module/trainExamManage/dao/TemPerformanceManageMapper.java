package com.lachesis.mnisqm.module.trainExamManage.dao;

import com.lachesis.mnisqm.module.trainExamManage.domain.TemPerformanceManage;
import java.util.List;

public interface TemPerformanceManageMapper {
    int deleteByPrimaryKey(Long seqId);

    int insert(TemPerformanceManage record);

    TemPerformanceManage selectByPrimaryKey(Long seqId);

    List<TemPerformanceManage> selectAll();

    int updateByPrimaryKey(TemPerformanceManage record);
}