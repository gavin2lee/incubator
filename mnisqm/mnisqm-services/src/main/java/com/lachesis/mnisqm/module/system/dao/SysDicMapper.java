package com.lachesis.mnisqm.module.system.dao;

import com.lachesis.mnisqm.module.system.domain.SysDic;
import java.util.List;

public interface SysDicMapper {
    int insert(SysDic record);

    List<SysDic> selectAll();
}