package com.lachesis.mnisqm.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.lachesis.mnisqm.BaseDataVo;
import com.lachesis.mnisqm.BaseMapVo;
import com.lachesis.mnisqm.common.ExcelObject;
import com.lachesis.mnisqm.common.WebContextUtils;
import com.lachesis.mnisqm.constants.Constants;
import com.lachesis.mnisqm.constants.MnisQmConstants;
import com.lachesis.mnisqm.core.CommRuntimeException;
import com.lachesis.mnisqm.core.utils.ExcelUtil;
import com.lachesis.mnisqm.core.utils.GsonUtils;
import com.lachesis.mnisqm.core.utils.StringUtils;
import com.lachesis.mnisqm.module.system.domain.SysDate;
import com.lachesis.mnisqm.module.system.domain.SysDic;
import com.lachesis.mnisqm.module.system.domain.SysRole;
import com.lachesis.mnisqm.module.system.domain.SysRoleFun;
import com.lachesis.mnisqm.module.system.domain.SysUser;
import com.lachesis.mnisqm.module.system.domain.SysUserRole;
import com.lachesis.mnisqm.module.system.service.ICacheService;
import com.lachesis.mnisqm.module.system.service.ISysService;
import com.lachesis.mnisqm.module.trainExamManage.domain.TemAttendanceManage;
import com.lachesis.mnisqm.vo.RolesVo;

/**
 * 系统木块接口
 * @author Administrator
 *
 */
@RequestMapping(value = "/system")
@Controller
public class SystemController {
	Logger log = LoggerFactory.getLogger(SystemController.class);
	
	@Autowired
	ISysService service;
	
	@Autowired
	ICacheService cacheService;
	
	/**
	 * 用户登录
	 * @param loginName
	 * @param password
	 * @return
	 */
	@RequestMapping(value = "/login")
	public @ResponseBody BaseMapVo login(@RequestBody SysUser user,HttpServletRequest request){
		
		BaseMapVo outVo = new BaseMapVo();
		if(StringUtils.isEmpty(user.getLoginName())){
			throw new CommRuntimeException("用户名不允许为空！");
		}
		
		//登录验证
		SysUser sysUser = service.checkLogin(user.getLoginName(), user.getPassword());
		outVo.addData("user",sysUser);
		//查询角色信息
		SysUserRole role = service.getUserRoleByUserCode(sysUser.getSeqId());
		outVo.addData("role", role);
		//用户功能列表
		outVo.addData("funs", service.getFunctionByRole(role.getRoleCode()));
		//更新session信息
		saveLoginToSession(request,sysUser);
		//成功标志
		outVo.setCode(Constants.Success);
		return outVo;
	}
	
	/**
	 * 
	 * @param request
	 * @param userInformation
	 */
	private void saveLoginToSession(HttpServletRequest request,
			SysUser user) {
		HttpSession session = request.getSession(true);
		session.setAttribute(WebContextUtils.SESSION_USER_INFO, user);
		WebContextUtils.setLocalUserInfo(user);
	}
	
	/**
	 * 所属角色功能列表获取
	 */
	@RequestMapping(value = "/getFunction")
	public @ResponseBody BaseDataVo getFunction(String roleCode,Long userRoleId){
		BaseDataVo outVo = new BaseDataVo();
		outVo.setCode(Constants.Success);
		outVo.setData(service.getFunctionByRole(roleCode));
		SysUserRole userRole = new SysUserRole();
		userRole.setRoleCode(roleCode);
		userRole.setSeqId(userRoleId);
		//获取用户编号
		userRole.setUserId(WebContextUtils.getSessionUserInfo().getSeqId());
		service.saveSysUserRole(userRole);
		log.debug(GsonUtils.toJson(outVo));
		return outVo;
	}
	
	
	/**
	 * 所属角色功能列表获取
	 */
	@RequestMapping(value = "/getHaveFunction")
	public @ResponseBody BaseDataVo getHaveFunction(String roleCode){
		BaseDataVo outVo = new BaseDataVo();
		outVo.setCode(Constants.Success);
		outVo.setData(service.getFunctionByRole(roleCode));
		log.debug(GsonUtils.toJson(outVo));
		return outVo;
	}
	
	/**
	 * 所属角色未有功能列表获取
	 */
	@RequestMapping(value = "/getNotHaveFunction")
	public @ResponseBody BaseDataVo getNotHaveFunction(String roleCode){
		BaseDataVo outVo = new BaseDataVo();
		outVo.setCode(Constants.Success);
		outVo.setData(service.getNotHaveRole(roleCode));
		log.debug(GsonUtils.toJson(outVo));
		return outVo;
	}
	
