package com.lachesis.mnisqm.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.lachesis.mnisqm.core.utils.CodeUtils;
import com.lachesis.mnisqm.core.utils.StringUtils;
import com.lachesis.mnisqm.module.qualityForm.domain.QualityForm;
import com.lachesis.mnisqm.module.qualityForm.domain.QualityFormDetail;
import com.lachesis.mnisqm.module.qualityForm.domain.QualityIndex;
import com.lachesis.mnisqm.module.qualityForm.domain.QualityIndexItem;
import com.lachesis.mnisqm.module.qualityForm.domain.QualityIssueManage;
import com.lachesis.mnisqm.module.qualityForm.domain.QualityModel;
import com.lachesis.mnisqm.module.qualityForm.domain.QualityModelTrans;
import com.lachesis.mnisqm.module.qualityForm.domain.QualityPlan;
import com.lachesis.mnisqm.module.qualityForm.domain.QualityResult;
import com.lachesis.mnisqm.module.qualityForm.domain.QualityResultDetail;
import com.lachesis.mnisqm.module.qualityForm.domain.QualityTask;
import com.lachesis.mnisqm.module.qualityForm.domain.QualityTeam;
import com.lachesis.mnisqm.module.qualityForm.service.IQualityFormService;
import com.lachesis.mnisqm.module.system.domain.SysUser;

@Controller
@RequestMapping("/quality")
public class QualityController {

	@Autowired
	private IQualityFormService qualityFormService;
	
	/**
	 * 保存质量检查模板
	 * @param qualityForm
	 * @return
	 */
	@RequestMapping(value="/saveQualityForm")
	public @ResponseBody BaseDataVo saveQualityForm( @RequestBody QualityForm qualityForm){
		BaseDataVo outVo = new BaseDataVo();
		//数据校验
		if(null == qualityForm || StringUtils.isEmpty(qualityForm.getFormCode()) || StringUtils.isEmpty(qualityForm.getFormName())){
			throw new CommRuntimeException("表单编号或名称不允许为空!");
		}
		qualityForm.setFormCode(CodeUtils.getSysInvokeId());
		//获取登录的用户信息
		SysUser user = WebContextUtils.getSessionUserInfo();
		qualityForm.setCreatePerson(user.getUserCode());//创建人
		qualityForm.setCreateTime(new Date());//创建时间
		qualityForm.setStatus(MnisQmConstants.STATUS_YX);//有效状态
		qualityFormService.insertQualityForm(qualityForm);
		//数据返回
		outVo.setCode(Constants.Success);
		return outVo;
	}
	
	/**
	 * 更新质量检查模板
	 * @param qualityForm
	 * @return
	 */
	@RequestMapping(value="/updateQualityForm")
	public @ResponseBody BaseDataVo updateQualityForm(@RequestBody QualityForm qualityForm){
		BaseDataVo outVo = new BaseDataVo();
		//数据校验
		if(null == qualityForm || StringUtils.isEmpty(qualityForm.getFormCode()) || StringUtils.isEmpty(qualityForm.getFormName())){
			throw new CommRuntimeException("表单编号或名称不允许为空!");
		}
		//获取登录的用户信息
		SysUser user = WebContextUtils.getSessionUserInfo();
		qualityForm.setUpdatePerson(user.getUserCode());//更新人
		qualityForm.setUpdateTime(new Date());//更新时间
		qualityFormService.updateQualityForm(qualityForm);
		//数据返回
		outVo.setCode(Constants.Success);
		return outVo;
	}
	
	/**
	 * 删除质量检查模板
	 * @param qualityForm
	 * @return
	 */
	@RequestMapping(value="/deleteQualityForm")
	public @ResponseBody BaseDataVo deleteQualityForm(String seqId){
		BaseDataVo outVo = new BaseDataVo();
		if(null == seqId || seqId.trim().length()==0){
			throw new CommRuntimeException("seqId不允许为空!");
		}
		QualityForm qualityForm = new QualityForm();
		qualityForm.setSeqId(Long.parseLong(seqId));
		//获取登录的用户信息
		SysUser user = WebContextUtils.getSessionUserInfo();
		qualityForm.setUpdatePerson(user.getUserCode());//更新人
		qualityForm.setUpdateTime(new Date());//更新时间
		qualityFormService.deleteQualityForm(qualityForm);
		//数据返回
		outVo.setCode(Constants.Success);
		return outVo;
	}
	
