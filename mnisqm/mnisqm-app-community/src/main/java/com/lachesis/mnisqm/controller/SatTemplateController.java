package com.lachesis.mnisqm.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lachesis.mnisqm.BaseDataVo;
import com.lachesis.mnisqm.common.WebContextUtils;
import com.lachesis.mnisqm.constants.Constants;
import com.lachesis.mnisqm.constants.MnisQmConstants;
import com.lachesis.mnisqm.core.CommRuntimeException;
import com.lachesis.mnisqm.core.utils.StringUtils;
import com.lachesis.mnisqm.module.satTemplate.domain.SatResult;
import com.lachesis.mnisqm.module.satTemplate.domain.SatTemplate;
import com.lachesis.mnisqm.module.satTemplate.service.ISatTemplateService;
import com.lachesis.mnisqm.module.system.domain.SysUser;

@RequestMapping("/satTemplate")
@Controller
public class SatTemplateController {

	@Autowired
	private ISatTemplateService satTemplateService;
	
	/**
	 * 保存满意度模板
	 * @param satTemplate
	 * @return
	 */
	@RequestMapping(value="/saveSatTemplate")
	public @ResponseBody BaseDataVo saveSatTemplate(@RequestBody SatTemplate satTemplate){
		BaseDataVo outVo = new BaseDataVo();
		//数据校验
		if(null == satTemplate || StringUtils.isEmpty(satTemplate.getFormName())){
			throw new CommRuntimeException("模板名称名称不允许为空!");
		}
		SatTemplate satTmeplate = satTemplateService.getByTemplateName(satTemplate.getFormName());
		if(null != satTmeplate){
			throw new CommRuntimeException("已存在的模板名!");
		}
		//获取登录的用户信息
		SysUser user = WebContextUtils.getSessionUserInfo();
		satTemplate.setCreatePerson(user.getUserCode());//创建人
		satTemplate.setCreateTime(new Date());//创建时间
		satTemplate.setStatus(MnisQmConstants.STATUS_YX);//有效状态
		satTemplateService.insertSatTemplate(satTemplate);
		//数据返回
		outVo.setCode(Constants.Success);
		return outVo;
	}
	
	/**
	 * 更新满意度模板
	 * @param satTemplate
	 * @return
	 */
	@RequestMapping(value="/updateSatTemplate")
	public @ResponseBody BaseDataVo updateSatTemplate(@RequestBody SatTemplate satTemplate){
		BaseDataVo outVo = new BaseDataVo();
		//数据校验
		if(null == satTemplate || StringUtils.isEmpty(satTemplate.getFormName())){
			throw new CommRuntimeException("模板名称名称不允许为空!");
		}
		//获取登录的用户信息
		SysUser user = WebContextUtils.getSessionUserInfo();
		satTemplate.setUpdatePerson(user.getUserCode());//更新人
		satTemplate.setUpdateTime(new Date());//更新时间
		satTemplateService.updateSatTemplate(satTemplate);
		//数据返回
		outVo.setCode(Constants.Success);
		return outVo;
	}
	
	/**
	 * 删除满意度模板
	 * @param satTemplate
	 * @return
	 */
	@RequestMapping(value="/deleteSatTemplate")
	public @ResponseBody BaseDataVo deleteSatTemplate(@RequestBody SatTemplate satTemplate){
		BaseDataVo outVo = new BaseDataVo();
		//数据校验
		if(null == satTemplate){
			throw new CommRuntimeException("模板不允许为空!");
		}
		//获取登录的用户信息
		SysUser user = WebContextUtils.getSessionUserInfo();
		satTemplate.setUpdatePerson(user.getUserCode());//更新人
		satTemplate.setUpdateTime(new Date());//更新时间
		satTemplateService.updateSatTemplateForDelete(satTemplate);
		//数据返回
		outVo.setCode(Constants.Success);
		return outVo;
	}
	
	/**
	 * 保存满意度调查结果
	 * @param satResult
	 * @return
	 */
	@RequestMapping(value="/saveSatResult")
	public @ResponseBody BaseDataVo saveSatResult(@RequestBody SatResult satResult){
		BaseDataVo outVo = new BaseDataVo();
		//数据校验
		if(null == satResult || StringUtils.isEmpty(satResult.getResultName())){
			throw new CommRuntimeException("模板名称名称不允许为空!");
		}
		//获取登录的用户信息
		SysUser user = WebContextUtils.getSessionUserInfo();
		satResult.setCreatePerson(user.getUserCode());//创建人
		satResult.setCreateTime(new Date());//创建时间
		satResult.setStatus(MnisQmConstants.STATUS_YX);//有效状态
		satTemplateService.insertSatResult(satResult);
		//数据返回
		outVo.setCode(Constants.Success);
		return outVo;
	}
	
