/**
 * (C) Copyright. LACHESIS All rights reserved.
 *
 */
package com.lachesis.mnis.web;

import com.lachesis.mnis.core.IdentityService;
import com.lachesis.mnis.core.PatientService;
import com.lachesis.mnis.core.exception.MnisException;
import com.lachesis.mnis.core.identity.entity.FPDataBean;
import com.lachesis.mnis.core.identity.entity.SysUser;
import com.lachesis.mnis.core.identity.entity.UserFinger;
import com.lachesis.mnis.core.identity.entity.UserInformation;
import com.lachesis.mnis.core.patient.entity.Patient;
import com.lachesis.mnis.core.util.DateUtil;
import com.lachesis.mnis.core.util.DateUtil.DateFormat;
import com.lachesis.mnis.web.common.ResultCst;
import com.lachesis.mnis.web.common.util.WebContextUtils;
import com.lachesis.mnis.web.common.vo.BaseDataVo;
import com.lachesis.mnis.web.common.vo.BaseMapVo;
import com.lachesis.mnis.web.common.vo.BaseVo;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.tools.generic.DateTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The class IndexAction.
 * 
 * 系统登录及主界面
 * 
 * @author: yanhui.wang
 * @since: 2014-6-10
 * @version: $Revision$ $Date$ $LastChangedBy$
 *
 */
@Controller
@RequestMapping("/index")
public class IndexAction {
	static final Logger LOGGER = LoggerFactory.getLogger(IndexAction.class);

