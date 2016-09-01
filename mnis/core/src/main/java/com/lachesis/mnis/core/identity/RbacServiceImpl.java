package com.lachesis.mnis.core.identity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lachesis.mnis.core.identity.entity.RolePermission;
import com.lachesis.mnis.core.identity.entity.SysGroup;
import com.lachesis.mnis.core.identity.entity.SysModule;
import com.lachesis.mnis.core.identity.entity.SysOperate;
import com.lachesis.mnis.core.identity.entity.SysRole;
import com.lachesis.mnis.core.mybatis.mapper.DictModuleMapper;
import com.lachesis.mnis.core.mybatis.mapper.DictOperateMapper;
import com.lachesis.mnis.core.mybatis.mapper.GroupMapper;
import com.lachesis.mnis.core.mybatis.mapper.RoleMapper;

/**
 *@author xin.chen
 **/
//@Service("rbacService")
public class RbacServiceImpl implements RbacService {
	
	//@Autowired
	private GroupMapper groupMapper;
	
	//@Autowired
	private RoleMapper roleMapper;
	
	//@Autowired
    private DictModuleMapper dictModuleMapper;
	
	//@Autowired
    private DictOperateMapper dictOperateMapper;
	
    private String roleIdStr = "roleId";
    private String moduleIdStr = "moduleId";
    private String operateCodeStr = "operateCode";
    private String validTimeStr = "validTime";
    private String userIdStr = "userId";
    private String groupIdStr = "groupId";

	@Override
	public int addPermissionToRole(int roleId,int moduleId,String[] operateCode,int validTime) {
		Map<String,Object> mapOfParam = new HashMap<String,Object>();
		mapOfParam.put(roleIdStr, roleId);
		mapOfParam.put(moduleIdStr, moduleId);
		mapOfParam.put(operateCodeStr, operateCode);
		mapOfParam.put(validTimeStr, validTime);
		return roleMapper.insertRolePermission(mapOfParam);
	}

	@Override
	public int deleteRolePermission(int roleId,int[] moduleId,String[] operateCode) {
		Map<String,Object> mapOfParam = new HashMap<String,Object>();
		mapOfParam.put(roleIdStr, roleId);
		mapOfParam.put(moduleIdStr, moduleId);
		mapOfParam.put(operateCodeStr, operateCode);
		return roleMapper.deleteRolePermission(mapOfParam);
	}

	@Override
	public RolePermission getRolePermissions(int roleId) {
		return roleMapper.getRolePermissions(roleId);
	}

	@Override
	public int addGroupToUser(int userId, int[] groupId) {
		Map<String,Object> mapOfParam = new HashMap<String,Object>();
		mapOfParam.put(userIdStr, userId);
		mapOfParam.put(groupIdStr, groupId);
		return groupMapper.insertUserGroup(mapOfParam);
	}

	@Override
	public int deleteUserGroup(int userId, int[] groupId) {
		Map<String,Object> mapOfParam = new HashMap<String,Object>();
		mapOfParam.put(userIdStr, userId);
		mapOfParam.put(groupIdStr, groupId);
		return groupMapper.deleteUserGroup(mapOfParam);
	}

	@Override
	public List<SysGroup> getUserGroups(int userId) {
		return groupMapper.getUserGroups(userId);
	}
	@Override
	public int addRoleToUser(int userId, int[] roleId) {
		Map<String,Object> mapOfParam = new HashMap<String,Object>();
		mapOfParam.put(userIdStr, userId);
		mapOfParam.put(roleIdStr, roleId);
		return roleMapper.insertUserRole(mapOfParam);
	}

	@Override
	public int deleteUserRole(int userId, int[] roleId) {
		Map<String,Object> mapOfParam = new HashMap<String,Object>();
		mapOfParam.put(userIdStr, userId);
		mapOfParam.put(roleIdStr, roleId);
		return roleMapper.deleteUserRole(mapOfParam);
	}

	@Override
	public List<SysRole> getUserRoles(int userId) {
		return roleMapper.getUserRoles(userId);
	}
	@Override
	public List<SysGroup> getAllGroup() {
		return groupMapper.getAllGroup();
	}
	@Override
	public List<SysRole> getAllRole() {
		return roleMapper.getAllRole();
	}
	@Override
	public List<SysOperate> getAllOperate() {
		return dictOperateMapper.getAllOperate();
	}
	@Override
	public List<SysModule> getAllModule() {
		return dictModuleMapper.getAllModule();
	}
	@Override
	public int deleteRole(Integer roleId) {
		return roleMapper.deleteByPrimaryKey(roleId);
	}
	@Override
	public int addRole(SysRole record) {
		return roleMapper.insert(record);
	}
	@Override
	public int updateRole(SysRole record) {
		return roleMapper.updateByPrimaryKey(record);
	}
	@Override
	public int deleteGroup(Integer groupId) {
		return groupMapper.deleteByPrimaryKey(groupId);
	}
	@Override
	public int addGroup(SysGroup record) {
		return groupMapper.insert(record);
	}
	@Override
	public int updateGroup(SysGroup record) {
		return groupMapper.updateByPrimaryKey(record);
	}
	@Override
	public int insertRoleGroup(int groupId, int[] roleId) {
		Map<String,Object> mapOfParam = new HashMap<String,Object>();
		mapOfParam.put(roleIdStr, roleId);
		mapOfParam.put(groupIdStr, groupId);
		return groupMapper.insertRoleGroup(mapOfParam);
	}
	@Override
	public int deleteRoleGroup(int groupId, int[] roleId) {
		Map<String,Object> mapOfParam = new HashMap<String,Object>();
		mapOfParam.put(roleIdStr, roleId);
		mapOfParam.put(groupIdStr, groupId);
		return groupMapper.deleteRoleGroup(mapOfParam);
	}
	@Override
	public List<SysRole> getRoleGroups(int groupId) {
		return groupMapper.getRoleGroups(groupId);
	}
}
