package com.lachesis.mnisqm.module.remote.user.dao;

import com.lachesis.mnisqm.module.user.domain.Allocate;
import java.util.List;

public interface AllocateMapper {

    List<Allocate> queryAllAllocate();
    
    List<Allocate> selectAll();
}