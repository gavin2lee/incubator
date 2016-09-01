package com.lachesis.mnisqm.module.remote.user.dao;

import com.lachesis.mnisqm.module.user.domain.SeniorSearch;

import java.util.List;
import java.util.Map;

public interface SeniorSearchMapper {

    List<SeniorSearch> selectAll();
    
    List<SeniorSearch> queryAllSeniorSearch(Map<String, String> conditionMap);
}