	/**
	 * 保存质量检查明细
	 * @param qualityFormDetail 明细list
	 * @return
	 */
	@RequestMapping(value="/saveQualityFormDetail")
	public @ResponseBody BaseDataVo saveQualityFormDetail(@RequestBody List<QualityFormDetail> qualityFormDetail){
		BaseDataVo outVo = new BaseDataVo();
		SysUser user = WebContextUtils.getSessionUserInfo();
		String userCode = user.getUserCode();
		for (QualityFormDetail object : qualityFormDetail) {
			object.setCreatePerson(userCode);//创建人
			object.setCreateTime(new Date());//创建时间
			object.setStatus(MnisQmConstants.STATUS_YX);//有效状态
			qualityFormService.insertQualityFormDetail(object);
		}
		//数据返回
		outVo.setCode(Constants.Success);
		return outVo;
	}
	
	/**
	 * 更新质量检查明细
	 * @param qualityFormDetail
	 * @return
	 */
	@RequestMapping(value="/updateQualityFormDetail")
	public @ResponseBody BaseDataVo updateQualityFormDetail(@RequestBody List<QualityFormDetail> qualityFormDetail){
		BaseDataVo outVo = new BaseDataVo();
		SysUser user = WebContextUtils.getSessionUserInfo();
		String userCode = user.getUserCode();
		for (QualityFormDetail object : qualityFormDetail) {
			object.setUpdatePerson(userCode);//更新人
			object.setUpdateTime(new Date());//更新时间
			qualityFormService.updateQualityFormDetail(object);
		}
		//数据返回
		outVo.setCode(Constants.Success);
		return outVo;
	}
	
	/**
	 * 删除质量检查明细
	 * @param qualityFormDetail
	 * @return
	 */
	@RequestMapping(value="/deleteQualityFormDetail")
	public @ResponseBody BaseDataVo deleteQualityFormDetail(String seqId){
		BaseDataVo outVo = new BaseDataVo();
		if(null == seqId || seqId.trim().length()==0){
			throw new CommRuntimeException("seqId不允许为空!");
		}
		QualityFormDetail qualityFormDetail = new QualityFormDetail();
		qualityFormDetail.setSeqId(Long.parseLong(seqId));
		//获取登录的用户信息
		SysUser user = WebContextUtils.getSessionUserInfo();
		qualityFormDetail.setUpdatePerson(user.getUserCode());//更新人
		qualityFormDetail.setUpdateTime(new Date());//更新时间
		qualityFormService.deleteQualityFormDetail(qualityFormDetail);
		//数据返回
		outVo.setCode(Constants.Success);
		return outVo;
	}
	
	/**
	 * 保存质量检查任务
	 * @param qualityTask 质量检查任务
	 * @return
	 */
	@RequestMapping(value="/saveQualityTask")
	public @ResponseBody BaseDataVo saveQualityTask(@RequestBody QualityTask qualityTask){
		BaseDataVo outVo = new BaseDataVo();
		
		//获取登录的用户信息
		SysUser user = WebContextUtils.getSessionUserInfo();
		qualityTask.setCreatePerson(user.getUserCode());//创建人
		qualityTask.setCreateTime(new Date());//创建时间
		qualityTask.setStatus(MnisQmConstants.STATUS_YX);//有效状态
		if(qualityTask.getSeqId()==null){
			qualityTask.setTaskCode(CodeUtils.getSysInvokeId());
			qualityFormService.insertQualityTask(qualityTask);
		}else{
			qualityFormService.updateQualityTask(qualityTask);
		}
		//数据返回
		outVo.setCode(Constants.Success);
		return outVo;
	}
	
