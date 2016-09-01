package com.lachesis.mnisqm.module.satTemplate.dao;

import com.lachesis.mnisqm.module.satTemplate.domain.SatTemplateDetail;
import java.util.List;

public interface SatTemplateDetailMapper {
    int deleteByPrimaryKey(Long seqId);

    int insert(SatTemplateDetail record);

    SatTemplateDetail selectByPrimaryKey(Long seqId);

    List<SatTemplateDetail> selectAll();

    int updateByPrimaryKey(SatTemplateDetail record);
}