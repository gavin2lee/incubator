package com.harmazing.openbridge.sys.user.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.harmazing.framework.authorization.IUserService;
import com.harmazing.framework.authorization.LoginLog;
import com.harmazing.framework.common.Page;
import com.harmazing.framework.util.LogUtil;
import com.harmazing.framework.util.Md5Util;
import com.harmazing.framework.util.StringUtil;
import com.harmazing.openbridge.sys.role.dao.SysRoleMapper;
import com.harmazing.openbridge.sys.role.model.SysRole;
import com.harmazing.openbridge.sys.role.service.ISysFuncService;
import com.harmazing.openbridge.sys.role.service.ISysRoleService;
import com.harmazing.openbridge.sys.user.dao.SysGroupMapper;
import com.harmazing.openbridge.sys.user.dao.SysUserDepartmentMapper;
import com.harmazing.openbridge.sys.user.dao.SysUserMapper;
import com.harmazing.openbridge.sys.user.model.SysUser;
import com.harmazing.openbridge.sys.user.model.SysUserDepartment;
import com.harmazing.openbridge.sys.user.service.ISysGroupService;
import com.harmazing.openbridge.sys.user.service.ISysUserService;

@Service("sysUserService")
public class SysUserService implements ISysUserService, IUserService {
	@Autowired
	private SysUserMapper userMapper;
	@Autowired
	private SysRoleMapper sysRoleMapper;
	@Autowired
	private SysUserDepartmentMapper userDepartmentMapper;
	@Autowired
	private SysGroupMapper groupMapper;
	@Autowired
	private ISysGroupService groupService;
	@Autowired
	private ISysRoleService sysRoleService;
	@Autowired
	private ISysFuncService sysFuncService;

	@Override
	@Transactional(readOnly = true)
	public SysUser getUserById(final String userId) {
		SysUser user = userMapper.getUserById(userId);
		if (user != null) {
			user = fillIUserProperty(user);
		}
		return user;
	}

	private List<String> getFunctionsByRole(String[] roles) {
		List<String> functionIds = new ArrayList<String>();
		if (roles != null) {
			for (String role : roles) {
				functionIds.addAll(sysFuncService.findAllFuncByRoleId(role));
			}
		}
		return functionIds;
	}

	private SysUser fillIUserProperty(SysUser user) {
		user.setUserGroup(groupService.getGroupByUserId(user.getUserId()));
		List<SysRole> roles = sysRoleMapper.findRoleByUserId(user.getUserId());
		String temp = "";
		for (int i = 0; i < roles.size(); i++) {
			if (i == 0) {
				temp += roles.get(i).getRoleId();
			} else {
				temp += ";" + roles.get(i).getRoleId();
			}
		}
		user.setRoles(temp);
		List<String> funcs = getFunctionsByRole(user.getUserRole());
		user.setUserFunc(funcs.toArray(new String[0]));
		try {
			Map<String, Object> tenant = userMapper.getTenantByUserId(user
					.getUserId());
			if (tenant != null) {
				user.setTenantId(tenant.get("tenantId").toString());
				user.setTenantName(tenant.get("tenantName").toString());
			}
		} catch (Exception e) {
			LogUtil.error("获取用户的租户信息出错", e);
			throw new RuntimeException("获取用户的租户信息出错", e);
		}
		return user;
	}

	@Override
	@Transactional(readOnly = true)
	public SysUser getUserByToken(final String token) {
		SysUser user = userMapper.getUserByToken(token);

		return fillIUserProperty(user);
	}