	/**
	 * 更新质量检查任务
	 * @param qualityTask
	 * @return
	 */
	@RequestMapping(value="/updateQualityTask")
	public @ResponseBody BaseDataVo updateQualityTask(@RequestBody QualityTask qualityTask){
		BaseDataVo outVo = new BaseDataVo();
		//数据校验
		if(null == qualityTask || StringUtils.isEmpty(qualityTask.getFormCode()) || StringUtils.isEmpty(qualityTask.getTaskCode())){
			throw new CommRuntimeException("表单编号或任务编号不允许为空!");
		}
		//获取登录的用户信息
		SysUser user = WebContextUtils.getSessionUserInfo();
		qualityTask.setUpdatePerson(user.getUserCode());//更新人
		qualityTask.setUpdateTime(new Date());//更新时间
		qualityFormService.updateQualityTask(qualityTask);
		//数据返回
		outVo.setCode(Constants.Success);
		return outVo;
	}
	
	/**
	 * 删除质量检查任务
	 * @param qualityTask
	 * @return
	 */
	@RequestMapping(value="/deleteQualityTask")
	public @ResponseBody BaseDataVo deleteQualityTask(String seqId){
		BaseDataVo outVo = new BaseDataVo();
		if(StringUtils.isEmpty(seqId)){
			throw new CommRuntimeException("seqId不允许为空!");
		}
		QualityTask qualityTask = new QualityTask();
		qualityTask.setSeqId(Long.parseLong(seqId));
		//获取登录的用户信息
		SysUser user = WebContextUtils.getSessionUserInfo();
		qualityTask.setUpdatePerson(user.getUserCode());//更新人
		qualityTask.setUpdateTime(new Date());//更新时间
		qualityFormService.deleteQualityTask(qualityTask);
		//数据返回
		outVo.setCode(Constants.Success);
		return outVo;
	}
	
	/**
	 * 保存检测结果主内容
	 * @param userCode 用户编号
	 * @param qualityResult 质量检查任务
	 * @return
	 */
	@RequestMapping(value="/saveQualityResult")
	public @ResponseBody BaseDataVo saveQualityResult( @RequestBody QualityResult qualityResult){
		BaseDataVo outVo = new BaseDataVo();
		qualityResult.setResultCode(CodeUtils.getSysInvokeId());
		//获取登录的用户信息
		SysUser user = WebContextUtils.getSessionUserInfo();
		qualityResult.setCreatePerson(user.getUserCode());//创建人
		qualityResult.setUpdatePerson(user.getUserCode());
		qualityResult.setStatus(MnisQmConstants.STATUS_YX);//有效状态
		if(qualityResult.getSeqId()==null){
			qualityResult.setResultCode(CodeUtils.getSysInvokeId());
			qualityFormService.insertQualityResult(qualityResult);
		}else{
			qualityFormService.updateQualityResult(qualityResult);
		}
		//数据返回
		outVo.setCode(Constants.Success);
		return outVo;
	}
	
	/**
	 * 更新检测结果主内容
	 * @param qualityResult
	 * @return
	 */
	@RequestMapping(value="/updateQualityResult")
	public @ResponseBody BaseDataVo updateQualityResult(@RequestBody QualityResult qualityResult){
		BaseDataVo outVo = new BaseDataVo();
		//数据校验
		if(null == qualityResult || StringUtils.isEmpty(qualityResult.getResultCode()) || StringUtils.isEmpty(qualityResult.getTaskCode())){
			throw new CommRuntimeException("表单编号或任务编号不允许为空!");
		}
		//获取登录的用户信息
		SysUser user = WebContextUtils.getSessionUserInfo();
		qualityResult.setUpdatePerson(user.getUserCode());//更新人
		qualityResult.setUpdateTime(new Date());//更新时间
		qualityFormService.updateQualityResult(qualityResult);
		//数据返回
		outVo.setCode(Constants.Success);
		return outVo;
	}
	
