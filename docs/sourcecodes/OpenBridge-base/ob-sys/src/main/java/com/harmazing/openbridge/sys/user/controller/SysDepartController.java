package com.harmazing.openbridge.sys.user.controller;


import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.harmazing.framework.common.JsonResponse;
import com.harmazing.framework.common.controller.AbstractController;
import com.harmazing.framework.authorization.IUser;
import com.harmazing.openbridge.sys.user.model.SysDepart;
import com.harmazing.openbridge.sys.user.service.ISysDepartService;
import com.harmazing.framework.util.LogUtil;
import com.harmazing.framework.util.WebUtil;

@Controller
@RequestMapping("/sys/user/depart")
public class SysDepartController extends AbstractController{
	private static final Log logger = LogFactory
			.getLog(SysDepartController.class);
	@Autowired
	private ISysDepartService departService;
	
	@RequestMapping("/index")
	public String index(HttpServletRequest request, Model model){
		try {
			String levelStructureStr = departService.getLevelStrutString();
			model.addAttribute("levelStructureStr", levelStructureStr);
			String departOptions = departService.getDepartOptionString(null);
			model.addAttribute("departOptions", departOptions);
			return getUrlPrefix() + "/index";
		} catch (Exception e) {
			logger.error("组织机构页面出错", e);
			request.setAttribute("exception", e);
			return forward(ERROR);
		}
	}

	@RequestMapping("/loadDepart")
	@ResponseBody
	public String loadDepart(HttpServletRequest request, String id) {
		JSONObject json = new JSONObject();
		try {
			SysDepart currentDepart = departService.getDepartById(id);
			String departOptions = departService.getDepartOptionString(currentDepart);
			
			json.put("option", departOptions);
			json.put("departName", currentDepart.getDeptName());
			json.put("order", currentDepart.getdOrder());
			json.put("msg", "success");
		} catch (Exception e) {
			LogUtil.error(e.toString());
			json.put("msg", "faild");
		}
		return json.toJSONString();

	}
	
	@RequestMapping("/deleteDepart")
	@ResponseBody
	public JsonResponse deleteDepart(HttpServletRequest request, HttpServletResponse Response) {
		try {
			String departId = request.getParameter("departId");
			int ret= departService.deleteDepartById(departId);
			if(ret==-1){
				return JsonResponse.failure(500, "该部门包含子部门，不能被删除");
			}
			return JsonResponse.success();
		} catch (Exception e) {
			logger.error("删除部门出错", e);
			request.setAttribute("exception", e);
			return JsonResponse.failure(500, e.getMessage());
		}
	}
	
	@RequestMapping("/saveOrUpdate")
	@ResponseBody
	public JsonResponse saveOrUpdate(HttpServletRequest request, HttpServletResponse Response) {
		try {
			String departId = request.getParameter("departId");
			String departName = request.getParameter("departName");
			String dOrder = request.getParameter("dOrder");
			String parentId = request.getParameter("parentId");
			SysDepart depart = new SysDepart();
			if(departId==null ||departId.equals("")){
				depart.setCreateTime(new Date());
				IUser user = WebUtil.getUserByRequest(request);
				depart.setCreateUser(user.getUserId());
			}
			depart.setDeptId(departId);
			depart.setDeptName(departName);
			depart.setParentId(parentId);
			depart.setdOrder(Integer.parseInt(dOrder));
			int ret = departService.saveOrUpdateDepart(depart);
			if(ret==-1){
				return JsonResponse.failure(500, "部门名重复");
			}
			return JsonResponse.success();
		} catch (Exception e) {
			logger.error("部门信息保存出错", e);
			request.setAttribute("exception", e);
			return JsonResponse.failure(500, e.getMessage());
		}
	}
	
}