	/**
	 * 获取下拉框的数据
	 * @param json
	 * @return
	 */
	@RequestMapping(value="/getSysDic")
	public @ResponseBody BaseMapVo getSysDic(){
		/*//参数获取
		Type type = new TypeToken<List<SysDic>>(){}.getType();
		List<SysDic> dicTypes = GsonUtils.fromJson(json, type);
		BaseMapVo outVo = new BaseMapVo();
		Map<String,List<SysDic>> dataMap = new HashMap<String,List<SysDic>>();
		if(null != dicTypes){
			//数据处理和获取
			for(SysDic dicType : dicTypes){
				dataMap.put(dicType.getDicType(), cacheService.getSysDics(dicType.getDicType()));
			}
			//清理缓存
			dicTypes = null;
		}*/
		BaseMapVo outVo = new BaseMapVo();
		List<String> list = cacheService.queryDicTypes();
		Map<String,List<SysDic>> dataMap = new HashMap<String,List<SysDic>>();
		if(null != list){
			for (String string : list) {
				dataMap.put(string, cacheService.getSysDics(string));
			}
		}
		//返回的数据
		outVo.addData("dicInfo",dataMap);
		//成功编号
		outVo.setCode(Constants.Success);
		//数据返回
		return outVo;
	}
	
	/**
	 * 获取所有字典类型
	 * @return
	 */
	@RequestMapping(value="/getSysDicTypes")
	@ResponseBody
	public BaseMapVo getSysDicTypes(){
		BaseMapVo outVo = new BaseMapVo();
		//返回的数据
		outVo.addData("sysDicTypes", cacheService.queryDicTypes());
		//成功编号
		outVo.setCode(Constants.Success);
		//数据返回
		return outVo;
	}
	
	/**
	 * 获取用户角色信息
	 * @param userCode  用户编号
	 * @return
	 */
	@RequestMapping(value="/getRoleLst")
	public @ResponseBody BaseDataVo getRoleLst(String userId){
		SysUser user = WebContextUtils.getSessionUserInfo();
		String seqId = user.getSeqId() +"";
		if("admin".equals(user.getLoginName()) && seqId.equals(userId)){
			userId = null;
		}
		BaseDataVo outVo = new BaseDataVo();
		List<SysRole> sysList = service.getUserSysRoles(userId);
		outVo.setData(sysList);
		outVo.setMsg(Constants.Success);
		return outVo;
	}
	
	//删除角色
	@RequestMapping(value="/deleteRole")
	public @ResponseBody BaseDataVo deleteRole(@RequestBody SysRole SysRole){
		BaseDataVo outVo = new BaseDataVo();
		//数据校验
		if(null == SysRole){
			throw new CommRuntimeException("角色信息为空！");
		}
		if(StringUtils.isEmpty(SysRole.getRoleCode())){
			throw new CommRuntimeException("角色编号为空！");
		}
		//数据删除
		service.deleteSysRole(SysRole);
		return outVo;
	}
	
	//新增角色信息
	@RequestMapping(value="/saveRole")
	public @ResponseBody BaseDataVo saveRole(@RequestBody SysRole SysRole){
		BaseDataVo outVo = new BaseDataVo();
		service.saveSysRole(SysRole);
		return outVo;
	}
	
	
	/**
	 * 新增用户角色信息
	 * @param record
	 * @return
	 */
	@RequestMapping(value="/saveSysUserRole")
	public @ResponseBody BaseDataVo saveSysUserRole(@RequestBody RolesVo rolesVo){
		BaseDataVo outVo = new BaseDataVo();
		//数据处理
		if(rolesVo==null
				|| rolesVo.getData()==null){
			throw new CommRuntimeException("数据对象为空!");
		}
		List<SysUserRole> userRoles = rolesVo.getData();
		SysUser sysUser = WebContextUtils.getSessionUserInfo();
		for(SysUserRole userRole: userRoles){
			if(StringUtils.isEmpty(userRole.getDeptCode())){
				throw new CommRuntimeException("科室编号不允许为空！");
			}
			if(StringUtils.isEmpty(userRole.getRoleCode())){
				throw new CommRuntimeException("角色编号不允许为空！");
			}
			if(null == userRole.getUserId()){
				throw new CommRuntimeException("系统用户ID不允许为空！");
			}
			userRole.setCreatePerson(sysUser.getUserCode());
			userRole.setUpdatePerson(sysUser.getUserCode());
			userRole.setStatus(MnisQmConstants.STATUS_YX);//有效
			userRole.setIsUse(MnisQmConstants.NO_USE);//未使用
		}
		
		//数据保存
		service.insertSysUserRole(userRoles);
		//数据返回	
		outVo.setData(Constants.Success);
		return outVo;
	}
	
