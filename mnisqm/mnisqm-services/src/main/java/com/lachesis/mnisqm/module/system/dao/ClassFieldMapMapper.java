package com.lachesis.mnisqm.module.system.dao;

import com.lachesis.mnisqm.module.system.domain.ClassFieldMap;
import java.util.List;

public interface ClassFieldMapMapper {
    int deleteByPrimaryKey(Long seqId);

    int insert(ClassFieldMap record);

    ClassFieldMap selectByPrimaryKey(Long seqId);

    List<ClassFieldMap> selectAll();

    int updateByPrimaryKey(ClassFieldMap record);
}