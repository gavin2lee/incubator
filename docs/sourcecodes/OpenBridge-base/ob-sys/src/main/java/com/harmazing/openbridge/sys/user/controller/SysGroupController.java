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

import com.harmazing.framework.common.JsonResponse;
import com.harmazing.framework.common.controller.AbstractController;
import com.harmazing.framework.authorization.IUser;
import com.harmazing.openbridge.sys.user.model.SysGroup;
import com.harmazing.openbridge.sys.user.service.ISysGroupService;
import com.harmazing.framework.util.StringUtil;
import com.harmazing.framework.util.WebUtil;

/**
 * 
 * @author taoshuangxi
 *
 */
@Controller
@RequestMapping("/sys/user/group")
public class SysGroupController extends AbstractController {
	private static final Log logger = LogFactory
			.getLog(SysGroupController.class);

	@Autowired
	private ISysGroupService groupService;
	
	/**
	 * 群组管理首页，列表显示
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/page")
	public String page(HttpServletRequest request, 
			HttpServletResponse response){
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			String keyWord = request.getParameter("keyWords");
			if(keyWord!=null && !keyWord.trim().equals("")){
				params.put("keyword", keyWord);
				request.setAttribute("keyWords", keyWord);
			}
			params.put("pageNo", StringUtil.getIntParam(request, "pageNo", 1));
			params.put("pageSize",
					StringUtil.getIntParam(request, "pageSize", 10));

			List<Map<String, Object>> pageData = groupService.groupPage(params);
			request.setAttribute("pageData", pageData);
			return getUrlPrefix() + "/page";
		} catch (Exception e) {
			logger.error("群组列表页面出错", e);
			request.setAttribute("exception", e);
			return forward(ERROR);
		}
	}
	
	
	/**
	 * 删除用户
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public JsonResponse deleteGroup(HttpServletRequest request,
			HttpServletResponse response){
		try{
			String groupId = request.getParameter("groupId");
			String batchDel = request.getParameter("batchDel");
			System.out.println(groupId);
			if(batchDel!=null && batchDel.equals("T")){
				String[] groupIds = groupId.split(",");
				groupService.deleteBatchGroup(groupIds);
			}
			else
			{
				groupService.deleteGroup(groupId);
			}
			return JsonResponse.success();
		}catch(Exception e){
			logger.error("删除群组出错", e);
			request.setAttribute("exception", e);
			return JsonResponse.failure(500, e.getMessage());
		}
	}

	/**
	 * 打开新建或修改群组对话框
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/addOrUpdate")
	public String group_addOrUpdate(HttpServletRequest request, 
			HttpServletResponse response){
		try {
			String groupId = request.getParameter("groupId");
			if(groupId!=null && !groupId.trim().equals("")){
				SysGroup group = groupService.getGroupById(groupId);
				request.setAttribute("groupId", groupId);
				request.setAttribute("groupName", group.getGroupName());
				
				List<Map<String, Object>> members = groupService.getUsersByGroupId(groupId);
				group.setUsers(members);
				request.setAttribute("groupUser", group);
				/*StringBuilder userIds = new StringBuilder();
				StringBuilder userNames = new StringBuilder();
				for(Map<String,Object> user: members){
					userIds.append(user.get("userId"));
					userIds.append(",");
					//userNames.append(user.get("userName"));
					//userNames.append(",");
				}
				String userId=userIds.toString();
				String userName = userNames.toString();
				if (userId.length()>0){
					request.setAttribute("groupUserId", userId.substring(0, userId.length()-1));
					request.setAttribute("groupUserName", userName.substring(0, userName.length()-1));
				}*/
			}
			return getUrlPrefix() + "/addOrModify";
		} catch (Exception e) {
			logger.error("群组页面出错", e);
			request.setAttribute("exception", e);
			return forward(ERROR);
		}
	}
	
	/**
	 * 新增或修改群组
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/saveOrUpdate")
	@ResponseBody
	public JsonResponse saveOrUpdateTag(HttpServletRequest request,
			HttpServletResponse response){
		try{
			String groupId = request.getParameter("groupId");
			String groupName = request.getParameter("groupName");
			String members = request.getParameter("members");
			String originMember = request.getParameter("originMember");
			
			if (groupId!=null && !groupId.equals("")){
				int ret = groupService.updateGroup(groupId,groupName, originMember,members);
				if(ret ==-1){
					return JsonResponse.failure(500, "群组名已存在!");
				}
			}else{
				
				SysGroup group = new SysGroup();
				group.setGroupId(StringUtil.getUUID());
				group.setGroupName(groupName);
				group.setCreateTime(new Date());
				IUser user = WebUtil.getUserByRequest(request);
				group.setCreaterId(user.getUserId());
				int ret =groupService.addGroup(group,members);
				if(ret ==-1){
					return JsonResponse.failure(500, "群组名已存在!");
				}
			}
			
			return JsonResponse.success();
		}catch(Exception e){
			logger.error("群组保存出错", e);
			request.setAttribute("exception", e);
			return JsonResponse.failure(500, e.getMessage());
		}
	}
}
