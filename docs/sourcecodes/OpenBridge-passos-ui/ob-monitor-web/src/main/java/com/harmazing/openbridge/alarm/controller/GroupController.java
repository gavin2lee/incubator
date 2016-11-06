package com.harmazing.openbridge.alarm.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.harmazing.framework.authorization.IUser;
import com.harmazing.framework.common.JsonResponse;
import com.harmazing.framework.common.Page;
import com.harmazing.framework.util.ExceptionUtil;
import com.harmazing.framework.util.StringUtil;
import com.harmazing.framework.util.WebUtil;
import com.harmazing.openbridge.alarm.model.Group;
import com.harmazing.openbridge.alarm.model.User;
import com.harmazing.openbridge.alarm.model.vo.GroupIndexDTO;
import com.harmazing.openbridge.alarm.model.vo.GroupManageDTO;
import com.harmazing.openbridge.alarm.model.vo.TemplateEditDTO;
import com.harmazing.openbridge.alarm.model.vo.TemplateIndexDTO;
import com.harmazing.openbridge.alarm.service.IGroupService;
import com.harmazing.openbridge.alarm.service.IHostService;
import com.harmazing.openbridge.alarm.service.ITemplateService;
import com.harmazing.openbridge.alarm.util.PageUtil;

/**
 * Created by 李杨 [liyang@yihecloud.com] on 2016/7/28 17:32.
 */
@Controller
@RequestMapping("/groups")
public class GroupController {
    private Logger logger = Logger.getLogger(this.getClass());
    @Autowired
    private IHostService hostService;
    @Autowired
    private IGroupService groupService;
    @Autowired
    private ITemplateService templateService;
    
   /* @RequestMapping(method = RequestMethod.GET)
    public ModelAndView findByAll() {
        return new ModelAndView("/alarm/group/index", "groups", groupService.findAllDTO());
    }*/
   @RequestMapping(method=RequestMethod.GET)
    public String findByAll(HttpServletRequest request,Integer pageNo,Integer pageSize) {
    	Page<GroupIndexDTO> page=null;
    	try {
    	if(pageNo==null) pageNo=1;
    	if(pageSize==null)  pageSize=6;
    	 page= groupService.getPageDTO(pageNo, pageSize);
    	 request.setAttribute("page", page);
        return "/alarm/group/index";
    	} catch (Exception e) {
            logger.error("查询group详情的时候出错", e);
            return "/alarm/group/index";
        }
    }
     
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ModelAndView getById(@PathVariable long id) {    	
    	Map<String,Object> map = new HashMap<>();
    	try{
    	map.put("hosts",hostService.findAll());
    	map.put("group", groupService.findById(id));
    	map.put("groupHosts", hostService.findByGroupId(id));
    	map.put("groupTpls", templateService.findByGroupId(id));
        return new ModelAndView("/alarm/group/edit", map);
    	} catch (Exception e) {
            logger.error("查询group详情的时候出错", e);
            return new ModelAndView("/alarm/group/edit", map);
        }
    }

    @RequestMapping(value = "/{id}/hosts", method = RequestMethod.GET)
    public ModelAndView getById(@PathVariable long id,
                                @RequestParam(value = "pageNo", required = false) Integer pageNo,
                                @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        try {
            Map<String, Object> params = PageUtil.initPageSize(pageNo, pageSize);
            params.put("id", id);
            return new ModelAndView("/alarm/host/index", "hosts", hostService.pageFindByGroupId(params));
        } catch (Exception e) {
            logger.error("查询hosts出错", e);
            return new ModelAndView("/alarm/host/index", "hosts", "");
        }
    }

    @RequestMapping(value = "/{id}/users")
    public String getUsersByGroupId(@PathVariable long id,HttpServletRequest request,Integer pageNo,Integer pageSize) {    
    	try{   
    		if(pageNo==null) pageNo=1;
        	if(pageSize==null)  pageSize=4;
    	 Page<User> page = groupService.findUserPageByGroupId(id,pageNo,pageSize);
    	 request.setAttribute("page", page);
    	return "alarm/group/user/list";		
    	}catch (Exception e) {
            logger.error("查询user出错", e);
            return  "alarm/group/user/list";
        }
    }
    
    
    @RequestMapping(value = "/{id}/templates")
    public String getStrategyByGroupId(@PathVariable long id,HttpServletRequest request,Integer pageNo,Integer pageSize) {    
    	//List<TemplateIndexDTO> templates = templateService.findByGroupId(id);
    	
    	if(pageNo==null) pageNo=1;
    	if(pageSize==null) pageSize=2;
    	try{
    	Page<TemplateEditDTO> page = templateService.findDtoPageByGroupId(id, pageNo, pageSize);
    	request.setAttribute("page", page);
    	return "alarm/group/template/list";
    	} catch (Exception e) {
            logger.error("查询templates出错", e);
            return "alarm/group/template/list";
        }
    }
    
    @RequestMapping(value="/create",method = RequestMethod.GET)
    public ModelAndView create() {
        return new ModelAndView("/alarm/group/create","hosts",hostService.findAll());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody()
    public String deleteById(@PathVariable long id){
        groupService.deleteById(id);
        return "groups";
    }
    @RequestMapping("/delete")
	@ResponseBody
	public JsonResponse delete(@RequestParam  long  id) {
		JsonResponse jsonResponse = JsonResponse.success();
		try{
			groupService.deleteById(id);
		}catch(Exception e){
			jsonResponse = JsonResponse.failure(1, ExceptionUtil.getExceptionString(e));
		}
		return jsonResponse;
	}
    @RequestMapping("/save")
	@ResponseBody
	public JsonResponse save(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			IUser user = WebUtil.getUserByRequest(request);
			String data = request.getParameter("data");
			String grpHostName=request.getParameter("grpHostName");
			String  id=request.getParameter("id");
			Group group = new Group();
			group.setGrpName(grpHostName);
			group.setComeFrom("1");
			if(StringUtil.isNotNull(id)){
				group.setId(Long.parseLong(id));
			}
			groupService.save(user.getUserId(),data,group);
			return JsonResponse.success();
		} catch (Exception e) {
			logger.error("新增节点组出错", e);
			return JsonResponse.failure(500, e.getMessage());
		}
	}
    @RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public JsonResponse saveDTO(@RequestBody GroupManageDTO dto) {
		try {
			groupService.saveDTO(dto);
			return JsonResponse.success();
		} catch (Exception e) {
			logger.error("新增节点组出错", e);
			return JsonResponse.failure(500, e.getMessage());
		}
	}
    
    @RequestMapping("/host/list")
	public String list(HttpServletRequest request, HttpServletResponse response) {
    	
    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("pageNo", StringUtil.getIntParam(request, "pageNo", 1));
		params.put("pageSize",
				StringUtil.getIntParam(request, "pageSize", 10));
		String keyword = request.getParameter("keyword");
		params.put("keyword", keyword);
		List<Map<String, Object>> pageData = hostService.Page(params);
		request.setAttribute("pageData", pageData);
		request.setAttribute("keyword", keyword);
		return  "/alarm/group/host/list";
	}
}
