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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.harmazing.framework.common.JsonResponse;
import com.harmazing.framework.util.StringUtil;
import com.harmazing.openbridge.alarm.model.Template;
import com.harmazing.openbridge.alarm.model.vo.ActionDTO;
import com.harmazing.openbridge.alarm.model.vo.SysUserDTO;
import com.harmazing.openbridge.alarm.service.IActionService;
import com.harmazing.openbridge.alarm.service.ITeamService;
import com.harmazing.openbridge.alarm.service.ITemplateService;

/**
 * Created by 李杨 [liyang@yihecloud.com] on 2016/8/3 17:33.
 */
@Controller
@RequestMapping("/templates")
public class TemplateController {
    private Log logger = LogFactory.getLog(TemplateController.class);
    @Autowired
    private ITemplateService templateService;

    @Autowired
    private ITeamService teamService;
    
    @Autowired
    private IActionService actionService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView findByAll() {
        return new ModelAndView("/alarm/template/index", "templates", templateService.findAllDTO());
    }

	@RequestMapping(value="/list")
	public String list(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> params = new HashMap<String, Object>();
    	params.put("pageNo", StringUtil.getIntParam(request, "pageNo", 1));
		params.put("pageSize",
				StringUtil.getIntParam(request, "pageSize", 10));
		String keyword = request.getParameter("keyword");
		params.put("keyword", keyword);
		List<Map<String, Object>> pageData = templateService.Page(params);
		request.setAttribute("pageData", pageData);
		request.setAttribute("keyword", keyword);
		return  "/alarm/template/list";
	}
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody()
    public JsonResponse create(@RequestBody Template template) {
        JsonResponse jsonResponse = JsonResponse.success();
        try{
            templateService.insert(template);
        }catch(Exception e){
        	logger.error("创建模板报错",e);
            jsonResponse = JsonResponse.failure(1, e.getMessage());
        }
        return jsonResponse;
    }

    @RequestMapping(value="/{id}",method = RequestMethod.GET)
    public ModelAndView get(@PathVariable long id) {
        return new ModelAndView("/alarm/template/edit","template",templateService.findDtoById(id));
    }

    @RequestMapping(value="/{id}",method = RequestMethod.PUT)
    @ResponseBody()
    public JsonResponse edit(@PathVariable long id, @RequestParam(value = "tplName")String tplName) {
        JsonResponse jsonResponse = JsonResponse.success();
        try{
            templateService.updateTplNameById(tplName,id);
        }catch(Exception e){
            logger.error(e);
            jsonResponse = JsonResponse.failure(1, e.getMessage());
        }
        return jsonResponse;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody()
    public JsonResponse deleteById(@PathVariable long id){
        JsonResponse jsonResponse = JsonResponse.success();
        try{
            templateService.deleteById(id);
        }catch(Exception e){
            jsonResponse = JsonResponse.failure(1, e.getMessage());
        }
        return jsonResponse;
    }

    @RequestMapping(value="/{id}/actions",method = RequestMethod.POST)
    @ResponseBody()
    public JsonResponse create(@PathVariable long id,@RequestBody ActionDTO dto) {
        JsonResponse jsonResponse = JsonResponse.success();
        try{
            dto.setTplId(id);
            if(teamService.getTeamByName(dto.getUic())==null){
                return JsonResponse.failure(1, "报警接收组不存在");
            }
            if(actionService.getCountByUic(dto.getUic())>0){
            	if(actionService.insert(dto)==null){
            		return JsonResponse.failure(1, "创建失败");
            	}          	
            }else{
            	return JsonResponse.failure(1, "该名称已存在，请重新输入名称！");
            }
        }catch(Exception e){
            jsonResponse = JsonResponse.failure(1, e.getMessage());
        }
        return jsonResponse;
    }

    @RequestMapping(value="/{id}/users",method = RequestMethod.GET)
    public String getSysusersById(@PathVariable long id,HttpServletRequest request) {
        try{

             List<SysUserDTO>	sysUsers=templateService.findUserByTid(id);   	  
             request.setAttribute("user", sysUsers);
        }catch(Exception e){
        	logger.error("得到sysusersId出错", e);          
        }   
        return "/alarm/template/user/list";
    }
}
