package com.lachesis.mnisqm.module.qualityForm.dao;

import java.util.List;

import com.lachesis.mnisqm.module.qualityForm.domain.QualityFormDetail;

public interface QualityFormDetailMapper {
    int deleteByPrimaryKey(Long seqId);

    Long insert(QualityFormDetail record);

    QualityFormDetail selectByPrimaryKey(Long seqId);

    List<QualityFormDetail> selectAll();

    int updateByPrimaryKey(QualityFormDetail record);
    
    int updateForDelete(QualityFormDetail record);
}