	/**
	 * 删除检测结果主内容
	 * @param qualityResult
	 * @return
	 */
	@RequestMapping(value="/deleteQualityResult")
	public @ResponseBody BaseDataVo deleteQualityResult(String seqId){
		BaseDataVo outVo = new BaseDataVo();
		if(null == seqId || seqId.trim().length()==0){
			throw new CommRuntimeException("seqId不允许为空!");
		}
		QualityResult qualityResult = new QualityResult();
		qualityResult.setSeqId(Long.parseLong(seqId));
		//获取登录的用户信息
		SysUser user = WebContextUtils.getSessionUserInfo();
		qualityResult.setUpdatePerson(user.getUserCode());//更新人
		qualityResult.setUpdateTime(new Date());//更新时间
		qualityFormService.deleteQualityResult(qualityResult);
		//数据返回
		outVo.setCode(Constants.Success);
		return outVo;
	}
	
	/**
	 * 保存检查结果明细
	 * @param qualityResultDetail 检查结果明细
	 * @return
	 */
	@RequestMapping(value="/saveQualityResultDetail")
	public @ResponseBody BaseDataVo saveQualityResultDetail(@RequestBody List<QualityResultDetail> qualityResultDetail){
		BaseDataVo outVo = new BaseDataVo();
		SysUser user = WebContextUtils.getSessionUserInfo();
		String userCode = user.getUserCode();
		for (QualityResultDetail object : qualityResultDetail) {
			object.setCreatePerson(userCode);//创建人
			object.setCreateTime(new Date());//创建时间
			object.setStatus(MnisQmConstants.STATUS_YX);//有效状态
			qualityFormService.insertQualityResultDetail(object);
		}
		//数据返回
		outVo.setCode(Constants.Success);
		return outVo;
	}
	
	/**
	 * 更新检查结果明细
	 * @param qualityResultDetail
	 * @return
	 */
	@RequestMapping(value="/updateQualityResultDetail")
	public @ResponseBody BaseDataVo updateQualityResultDetail(@RequestBody List<QualityResultDetail> qualityResultDetail){
		BaseDataVo outVo = new BaseDataVo();
		SysUser user = WebContextUtils.getSessionUserInfo();
		String userCode = user.getUserCode();
		for (QualityResultDetail object : qualityResultDetail) {
			object.setUpdatePerson(userCode);//更新人
			object.setUpdateTime(new Date());//更新时间
			qualityFormService.updateQualityResultDetail(object);
		}
		//数据返回
		outVo.setCode(Constants.Success);
		return outVo;
	}
	
	/**
	 * 删除检查结果明细
	 * @param qualityResultDetail
	 * @return
	 */
	@RequestMapping(value="/deleteQualityResultDetail")
	public @ResponseBody BaseDataVo deleteQualityResultDetail(String seqId){
		BaseDataVo outVo = new BaseDataVo();
		if(null == seqId || seqId.trim().length()==0){
			throw new CommRuntimeException("seqId不允许为空!");
		}
		QualityResultDetail qualityResultDetail = new QualityResultDetail();
		qualityResultDetail.setSeqId(Long.parseLong(seqId));
		//获取登录的用户信息
		SysUser user = WebContextUtils.getSessionUserInfo();
		qualityResultDetail.setUpdatePerson(user.getUserCode());//更新人
		qualityResultDetail.setUpdateTime(new Date());//更新时间
		qualityFormService.deleteQualityResultDetail(qualityResultDetail);
		//数据返回
		outVo.setCode(Constants.Success);
		return outVo;
	}
	
	
	/**
	 * 保存质量检测问题处理记录
	 * @param userCode
	 * @param qualityIssueManage
	 * @return
	 */
	@RequestMapping(value="/saveQualityIssueManage")
	public @ResponseBody BaseDataVo saveQualityIssueManage(@RequestBody QualityIssueManage qualityIssueManage){
		BaseDataVo outVo = new BaseDataVo();
		//数据校验
		if(null == qualityIssueManage || StringUtils.isEmpty(qualityIssueManage.getResultCode())){
			throw new CommRuntimeException("表单编号不允许为空!");
		}
		//获取登录的用户信息
		SysUser user = WebContextUtils.getSessionUserInfo();
		qualityIssueManage.setCreatePerson(user.getUserCode());//创建人
		qualityIssueManage.setCreateTime(new Date());//创建时间
		qualityIssueManage.setStatus(MnisQmConstants.STATUS_YX);//有效状态
		qualityFormService.insertQualityIssueManage(qualityIssueManage);
		//数据返回
		outVo.setCode(Constants.Success);
		return outVo;
	}
	
