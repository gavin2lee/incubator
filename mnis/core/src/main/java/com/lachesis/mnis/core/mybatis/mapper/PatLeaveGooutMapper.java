package com.lachesis.mnis.core.mybatis.mapper;

import java.util.List;

import com.lachesis.mnis.core.patientManage.entity.PatLeaveGoout;

public interface PatLeaveGooutMapper {
    int deleteByPrimaryKey(Long seqId);

    int insert(PatLeaveGoout record);

    PatLeaveGoout selectByPrimaryKey(Long seqId);

    List<PatLeaveGoout> selectAll();

    int updateByPrimaryKey(PatLeaveGoout record);
}
