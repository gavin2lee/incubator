package com.harmazing.openbridge.sys.role.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.harmazing.framework.util.ResourceUtil;
import com.harmazing.framework.util.XmlUtil;
import com.harmazing.openbridge.sys.init.ILaunchSystemBean;
import com.harmazing.openbridge.sys.init.InitContext;
import com.harmazing.openbridge.sys.role.dao.SysRoleMapper;
import com.harmazing.openbridge.sys.role.model.SysRole;
import com.harmazing.openbridge.sys.user.dao.SysUserMapper;

/**
 * 初始化系统角色，功能，角色功能映射
 * 
 * @author taoshuangxi
 *
 */
@Service("SysRoleInitialBean")
public class SysRoleInitialBean implements ILaunchSystemBean {
	@Autowired
	private SysRoleMapper roleMapper;
	@Autowired
	private ISysRoleService sysRoleService;
	@Autowired
	private ISysFuncService funcService;
	@Autowired
	private SysUserMapper userMapper;

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void onFirstStartup(InitContext context) throws Exception {
		Resource[] r = ResourceUtil.getResources("classpath:config/init-role.xml"); 
		Document doc = XmlUtil.buildDocument(r[0].getInputStream());
		List<Element> elements = XmlUtil.getChildElements(doc
				.getDocumentElement());
		for (Element e : elements) {
			String elementId = e.getAttribute("id");
			if (elementId.equals("roles")) {
				List<Element> initRoleElements = XmlUtil
						.getChildElementsByTagName(e, "role");
				initSysRole(initRoleElements);
			} else if (elementId.equals("roleFunctionMappers")) {
				List<Element> initRoleFunctionElements = XmlUtil
						.getChildElementsByTagName(e, "roleFunctionMapper");
				initSysRoleFunc(initRoleFunctionElements);
			}
		}
	}

	/**
	 * 初始化sys_role表
	 * 
	 * @param initRoleElements
	 */
	private void initSysRole(List<Element> initRoleElements) {
		for (Element role : initRoleElements) {
			String roleId = role.getAttribute("id");
			String roleName = role.getAttribute("name");
			String roleSystem = role.getAttribute("system");
			String roleDesc = role.getAttribute("description");
			// 检查该角色是否已经存在，如果不存在则添加
			SysRole oldRole = sysRoleService.findByName(roleName);
			if (oldRole != null) {
				roleMapper.deleteFuncRelation(oldRole.getRoleId());
				roleMapper.deleteUserRelation(oldRole.getRoleId());
				roleMapper.delete(oldRole.getRoleId());
			}
			roleMapper.deleteFuncRelation(roleId);
			SysRole newRole = new SysRole();
			newRole.setRoleId(roleId);
			newRole.setRoleName(roleName);
			newRole.setRoleSystem(roleSystem);
			newRole.setRoleDesc(roleDesc);
			roleMapper.save(newRole);
		}
	}

	/**
	 * 初始化sys_role_func表
	 * 
	 * @param initSysRoleFunctionElements
	 */
	private void initSysRoleFunc(List<Element> initSysRoleFunctionElements) {
		for (Element roleFunction : initSysRoleFunctionElements) {
			String id = roleFunction.getAttribute("id");
			String roleId = roleFunction.getAttribute("roleId");
			String functionId = roleFunction.getAttribute("functionId");
			roleMapper.addFuncRelation(id, roleId, functionId);
		}
	}
}