	/**
	 * 更新质量检测问题处理记录
	 * @param qualityIssueManage
	 * @return
	 */
	@RequestMapping(value="/updateQualityIssueManage")
	public @ResponseBody BaseDataVo updateQualityIssueManage(@RequestBody QualityIssueManage qualityIssueManage){
		BaseDataVo outVo = new BaseDataVo();
		//数据校验
		if(null == qualityIssueManage || StringUtils.isEmpty(qualityIssueManage.getResultCode())){
			throw new CommRuntimeException("表单编号不允许为空!");
		}
		//获取登录的用户信息
		SysUser user = WebContextUtils.getSessionUserInfo();
		qualityIssueManage.setUpdatePerson(user.getUserCode());//更新人
		qualityIssueManage.setUpdateTime(new Date());//更新时间
		qualityFormService.updateQualityIssueManage(qualityIssueManage);
		//数据返回
		outVo.setCode(Constants.Success);
		return outVo;
	}
	
	/**
	 * 删除质量检测问题处理记录
	 * @param qualityIssueManage
	 * @return
	 */
	@RequestMapping(value="/deleteQualityIssueManage")
	public @ResponseBody BaseDataVo deleteQualityIssueManage(String seqId){
		BaseDataVo outVo = new BaseDataVo();
		if(null == seqId || seqId.trim().length()==0){
			throw new CommRuntimeException("seqId不允许为空!");
		}
		qualityFormService.deleteQualityIssueManage(seqId);
		//数据返回
		outVo.setCode(Constants.Success);
		return outVo;
	}
	
	/**
	 * 根据部门号和表单类型查找质量检查表
	 * @param type 表单类型
	 * @return
	 */
	@RequestMapping(value="/queryQualityFormByDeptCodeAndType")
	public @ResponseBody BaseDataVo queryQualityFormByDeptCodeAndType(String type){
		BaseDataVo outVo = new BaseDataVo();
		outVo.setData(qualityFormService.queryQualityFormByDeptCodeAndType(type));
		//数据返回
		outVo.setCode(Constants.Success);
		return outVo;
	}
	
	/**
	 * 根据条件查询质量检查任务
	 * @param date 任务日期
	 * @param deptCode 部门编号
	 * @param type 表单类型
	 * @return
	 */
	@RequestMapping(value="/queryQTRByTimeAndTypeAndDeptCode")
	public @ResponseBody BaseDataVo queryQTRByTimeAndTypeAndDeptCode(String date, String deptCode, String type){
		BaseDataVo outVo = new BaseDataVo();
		outVo.setData(qualityFormService.queryQTRByTimeAndTypeAndDeptCode(date, deptCode, type));
		//数据返回
		outVo.setCode(Constants.Success);
		return outVo;
	}
	
	/**
	 * 根据检查结果表编号查找质检问题处理记录
	 * @param resultCode
	 * @return
	 */
	@RequestMapping(value="/getQualityIssueManageByResultCode")
	public @ResponseBody BaseDataVo getQualityIssueManageByResultCode(String resultCode){
		BaseDataVo outVo = new BaseDataVo();
		if(null == resultCode || resultCode.trim().length() != 0){
			outVo.setData(qualityFormService.getQualityIssueManageByResultCode(resultCode));
		}
		//数据返回
		outVo.setCode(Constants.Success);
		return outVo;
	}
	
