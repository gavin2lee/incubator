package com.harmazing.openbridge.sys.access.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.harmazing.framework.common.JsonResponse;
import com.harmazing.framework.common.controller.AbstractController;
import com.harmazing.framework.util.StringUtil;
import com.harmazing.openbridge.sys.role.model.SysFunc;
import com.harmazing.openbridge.sys.role.model.SysRole;
import com.harmazing.openbridge.sys.role.service.ISysFuncService;
import com.harmazing.openbridge.sys.role.service.ISysRoleService;
import com.harmazing.openbridge.sys.user.service.ISysUserService;

@Controller
@RequestMapping("/sys/access")
public class SysAccessController extends AbstractController {
	@Autowired
	private ISysRoleService sysRoleService;
	@Autowired
	private ISysFuncService sysFuncService;
	@Autowired
	private ISysUserService sysUserService;

	@RequestMapping("/function")
	public String function(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			String roleId = request.getParameter("roleId");
			String systemKey = request.getParameter("systemKey");
			SysRole role = null;
			List<SysRole> roles = sysRoleService.findRoleBySystemKey(systemKey);
			List<SysFunc> allFunc = sysFuncService
					.findFuncBySystemKey(systemKey);
			if (StringUtil.isNull(roleId) && roles != null && roles.size() > 0) {
				roleId = roles.get(0).getRoleId();
			}
			if (StringUtil.isNotNull(roleId)) {
				role = sysRoleService.findById(roleId);
			}
			Map<String, List<SysFunc>> funcs = new HashMap<String, List<SysFunc>>();

			if (allFunc != null) {
				for (int i = 0; i < allFunc.size(); i++) {
					SysFunc func = allFunc.get(i);
					String module = func.getFuncModule();
					if (funcs.containsKey(module)) {
						funcs.get(module).add(func);
					} else {
						List<SysFunc> x = new ArrayList<SysFunc>();
						x.add(func);
						funcs.put(module, x);
					}
				}
			}

			request.setAttribute("roles", roles);
			request.setAttribute("funcs", funcs);
			request.setAttribute("role", role);
			return getUrlPrefix() + "/function";
		} catch (Exception e) {
			logger.error("角色列表页面出错", e);
			request.setAttribute("exception", e);
			return forward(ERROR);
		}
	}

	@RequestMapping("/rolefunc")
	@ResponseBody
	public JsonResponse role(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			String roleId = request.getParameter("roleId");
			Map<String, Object> resultMap = new HashMap<String, Object>();
			List<Map<String, Object>> funcs = null;
			if (StringUtil.isNotNull(roleId)) {
				funcs = sysFuncService.findFuncByRoleId(roleId);
				resultMap.put("funcs", funcs);
			}
			if (StringUtil.isNotNull(roleId)) {
				SysRole role = sysRoleService.findById(roleId);
				resultMap.put("role", role);
			}
			if (StringUtil.isNull(roleId)) {
				List<Map<String, Object>> users = sysUserService.findAllUser();
				SysRole r = new SysRole();
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
				r.setUsers(users);
				resultMap.put("role", r);
			}
			return JsonResponse.success(resultMap);
			// return JsonResponse.success(funcs);
		} catch (Exception e) {
			logger.error("角色列表页面出错", e);
			request.setAttribute("exception", e);
			return JsonResponse.failure(500, e.getMessage());
		}
	}

	@RequestMapping("/role/add")
	public String roleAdd(HttpServletRequest request,
			HttpServletResponse response) {
		try {

			return getUrlPrefix() + "/role/add";
		} catch (Exception e) {
			logger.error("角色列表页面出错", e);
			request.setAttribute("exception", e);
			return forward(ERROR);
		}
	}

	@RequestMapping("/role/save")
	@ResponseBody
	public JsonResponse roleSave(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			SysRole role = new SysRole();
			String userIds = request.getParameter("userIds");
			String roleName = request.getParameter("roleName");
			String roleDesc = request.getParameter("roleDesc");
			String roleSystem = request.getParameter("roleSystem");
			role.setRoleId(StringUtil.getUUID());
			role.setRoleName(roleName);
			role.setRoleDesc(roleDesc);
			role.setRoleSystem(roleSystem);
			String[] userId = userIds.split(";");
			sysRoleService.addRoleUser(role, userId);
			return JsonResponse.success();
		} catch (Exception e) {
			logger.error("角色列表页面出错", e);
			request.setAttribute("exception", e);
			return JsonResponse.failure(500, e.getMessage());
		}
	}

	@RequestMapping("/role/edit")
	public String roleEdit(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			String roleId = request.getParameter("roleId");
			SysRole role = sysRoleService.findById(roleId);
			request.setAttribute("role", role);
			return getUrlPrefix() + "/role/edit";
		} catch (Exception e) {
			logger.error("角色列表页面出错", e);
			request.setAttribute("exception", e);
			return forward(ERROR);
		}
	}

	@RequestMapping("/role/update")
	@ResponseBody
	public JsonResponse roleUpdate(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			String roleId = request.getParameter("roleId");
			SysRole role = sysRoleService.findById(roleId);
			String userIds = request.getParameter("userIds");
			String roleName = request.getParameter("roleName");
			String roleDesc = request.getParameter("roleDesc");
			String[] userId = userIds.split(";");
			role.setRoleName(roleName);
			role.setRoleDesc(roleDesc);
			sysRoleService.saveRoleUser(role, userId);
			return JsonResponse.success();
		} catch (Exception e) {
			logger.error("角色列表页面出错", e);
			request.setAttribute("exception", e);
			return JsonResponse.failure(500, e.getMessage());
		}
	}

	@RequestMapping("/role/delete")
	@ResponseBody
	public JsonResponse roleDelete(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			String roleId = request.getParameter("roleId");
			sysRoleService.delete(roleId);
			return JsonResponse.success();
		} catch (Exception e) {
			logger.error("删除角色出错", e);
			request.setAttribute("exception", e);
			return JsonResponse.failure(500, e.getMessage());
		}
	}

	@RequestMapping("/saveRoleFunc")
	@ResponseBody
	public JsonResponse saveRoleFunc(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			String roleId = request.getParameter("roleId");
			String[] funcIds = request.getParameterValues("funcId");
			sysRoleService.saveRoleFunc(roleId, funcIds);
			return JsonResponse.success();
		} catch (Exception e) {
			logger.error("角色列表页面出错", e);
			request.setAttribute("exception", e);
			return JsonResponse.failure(500, e.getMessage());
		}
	}
}
