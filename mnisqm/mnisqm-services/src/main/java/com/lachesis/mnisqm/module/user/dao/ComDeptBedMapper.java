package com.lachesis.mnisqm.module.user.dao;

import com.lachesis.mnisqm.module.user.domain.ComDeptBed;
import java.util.List;

public interface ComDeptBedMapper {
    int insert(ComDeptBed record);

    List<ComDeptBed> selectAll();
}