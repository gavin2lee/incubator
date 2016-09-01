package com.lachesis.mnis.core.mybatis.mapper;

import java.util.List;

import com.lachesis.mnis.core.identity.entity.SysOperate;

public interface DictOperateMapper {
    int deleteByPrimaryKey(String operateCode);

    int insert(SysOperate record);

    int insertSelective(SysOperate record);

    SysOperate selectByPrimaryKey(String operateCode);

    int updateByPrimaryKeySelective(SysOperate record);

    int updateByPrimaryKey(SysOperate record);
    
    List<SysOperate> getAllOperate();
}