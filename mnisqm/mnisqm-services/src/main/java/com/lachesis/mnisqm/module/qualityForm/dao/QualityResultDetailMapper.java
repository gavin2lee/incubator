package com.lachesis.mnisqm.module.qualityForm.dao;

import com.lachesis.mnisqm.module.qualityForm.domain.QualityResultDetail;
import java.util.List;

public interface QualityResultDetailMapper {
    int deleteByPrimaryKey(Long seqId);

    int insert(QualityResultDetail record);

    QualityResultDetail selectByPrimaryKey(Long seqId);

    List<QualityResultDetail> selectAll();

    int updateByPrimaryKey(QualityResultDetail record);
    
    int updateForDelete(QualityResultDetail record);
}