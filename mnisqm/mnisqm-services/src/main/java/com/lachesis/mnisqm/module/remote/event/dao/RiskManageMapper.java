package com.lachesis.mnisqm.module.remote.event.dao;

import com.lachesis.mnisqm.module.event.domain.RiskManage;
import java.util.List;

public interface RiskManageMapper {
    int insert(RiskManage record);

    List<RiskManage> selectAll();
}