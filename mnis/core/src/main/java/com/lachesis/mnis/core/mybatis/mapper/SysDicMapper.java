package com.lachesis.mnis.core.mybatis.mapper;

import java.util.List;

import com.lachesis.mnis.core.sysDic.entity.SysDic;

public interface SysDicMapper {

	int insert(SysDic record);

    List<SysDic> selectAll();
    
    List<String> queryDicTypes();
	
}
