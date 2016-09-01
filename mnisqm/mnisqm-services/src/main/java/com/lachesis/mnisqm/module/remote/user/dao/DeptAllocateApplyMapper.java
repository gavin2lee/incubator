package com.lachesis.mnisqm.module.remote.user.dao;

import com.lachesis.mnisqm.module.user.domain.DeptAllocateApply;
import java.util.List;

public interface DeptAllocateApplyMapper {
    int insert(DeptAllocateApply record);

    List<DeptAllocateApply> selectAll();
}