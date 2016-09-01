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
import com.lachesis.mnisqm.module.manualWork.domain.ManualWork;
import com.lachesis.mnisqm.module.manualWork.domain.ManualWorkDetail;
import com.lachesis.mnisqm.module.manualWork.domain.ManualWorkItem;
import com.lachesis.mnisqm.module.manualWork.service.IManualWorkService;
import com.lachesis.mnisqm.module.system.domain.SysUser;

@RequestMapping("/manualWork")
@Controller
public class ManualWorkController {

	@Autowired
	private IManualWorkService manualWorkService;
	
	/**
	 * 新增工作项目记录
	 * @param manualWorkItem
	 * @return
	 */
	@RequestMapping(value="/saveManualWorkItem")
	public @ResponseBody BaseDataVo saveManualWorkItem(@RequestBody ManualWorkItem manualWorkItem){
		BaseDataVo outVo = new BaseDataVo();
		//数据校验
		if((null == manualWorkItem) || (StringUtils.isEmpty(manualWorkItem.getItemName()) )){
			throw new CommRuntimeException("工作项目名称不允许为空!");
		}
		//获取登录的用户信息
		SysUser user = WebContextUtils.getSessionUserInfo();
		manualWorkItem.setCreatePerson(user.getUserCode());//创建人
		manualWorkItem.setCreateTime(new Date());//创建时间
		manualWorkItem.setStatus(MnisQmConstants.STATUS_YX);//有效状态
		manualWorkService.insertManualWorkItem(manualWorkItem);
		//数据返回
		outVo.setCode(Constants.Success);
		return outVo;
	}
	
	/**
	 * 修改工作项目记录
	 * @param manualWorkItem
	 * @return
	 */
	@RequestMapping(value="/updateManualWorkItem")
	public @ResponseBody BaseDataVo updateManualWorkItem(@RequestBody ManualWorkItem manualWorkItem){
		BaseDataVo outVo = new BaseDataVo();
		//数据校验
		if(null == manualWorkItem || StringUtils.isEmpty(manualWorkItem.getItemName()) || StringUtils.isEmpty(manualWorkItem.getItemCode())){
			throw new CommRuntimeException("工作项目编号或名称不允许为空!");
		}
		//获取登录的用户信息
		SysUser user = WebContextUtils.getSessionUserInfo();
		manualWorkItem.setUpdatePerson(user.getUserCode());//更新人
		manualWorkItem.setUpdateTime(new Date());//更新时间
		manualWorkService.updateManualWorkItem(manualWorkItem);
		//数据返回
		outVo.setCode(Constants.Success);
		return outVo;
	}
	
	/**
	 * 删除工作项目记录
	 * @param manualWorkItem
	 * @return
	 */
	@RequestMapping(value="/deleteManualWorkItem")
	public @ResponseBody BaseDataVo deleteManualWorkItem(@RequestBody ManualWorkItem manualWorkItem){
		BaseDataVo outVo = new BaseDataVo();
		//数据校验
		if(null == manualWorkItem || StringUtils.isEmpty(manualWorkItem.getItemName()) || StringUtils.isEmpty(manualWorkItem.getItemCode())){
			throw new CommRuntimeException("工作项目编号或名称不允许为空!");
		}
		//获取登录的用户信息
		SysUser user = WebContextUtils.getSessionUserInfo();
		manualWorkItem.setUpdatePerson(user.getUserCode());//更新人
		manualWorkItem.setUpdateTime(new Date());//更新时间
		manualWorkService.deleteManualWorkItem(manualWorkItem);
		//数据返回
		outVo.setCode(Constants.Success);
		return outVo;
	}
	
	/**
	 * 新增工作安排记录
	 * @param manualWorkItem
	 * @return
	 */
	@RequestMapping(value="/saveManualWork")
	public @ResponseBody BaseDataVo saveManualWork(@RequestBody ManualWork manualWork){
		BaseDataVo outVo = new BaseDataVo();
		//数据校验
		if(null == manualWork || StringUtils.isEmpty(manualWork.getItemCode())){
			throw new CommRuntimeException("工作项目编号不允许为空!");
		}
		//获取登录的用户信息
		SysUser user = WebContextUtils.getSessionUserInfo();
		manualWork.setCreatePerson(user.getUserCode());//创建人
		manualWork.setCreateTime(new Date());//创建时间
		manualWork.setStatus(MnisQmConstants.STATUS_YX);//有效状态
		manualWorkService.insertManualWork(manualWork);
		//数据返回
		outVo.setCode(Constants.Success);
		return outVo;
	}
	
	/**
	 * 修改工作安排记录
	 * @param manualWorkItem
	 * @return
	 */
	@RequestMapping(value="/updateManualWork")
	public @ResponseBody BaseDataVo updateManualWork(@RequestBody ManualWork manualWork){
		BaseDataVo outVo = new BaseDataVo();
		//数据校验
		if(null == manualWork || StringUtils.isEmpty(manualWork.getWorkCode()) || StringUtils.isEmpty(manualWork.getItemCode())){
			throw new CommRuntimeException("工作项目编号或工作编号不允许为空!");
		}
		//获取登录的用户信息
		SysUser user = WebContextUtils.getSessionUserInfo();
		manualWork.setUpdatePerson(user.getUserCode());//更新人
		manualWork.setUpdateTime(new Date());//更新时间
		manualWorkService.updateManualWork(manualWork);
		//数据返回
		outVo.setCode(Constants.Success);
		return outVo;
	}
	
