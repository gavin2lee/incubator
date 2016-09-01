package com.lachesis.mnis.web;

import java.lang.reflect.Type;
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

import com.google.gson.reflect.TypeToken;
import com.lachesis.mnis.core.IdentityService;
import com.lachesis.mnis.core.identity.entity.Dict;
import com.lachesis.mnis.core.identity.entity.LoginCardInfo;
import com.lachesis.mnis.core.identity.entity.LoginCardManager;
import com.lachesis.mnis.core.identity.entity.SysDept;
import com.lachesis.mnis.core.identity.entity.UserInformation;
import com.lachesis.mnis.core.util.DateUtil;
import com.lachesis.mnis.core.util.GsonUtils;
import com.lachesis.mnis.core.util.DateUtil.DateFormat;
import com.lachesis.mnis.web.common.ResultCst;
import com.lachesis.mnis.web.common.util.WebContextUtils;
import com.lachesis.mnis.web.common.vo.BaseMapVo;

@Controller
@RequestMapping
public class UserAction {
	private static Logger LOGGER = LoggerFactory.getLogger(UserAction.class);
	@Autowired
	private IdentityService identityService;
	

	@RequestMapping(value = "/deptAction/getDepts")
	@ResponseBody
	public List<SysDept> getDepts() {
		return identityService.queryDeptments();
	}
	
	/**
	 * 根据护士code获取其相应的部门
	 * @param nurseCode
	 * @return
	 */
	@RequestMapping(value = "/deptAction/getDeptmentsByNurseCode")
	@ResponseBody
	public List<SysDept> getDeptmentsByNurseCode(String nurseCode) {
		return identityService.getDeptmentsByCode(nurseCode,null);
	}
	
	/**
	 * 获取所有的科室列表
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/common/queryDeptList")
	public @ResponseBody
	List<SysDept> queryAllDeptList() throws Exception {
		return identityService.queryDeptments();
	}

	/**
	 * 获取所有的诊断列表
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/common/queryDiagList")
	public @ResponseBody
	List<Dict> queryDiagList() throws Exception {
		return identityService.queryDiagList();
	}
	
	/**
	 * 登陆牌信息界面
	 */
	
	@RequestMapping("/nur/loginCard/loginCardInfoMain")
	public String loginCardInfoMain(Model model){
		UserInformation sessionUserInfo = WebContextUtils.getSessionUserInfo();
		
		model.addAttribute("today", DateUtil.format(DateFormat.YMD));
		model.addAttribute("user", sessionUserInfo);
		model.addAttribute("deptName", sessionUserInfo.getDeptName());
		
		model.addAttribute("hospitalName", identityService.getHospitalName());
		model.addAttribute("date", new DateTool()); 
		return "nur/loginCardInfoMain";
	}
	
	/**
	 * 登陆牌生成界面
	 */
	
	@RequestMapping("/nur/loginCard/loginCardManagerMain")
	public String loginCardManagerMain(Model model){
		UserInformation sessionUserInfo = WebContextUtils.getSessionUserInfo();
		
		model.addAttribute("today", DateUtil.format(DateFormat.YMD));
		model.addAttribute("user", sessionUserInfo);
		model.addAttribute("deptName", sessionUserInfo.getDeptName());
		
		model.addAttribute("hospitalName", identityService.getHospitalName());
		model.addAttribute("date", new DateTool()); 
		return "nur/loginCardManagerMain";
	}
	
	/**
	 * 获取登陆牌信息
	 */
	@RequestMapping("/nur/loginCard/getLoginCardInfos")
	@ResponseBody
	public BaseMapVo getLoginCardInfos(String deptCode,String nurseCode,String nurseName){
		
		BaseMapVo vo = new BaseMapVo();
		List<LoginCardInfo> loginCardInfos = null;
		try {
			loginCardInfos = identityService.getLoginCardInfos(deptCode, nurseCode, nurseName);
			vo.addData("loginCardInfos", loginCardInfos);
			vo.setRslt(ResultCst.SUCCESS);
		} catch (Exception e) {
			LOGGER.error("UserAction getLoginCardInfos error:" + e.getMessage());
			vo.setMsg("获取数据失败!");
			vo.setRslt(ResultCst.ALERT_ERROR);
		}
		
		return vo;
	}
	