	/**
	 * 更新满意度调查结果
	 * @param satResult
	 * @return
	 */
	@RequestMapping(value="/updateSatResult")
	public @ResponseBody BaseDataVo updateSatResult(@RequestBody SatResult satResult){
		BaseDataVo outVo = new BaseDataVo();
		//数据校验
		if(null == satResult || StringUtils.isEmpty(satResult.getResultName())){
			throw new CommRuntimeException("模板名称名称不允许为空!");
		}
		//获取登录的用户信息
		SysUser user = WebContextUtils.getSessionUserInfo();
		satResult.setUpdatePerson(user.getUserCode());//更新人
		satResult.setUpdateTime(new Date());//更新时间
		satTemplateService.updateSatResult(satResult);
		//数据返回
		outVo.setCode(Constants.Success);
		return outVo;
	}
	
	/**
	 * 删除满意度调查结果
	 * @param satResult
	 * @return
	 */
	@RequestMapping(value="/deleteSatResult")
	public @ResponseBody BaseDataVo deleteSatResult(@RequestBody SatResult satResult){
		BaseDataVo outVo = new BaseDataVo();
		//数据校验
		if(null == satResult){
			throw new CommRuntimeException("模板不允许为空!");
		}
		//获取登录的用户信息
		SysUser user = WebContextUtils.getSessionUserInfo();
		satResult.setUpdatePerson(user.getUserCode());//更新人
		satResult.setUpdateTime(new Date());//更新时间
		satTemplateService.updateSatResultForDelete(satResult);
		//数据返回
		outVo.setCode(Constants.Success);
		return outVo;
	}
	
	/**
	 * 根据表单类型查找满意度模板
	 * @param formType 表单类型
	 * @param userType 使用类型 1：患者；2：护士
	 * @return
	 */
	@RequestMapping(value="/querySatTemplateByFormType")
	public @ResponseBody BaseDataVo querySatTemplateByFormType(String formType, String userType){
		BaseDataVo outVo = new BaseDataVo();
		//数据校验
		if(StringUtils.isEmpty(userType)){
			throw new CommRuntimeException("使用类型不允许为空!");
		}
		outVo.setData(satTemplateService.querySatTemplateByFormType(formType, userType));
		//数据返回
		outVo.setCode(Constants.Success);
		return outVo;
	}
	
	/**
	 * 根据表单编号查找满意度模板
	 * @param formCode 表单编号
	 * @return
	 */
	@RequestMapping(value="/getSatTemplateByFormCode")
	public @ResponseBody BaseDataVo getSatTemplateByFormCode(String formCode){
		BaseDataVo outVo = new BaseDataVo();
		//数据校验
		if(StringUtils.isEmpty(formCode) ){
			throw new CommRuntimeException("模板编号不允许为空!");
		}
		outVo.setData(satTemplateService.getSatTemplateByFormCode(formCode));
		//数据返回
		outVo.setCode(Constants.Success);
		return outVo;
	}
	
	/**
	 * 获取所有表单编号和表单名
	 * @return
	 */
	@RequestMapping(value="/queryAllSatTemplateCodeAndFormName")
	public @ResponseBody BaseDataVo queryAllSatTemplateCodeAndFormName(){
		BaseDataVo outVo = new BaseDataVo();
		outVo.setData(satTemplateService.queryAllSatTemplateCodeAndFormName());
		//数据返回
		outVo.setCode(Constants.Success);
		return outVo;
	}
	
	/**
	 * 根据时间、表单类型、患者姓名、查询患者调查结果
	 * @param yearAndMonth 年月
	 * @param formType 调查表类型
	 * @param patientName 患者姓名
	 * @param beginTime 开始时间
	 * @param endTime 结束时间
	 * @param deptCode 病区
	 * @param userType 使用类型 1：患者；2：护士
	 * @return
	 */
	@RequestMapping(value="/queryByDateAndFormTypeAndPatient")
	public @ResponseBody BaseDataVo queryByDateAndFormTypeAndPatient(String yearAndMonth,String beginTime, String endTime , String formType, String patientName, String deptCode, String userType){
		BaseDataVo outVo = new BaseDataVo();
		outVo.setData(satTemplateService.queryByDateAndFormTypeAndPatient(yearAndMonth, beginTime, endTime, formType, patientName, deptCode,userType));
		//数据返回
		outVo.setCode(Constants.Success);
		return outVo;
	}
	
}