	@Autowired
	private PatientService patientService;
	@Autowired
	private IdentityService identityService;

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(DateUtil.FORMAT_FULL, true));
	}
	
	/**
	 * 判断是否已经登录过，如果未登录，则进入登录界面
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/login")
	public String login(HttpServletRequest request) {
		return "/index/login";
	}

	@RequestMapping("/nurseLogin")
	public String nurseLogin(HttpServletRequest request,HttpServletResponse response
			,String deptCode,String patId,Model model){
	//用户登录信息
		UserInformation sessionUserInfo = new UserInformation();
		SysUser user = new SysUser();
		user.setCode("admin");
		user.setName("admin");
		sessionUserInfo.setDeptCode(deptCode);
		sessionUserInfo.setUser(user);
		setUserInformationToServer(request, sessionUserInfo);
		//获取患者信息
		Patient patient = patientService.getPatientByPatId(patId);
		//传递给页面的信息
		model.addAttribute("id", patId);
		
		model.addAttribute("patient", patient);
		model.addAttribute("currPatient", patient);
		model.addAttribute("today", DateUtil.format(DateFormat.YMD));
		model.addAttribute("user", sessionUserInfo);
		model.addAttribute("deptName", sessionUserInfo.getDeptName());
		model.addAttribute("hospitalName", identityService.getHospitalName());
		model.addAttribute("date", new DateTool()); 
		model.addAttribute("deptCode",deptCode);
		model.addAttribute("viewHead",0);//不显示
		model.addAttribute("edit",0);//不可编辑
		
		return "/index/patientMainFrame";
	}
	
	/**
	 * NDA端密码登录
	 * 
	 * @param loginName
	 * @param password
	 * @return
	 */
	@RequestMapping("/ndaDoLogin")
	public @ResponseBody BaseDataVo doNdaLogin(String loginName, String password,String deptCode, HttpServletRequest request) {
		BaseDataVo vo = new BaseDataVo();
		try {
			UserInformation sessionUserInfo = identityService.verifyUser(loginName,
					password,deptCode,false);
			if (sessionUserInfo == null) {
				vo.setRslt(ResultCst.LOGIN_FAILD);
				vo.setMsg("用户名或者密码错误");
				return vo;
			}
			
			sessionUserInfo.setSysTime(DateUtil.format());
			setUserInformationToServer(request, sessionUserInfo);
			setConfigData(sessionUserInfo);
			vo.setData(sessionUserInfo);
			vo.setRslt(ResultCst.SUCCESS);
		} catch (Exception e) {
			LOGGER.error("", e);
			vo.setRslt(ResultCst.LOGIN_FAILD);
			vo.setMsg(e.getMessage());
		}
		return vo;
	}
	
	/**
	 * NDA端登陆牌登录
	 * 
	 * @param loginName
	 * @param password
	 * @return
	 */
	@RequestMapping("/doNdaLoginCard")
	public @ResponseBody BaseDataVo doNdaLoginCard(String loginCardCode, HttpServletRequest request) {
		BaseDataVo vo = new BaseDataVo();
		try {
			//根据登陆牌获取
			UserInformation sessionUserInfo = identityService.verfyLoginCardCode(loginCardCode);
			if (sessionUserInfo == null) {
				vo.setRslt(ResultCst.LOGIN_FAILD);
				vo.setMsg("用户名或者密码错误");
				return vo;
			}
			
			sessionUserInfo.setSysTime(DateUtil.format());
			setUserInformationToServer(request, sessionUserInfo);
			setConfigData(sessionUserInfo);
			vo.setData(sessionUserInfo);
			vo.setRslt(ResultCst.SUCCESS);
		} catch (Exception e) {
			LOGGER.error("", e);
			vo.setRslt(ResultCst.LOGIN_FAILD);
			vo.setMsg(e.getMessage());
		}
		return vo;
	}

	private void setConfigData(UserInformation sessionUserInfo) {
		sessionUserInfo.setConfigs(
				identityService.queryConfig(
						sessionUserInfo.getCode(), sessionUserInfo.getDeptCode()));
	}

	@RequestMapping("/loginWithFP")
	public @ResponseBody BaseDataVo doNdaLoginWithFP(
			String loginName,
			String secretKey, 
			HttpServletRequest request) {
		BaseDataVo vo = new BaseDataVo();
		UserInformation userInformation = identityService.verifyUser(loginName, secretKey,null,false);
		vo.setData(userInformation);
		vo.setRslt(ResultCst.SUCCESS);
		setConfigData(userInformation);
		setUserInformationToServer(request, userInformation);
		
		return vo;
	}

	/**
	 * Nda 端登出
	 * 
	 * @param token
	 * @return
	 */
	@RequestMapping("/ndaLogout")
	public @ResponseBody BaseVo logout(String token, HttpServletRequest request) {
		BaseVo vo = new BaseVo();
		//移除session
		UserInformation sessionUserInfo = WebContextUtils.getSessionUserInfo();
		if(sessionUserInfo != null){
			HttpSession session = request.getSession();
			session.removeAttribute(WebContextUtils.SESSION_USER_INFO);
			WebContextUtils.remove();
		}
		
		vo.setRslt(ResultCst.SUCCESS);
		return vo;
	}

	/**
	 * 获取用户绑定的指纹个数
	 * 
	 * @param token
	 * @param nurseCode
	 * @param deptCode
	 * @return
	 */
	@RequestMapping("/getNumberOfRegisteredFP")
	public @ResponseBody BaseVo getNumberOfRegisteredFP(
			String nurseCode, String deptCode) {
		BaseDataVo vo = new BaseDataVo();
		int cnt = 0;
		List<UserFinger> fpList = identityService.queryFingerByDeptCodeAndDate(deptCode, null);
		if (fpList != null) {
			for (UserFinger fpEntity : fpList) {
				if (nurseCode.equals(fpEntity.getUserCode())) {
					cnt++;
				}
			}
		}
		vo.setData(cnt);
		vo.setRslt(ResultCst.SUCCESS);

		return vo;
	}

	/**
	 * 上传指纹
	 * 
	 * @param userCode
	 * @param deptCode
	 * @param feature
	 * @param lastUpdateDate
	 * @param managerId
	 * @return
	 */
	@RequestMapping("/uploadFP")
	public @ResponseBody BaseVo uploadFP(String userCode, String deptCode,
			String feature, Date lastUpdateDate, String managerId) {
		BaseMapVo vo = new BaseMapVo();
		
		if(StringUtils.isBlank(userCode) &&  StringUtils.isBlank(deptCode) ){
			vo.setRslt(ResultCst.FAILURE);
			return vo;
		}
		
		int uploadedNum = identityService.saveUserFinger(userCode, deptCode, feature, managerId);
		// 成功保存， 获取所有科室...所有更新的指纹数据
		if (uploadedNum > 0) {
			return updateLocalFPLib(null, deptCode,
					lastUpdateDate);
		}
		vo.setRslt(ResultCst.SUCCESS);
		return vo;
	}

	/**
	 * 更新部门指纹到NDA
	 * 
	 * @param token
	 * @param deptCode
	 * @param lastUpdateDate
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping("/updateLocalFPLib")
	public @ResponseBody BaseVo updateLocalFPLib(String token, String deptCode,
			Date lastUpdateDate) {
		BaseMapVo vo = new BaseMapVo();
		List<UserFinger> list = identityService
				.queryFingerByDeptCodeAndDate(deptCode, lastUpdateDate);
		List<FPDataBean> rsltList = new ArrayList<FPDataBean>();
		Date lastDate = null;
		try {
			for (UserFinger entity : list) {
				if (entity.getCreateDate() != null) {
					if (lastDate == null) {
						lastDate = entity.getCreateDate();
					} else {
						Date curDate =entity.getCreateDate();
						if (curDate.after(lastDate)) {
							lastDate = curDate;
						}
					}
				}
				rsltList.add(new FPDataBean(entity));
			}
		} catch (Exception e) {
			LOGGER.error("", e);
		}

		vo.addData("fpDataList", rsltList);
		if (lastDate == null) {
			vo.addData("versionTag", DateUtil.format());
		} else {
			vo.addData("versionTag", DateUtil.format(lastDate,
					DateFormat.FULL));
		}
		vo.setRslt(ResultCst.SUCCESS);
		return vo;
	}

	/**
	 * 
	 * 根据用户名及密，处理用户登录，登录成功后保存用户信息到session中
	 * 
	 * @param user
	 *            前端输入的用户密码及密码信息
	 * @param request
	 *            当前请求
	 * @param modelMap
	 *            参数传递
	 * @param sessionUserInfo
	 *            绑定用户session
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/doLogin", method = RequestMethod.POST)
	public String doLogin(
			String userName,
			String password, 
			String deptCode,
			boolean isDefault,
			HttpServletRequest request,
			RedirectAttributes model) {
		
		UserInformation userInformation = identityService.verifyUser(userName, password, deptCode,isDefault);
		if (userInformation == null) {
			model.addFlashAttribute("error", "用户名或密码错误");
			return "redirect:/index/login";
		}	

		LOGGER.debug("user login in success.");
		setUserInformationToServer(request, userInformation);

		return "redirect:/nur/patientGlance/patientGlanceMain";
	}

	private void setUserInformationToServer(HttpServletRequest request,
			UserInformation userInformation) {
		HttpSession session = request.getSession(true);
		session.setAttribute(WebContextUtils.SESSION_USER_INFO, userInformation);
		WebContextUtils.setLocalUserInfo(userInformation);
	}

	/**
	 * 根据患者编号，返回以患者为视角主界面
	 * 
	 * @param id
	 * @param mdelMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/patientMain")
	public String patientMain(@RequestParam String id, Model model){
		model.addAttribute("id", id);

		Patient patient = patientService.getPatientByPatId(id);
		UserInformation sessionUserInfo = WebContextUtils.getSessionUserInfo();
		model.addAttribute("patient", patient);
		model.addAttribute("currPatient", patient);
		model.addAttribute("today", DateUtil.format(DateFormat.YMD));
		model.addAttribute("user", sessionUserInfo);
		model.addAttribute("deptName", sessionUserInfo.getDeptName());
		
		model.addAttribute("hospitalName", identityService.getHospitalName());
		model.addAttribute("date", new DateTool()); 
		return "/index/patientMainFrame";
	}
	
	/**
	 * 根据患者编号，返回以患者为视角出院转科界面
	 * 
	 * @param id
	 * @param mdelMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/patientMain2")
	public String patientMain2(String id,Model model){

		model.addAttribute("id", id);

		if(!StringUtils.isEmpty(id)){
			Patient patient = patientService.getPatientByPatId(id);
			model.addAttribute("patient", patient);
			model.addAttribute("currPatient", patient);
		}
		
		UserInformation sessionUserInfo = WebContextUtils.getSessionUserInfo();
		
		model.addAttribute("today", DateUtil.format(DateFormat.YMD));
		model.addAttribute("user", sessionUserInfo);
		model.addAttribute("deptName", sessionUserInfo.getDeptName());
		
		model.addAttribute("hospitalName", identityService.getHospitalName());
		model.addAttribute("date", new DateTool()); 
		return "/index/patientMainFrame2";
	}
	
	/**
	 * 根据患者编号，返回以患者为视角历史界面
	 * 
	 * @param id
	 * @param mdelMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/patientHistoryMain")
	public String patientHistoryMain(String id,Model model){

		model.addAttribute("id", id);

		if(!StringUtils.isEmpty(id)){
			Patient patient = patientService.getPatientByPatId(id);
			model.addAttribute("patient", patient);
			model.addAttribute("currPatient", patient);
		}
		
		UserInformation sessionUserInfo = WebContextUtils.getSessionUserInfo();
		
		model.addAttribute("today", DateUtil.format(DateFormat.YMD));
		model.addAttribute("user", sessionUserInfo);
		model.addAttribute("deptName", sessionUserInfo.getDeptName());
		
		model.addAttribute("hospitalName", identityService.getHospitalName());
		model.addAttribute("isHistoryDoc",true);
		model.addAttribute("date", new DateTool()); 
		return "/index/patientMainFrame3";
	}

	/**
	 * 返回以护士为视角主界面
	 * 
	 * @param mdelMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/nurseMain")
	public String nurseMain(Model model, String menuId){
		// 获取当前用户
		UserInformation user = WebContextUtils.getSessionUserInfo();
		model.addAttribute("deptCode", user.getDeptCode());
		model.addAttribute("deptName", user.getDeptName());
		model.addAttribute("menuId", menuId);
		model.addAttribute("user", user);
		model.addAttribute("today", DateUtil.format(DateFormat.YMD));
		model.addAttribute("hospitalName", identityService.getHospitalName());

		return "/index/nurseMainFrame";
	}
	
	@RequestMapping(value="/refreshSystemConfig")
	@ResponseBody
	public BaseMapVo refreshSystemConfig(){
		BaseMapVo vo = new BaseMapVo();
	
		identityService.init();
		
		UserInformation sessionUserInfo = WebContextUtils.getSessionUserInfo();
		setConfigData(sessionUserInfo);
		vo.setMsg("刷新成功!");
		vo.setRslt(ResultCst.SUCCESS);
		vo.addData("system",sessionUserInfo.getConfigs());
		return vo;
	}
	
	@RequestMapping("/saveDefaultDept")
	@ResponseBody
	public BaseMapVo saveDefaultDept(String nurseCode,String deptCode){
		BaseMapVo vo = new BaseMapVo();
		try {
			identityService.saveDefaultDept(nurseCode, deptCode);
			vo.setRslt(ResultCst.SUCCESS);
			vo.setMsg("保存成功!");
		} catch (Exception e) {
			LOGGER.error("IndexAction saveDefaultDept error:" + e.getMessage());
			vo.setRslt(ResultCst.ALERT_ERROR);
			vo.setMsg("保存失败!");
		}
		return vo;
	}

	@RequestMapping("/patientInOutMain")
	public String patientInOutMain(Model model){

		UserInformation sessionUserInfo = WebContextUtils.getSessionUserInfo();

		model.addAttribute("today", DateUtil.format(DateFormat.YMD));
		model.addAttribute("user", sessionUserInfo);
		model.addAttribute("deptName", sessionUserInfo.getDeptName());

		model.addAttribute("hospitalName", identityService.getHospitalName());
		model.addAttribute("isHistoryDoc",true);
		model.addAttribute("date", new DateTool());

		return "/nur/patientInOutMain";
	}

	/**
	 * 根据传入的用户编号获取用户信息
	 * @param userCode 用户编号
	 * @return 用户信息
     */
    @RequestMapping("/getUserInfo")
	@ResponseBody
	public BaseDataVo getUserInfoByLoginID(String LoginID){
		BaseDataVo dataVo = new BaseDataVo();
		try{
			if(StringUtils.isEmpty(LoginID)){
				throw new MnisException("传入参数为空");
			}
			UserInformation userInformation = identityService.queryUserByLoginName(LoginID, null);
			if (userInformation == null || null==userInformation.getUser()) {
				throw new MnisException("该用户不存在。");
			}
			SysUser user = userInformation.getUser();
			user.setPassword(null);//抹去密码等重要信息
			dataVo.setData(user);
			dataVo.setRslt(ResultCst.SUCCESS);
		}catch (Exception e){
			dataVo.setRslt(ResultCst.FAILURE);
			dataVo.setMsg(e.getMessage());
		}

		return dataVo;
	}
}
