package com.lachesis.mnis.core.mybatis.mapper;

import java.util.List;
import java.util.Map;

import com.lachesis.mnis.core.identity.entity.SysGroup;
import com.lachesis.mnis.core.identity.entity.SysRole;


public interface GroupMapper {
    int deleteByPrimaryKey(Integer groupId);

    int insert(SysGroup record);

    int updateByPrimaryKey(SysGroup record);
    
    int insertSelective(SysGroup record);

    SysGroup selectByPrimaryKey(Integer groupId);

    int updateByPrimaryKeySelective(SysGroup record);

    int insertUserGroup(Map<String,Object> mapOfParam);
    
    int deleteUserGroup(Map<String,Object> mapOfParam);
    
    List<SysGroup> getUserGroups(int userId);
    
    List<SysGroup> getAllGroup();
    
    int insertRoleGroup(Map<String,Object> mapOfParam);
    
    int deleteRoleGroup(Map<String,Object> mapOfParam);
    
    List<SysRole> getRoleGroups(int groupId);
    
}