	/**
	 * 根据条件查询质量检查问题
	 * @param date 日期
	 * @param deptCode 部门编号
	 * @param type 表单类型
	 * @return
	 */
	@RequestMapping(value="/queryQIMByTimeAndTypeAndDeptCode")
	public @ResponseBody BaseDataVo queryQIMByTimeAndTypeAndDeptCode(String date, String deptCode, String type){
		BaseDataVo outVo = new BaseDataVo();
		outVo.setData(qualityFormService.queryQIMByTimeAndTypeAndDeptCode(date, deptCode, type));
		//数据返回
		outVo.setCode(Constants.Success);
		return outVo;
	}
	
	/**
	 * 根据条件查询质量检查结果
	 * @param date 日期
	 * @param deptCode 部门编号
	 * @param formName 表单类型
	 * @return
	 */
	@RequestMapping(value="/queryQRByTimeAndFormNameAndDeptCode")
	public @ResponseBody BaseDataVo queryQRByTimeAndFormNameAndDeptCode(String date, String deptCode){
		BaseDataVo outVo = new BaseDataVo();
		outVo.setData(qualityFormService.queryQRByTimeAndFormNameAndDeptCode(date, deptCode));
		//数据返回
		outVo.setCode(Constants.Success);
		return outVo;
	}
	
	/**
	 * 获取所有质量检查表单
	 * @return
	 */
	@RequestMapping(value="/queryAllQualityForm")
	public @ResponseBody BaseDataVo queryAllQualityForm(){
		BaseDataVo outVo = new BaseDataVo();
		outVo.setData(qualityFormService.queryAllQualityForm());
		//数据返回
		outVo.setCode(Constants.Success);
		return outVo;
	}
	
	/**
	 * 获取所有质量检查结果表单
	 * @return
	 */
	@RequestMapping(value="/queryAllQualityResult")
	public @ResponseBody BaseDataVo queryAllQualityResult(){
		BaseDataVo outVo = new BaseDataVo();
		outVo.setData(qualityFormService.queryAllQualityResult());
		//数据返回
		outVo.setCode(Constants.Success);
		return outVo;
	}
	
	/**
	 * 获取质量管理主界面信息
	 * @return
	 */
	@RequestMapping(value="/queryQualityInfo")
	public @ResponseBody BaseDataVo queryQualityInfo(){
		BaseDataVo outVo = new BaseDataVo();
		outVo.setData(qualityFormService.selectAllQualityInfo());
		//数据返回
		outVo.setCode(Constants.Success);
		return outVo;
	}
	
	/**
	 * 保存质控小组
	 * @param qualityTeam
	 * @return
	 */
	@RequestMapping(value="/saveQualityTeam")
	public @ResponseBody BaseDataVo saveQualityTeam(
			@RequestBody QualityTeam qualityTeam){
		BaseDataVo vo = new BaseDataVo();
		SysUser user = WebContextUtils.getSessionUserInfo();
		qualityTeam.setCreatePerson(user.getUserCode());
		qualityTeam.setUpdatePerson(user.getUserCode());
		qualityFormService.saveQualityTeam(qualityTeam);
		vo.setMsg(Constants.Success);
		return vo;
	}
	
	/**
	 * 根据队名查询质控小组
	 * @param teamName
	 * @return
	 */
	@RequestMapping(value="/queryQualityTeamByTeamName")
	public @ResponseBody BaseDataVo queryQTByTeamName(String teamName){
		BaseDataVo vo = new BaseDataVo();
		vo.setData(qualityFormService.selectQualityTeamByTeamName(teamName));
		vo.setMsg(Constants.Success);
		return vo;
	}
	
	/**
	 * 根据主键删除质控小组
	 * @param teamName
	 * @param userCode
	 * @return
	 */
	@RequestMapping(value="/deleteQualityTeam")
	public @ResponseBody BaseDataVo deleteQualityTeam(String teamCode){
		BaseDataVo vo = new BaseDataVo();
		QualityTeam team = new QualityTeam();
		team.setTeamCode(teamCode);
		qualityFormService.deletQualityTeamByPrimaryKey(team);
		vo.setMsg(Constants.Success);
		return vo;
	}
	