	@Override
	@Transactional(readOnly = true)
	public SysUser login(String loginName, String password) throws Exception {
		SysUser user = userMapper.getUserByLoginName(loginName);
		if (user != null) {
			if (!user.getActivate()) {
				throw new Exception("帐号被禁用");
			}
			if (user.getLoginPassword().equals(password)) {
				user = fillIUserProperty(user);
				if (!user.isAdministrator()
						&& StringUtil.isNull(user.getTenantId())) {
					throw new Exception("帐号没有被分配组织信息");
				}
				return user;
			} else {
				throw new Exception("密码错误");
			}
		} else {
			throw new Exception("用户名不存在");
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public SysUser mLogin(String loginName, String password) throws Exception {
		SysUser user = userMapper.getUserByLoginName(loginName);
		if (user != null) {
			if (!user.getActivate()) {
				throw new Exception("帐号被禁用");
			}
			if (user.getLoginPassword().equals(password)) {
				String token = Md5Util.getMD5String(user.getUserId()
						+ System.currentTimeMillis());
				userMapper.updateUserToken(user.getUserId(), token);
				user = userMapper.getUserByToken(token);
				return fillIUserProperty(user);
			} else {
				throw new Exception("密码错误");
			}
		} else {
			throw new Exception("用户名不存在");
		}
	}

	/**
	 * 查询用户列表
	 */
	@Override
	@Transactional(readOnly = true)
	public Page<Map<String, Object>> userPage(Map<String, Object> params) {
		Page<Map<String, Object>> xpage = Page.create(params);
		xpage.setRecordCount(userMapper.userPageRecordCount(params));
		xpage.addAll(userMapper.userPage(params));
		return xpage;
	}

	/**
	 * 新增用户
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public SysUser userCreate(SysUser user) {
		String token = Md5Util.getMD5String(user.getUserId()
				+ System.currentTimeMillis());
		user.setApiAuthKey(StringUtil.getUUID());
		user.setToken(token);
		userMapper.create(user);
		return user;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public SysUser userRegister(SysUser user) {
		userMapper.register(user);
		return user;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void userActivate(boolean activate) {
		userMapper.activate(activate);
	}

	/**
	 * 修改用户，包括用户所属部门
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int updateUserAndDepart(SysUser user, String OriginDepId,
			String departmentId) {
		SysUser currentUser = userMapper
				.getUserByLoginName(user.getLoginName());
		if (currentUser != null
				&& !currentUser.getUserId().equals(user.getUserId())) {
			return -1;
		}
		// 修改用户信息至sys_user表
		userMapper.update(user);

		if ((OriginDepId == null || OriginDepId.trim().equals(""))
				&& !departmentId.equals("")) {
			// 添加用户与部门的关联
			SysUserDepartment userDepartment = new SysUserDepartment();
			userDepartment.setRelationId(StringUtil.getUUID());
			userDepartment.setDepId(departmentId);
			userDepartment.setUserId(user.getUserId());
			userDepartmentMapper.addUserDepartment(userDepartment);
		} else {
			// 修改用户与部门的关联
			System.out.println(user.getUserId() + "----" + departmentId);
			if (departmentId != null && !departmentId.trim().equals("")) {
				userDepartmentMapper.updateUserDepartment(departmentId,
						user.getUserId());
			} else {
				userDepartmentMapper.delUserDepartment(user.getUserId());
			}
		}
		return 1;
	}

	/**
	 * 修改用户，包括用户所属部门
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int updateUserAndDepart(SysUser user, String OriginDepId,
			String departmentId, String roleIds) {
		int m = updateUserAndDepart(user, OriginDepId, departmentId);
		if (m == -1) {
			return m;
		}
		sysRoleService.deleteUserRelationByUserId(user.getId());
		if (StringUtils.hasText(roleIds)) {
			String[] str = roleIds.split(",");
			List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
			for (String fid : str) {
				Map<String, Object> p = new HashMap<String, Object>();
				p.put("roleId", fid);
				p.put("userId", user.getUserId());
				p.put("id", StringUtil.getUUID());
				result.add(p);
			}
			sysRoleService.saveUserRelation(result);
		}
		return m;
	}

	/**
	 * 删除用户，删除其所属部门信息
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteUser(String userId) {
		// delete user and department relation
		userDepartmentMapper.delUserDepartment(userId);
		// delete user and group relation
		groupMapper.deleteRelationByUser(userId);
		// delete user from sys_user
		userMapper.deleteById(userId);
		// delete sys_user_role
		sysRoleService.deleteUserRelationByUserId(userId);
	}

	/**
	 * 修改用户是否启用的状态
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void updateUserStatus(String userId, Boolean status) {
		userMapper.updateUserStatus(userId, status);
	}

	/**
	 * 批量删除用户
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteBatchUser(String[] userIds) {
		if (userIds != null) {
			for (String id : userIds) {
				// delete user and department relation
				userDepartmentMapper.delUserDepartment(id);
				// delete user and group relation
				groupMapper.deleteRelationByUser(id);
				// delete user from sys_user
				userMapper.deleteById(id);
				// delete user from sys_user_role
				sysRoleService.deleteUserRelationByUserId(id);
			}
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int addUser(SysUser user, String departmentId, String roleIds) {
		int m = addUser(user, departmentId);
		// sysRoleService.deleteUserRelationByUserId(user.getId());
		if (StringUtils.hasText(roleIds)) {
			String[] str = roleIds.split(",");
			List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
			for (String fid : str) {
				Map<String, Object> p = new HashMap<String, Object>();
				p.put("roleId", fid);
				p.put("userId", user.getUserId());
				p.put("id", StringUtil.getUUID());
				result.add(p);
			}
			sysRoleService.saveUserRelation(result);
		}
		return m;
	}

	/**
	 * 新增用户
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int addUser(SysUser user, String departmentId) {
		SysUser oldUser = userMapper.getUserByLoginName(user.getLoginName());
		if (oldUser != null) {
			return -1;
		}
		// 新增用户
		user.setApiAuthKey(StringUtil.getUUID());
		user.setToken(user.getUserId());
		userMapper.create(user);
		// 新增用户与部门的关联
		if (departmentId != null && !departmentId.trim().equals("")) {
			SysUserDepartment userDepartment = new SysUserDepartment();
			userDepartment.setRelationId(StringUtil.getUUID());
			userDepartment.setUserId(user.getUserId());
			userDepartment.setDepId(departmentId);
			userDepartmentMapper.addUserDepartment(userDepartment);
		}
		return 1;
	}

	/**
	 * 查询用户所属部门
	 */
	@Override
	@Transactional(readOnly = true)
	public SysUserDepartment getDepartmentByUserId(String userId) {
		return userDepartmentMapper.getByUserId(userId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int updateUserPwd(String userId, String oldPassword,
			String newPassword) {
		SysUser user = userMapper.getUserById(userId);
		if (user != null) {
			if (!user.getLoginPassword().equals(oldPassword)) {
				return -1;
			} else {
				userMapper.updateUserPassword(userId, newPassword);
			}
		}
		return 0;
	}

	@Override
	public List<Map<String, Object>> findAllUser() {
		return userMapper.findAllUser();
	}

	public Map<String, String> findAllUserLoginName() {
		List<Map<String, Object>> users = userMapper.findAllUserLoginName();
		Map<String, String> userLoginNames = new HashMap<String, String>();
		for (int i = 0; i < users.size(); i++) {
			userLoginNames.put(users.get(i).get("user_id").toString(), users
					.get(i).get("login_name").toString());
		}
		return userLoginNames;
	}

	@Override
	public List<SysUser> getUsersByIds(List<String> ids) {
		return userMapper.getUsersByIds(ids);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void resetSecretKey(String userId, String secretKey) {
		userMapper.resetSecretKey(userId, secretKey);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void writeUserLoginLog(LoginLog log) {
		userMapper.writeUserLoginLog(log);
	}
}
