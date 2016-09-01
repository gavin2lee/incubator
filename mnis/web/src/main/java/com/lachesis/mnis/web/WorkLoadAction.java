package com.lachesis.mnis.web;

import java.security.Identity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.velocity.tools.generic.DateTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lachesis.mnis.core.IdentityService;
import com.lachesis.mnis.core.WorkLoadService;
import com.lachesis.mnis.core.identity.IdentityServiceImpl;
import com.lachesis.mnis.core.identity.entity.UserInformation;
import com.lachesis.mnis.core.util.DateUtil;
import com.lachesis.mnis.core.util.DateUtil.DateFormat;
import com.lachesis.mnis.web.common.ResultCst;
import com.lachesis.mnis.web.common.util.WebContextUtils;
import com.lachesis.mnis.web.common.vo.BaseMapVo;

/**
 * 工作量统计
 * 
 * @author ThinkPad
 *
 */
@RequestMapping("/nur/workload")
@Controller
public class WorkLoadAction {

	private static Logger LOGGER = LoggerFactory
			.getLogger(WorkLoadAction.class);

	@Autowired
	private WorkLoadService workLoadService;
	@Autowired
	private IdentityService identityService;

	@RequestMapping("/workLoadMain")
	public String workLoadMain(Model model) {
		UserInformation sessionUserInfo = WebContextUtils.getSessionUserInfo();
		
		model.addAttribute("today", DateUtil.format(DateFormat.YMD));
		model.addAttribute("user", sessionUserInfo);
		model.addAttribute("deptName", sessionUserInfo.getDeptName());
		
		model.addAttribute("hospitalName", identityService.getHospitalName());
		model.addAttribute("date", new DateTool()); 
		return "/nur/workLoadMain";
	}

	@RequestMapping("/getWorkLoadTypes")
	@ResponseBody
	public BaseMapVo getWorkLoadTypes() {
		BaseMapVo vo = new BaseMapVo();
		vo.addData("types", workLoadService.getWorkLoadTypes());
		vo.setRslt(ResultCst.SUCCESS);
		return vo;
	}

	@RequestMapping("/getWorkLoadInfosByNurse")
	@ResponseBody
	public BaseMapVo getWorkLoadInfosByNurse(String deptCode,
			String nurseCode, String types, String startDate, String endDate) {
		BaseMapVo vo = new BaseMapVo();

		List<String> workLoadTypes = new ArrayList<String>();
		if (StringUtils.isNotBlank(types)) {
			Collections.addAll(workLoadTypes, types.split(","));
		}
		try {
			vo.addData("wlInfs", workLoadService.getWorkLoadInfosByNurse(
					deptCode, nurseCode, workLoadTypes, startDate, endDate));
			vo.setRslt(ResultCst.SUCCESS);
		} catch (Exception e) {
			LOGGER.error("WorkLoadAction getWorkLoadInfosByNurse error:"
					+ e.getMessage());
			vo.setRslt(ResultCst.ALERT_ERROR);
			vo.setMsg("获取数据异常!");
		}

		return vo;
	}

	@RequestMapping("/getWorkLoadInfosByNurseType")
	@ResponseBody
	public BaseMapVo getWorkLoadInfosByNurseType(String deptCode,
			String nurseCode, String types, String childrenTypes,
			String startDate, String endDate) {
		BaseMapVo vo = new BaseMapVo();

		List<String> workLoadTypes = new ArrayList<String>();
		if (StringUtils.isNotBlank(types)) {
			Collections.addAll(workLoadTypes, types.split(","));
		}

		List<String> childrenWorkLoadTypes = new ArrayList<String>();
		if (StringUtils.isNotBlank(childrenTypes)) {
			Collections.addAll(childrenWorkLoadTypes, childrenTypes.split(","));
		}

		try {
			vo.addData("wlInfs", workLoadService
					.getWorkLoadInfosByNurseType(deptCode, nurseCode,
							workLoadTypes, childrenWorkLoadTypes, startDate,
							endDate));
			vo.setRslt(ResultCst.SUCCESS);
		} catch (Exception e) {
			LOGGER.error("WorkLoadAction getWorkLoadInfosByNurseType error:"
					+ e.getMessage());
			vo.setRslt(ResultCst.ALERT_ERROR);
			vo.setMsg("获取数据异常!");
		}

		return vo;
	}
}