	/**
	 * 删除工作安排记录
	 * @param manualWorkItem
	 * @return
	 */
	@RequestMapping(value="/deleteManualWork")
	public @ResponseBody BaseDataVo deleteManualWork(@RequestBody ManualWork manualWork){
		BaseDataVo outVo = new BaseDataVo();
		//数据校验
		if(null == manualWork || StringUtils.isEmpty(manualWork.getWorkCode()) || StringUtils.isEmpty(manualWork.getItemCode())){
			throw new CommRuntimeException("工作项目编号或工作编号不允许为空!");
		}
		//获取登录的用户信息
		SysUser user = WebContextUtils.getSessionUserInfo();
		manualWork.setUpdatePerson(user.getUserCode());//更新人
		manualWork.setUpdateTime(new Date());//更新时间
		manualWorkService.deleteManualWork(manualWork);
		//数据返回
		outVo.setCode(Constants.Success);
		return outVo;
	}
	
	/**
	 * 新增工作明细记录
	 * @param manualWorkItem
	 * @return
	 */
	@RequestMapping(value="/saveManualWorkDetail")
	public @ResponseBody BaseDataVo saveManualWorkDetail(@RequestBody ManualWorkDetail manualWorkDetail){
		BaseDataVo outVo = new BaseDataVo();
		//数据校验
		if(null == manualWorkDetail || StringUtils.isEmpty(manualWorkDetail.getWorkCode()) || StringUtils.isEmpty(manualWorkDetail.getWorkItem())){
			throw new CommRuntimeException("工作编号或工作信息不允许为空!");
		}
		//获取登录的用户信息
		SysUser user = WebContextUtils.getSessionUserInfo();
		manualWorkDetail.setCreatePerson(user.getUserCode());//创建人
		manualWorkDetail.setCreateTime(new Date());//创建时间
		manualWorkDetail.setStatus(MnisQmConstants.STATUS_YX);//有效状态
		manualWorkService.insertManualWorkDetail(manualWorkDetail);
		//数据返回
		outVo.setCode(Constants.Success);
		return outVo;
	}
	
	/**
	 * 修改工作明细记录
	 * @param manualWorkItem
	 * @return
	 */
	@RequestMapping(value="/updateManualWorkDetail")
	public @ResponseBody BaseDataVo updateManualWorkDetail(@RequestBody ManualWorkDetail manualWorkDetail){
		BaseDataVo outVo = new BaseDataVo();
		//数据校验
		if(null == manualWorkDetail || StringUtils.isEmpty(manualWorkDetail.getWorkCode()) || StringUtils.isEmpty(manualWorkDetail.getWorkItem())){
			throw new CommRuntimeException("工作编号或工作信息不允许为空!");
		}
		//获取登录的用户信息
		SysUser user = WebContextUtils.getSessionUserInfo();
		manualWorkDetail.setUpdatePerson(user.getUserCode());//更新人
		manualWorkDetail.setUpdateTime(new Date());//更新时间
		manualWorkService.updateManualWorkDetail(manualWorkDetail);
		//数据返回
		outVo.setCode(Constants.Success);
		return outVo;
	}
	
	/**
	 * 删除工作明细记录
	 * @param manualWorkItem
	 * @return
	 */
	@RequestMapping(value="/deleteManualWorkDetail")
	public @ResponseBody BaseDataVo deleteManualWorkDetail(@RequestBody ManualWorkDetail manualWorkDetail){
		BaseDataVo outVo = new BaseDataVo();
		//数据校验
		if(null == manualWorkDetail || StringUtils.isEmpty(manualWorkDetail.getWorkCode()) || StringUtils.isEmpty(manualWorkDetail.getWorkItem())){
			throw new CommRuntimeException("工作编号或工作信息不允许为空!");
		}
		//获取登录的用户信息
		SysUser user = WebContextUtils.getSessionUserInfo();
		manualWorkDetail.setUpdatePerson(user.getUserCode());//更新人
		manualWorkDetail.setUpdateTime(new Date());//更新时间
		manualWorkService.deleteManualWorkDetail(manualWorkDetail);
		//数据返回
		outVo.setCode(Constants.Success);
		return outVo;
	}

	/**
	 * 根据科室编号查询工作项目记录
	 * @param deptCode 科室编号
	 * @return
	 */
	@RequestMapping(value="/queryManualWorkItemByDeptCode")
	public @ResponseBody BaseDataVo queryManualWorkItemByDeptCode(String deptCode){
		BaseDataVo outVo = new BaseDataVo();
		//数据校验
		if(StringUtils.isEmpty(deptCode)){
			throw new CommRuntimeException("科室编号不允许为空!");
		}
		outVo.setData(manualWorkService.queryManualWorkItemByDeptCode(deptCode));
		//数据返回
		outVo.setCode(Constants.Success);
		return outVo;
	}
	

	/**
	 * 根据科室编号和月份查询工作项目记录
	 * @param deptCode 科室编号
	 * @param beginTime 开始时间
	 * @param endTime 结束时间
	 * @return
	 */
	@RequestMapping(value="/queryManualWorkByDeptCodeAndDate")
	public @ResponseBody BaseDataVo queryManualWorkByDeptCodeAndDate(String deptCode, String beginTime, String endTime){
		BaseDataVo outVo = new BaseDataVo();
		//数据校验
		if(StringUtils.isEmpty(deptCode) || StringUtils.isEmpty(beginTime)|| StringUtils.isEmpty(endTime)){
			throw new CommRuntimeException("科室编号或者日期不允许为空!");
		}
		outVo.setData(manualWorkService.queryManualWorkByDeptCodeAndDate(deptCode, beginTime, endTime));
		//数据返回
		outVo.setCode(Constants.Success);
		return outVo;
	}
	
}