	/**
	 * 查询检查模板
	 * @param modelName
	 * @param isUsed
	 * @return
	 */
	@RequestMapping(value="/getQualityModel")
	public @ResponseBody BaseDataVo getQualityModel(Long seqId,
			String modelName,String isUsed){		
		BaseDataVo vo = new BaseDataVo();
		QualityModel model = new QualityModel();
		model.setSeqId(seqId);
		model.setModelName(modelName);
		model.setIsUsed(isUsed);
		vo.setData(qualityFormService.selectQualityModel(model));
		vo.setMsg(Constants.Success);
		return vo;
	}
	
	/**
	 * 保存检查模板
	 * @param qualityModel
	 * @return
	 */
	@RequestMapping(value="/saveQualityModel")
	public @ResponseBody BaseDataVo saveQualityModel(
			@RequestBody QualityModel qualityModel){
		BaseDataVo vo = new BaseDataVo();
		SysUser user = WebContextUtils.getSessionUserInfo();
		qualityModel.setCreatePerson(user.getUserCode());
		qualityModel.setUpdatePerson(user.getUserCode());
		vo.setData(qualityFormService.saveQualityModel(qualityModel));
		vo.setMsg(Constants.Success);
		return vo;
	}
	
	/**
	 * 根据id删除检查模板
	 * @param seqId
	 * @return
	 */
	@RequestMapping(value="/deleteQualityModel")
	public @ResponseBody BaseDataVo deleteQualityModel(Long seqId){
		BaseDataVo vo = new BaseDataVo();
		qualityFormService.deleteQualityModel(seqId);
		vo.setMsg(Constants.Success);
		return vo;
	}
	
	/**
	 * 保存检查计划
	 * @param qualityPlan
	 * @return
	 */
	@RequestMapping(value="/saveQualityPlan")
	public @ResponseBody BaseDataVo saveQualityPlan(
			@RequestBody QualityPlan qualityPlan){
		BaseDataVo vo = new BaseDataVo();
		SysUser user = WebContextUtils.getSessionUserInfo();
		qualityPlan.setCreatePerson(user.getUserCode());
		qualityPlan.setUpdatePerson(user.getUserCode());
		vo.setData(qualityFormService.saveQualityPlan(qualityPlan));
		vo.setMsg(Constants.Success);
		return vo;
	}
	
	/**
	 * 获取检查计划
	 * @param qualityPlan
	 * @return
	 */
	@RequestMapping(value="/getQualityPlan")
	public @ResponseBody BaseDataVo getQualityPlan(
			Long seqId, String teamName, String startDate, String endDate){
		QualityPlan qualityPlan =  new QualityPlan();
		if(seqId!=null){
			qualityPlan.setSeqId(seqId);
		}
		if(teamName!=null){
			qualityPlan.setTeamName(teamName);
		}
		if(startDate!=null){
			qualityPlan.setStartDate(startDate);
		}
		if(endDate!=null){
			qualityPlan.setEndDate(endDate);
		}
		BaseDataVo vo = new BaseDataVo();
		vo.setData(qualityFormService.selectQualityPlan(qualityPlan));
		vo.setMsg(Constants.Success);
		return vo;
	}
	
	/**
	 * 删除检查计划
	 * @param seqId
	 * @return
	 */
	@RequestMapping(value="/deleteQualityPlan")
	public @ResponseBody BaseDataVo deleteQualityPlan(Long seqId){
		BaseDataVo vo = new BaseDataVo();
		qualityFormService.deleteQualityPlan(seqId);
		vo.setMsg(Constants.Success);
		return vo;
	}
	
