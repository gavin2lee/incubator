package com.harmazing.openbridge.sys.user.service;

import java.util.List;
import java.util.Map;

import com.harmazing.framework.common.Page;
import com.harmazing.framework.common.service.IBaseService;
import com.harmazing.openbridge.sys.user.model.SysUser;
import com.harmazing.openbridge.sys.user.model.SysUserDepartment;

public interface ISysUserService extends IBaseService {
	public SysUser getUserById(String userId);

	/**
	 * 用户登录验证用户名密码是否正确
	 *
	 * @param username
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public SysUser login(String loginName, String password) throws Exception;

	public SysUser mLogin(String loginName, String password) throws Exception;

	/**
	 * 查询用户列表
	 */
	public Page<Map<String, Object>> userPage(Map<String, Object> params);

	/**
	 * 新增用户
	 * 
	 * @param user
	 * @return
	 */
	public SysUser userCreate(SysUser user);

	public SysUser userRegister(SysUser user);

	public void userActivate(boolean activate);

	/**
	 * 修改用户，包括用户信息和所属部门
	 * 
	 * @param user
	 * @param OriginDepId
	 * @param departmentId
	 */
	public int updateUserAndDepart(SysUser user, String OriginDepId,
			String departmentId);

	/**
	 * 修改用户，包括用户信息和所属部门
	 * 
	 * @param user
	 * @param OriginDepId
	 * @param departmentId
	 */
	public int updateUserAndDepart(SysUser user, String OriginDepId,
			String departmentId, String roleIds);

	/**
	 * 删除用户
	 * 
	 * @param userId
	 */
	public void deleteUser(String userId);

	public void deleteBatchUser(String[] userIds);

	public void updateUserStatus(String userId, Boolean status);

	public int updateUserPwd(String userId, String oldPassword,
			String newPassword);

	public int addUser(SysUser user, String departmentId);

	/**
	 * 新增用户
	 * 
	 * @param user
	 * @param departmentId
	 * @param roleIds
	 * @return
	 */
	public int addUser(SysUser user, String departmentId, String roleIds);

	/**
	 * 根据用户id获取其所属的部门
	 * 
	 * @param userId
	 * @return SysUserDepartment
	 */
	public SysUserDepartment getDepartmentByUserId(String userId);

	/**
	 * 
	 * <pre>
	 * 查询所有用户
	 * </pre>
	 * 
	 * @return
	 */
	public List<Map<String, Object>> findAllUser();

	public Map<String, String> findAllUserLoginName();

	public List<SysUser> getUsersByIds(List<String> ids);

	public void resetSecretKey(String userId, String secretKey);
}
