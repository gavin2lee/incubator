package com.lachesis.mnisqm.module.remote.qualityForm.dao;

import com.lachesis.mnisqm.module.qualityForm.domain.QualityInfo;
import java.util.List;

public interface QualityInfoMapper {
    int insert(QualityInfo record);

    List<QualityInfo> selectAll();
}