	/**
	 * 修改用户角色信息
	 * @param record
	 * @return
	 */
	@RequestMapping(value="/updateSysUserRole")
	public BaseDataVo updateSysUserRole(@RequestBody SysUserRole record){
		BaseDataVo outVo = new BaseDataVo();
		try {
			int revV = service.updateSysUserRole(record);
			if(revV<1){
				outVo.setCode(Constants.FailureCode);
				outVo.setMsg("修改数据失败！");
			}
		} catch (Exception e) {
			outVo.setCode(Constants.FailureCode);
			outVo.setMsg("修改数据失败！");
		}
		return outVo;
	}
	
	/**
	 * 新增角色菜单数据
	 * @param record
	 * @return
	 */
	@RequestMapping(value="/saveSysRoleFuns")
	public @ResponseBody BaseDataVo saveSysRoleFuns(@RequestBody SysRole sysRole){
		BaseDataVo outVo = new BaseDataVo();
		
		List<SysRoleFun> sysRoleFuns = sysRole.getFuns();
		//数据校验
		if(sysRole==null
				|| StringUtils.isEmpty(sysRole.getRoleCode())){
			throw new CommRuntimeException("角色、角色编号不能为空！");
		}
		
		//获取session数据
		SysUser sysUser= WebContextUtils.getSessionUserInfo();
		//员工编号
		String code  = sysUser.getUserCode();
		
		sysRole.setUpdatePerson(code);
		
		//角色功能信息
		if(sysRoleFuns!=null){
			for (SysRoleFun sysRoleFun : sysRoleFuns) {
				sysRoleFun.setCreatePerson(code);
				sysRoleFun.setUpdatePerson(code);
				sysRoleFun.setRoleCode(sysRole.getRoleCode());
				sysRoleFun.setStatus(MnisQmConstants.STATUS_YX);//有效状态
			}
		}
		//数据保存
		service.saveSysRoleFun(sysRoleFuns, sysRole);
		
		outVo.setCode(Constants.Success);
		return outVo;
	}
	
	/**
	 * 新增用户或修改密码
	 * @param sysUser
	 * @return
	 */
	@RequestMapping(value="/saveSysUser")
	public @ResponseBody
	BaseDataVo saveSysUser(@RequestBody SysUser sysUser) {
		BaseDataVo outVo = new BaseDataVo();
		//数据校验
		if (sysUser == null) {
			throw new CommRuntimeException("用户数据为空!");
		}
		//校验员工编号
		if(StringUtils.isEmpty(sysUser.getUserCode())){
			throw new CommRuntimeException("员工编号为空!");
		}
		//校验员工编号
		if(StringUtils.isEmpty(sysUser.getLoginName())){
			throw new CommRuntimeException("登录账号为空!");
		}

		// 获取session中的数据
		SysUser user = WebContextUtils.getSessionUserInfo();
		String code = user.getUserCode();

		//创建人
		sysUser.setCreatePerson(code);
		//更新人
		sysUser.setUpdatePerson(code);
		sysUser.setStatus(MnisQmConstants.STATUS_YX);//有效状态
		//数据保存
		service.saveSysUser(sysUser);
		//成功标志
		outVo.setCode(Constants.Success);
		return outVo;
	}
	
	
	/**
	 * 获取系统用户
	 * @param deptCode
	 * @param queryKey
	 * @return
	 */
	@RequestMapping(value="/getSysUserList")
	public @ResponseBody BaseDataVo getSysUserList(String deptCode){
		BaseDataVo outVo = new BaseDataVo();
		
		if(StringUtils.isEmpty(deptCode)){
			throw new CommRuntimeException("请选择科室！");
		}
		List<SysUser> sysList = service.getSysUsers(deptCode,"");
		outVo.setData(sysList);
		//成功标志
		outVo.setCode(Constants.Success);
		return outVo;
	}
	
