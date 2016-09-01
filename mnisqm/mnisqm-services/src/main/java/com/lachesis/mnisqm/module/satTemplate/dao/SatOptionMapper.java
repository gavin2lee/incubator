package com.lachesis.mnisqm.module.satTemplate.dao;

import com.lachesis.mnisqm.module.satTemplate.domain.SatOption;
import java.util.List;

public interface SatOptionMapper {
    int deleteByPrimaryKey(Long seqId);

    int insert(SatOption record);

    SatOption selectByPrimaryKey(Long seqId);

    List<SatOption> selectAll();

    int updateByPrimaryKey(SatOption record);
}