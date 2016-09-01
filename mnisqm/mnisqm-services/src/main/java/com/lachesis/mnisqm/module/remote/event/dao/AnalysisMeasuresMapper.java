package com.lachesis.mnisqm.module.remote.event.dao;

import com.lachesis.mnisqm.module.event.domain.AnalysisMeasures;
import java.util.List;

public interface AnalysisMeasuresMapper {
    int insert(AnalysisMeasures record);

    List<AnalysisMeasures> selectAll();
}