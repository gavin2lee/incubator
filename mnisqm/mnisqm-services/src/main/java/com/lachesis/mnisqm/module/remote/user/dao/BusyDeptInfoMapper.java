package com.lachesis.mnisqm.module.remote.user.dao;

import com.lachesis.mnisqm.module.user.domain.BusyDeptInfo;
import java.util.List;

public interface BusyDeptInfoMapper {
    int insert(BusyDeptInfo record);

    List<BusyDeptInfo> selectAll();
}