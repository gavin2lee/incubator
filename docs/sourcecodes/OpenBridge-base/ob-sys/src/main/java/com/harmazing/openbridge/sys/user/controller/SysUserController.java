package com.harmazing.openbridge.sys.user.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.harmazing.framework.authorization.IHttpUserManager;
import com.harmazing.framework.authorization.IUser;
import com.harmazing.framework.common.JsonResponse;
import com.harmazing.framework.common.controller.AbstractController;
import com.harmazing.framework.util.StringUtil;
import com.harmazing.framework.util.WebUtil;
import com.harmazing.openbridge.sys.role.model.SysRole;
import com.harmazing.openbridge.sys.role.service.ISysRoleService;
import com.harmazing.openbridge.sys.user.model.SysUser;
import com.harmazing.openbridge.sys.user.model.SysUserDepartment;
import com.harmazing.openbridge.sys.user.service.ISysDepartService;
import com.harmazing.openbridge.sys.user.service.ISysGroupService;
import com.harmazing.openbridge.sys.user.service.ISysUserService;

@Controller
@RequestMapping("/sys/user")
public class SysUserController extends AbstractController {
	private static final Log logger = LogFactory
			.getLog(SysUserController.class);

	@Autowired
	private IHttpUserManager httpUserManager;
	@Autowired
	private ISysUserService userService;
	@Autowired
	private ISysDepartService departmentService;
	@Autowired
	private ISysGroupService groupService;
	@Autowired
	private ISysRoleService sysRoleService;

