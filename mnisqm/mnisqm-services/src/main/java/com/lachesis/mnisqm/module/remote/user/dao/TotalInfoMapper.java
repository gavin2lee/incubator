package com.lachesis.mnisqm.module.remote.user.dao;

import java.util.List;

import com.lachesis.mnisqm.module.user.domain.TotalInfo;

public interface TotalInfoMapper {
    int insert(TotalInfo record);

    List<TotalInfo> selectAll();
    
    TotalInfo selectMainInfo();
}