	@RequestMapping(value="/deleteSysUser")
	public @ResponseBody BaseDataVo deleteSysUser(@RequestBody SysUser user){
		BaseDataVo outVo = new BaseDataVo();
		service.deleteSysUser(user);
		//成功标志
		outVo.setCode(Constants.Success);
		return outVo;
	}
	
	//逻辑删除用户角色数据
	@RequestMapping(value="/deleteSysUserRole")
	public @ResponseBody BaseDataVo deleteSysUserRole(@RequestBody Long seqId){
		BaseDataVo outVo = new BaseDataVo();
		//数据校验
		
		if(null == seqId){
			throw new CommRuntimeException("seqId为空！");
		}
		SysUserRole userRole = new SysUserRole();
		SysUser user = WebContextUtils.getSessionUserInfo();
		userRole.setSeqId(seqId);
		userRole.setUpdatePerson(user.getUserCode());//更新人的信息
		service.deleteUserRole(userRole);
		//成功标志
		outVo.setCode(Constants.Success);
		return outVo;
	}
	
	/**
	 * 生成规定年份的系统日期表
	 * @param year 年份 如：2016
	 * @return
	 */
	@RequestMapping(value="/creatSysDate")
	public @ResponseBody BaseDataVo creatSysDate(String year){
		BaseDataVo outVo = new BaseDataVo();
		if(null == year || year.trim().length() == 0){
			outVo.setCode(Constants.FailureCode);
			return outVo; 
		}
		service.creatSysDate(year);
		//成功标志
		outVo.setCode(Constants.Success);
		return outVo;
	}
	
	/**
	 * 更新系统日期
	 * @param sysDate
	 * @return
	 */
	@RequestMapping(value="/updateSysDate")
	public @ResponseBody BaseDataVo updateSysDate(@RequestBody SysDate sysDate){
		BaseDataVo outVo = new BaseDataVo();
		if(null != sysDate){
			service.updateSysDate(sysDate);
		}
		//成功标志
		outVo.setCode(Constants.Success);
		return outVo;
	}
	
	/**
	 * 获取规定年份的系统日期表
	 * @param year 年份 如：2016
	 * @param month 月份  如：01
	 * @return
	 */
	@RequestMapping(value="/querySysDate")
	public @ResponseBody BaseDataVo querySysDate(String year, String month){
		BaseDataVo outVo = new BaseDataVo();
		outVo.setData(service.querySysDate(year,month));
		//成功标志
		outVo.setCode(Constants.Success);
		return outVo;
	}
	
	@RequestMapping(value="/exportExcel")
	public void exportExcel(@RequestBody ExcelObject excelObject, HttpServletResponse response){
		ExcelUtil<TemAttendanceManage> e = new ExcelUtil<TemAttendanceManage>();
		HSSFWorkbook wb = e.exportExcelByArray(excelObject.getHead(), excelObject.getBody());
		response.setContentType("application/vnd.ms-excel");    
        response.setHeader("Content-disposition", "attachment;filename=Excel.xls");    
        OutputStream ouputStream;
		try {
			ouputStream = response.getOutputStream();
			wb.write(ouputStream);    
	        ouputStream.flush();    
	        ouputStream.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}    
            
	}
	
	/**
	 * 导入护理人员信息
	 * @param file
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/importSysUserExcel")
	public @ResponseBody BaseDataVo importSysUserExcel(@RequestParam MultipartFile file, HttpServletRequest request){
		BaseDataVo vo = new BaseDataVo();
		if (null == file){
			throw new CommRuntimeException("请选择文件!");
		}
		String name = file.getOriginalFilename();// 获取上传文件名,包括路径
		long size = file.getSize();
		if ((name == null || name.equals("")) && size == 0)
			return null;
		try {
            ExcelUtil<SysUser> excelReader = new ExcelUtil<SysUser>();
            // 对读取Excel表格内容测试
            InputStream is = file.getInputStream();
//            InputStream is = new FileInputStream("D:\\护理质量\\护理人员信息导入表.xls");
            SysUser sysUser = new SysUser();
            Map<String, Object> map  = excelReader.readExcelContent(is, sysUser, "com.lachesis.mnisqm.module.system.domain.SysUser");
            Set<String> set = map.keySet();
            Iterator<String> it = set.iterator();
            while (it.hasNext()) {
                String key = (String) it.next();
                SysUser value = (SysUser) map.get(key);
                service.saveSysUser(value);
            }
            is.close();
            vo.setCode(Constants.Success);
        } catch (Exception e) {
            e.printStackTrace();
        }
		return vo;  
	}

}