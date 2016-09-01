package com.lachesis.mnis.core.sysDic.entity.repository;

import java.util.List;

import com.lachesis.mnis.core.sysDic.entity.SysDic;


public interface SysDicRepository {
	
	int save(SysDic record);

    List<SysDic> queryAll();
    
    List<String> queryDicTypes();
	
}
