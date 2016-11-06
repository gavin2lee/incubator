package com.harmazing.openbridge.alarm.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
import com.harmazing.framework.util.WebUtil;
import com.harmazing.openbridge.alarm.model.Strategy;
import com.harmazing.openbridge.alarm.model.Template;
import com.harmazing.openbridge.alarm.model.vo.StrategyEditDTO;
import com.harmazing.openbridge.alarm.service.IStrategyService;
import com.harmazing.openbridge.alarm.service.ITemplateService;
import com.harmazing.openbridge.alarm.util.MetricsUtil;

/**
 * Created by 李杨 [liyang@yihecloud.com] on 2016/8/4 14:18.
 */
@Controller
@RequestMapping("/strategies")
public class StrategyController {
    private Logger logger = Logger.getLogger(this.getClass());
    @Autowired
    private ITemplateService templateService;
    @Autowired
    private IStrategyService strategyService;
    @Autowired
    private MetricsUtil metricsUtil;

    @RequestMapping(value="/metrics",method = RequestMethod.GET)
    @ResponseBody()
    public List<String> get(@RequestParam String query) {
        return metricsUtil.getMertics(query);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView findByAll() {
        return new ModelAndView("/alarm/template/index", "templates", templateService.findAllDTO());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody()
    public String deleteById(@PathVariable long id){
        strategyService.deleteById(id);
        return "template";
    }
    
    @RequestMapping(value = "/modify",method = RequestMethod.POST)
    @ResponseBody()
    public JsonResponse modify(@RequestBody Strategy strategy){
        JsonResponse jsonResponse = JsonResponse.success();
        try{
            strategyService.update(strategy);
        }catch(Exception e){
            jsonResponse = JsonResponse.failure(1, e.getMessage());
        }
        return jsonResponse;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody()
    public JsonResponse create(@RequestBody Strategy strategy){
        JsonResponse jsonResponse = JsonResponse.success();
        try{
            if(!metricsUtil.isExistMetrics(strategy.getMetric())){
                return JsonResponse.failure(1, "Metric不存在");
            }
            strategyService.insert(strategy);
        }catch(Exception e){
            jsonResponse = JsonResponse.failure(1, e.getMessage());
        }
        return jsonResponse;
    }
    
    /**
     * @author luoan
     * 用于在monitor的策略编辑界面接收提交的结果。增加也可以
     * @param strategies
     * @return
     */
    @RequestMapping(value="/editStrategies",method = RequestMethod.POST)
    @ResponseBody()
    public JsonResponse editStrategies(@RequestBody StrategyEditDTO strategies,HttpServletRequest request){
    	JsonResponse jsonResponse = JsonResponse.success();
    	IUser user = WebUtil.getUserByRequest(request);
    	//来标识tpl的id
    	long id = 0;
        try{
              	if(strategies.getTplId() == 0){
        		//策略组增加，由于众多是默认的，所以只需将需要的参数传入就可以了。
        		//插入
        		Template template = new Template();
        		template.setActionId(strategies.getActionId());
        		template.setTplName(strategies.getTplName());
        		template.setParentId(0);
        		template.setCreateUser(user.getLoginName());
        		templateService.insert(template);
        		//获得新template的id
        		Template newTemplate = templateService.findByTplName(strategies.getTplName());
        		for(int i = 0;i < strategies.getStrategys().size();i++){
        			strategies.getStrategys().get(i).setTplId(newTemplate.getId());
            		strategyService.insert(strategies.getStrategys().get(i));
            	}
        		id = newTemplate.getId();
        	}else{
        		//属于修改
                //有3个动作，1、更新策略组名称;2、更新报警接收组；更新策略项；
            	//1、更新报警接收组
            	templateService.updateActionIdById(strategies.getActionId(), strategies.getTplId());
            	//2、更新策略组名称
            	templateService.updateTplNameById(strategies.getTplName(), strategies.getTplId());
            	//更新策略项组
            	//先把所有数据删除。
            	strategyService.deleteByTplId(strategies.getTplId());
            	for(int i = 0;i < strategies.getStrategys().size();i++){
            		strategyService.insert(strategies.getStrategys().get(i));
            	}
            	id = strategies.getTplId();
        	}
              	jsonResponse = new JsonResponse(0,id);
        }catch(Exception e){
        	logger.error("策略更新出错editStrategies", e);
            jsonResponse = JsonResponse.failure(1, e.getMessage());
        }
        //返回特定id供刷新
        return jsonResponse;
    }
}
