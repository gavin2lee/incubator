package com.lachesis.mnis.core.mybatis.mapper;

import java.util.List;

import com.lachesis.mnis.core.patientManage.entity.PatOrderConfiguration;

public interface PatOrderConfigurationMapper {
    int deleteByPrimaryKey(Long seqId);

    int insert(PatOrderConfiguration record);

    PatOrderConfiguration selectByPrimaryKey(Long seqId);

    List<PatOrderConfiguration> selectAll();

    int updateByPrimaryKey(PatOrderConfiguration record);
}