	@RequestMapping("/dialog")
	public String dialog(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			IUser user = WebUtil.getUserByRequest(request);
			String table = request.getParameter("table");
			String query = request.getParameter("query");
			/*if(query.equals("tenant"))
			{
				params.put("tenantId_user", user.getTenantId());
			}*/
			params.put("query", query);
			params.put("tenantId_user", user.getTenantId());
			params.put("pageNo", StringUtil.getIntParam(request, "pageNo", 1));
			params.put("pageSize",
					StringUtil.getIntParam(request, "pageSize", 8));
			String keyword = request.getParameter("keyword");
			params.put("keyword", keyword);
			params.put("sysUser", 0);
			if(!StringUtil.isEmpty(table)){
				params.put("table", table);
				request.setAttribute("table", table);
				String tenantId = request.getParameter("tenantId");
				params.put("tenantId", tenantId);
				request.setAttribute("tenantId", tenantId);
			}
			List<Map<String, Object>> pageData = userService.userPage(params);
			request.setAttribute("pageData", pageData);
			request.setAttribute("keyword", keyword);
			return getUrlPrefix() + "/dialog";
		} catch (Exception e) {
			logger.error("用户列表页面出错", e);
			request.setAttribute("exception", e);
			return forward(ERROR);
		}
	}

	/**
	 * 列表显示用户信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/page")
	public String pageUser(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			String keyWord = request.getParameter("keyWords");
			if (keyWord != null && !keyWord.trim().equals("")) {
				params.put("keyword", keyWord);
				request.setAttribute("keyWords", keyWord);
			}
			params.put("pageNo", StringUtil.getIntParam(request, "pageNo", 1));
			params.put("pageSize",
					StringUtil.getIntParam(request, "pageSize", 10));

			List<Map<String, Object>> pageData = userService.userPage(params);
			request.setAttribute("pageData", pageData);
			return getUrlPrefix() + "/user/page";
		} catch (Exception e) {
			logger.error("用户列表页面出错", e);
			request.setAttribute("exception", e);
			return forward(ERROR);
		}
	}

	/**
	 * 跳转到修改页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/modify")
	public String modify(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			String userId = request.getParameter("userId");
			SysUser user = userService.getUserById(userId);

			Map<String, String> roles = new HashMap<String, String>();
			SysRole r1=new SysRole();
			r1.setRoleSystem("api");
			List<SysRole> role = sysRoleService.findByEntity(r1);
			 if(role!=null && role.size()>0){
				 for(SysRole r : role){
					 roles.put(r.getRoleId(), r.getRoleName());
				 }
			 }
			roles.put("administrator", "超级管理员");
			SysUserDepartment userDepartment = userService
					.getDepartmentByUserId(userId);
			if (userDepartment != null) {
				String departOptions = departmentService
						.getUserDepartOptionString(userDepartment.getDepId());
				request.setAttribute("userDepartment",
						userDepartment.getDepId());
				request.setAttribute("departments", departOptions);
			} else {
				String departOptions = departmentService
						.getUserDepartOptionString("");
				request.setAttribute("departments", departOptions);
			}
			Map<String,List> userInfo = sysRoleService.findRoleInfo(userId);
			if(userInfo.containsKey("role") && userInfo.get("role")!=null){
				StringBuilder roleIds = new StringBuilder();
				for(Object r : userInfo.get("role")){
					SysRole rr = (SysRole)r;
					roleIds.append(rr.getRoleId());
					roleIds.append(",");
				}
				user.setRoles(roleIds.toString());
			}
			request.setAttribute("user", user);
			request.setAttribute("roles", roles);
			return getUrlPrefix() + "/user/modify";
		} catch (Exception e) {
			logger.error("用户修改页面出错", e);
			request.setAttribute("exception", e);
			return forward(ERROR);
		}
	}

	/**
	 * 保存对用户的修改
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/save")
	@ResponseBody
	public JsonResponse saveOrUpdate(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			String userId = request.getParameter("userId");
			String userName = request.getParameter("userName");
			String loginName = request.getParameter("loginName");
			String mobile = request.getParameter("mobile");
			// String password = request.getParameter("loginPassword");
			String roleIds = request.getParameter("roles");
			String originalDepId = request.getParameter("originDepId");
			String departmentId = request.getParameter("department");
			String email = request.getParameter("email");
			String activate = request.getParameter("activate");
			SysUser user = new SysUser();
			user.setUserId(userId);
			user.setName(userName);
			user.setLoginName(loginName);
			user.setMobile(mobile);
			// user.setLoginPassword(password);
//			user.setRoles(roleIds);
			user.setEmail(email);
			if (activate != null && activate.equals("true")) {
				user.setActivate(true);
			} else {
				user.setActivate(false);
			}
			int ret = userService.updateUserAndDepart(user, originalDepId,
					departmentId,roleIds);
			if (ret == -1) {
				return JsonResponse.failure(500, "用户名已存在");
			}
			return JsonResponse.success();
		} catch (Exception e) {
			logger.error("用户修改保存出错", e);
			request.setAttribute("exception", e);
			return JsonResponse.failure(500, e.getMessage());
		}
	}

	/**
	 * 删除用户
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public JsonResponse deleteUser(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			String userId = request.getParameter("userId");
			String batchDel = request.getParameter("batchDel");
			System.out.println(userId);
			if (batchDel != null && batchDel.equals("T")) {
				String[] userIds = userId.split(",");
				userService.deleteBatchUser(userIds);
			} else {
				userService.deleteUser(userId);
			}
			return JsonResponse.success();
		} catch (Exception e) {
			logger.error("删除用户出错", e);
			request.setAttribute("exception", e);
			return JsonResponse.failure(500, e.getMessage());
		}
	}

	/**
	 * 修改用户 是否启用的状态
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/updateUserStatus")
	@ResponseBody
	public JsonResponse updateUserStatus(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			String userId = request.getParameter("userId");
			String currentStatus = request.getParameter("currentStatus");
			Boolean status = false;
			if (currentStatus != null && currentStatus.equals("true")) {
				status = false;
			} else {
				status = true;
			}
			userService.updateUserStatus(userId, status);
			return JsonResponse.success();
		} catch (Exception e) {
			logger.error("删除用户出错", e);
			request.setAttribute("exception", e);
			return JsonResponse.failure(500, e.getMessage());
		}
	}

	/**
	 * 跳转到修改密码页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/modifypwd")
	public String modifypwd_dialog(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			String userId = request.getParameter("userId");
			SysUser user = userService.getUserById(userId);

			request.setAttribute("user", user);
			return getUrlPrefix() + "/user/modifypwd";
		} catch (Exception e) {
			logger.error("用户密码修改页面加载出错", e);
			request.setAttribute("exception", e);
			return forward(ERROR);
		}
	}

	/**
	 * 修改用户密码
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/AdminPassword")
	@ResponseBody
	public JsonResponse adminPassword(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			String userId = request.getParameter("userId");
			String originPassword = request.getParameter("originPassword");
			String newPassword = request.getParameter("loginPassword");

			int ret = userService.updateUserPwd(userId, originPassword,
					newPassword);
			if (ret == -1) {
				return JsonResponse.failure(500, "原密码不正确");
			}
			return JsonResponse.success();
		} catch (Exception e) {
			logger.error("用户修改密码出错", e);
			request.setAttribute("exception", e);
			return JsonResponse.failure(500, e.getMessage());
		}
	}

	/**
	 * 显示新增用户表单
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/add")
	public String userDialogAdd(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			// List<SysDepartment> departments = departmentService.getAll();
			Map<String, String> roles = new HashMap<String, String>();
//			for (SysRole role : SysRole.values()) {
//				roles.put(role.toString(), role.getName());
//			}
//			request.setAttribute("roles", roles);
			SysRole r1=new SysRole();
			r1.setRoleSystem("api");
			List<SysRole> role = sysRoleService.findByEntity(r1);
			
			if(role!=null && role.size()>0){
				 for(SysRole r : role){
					 roles.put(r.getRoleId(), r.getRoleName());
				 }
			 }
			 request.setAttribute("roles", roles);
			String departmentOptions = departmentService
					.getUserDepartOptionString("");
			request.setAttribute("departments", departmentOptions);
			return getUrlPrefix() + "/user/add";
		} catch (Exception e) {
			logger.error("新增用户页面出错", e);
			request.setAttribute("exception", e);
			return forward(ERROR);
		}
	}

	/**
	 * 新增用户
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/addUser")
	@ResponseBody
	public JsonResponse addUser(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			String userName = request.getParameter("userName").trim();
			String loginName = request.getParameter("loginName").trim();
			String loginPassword = request.getParameter("loginPassword").trim();
			String email = request.getParameter("email").trim();
			String roles = request.getParameter("roles");
			String department = request.getParameter("department");
			String activate = request.getParameter("activate");
			String mobile = request.getParameter("mobile").trim();
			SysUser user = new SysUser();
			user.setUserId(StringUtil.getUUID());
			user.setUserName(userName);
			user.setLoginName(loginName);
			user.setMobile(mobile);
			user.setLoginPassword(loginPassword);
			if (activate != null && activate.equals("true"))
				user.setActivate(true);
			else
				user.setActivate(false);
//			user.setRoles(roles);
			user.setCreateTime(new Date());
			user.setEmail(email);
			int ret = userService.addUser(user, department,roles);
			if (ret == -1) {
				return JsonResponse.failure(500, "用户名已存在!");
			}
			return JsonResponse.success();
		} catch (Exception e) {
			logger.error("新增用户出错", e);
			request.setAttribute("exception", e);
			return JsonResponse.failure(500, e.getMessage());
		}
	}

	/**
	 * 校验用户密码
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/validatePwd")
	@ResponseBody
	public JsonResponse validatePwd(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			String pwd = request.getParameter("pwd");
			IUser user = WebUtil.getUserByRequest(request);
			if (user != null) {
				SysUser cur = this.userService.getUserById(user.getUserId());
				if (cur != null && cur.getLoginPassword().equals(pwd)) {
					return JsonResponse.success();
				}
			}
			return JsonResponse.failure(500, "用户密码校验不通过");
		} catch (Exception e) {
			logger.error("用户密码校验失败", e);
			request.setAttribute("exception", e);
			return JsonResponse.failure(500, e.getMessage());
		}
	}

	/**
	 * 校验用户密码
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/resetSecretKey")
	@ResponseBody
	public JsonResponse resetSecretKey(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			IUser user = WebUtil.getUserByRequest(request);
			String userId = user.getUserId();
			String secretKey = StringUtil.getUUID();
			userService.resetSecretKey(userId, secretKey);
			return JsonResponse.success();
		} catch (Exception e) {
			logger.error("用户密码校验失败", e);
			request.setAttribute("exception", e);
			return JsonResponse.failure(500, e.getMessage());
		}
	}
}
