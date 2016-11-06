package com.harmazing.openbridge.sys.role.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.harmazing.framework.common.controller.AbstractController;
import com.harmazing.openbridge.sys.role.model.SysFunc;
import com.harmazing.openbridge.sys.role.model.SysRole;
import com.harmazing.openbridge.sys.role.service.ISysFuncService;
import com.harmazing.openbridge.sys.role.service.ISysRoleService;

@Controller
@RequestMapping("/sys/user/role")
public class SysRoleController extends AbstractController {
	private static final Log logger = LogFactory
			.getLog(SysRoleController.class);

	@Autowired
	private ISysRoleService iSysRoleService;

	@Autowired
	private ISysFuncService iSysFuncService;

	@RequestMapping("/save")
	@ResponseBody
	public Object save(SysRole sysRole) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			if ("超级管理员".equals(sysRole.getRoleName())) {
				result.put("status", "-1");
				result.put("info", "不能创建超级管理员角色");
				return result;
			}
			iSysRoleService.save(sysRole);
			result.put("status", "0");
		} catch (Exception e) {
			logger.error("保存失败", e);
			e.printStackTrace();
			result.put("status", "-1");
			result.put("info", "保存失败");
		}
		return result;
	}

	@RequestMapping("/delete")
	@ResponseBody
	public Object delete(@RequestParam String roleId) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			iSysRoleService.delete(roleId);
			result.put("status", "0");
		} catch (Exception e) {
			logger.error("删除失败", e);
			e.printStackTrace();
			result.put("status", "-1");
			result.put("info", "删除失败失败");
		}
		return result;
	}

	@RequestMapping("/findById")
	@ResponseBody
	public Object findById(@RequestParam String roleId) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			SysRole info = iSysRoleService.findById(roleId);
			result.put("info", info);
			result.put("status", "0");
		} catch (Exception e) {
			logger.error("获取数据失败" + roleId, e);
			e.printStackTrace();
			result.put("status", "-1");
			result.put("info", "获取数据失败" + roleId);
		}
		return result;
	}

	/**
	 * 列表显示角色信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/func/dialog")
	public String funcDialog(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			List<SysFunc> funcs = iSysFuncService.findAll();
			Map<String, List<SysFunc>> reference = new HashMap<String, List<SysFunc>>();
			if (funcs != null && funcs.size() != 0) {
				for (SysFunc fun : funcs) {
					String moduleName = "";
					if (StringUtils.hasText(fun.getFuncModule())) {
						moduleName = fun.getFuncModule();
					} else {
						moduleName = "其它";
					}
					if (!reference.containsKey(moduleName)) {
						reference.put(moduleName, new ArrayList<SysFunc>());
					}
					reference.get(moduleName).add(fun);
				}
			}

			request.setAttribute("pageData", reference);
			return "/sys/user/func_dialog";
		} catch (Exception e) {
			logger.error("功能页面出错", e);
			request.setAttribute("exception", e);
			return forward(ERROR);
		}
	}
}
