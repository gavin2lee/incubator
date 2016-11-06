package com.harmazing.openbridge.alarm.controller;

import com.harmazing.openbridge.alarm.model.Action;
import com.harmazing.openbridge.alarm.model.User;
import com.harmazing.openbridge.alarm.model.vo.ActionFalconDTO;
import com.harmazing.openbridge.alarm.service.IActionService;
import com.harmazing.openbridge.alarm.service.ITeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 李杨 [liyang@yihecloud.com] on 2016/8/8 16:10.
 * 为open-falcon Alarm组件提供服务，/team/users*;/api/action/**必须能够匿名访问
 */
@Controller
@RequestMapping
public class AlarmFalconController {
    @Autowired
    private IActionService actionService;
    @Autowired
    private ITeamService teamService;

    @RequestMapping(value = "/api/action/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Map getById(@PathVariable long id) {
        Map<String,Object> map = new HashMap<>();
        map.put("msg","");
        map.put("data",actionService.findFalconById(id));
        return map;
    }

    @RequestMapping(value="/team/users",method=RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> getAllUsesrByTeamName(@RequestParam String name){
        Map<String,Object> map = new HashMap<>();
        map.put("msg","");
        map.put("users",teamService.getAllUserByTeamName(name));
        return map;
    }
}
