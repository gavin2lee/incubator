package com.lachesis.mnisqm.module.remote.event.dao;

import com.lachesis.mnisqm.module.event.domain.RiskTrend;
import java.util.List;

public interface RiskTrendMapper {
    int insert(RiskTrend record);

    List<RiskTrend> selectAll();
}