	/**
	 * 获取登陆牌生命界面信息
	 */
	@RequestMapping("/nur/loginCard/getLoginCardManagers")
	@ResponseBody
	public BaseMapVo getLoginCardManagers(String deptCode,String nurseCode,String nurseName){
		
		BaseMapVo vo = new BaseMapVo();
		List<LoginCardManager> loginCardManagers = null;
		try {
			loginCardManagers = identityService.getLoginCardManagers(deptCode, nurseCode, nurseName);
			vo.addData("loginCardManagers", loginCardManagers);
			vo.setRslt(ResultCst.SUCCESS);
		} catch (Exception e) {
			LOGGER.error("UserAction getLoginCardInfos error:" + e.getMessage());
			vo.setMsg("获取数据失败!");
			vo.setRslt(ResultCst.ALERT_ERROR);
		}
		
		return vo;
	}
	
	/**
	 * 获取登陆牌生命界面信息
	 */
	@RequestMapping("/nur/loginCard/saveLoginCardInfo")
	@ResponseBody
	public BaseMapVo saveLoginCardInfo(String loginCardInfos){
		
		BaseMapVo vo = new BaseMapVo();
		
		if(StringUtils.isBlank(loginCardInfos)){
			LOGGER.error("UserAction saveLoginCardInfo loginCardInfo is null");
			vo.setMsg("保存数据为空!");
			vo.setRslt(ResultCst.ALERT_ERROR);
		}
		
		try {
			Type type = new TypeToken<List<LoginCardInfo>>(){}.getType();
			List<LoginCardInfo> infos = GsonUtils.fromJson(loginCardInfos, type);
			identityService.batchInsertLoginCardInfo(infos);
			vo.setMsg("保存成功!");
			vo.setRslt(ResultCst.SUCCESS);
		} catch (Exception e) {
			LOGGER.error("UserAction saveLoginCardInfo error:" + e.getMessage());
			vo.setMsg("保存失败!");
			vo.setRslt(ResultCst.ALERT_ERROR);
		}
		
		return vo;
	}
	
	/**
	 * 获取登陆牌生命界面作废登陆牌
	 */
	@RequestMapping("/nur/loginCard/invalidLoginCardInfo")
	@ResponseBody
	public BaseMapVo invalidLoginCardInfo(String loginCardInfo){
		
		BaseMapVo vo = new BaseMapVo();
		try {
			LoginCardInfo info = GsonUtils.fromJson(loginCardInfo, LoginCardInfo.class);
			identityService.invalidLoginCardInfo(info);
			vo.setMsg("已作废!");
			vo.setRslt(ResultCst.SUCCESS);
		} catch (Exception e) {
			LOGGER.error("UserAction invalidLoginCardInfo error:" + e.getMessage());
			vo.setMsg("作废失败!");
			vo.setRslt(ResultCst.ALERT_ERROR);
		}
		
		return vo;
	}
	
	/**
	 * 获取登陆牌生命界面作废登陆牌--批量作废
	 */
	@RequestMapping("/nur/loginCard/invalidLoginCardInfos")
	@ResponseBody
	public BaseMapVo invalidLoginCardInfos(String loginCardInfos){
		
		BaseMapVo vo = new BaseMapVo();
		if(StringUtils.isBlank(loginCardInfos)){
			LOGGER.error("UserAction invalidLoginCardInfos loginCardInfo is null");
			vo.setMsg("作废数据为空!");
			vo.setRslt(ResultCst.ALERT_ERROR);
		}
		
		try {
			Type type = new TypeToken<List<LoginCardInfo>>(){}.getType();
			List<LoginCardInfo> infos = GsonUtils.fromJson(loginCardInfos, type);
			identityService.batchInvalidLoginCardInfos(infos);
			vo.setMsg("已作废!");
			vo.setRslt(ResultCst.SUCCESS);
		} catch (Exception e) {
			LOGGER.error("UserAction invalidLoginCardInfo error:" + e.getMessage());
			vo.setMsg("作废失败!");
			vo.setRslt(ResultCst.ALERT_ERROR);
		}
		
		return vo;
	}
}
