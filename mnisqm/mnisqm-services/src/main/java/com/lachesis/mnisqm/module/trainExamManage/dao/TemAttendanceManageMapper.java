package com.lachesis.mnisqm.module.trainExamManage.dao;

import com.lachesis.mnisqm.module.trainExamManage.domain.TemAttendanceManage;
import java.util.List;

public interface TemAttendanceManageMapper {
    int deleteByPrimaryKey(Long seqId);

    int insert(TemAttendanceManage record);

    TemAttendanceManage selectByPrimaryKey(Long seqId);

    List<TemAttendanceManage> selectAll();

    int updateByPrimaryKey(TemAttendanceManage record);
}