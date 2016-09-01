package com.lachesis.mnisqm.module.remote.event.dao;

import com.lachesis.mnisqm.module.event.domain.NursingMeasures;
import java.util.List;

public interface NursingMeasuresMapper {
    int insert(NursingMeasures record);

    List<NursingMeasures> selectAll();
}