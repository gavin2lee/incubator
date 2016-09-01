package com.lachesis.mnis.core.mybatis.mapper;

import java.util.List;
import java.util.Map;

import com.lachesis.mnis.core.identity.entity.RolePermission;
import com.lachesis.mnis.core.identity.entity.SysRole;

public interface RoleMapper {
    int deleteByPrimaryKey(Integer roleId);

    int insert(SysRole record);
    
    int updateByPrimaryKey(SysRole record);

    int insertSelective(SysRole record);

    SysRole selectByPrimaryKey(Integer roleId);

    int updateByPrimaryKeySelective(SysRole record);
    
    int insertUserRole(Map<String,Object> mapOfParam);
    
    int deleteUserRole(Map<String,Object> mapOfParam);
    
    List<SysRole> getUserRoles(int userId);
    
    List<SysRole> getAllRole();
    
    int insertRolePermission(Map<String,Object> mapOfParam);
    
    int deleteRolePermission(Map<String,Object> mapOfParam);
    
    RolePermission getRolePermissions(int roleId);
}