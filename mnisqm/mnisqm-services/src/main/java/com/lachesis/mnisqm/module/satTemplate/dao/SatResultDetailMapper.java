package com.lachesis.mnisqm.module.satTemplate.dao;

import com.lachesis.mnisqm.module.satTemplate.domain.SatResultDetail;
import java.util.List;

public interface SatResultDetailMapper {
    int deleteByPrimaryKey(Long seqId);

    int insert(SatResultDetail record);

    SatResultDetail selectByPrimaryKey(Long seqId);

    List<SatResultDetail> selectAll();

    int updateByPrimaryKey(SatResultDetail record);
}