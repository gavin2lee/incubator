package com.harmazing.openbridge.alarm.controller;

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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.harmazing.framework.authorization.IUser;
import com.harmazing.framework.common.JsonResponse;
import com.harmazing.framework.common.Page;
import com.harmazing.framework.common.controller.AbstractController;
import com.harmazing.framework.util.ExceptionUtil;
import com.harmazing.framework.util.StringUtil;
import com.harmazing.framework.util.WebUtil;
import com.harmazing.openbridge.alarm.model.Team;
import com.harmazing.openbridge.alarm.service.ITeamService;
import com.harmazing.openbridge.sys.user.model.SysUser;


@Controller
@RequestMapping("/teams")
public class TeamController extends AbstractController {
	private static final Log logger = LogFactory
			.getLog(TeamController.class);
	
	   @Autowired
	    private ITeamService teamService;
	   	   
		@RequestMapping
		public String list(HttpServletRequest request, HttpServletResponse response){
			return "alarm/team/list";
		}
		@RequestMapping("/table")
		public String list(HttpServletRequest request, HttpServletResponse response,
				Integer pageNo,Integer pageSize) {
			if(pageNo==null)	pageNo = 1;
			if(pageSize==null)	pageSize = 10;			
			Page<Team> page = teamService.getPage(pageNo, pageSize);
			request.setAttribute("page", page);
			return  "alarm/team/table";
		}
	   
	/**
	 * 列表显示信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/page")
	public String page(HttpServletRequest request,
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

		//	List<Map<String, Object>> pageData = teamService.getPage(pageNo,pageSize);
			//request.setAttribute("pageData", pageData);
			return getUrlPrefix() + "/page";
		} catch (Exception e) {
			logger.error("列表页面出错", e);
			request.setAttribute("exception", e);
			return forward(ERROR);
		}
	}
	
	@RequestMapping("/add")
	public String addTenant(HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");
		Team team = new Team();
		if(StringUtil.isNotNull(id)){
			team = teamService.get(id);
		}
		request.setAttribute("team", team);
		return  "alarm/team/add";
	}
	
	@RequestMapping("/saveOrUpdate")
	@ResponseBody
	public JsonResponse saveOrUpdate(String json,HttpServletRequest request) {
		Team team = null;
		IUser user = WebUtil.getUserByRequest(request);
		JsonResponse jsonResponse = JsonResponse.success();
		try{
			if(StringUtil.isNotNull(json)){
				team = JSON.parseObject(json, Team.class);
			}
			if(team!=null){
				teamService.saveOrUpdate(team,user.getUserId());
			}
		}catch(Exception e){
			jsonResponse = JsonResponse.failure(1, e.getMessage());
		}
		return jsonResponse;
	}
	
	@RequestMapping("/delete")
	@ResponseBody
	public JsonResponse delete(@RequestParam String id) {
		JsonResponse jsonResponse = JsonResponse.success();
		try{
			teamService.delete(id);
		}catch(Exception e){
			jsonResponse = JsonResponse.failure(1, ExceptionUtil.getExceptionString(e));
		}
		return jsonResponse;
	}
	
	@RequestMapping("/detail")
	public String detail(HttpServletRequest request, HttpServletResponse response) {
		
		String userId = request.getParameter("userId");
		SysUser user = teamService.getUserById(userId);
		request.setAttribute("user", user);
		return  "alarm/team/detail";
	}

	@RequestMapping(value="/listTeam",method = RequestMethod.GET)
	public String getTeamName(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> params = new HashMap<String, Object>();
    	params.put("pageNo", StringUtil.getIntParam(request, "pageNo", 1));
		params.put("pageSize",
				StringUtil.getIntParam(request, "pageSize", 10));
		String keyword = request.getParameter("keyword");
		params.put("keyword", keyword);
		List<Map<String, Object>> pageData = teamService.Page(params);
		request.setAttribute("pageData", pageData);
		request.setAttribute("keyword", keyword);
		return  "alarm/template/listTeam";
	}
}
