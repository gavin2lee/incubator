package com.lachesis.mnis.core.mybatis.mapper;

import java.util.List;

import com.lachesis.mnis.core.identity.entity.SysModule;


public interface DictModuleMapper {
    int deleteByPrimaryKey(Integer moduleId);

    int insert(SysModule record);

    int insertSelective(SysModule record);

    SysModule selectByPrimaryKey(Integer moduleId);

    int updateByPrimaryKeySelective(SysModule record);

    int updateByPrimaryKey(SysModule record);
    
    List<SysModule> getAllModule();
}