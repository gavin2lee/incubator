package com.harmazing.openbridge.monitor.controller;

import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.harmazing.framework.common.controller.AbstractController;
import com.harmazing.openbridge.monitor.service.IAppMonitorService;
import com.harmazing.openbridge.util.KubernetesUtil;

/**
 * Created by 李杨 [liyang@yihecloud.com] on 2016/8/12 17:26.
 */
@Controller
@RequestMapping("/apps/monitors")
public class AppMonitorController extends AbstractController {
    private Log logger = LogFactory.getLog(this.getClass());
    @Autowired
    private IAppMonitorService monitorAppService;

    @RequestMapping(method = RequestMethod.GET)
    public String getDeploys(ModelMap model) {
        try {
//            IUser user = WebUtil.getUserByRequest(request);
//            String projectType = request.getParameter("projectType");
//            String tenantId = request.getParameter("tenantId");
//            String envType = request.getParameter("envType");
//            String status = request.getParameter("status");
//            String keyword = request.getParameter("keyword");
//            String userId= request.getParameter("userId");
//            int pageNo = StringUtil.getIntParam(request, "pageNo", 1);
//            int pageSize = StringUtil.getIntParam(request, "pageSize", 10);
//            Map<String, Object> params = new HashMap<String, Object>();
//            params.put("pageNo", pageNo);
//            params.put("pageSize", pageSize);
//            params.put("projectType", projectType);
//            params.put("tenantId", tenantId);
//            params.put("envType", envType);
//            params.put("status", status);
//            params.put("keyword", keyword);
//            params.put("userId", userId);
//            Page<PaaSTenant> tenant = paaSTenantService.getPage(1, 100);
//            request.setAttribute("tenant", tenant);
//            Page<Map<String, Object>> pageDate = iPaasProjectDeployService.getAllDeploy(params);
//            request.setAttribute("pageData", pageDate);
//
//            //dxq
//            String remark = ConfigUtil.getConfigString("paasos.evnmark");
//            if(StringUtils.hasText(remark)){
//                JSONObject jo = JSONObject.parseObject(remark);
//                request.setAttribute("envMark", jo);
//            }
            return "/monitor/app/monitor_list";
        } catch (Exception e) {
            logger.error("失败", e);
            model.addAttribute("exception", e);
            return forward(ERROR);
        }
    }

    @RequestMapping(value = "/{projectId}",method = RequestMethod.GET)
    public String getPods(@PathVariable String projectId, ModelMap model) {
        try {
            model.addAttribute("projectDeploys", monitorAppService.findDeployByProjectId(10, projectId));
            model.addAttribute("podList", KubernetesUtil.getPods(null, new HashMap<String, String>() {{
                put("projectCode", monitorAppService.findProjectCodeByProjectId(projectId));
            }}));
            return "/monitor/app/monitor_index";
        } catch (Exception e) {
            logger.error("失败", e);
            model.addAttribute("exception", e);
            return forward(ERROR);
        }
    }
}
