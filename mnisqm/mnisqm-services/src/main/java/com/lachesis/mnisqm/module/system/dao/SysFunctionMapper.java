package com.lachesis.mnisqm.module.system.dao;

import com.lachesis.mnisqm.module.system.domain.SysFunction;
import java.util.List;

public interface SysFunctionMapper {
    int insert(SysFunction record);

    List<SysFunction> selectAll();
}