	/**
	 * 保存指标分子分母
	 * @param qualityIndexItem
	 * @return
	 */
	@RequestMapping(value="/saveQualityIndexItem")
	public @ResponseBody BaseDataVo saveQualityIndexItem(
			@RequestBody QualityIndexItem qualityIndexItem){
		BaseDataVo vo = new BaseDataVo();
		SysUser user = WebContextUtils.getSessionUserInfo();
		qualityIndexItem.setCreatePerson(user.getUserCode());
		qualityIndexItem.setUpdatePerson(user.getUserCode());
		qualityFormService.saveQualityIndexItem(qualityIndexItem);
		vo.setMsg(Constants.Success);
		return vo;
	}
	
	/**
	 * 获得指标分子分母项
	 * @param indexItemName 名称
	 * @param indexItemType 类型，01分子，02分母
	 * @return
	 */
	@RequestMapping(value="/getQualityIndexItem")
	public @ResponseBody BaseDataVo getQualityIndexItem(
			String indexItemName, 
			String indexItemType){
		BaseDataVo vo = new BaseDataVo();
		Map<String, Object> map = new HashMap<String, Object>();
		if(indexItemName!=null&&!"".equals(indexItemName)){
			map.put("indexItemName", indexItemName);
		}
		if(indexItemType!=null&&!"".equals(indexItemType)){
			map.put("indexItemType", indexItemType);
		}
		vo.setData(qualityFormService.selectQualityIndexItem(map));
		vo.setMsg(Constants.Success);
		return vo;
	}
	
	/**
	 * 删除指标分子分母
	 * @param seqId
	 * @return
	 */
	@RequestMapping(value="/deleteQualityIndexItem")
	public @ResponseBody BaseDataVo deleteQualityIndexItem(Long seqId){
		BaseDataVo vo = new BaseDataVo();
		qualityFormService.deleteQualityIndexItem(seqId);
		vo.setMsg(Constants.Success);
		return vo;
	}
	
	/**
	 * 保存质量指标
	 * @param qualityIndex
	 * @return
	 */
	@RequestMapping(value="/saveQualityIndex")
	public @ResponseBody BaseDataVo saveQualityIndex(
			@RequestBody QualityIndex qualityIndex){
		BaseDataVo vo = new BaseDataVo();
		SysUser user = WebContextUtils.getSessionUserInfo();
		qualityIndex.setCreatePerson(user.getUserCode());
		qualityIndex.setUpdatePerson(user.getUserCode());
		qualityFormService.saveQualityIndex(qualityIndex);
		vo.setMsg(Constants.Success);
		return vo;
	}
	
	/**
	 * 获得质量指标
	 * @param qualityIndex
	 * @return
	 */
	@RequestMapping(value="/getQualityIndex")
	public @ResponseBody BaseDataVo getQualityIndex(
			@RequestBody QualityIndex qualityIndex){
		BaseDataVo vo = new BaseDataVo();
		vo.setData(qualityFormService.selectQualityIndex(qualityIndex));
		vo.setMsg(Constants.Success);
		return vo;
	}
	
	/**
	 * 删除质量指标
	 * @param seqId
	 * @return
	 */
	@RequestMapping(value="/deleteQualityIndex")
	public @ResponseBody BaseDataVo deleteQualityIndex(Long seqId){
		BaseDataVo vo = new BaseDataVo();
		qualityFormService.deleteQualityIndex(seqId);
		vo.setMsg(Constants.Success);
		return vo;
	}
	
	@RequestMapping(value="/saveQualityModelDetail")
	public @ResponseBody BaseDataVo saveQualityModelDetail(
			@RequestBody QualityModelTrans qualityModelTrans){
		BaseDataVo vo = new BaseDataVo();
		qualityFormService.savetModelDetail(qualityModelTrans);
		vo.setMsg(Constants.Success);
		return vo;
	}
	
	/**
	 * 复制检查模板
	 * @param seqId
	 * @return
	 */
	@RequestMapping(value="/copyQualityModel")
	public @ResponseBody BaseDataVo copyQualityModel(Long seqId){
		BaseDataVo vo = new BaseDataVo();
		SysUser user = WebContextUtils.getSessionUserInfo();
		vo.setData(qualityFormService.copyQualityModel(seqId, user.getUserCode()));
		vo.setMsg(Constants.Success);
		return vo;
	}
}
