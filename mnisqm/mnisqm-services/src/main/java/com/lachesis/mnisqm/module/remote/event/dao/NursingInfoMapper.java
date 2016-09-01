package com.lachesis.mnisqm.module.remote.event.dao;

import com.lachesis.mnisqm.module.event.domain.NursingInfo;
import java.util.List;

public interface NursingInfoMapper {
    int insert(NursingInfo record);

    List<NursingInfo> selectAll();
}