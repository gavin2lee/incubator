package com.harmazing.openbridge.sys.role.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.harmazing.framework.common.Page;
import com.harmazing.framework.util.StringUtil;
import com.harmazing.openbridge.sys.role.dao.SysRoleMapper;
import com.harmazing.openbridge.sys.role.model.SysFunc;
import com.harmazing.openbridge.sys.role.model.SysRole;
import com.harmazing.openbridge.sys.role.service.ISysFuncService;
import com.harmazing.openbridge.sys.role.service.ISysRoleService;

@Service
public class SysRoleService implements ISysRoleService {

	@Autowired
	private SysRoleMapper sysRoleMapper;

	@Autowired
	private ISysFuncService sysFuncService;

	@Transactional(readOnly = true)
	public List<SysRole> findRoleBySystemKey(String systemKey) {
		return sysRoleMapper.findRoleBySystemKey(systemKey);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void save(SysRole sysRole) {
		if (StringUtils.hasText(sysRole.getRoleId())) {
			sysRoleMapper.update(sysRole);
		} else {
			String roleId = StringUtil.getUUID();
			sysRole.setRoleId(roleId);
			sysRoleMapper.save(sysRole);
		}
		sysRoleMapper.deleteFuncRelation(sysRole.getRoleId());
		sysRoleMapper.deleteUserRelation(sysRole.getRoleId());

		if (StringUtils.hasText(sysRole.getFuncIds())) {
			String[] fids = sysRole.getFuncIds().split("\\;");
			List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
			for (String fid : fids) {
				Map<String, Object> p = new HashMap<String, Object>();
				p.put("roleId", sysRole.getRoleId());
				p.put("funcId", fid);
				p.put("id", StringUtil.getUUID());
				result.add(p);
			}
			sysRoleMapper.saveFuncRelations(result);
		}

		if (StringUtils.hasText(sysRole.getUserIds())) {
			String[] fids = sysRole.getUserIds().split("\\;");
			List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
			for (String fid : fids) {
				Map<String, Object> p = new HashMap<String, Object>();
				p.put("roleId", sysRole.getRoleId());
				p.put("userId", fid);
				p.put("id", StringUtil.getUUID());
				result.add(p);
			}
			sysRoleMapper.saveUserRelation(result);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void saveRoleFunc(String roleId, String[] funcId) {
		sysRoleMapper.deleteFuncRelation(roleId);

		if (funcId != null && funcId.length > 0) {
			List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
			for (String fid : funcId) {
				Map<String, Object> p = new HashMap<String, Object>();
				p.put("roleId", roleId);
				p.put("funcId", fid);
				p.put("id", StringUtil.getUUID());
				result.add(p);
			}
			sysRoleMapper.saveFuncRelations(result);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void saveRoleUser(SysRole sysRole, String[] userId) {
		sysRoleMapper.update(sysRole);
		sysRoleMapper.deleteUserRelation(sysRole.getRoleId());

		if (userId != null && userId.length > 0) {
			List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
			for (String fid : userId) {
				Map<String, Object> p = new HashMap<String, Object>();
				p.put("roleId", sysRole.getRoleId());
				p.put("userId", fid);
				p.put("id", StringUtil.getUUID());
				result.add(p);
			}
			sysRoleMapper.saveUserRelation(result);
		}
	}

	public void addRoleUser(SysRole sysRole, String[] userId) {
		sysRoleMapper.save(sysRole);
		sysRoleMapper.deleteUserRelation(sysRole.getRoleId());

		if (userId != null && userId.length > 0) {
			List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
			for (String fid : userId) {
				Map<String, Object> p = new HashMap<String, Object>();
				p.put("roleId", sysRole.getRoleId());
				p.put("userId", fid);
				p.put("id", StringUtil.getUUID());
				result.add(p);
			}
			sysRoleMapper.saveUserRelation(result);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void saveUserRelation(List<Map<String, Object>> reuslt) {
		sysRoleMapper.saveUserRelation(reuslt);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteUserRelationByUserId(String userId) {
		sysRoleMapper.deleteUserRelationByUserId(userId);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void delete(String roleId) {
		sysRoleMapper.deleteFuncRelation(roleId);
		sysRoleMapper.deleteUserRelation(roleId);
		sysRoleMapper.delete(roleId);
	}

	public SysRole findById(String roleId) {
		SysRole r = sysRoleMapper.findById(roleId);
		if (r == null) {
			return null;
		}
		List<Map<String, Object>> funs = sysFuncService
				.findFuncByRoleId(roleId);
		List<Map<String, Object>> users = sysRoleMapper
				.findUserByRoleId(roleId);
		if (funs != null && funs.size() != 0) {
			StringBuffer f = new StringBuffer();
			for (Map<String, Object> info : funs) {
				f.append(info.get("funcId"));
				f.append(";");
			}
			String m = f.toString();
			r.setFuncIds(m.substring(0, m.length() - 1));
			r.setFuns(funs);
		}
		if (users != null && users.size() != 0) {
			StringBuffer f = new StringBuffer();
			for (Map<String, Object> info : users) {
				f.append(info.get("userId"));
				f.append(";");
			}
			String m = f.toString();
			r.setUserIds(m.substring(0, m.length() - 1));
			r.setUsers(users);
		}
		return r;
	}

	@Override
	public List<Map<String, Object>> funcDialog(Map<String, Object> params) {
		Page<Map<String, Object>> xpage = Page.create(params);
		xpage.setRecordCount(sysFuncService.funcPageRecordCount(params));
		xpage.addAll(sysFuncService.funcPage(params));
		return xpage;
	}

	/**
	 * 根据角色名去查找
	 * 
	 * @param role
	 * @return
	 */
	@Override
	public SysRole findByName(String roleName) {
		SysRole role = new SysRole();
		role.setRoleName(roleName);
		List<SysRole> result = findByEntity(role);
		if (result != null && result.size() > 1) {
			throw new RuntimeException(roleName + " 具有重复的角色名");
		}
		if (result == null || result.size() == 0) {
			return null;
		}
		return result.get(0);
	}

	/**
	 * 根据角色去查找
	 * 
	 * @param role
	 * @return
	 */
	public List<SysRole> findByEntity(SysRole role) {
		return sysRoleMapper.findByEntity(role);
	}

	/**
	 * 根据用户去查找 角色 ，权限 key : role 角色 key : func 功能
	 * 
	 * @param userId
	 * @return
	 */
	public Map<String, List> findRoleInfo(String userId) {
		List<SysRole> role = sysRoleMapper.findRoleByUserId(userId);
		List<SysFunc> func = sysFuncService.findFuncByUserId(userId);
		Map<String, List> r = new HashMap<String, List>();
		r.put("role", role);
		r.put("func", func);
		return r;
	}

}
