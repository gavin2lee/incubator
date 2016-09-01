package com.lachesis.mnisqm.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lachesis.mnisqm.BaseDataVo;
import com.lachesis.mnisqm.BaseMapVo;
import com.lachesis.mnisqm.common.WebContextUtils;
import com.lachesis.mnisqm.constants.Constants;
import com.lachesis.mnisqm.constants.MnisQmConstants;
import com.lachesis.mnisqm.core.CommRuntimeException;
import com.lachesis.mnisqm.core.utils.CodeUtils;
import com.lachesis.mnisqm.core.utils.StringUtils;
import com.lachesis.mnisqm.module.event.domain.AnalysisInfo;
import com.lachesis.mnisqm.module.event.domain.EventAssessment;
import com.lachesis.mnisqm.module.event.domain.EventElement;
import com.lachesis.mnisqm.module.event.domain.EventLevel;
import com.lachesis.mnisqm.module.event.domain.EventMeasures;
import com.lachesis.mnisqm.module.event.domain.EventReport;
import com.lachesis.mnisqm.module.event.domain.EventReportDetail;
import com.lachesis.mnisqm.module.event.domain.EventRequirement;
import com.lachesis.mnisqm.module.event.domain.EventType;
import com.lachesis.mnisqm.module.event.domain.RiskTrendDate;
import com.lachesis.mnisqm.module.event.service.IEventService;
import com.lachesis.mnisqm.module.system.domain.SysTask;
import com.lachesis.mnisqm.module.system.domain.SysTaskLog;
import com.lachesis.mnisqm.module.system.domain.SysUser;
import com.lachesis.mnisqm.module.system.service.ISysService;
import com.lachesis.mnisqm.module.user.service.IUserService;
import com.lachesis.mnisqm.vo.event.EventVo;

@RequestMapping("/event")
@Controller
public class EventController {
	Logger log = LoggerFactory.getLogger(EventController.class);

	@Autowired
	IEventService service;

	@Autowired
	private ISysService sysService;
	
	@Autowired
	IUserService userService;

	@RequestMapping("/getEventTypeList")
	public @ResponseBody BaseMapVo getEventTypeList() {
		BaseMapVo outVo = new BaseMapVo();
		outVo.addData("lst", service.getEventType());
		// 成功
		outVo.setCode(Constants.Success);
		// 数据输出
		return outVo;
	}

	/**
	 * 保存不良事件类型
	 * 
	 * @param eventType
	 * @return
	 */
	@RequestMapping(value = "saveEventType")
	public @ResponseBody BaseDataVo saveEventType(
			@RequestBody EventType eventType) {
		BaseDataVo vo = new BaseDataVo();
		SysUser user = WebContextUtils.getSessionUserInfo();
		eventType.setCreatePerson(user.getUserCode());
		eventType.setUpdatePerson(user.getUserCode());
		if (StringUtils.isEmpty(eventType.getEventTypeCode())) {
			throw new CommRuntimeException("不良事件类型编号不允许为空!");
		}
		if (StringUtils.isEmpty(eventType.getEventTypeName())) {
			throw new CommRuntimeException("不良事件类型名称不允许为空!");
		}
		if (StringUtils.isEmpty(eventType.getDeptCode())) {
			throw new CommRuntimeException("部门编号不允许为空!");
		}
		service.saveEventType(eventType);
		vo.setCode(Constants.Success);
		return vo;
	}

	/**
	 * 删除不良事件类型
	 * 
	 * @param seqId
	 * @return
	 */
	@RequestMapping(value = "deleteEventType")
	public @ResponseBody BaseDataVo deleteEventType(Long seqId) {
		BaseDataVo vo = new BaseDataVo();
		EventType eventType = new EventType();
		SysUser user = WebContextUtils.getSessionUserInfo();
		eventType.setUpdatePerson(user.getUserCode());
		eventType.setSeqId(seqId);
		service.deleteEventType(eventType);
		vo.setCode(Constants.Success);
		return vo;
	}

	/**
	 * 保存不良事件类型元素
	 * 
	 * @param eventElement
	 * @return
	 */
	@RequestMapping(value = "saveEventElement")
	public @ResponseBody BaseDataVo saveEventElement(
			@RequestBody EventElement eventElement) {
		BaseDataVo vo = new BaseDataVo();
		SysUser user = WebContextUtils.getSessionUserInfo();
		eventElement.setCreatePerson(user.getUserCode());
		eventElement.setUpdatePerson(user.getUserCode());
		if (StringUtils.isEmpty(eventElement.getEventTypeCode())) {
			throw new CommRuntimeException("不良事件类型编号不允许为空!");
		}
		if (StringUtils.isEmpty(eventElement.getElementCode())) {
			throw new CommRuntimeException("元素编号不允许为空!");
		}
		if (StringUtils.isEmpty(eventElement.getElementName())) {
			throw new CommRuntimeException("元素名不允许为空!");
		}
		if (StringUtils.isEmpty(eventElement.getViewName())) {
			throw new CommRuntimeException("元素显示名称不允许为空!");
		}
		if (StringUtils.isEmpty(eventElement.getGroupCode())) {
			throw new CommRuntimeException("元素分组编号不允许为空!");
		}
		if (StringUtils.isEmpty(eventElement.getElementWidth())) {
			throw new CommRuntimeException("元素宽度不允许为空!");
		}
		if (eventElement.getRequired() == null) {
			throw new CommRuntimeException("是否必填不允许为空!1为必填，0为不必填");
		}
		service.saveEventElement(eventElement);
		vo.setCode(Constants.Success);
		return vo;
	}

	/**
	 * 获取所有不良事件类型元素列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "getAllEventElement")
	public @ResponseBody BaseDataVo getAllEventElement() {
		BaseDataVo vo = new BaseDataVo();
		vo.setData(service.selectAllEventElement());
		vo.setCode(Constants.Success);
		return vo;
	}

	/**
	 * 根据不良事件类型编码获取不良事件类型元素列表
	 * 
	 * @param eventTypeCode
	 * @return
	 */
	@RequestMapping(value = "getEventElementByEventTypeCode")
	public @ResponseBody BaseDataVo getEventElementByEventTypeCode(
			String eventTypeCode) {
		BaseDataVo vo = new BaseDataVo();
		vo.setData(service.getEventElementByTypeCode(eventTypeCode));
		vo.setCode(Constants.Success);
		return vo;
	}

	/**
	 * 删除不良事件类型元素
	 * 
	 * @param seqId
	 * @return
	 */
	@RequestMapping(value = "deleteEventElement")
	public @ResponseBody BaseDataVo deleteEventElement(Long seqId) {
		BaseDataVo vo = new BaseDataVo();
		EventElement eventElement = new EventElement();
		SysUser user = WebContextUtils.getSessionUserInfo();
		eventElement.setUpdatePerson(user.getUserCode());
		eventElement.setSeqId(seqId);
		service.deleteEventElementByPrimaryKey(eventElement);
		vo.setCode(Constants.Success);
		return vo;
	}

	/**
	 * 不良事件报告列表查询
	 * 
	 * @param report
	 * @return
	 */
	@RequestMapping(value = "getEventList")
	public @ResponseBody BaseMapVo getEventList(String deptCode, String status,
			String userCode, String reportCode) {
		BaseMapVo outVo = new BaseMapVo();
		EventReport report = new EventReport();
		if("0410".equals(deptCode)){
			deptCode=null;
		}
		report.setDeptCode(deptCode);
		report.setStatus(status);
		report.setUserCode(userCode);
		report.setReportCode(reportCode);
		SysUser user = WebContextUtils.getSessionUserInfo();
//		SysUser user=sysService.checkLogin("283", "123");
		//审批人
		String apprvUserCode = user.getUserCode();
		System.out.println("要查询的userCode---->"+userCode);
		System.out.println("要查询的deptCode---->"+deptCode);
		System.out.println("要查询的reportCode---->"+reportCode);
		// 数据查询
		List<EventReport> lst = service.getEventReport(report,apprvUserCode);
		outVo.addData("lst",lst );

		outVo.setCode(Constants.Success);
		return outVo;
	}

	/**
	 * 获取不良事件级别
	 * 
	 * @return
	 */
	@RequestMapping(value = "getEventLevels")
	public @ResponseBody BaseMapVo getEventLevels() {
		BaseMapVo outVo = new BaseMapVo();
		outVo.addData("lst", service.getEventLevels());
		outVo.setCode(Constants.Success);

		outVo.setCode(Constants.Success);// 成功状态
		return outVo;
	}

	/**
	 * 保存不良事件级别
	 * 
	 * @return
	 */
	@RequestMapping(value = "saveEventLevel")
	public @ResponseBody BaseDataVo saveEventLevel(@RequestBody EventLevel level) {
		BaseDataVo outVo = new BaseDataVo();
		// 获取登录的用户信息
		SysUser user = WebContextUtils.getSessionUserInfo();
		level.setUpdatePerson(user.getUserCode());// 更新人
		level.setCreatePerson(user.getUserCode());// 创建人
		level.setStatus(MnisQmConstants.STATUS_YX);// 有效状态
		level.setDamageLevel(CodeUtils.getSysInvokeId());// 唯一编号
		// 数据校验
		if (StringUtils.isEmpty(level.getDamageLevelName())) {
			throw new CommRuntimeException("不良事件等级不允许为空!");
		}
		service.saveEventLevel(level);
		// 数据返回
		outVo.setCode(Constants.Success);
		return outVo;
	}

	/**
	 * 删除不良事件级别
	 * 
	 * @return
	 */
	@RequestMapping(value = "deleteEventLevel")
	public @ResponseBody BaseDataVo deleteEventLevel(
			@RequestBody EventLevel level) {
		BaseDataVo outVo = new BaseDataVo();
		// 获取登录的用户信息
		SysUser user = WebContextUtils.getSessionUserInfo();
		level.setUpdatePerson(user.getUserCode());// 更新人
		level.setStatus(MnisQmConstants.STATUS_WX);
		service.deleteEventLevel(level);
		outVo.setCode(Constants.Success);
		return outVo;
	}

	/**
	 * 新增或修改不良事件 暂存状态
	 * 
	 * @param report
	 * @return
	 */
	@RequestMapping(value = "saveEventReport")
	public @ResponseBody BaseDataVo saveEventReport(
			@RequestBody EventReport report) {
		BaseDataVo outVo = new BaseDataVo();
		// 从session中获取登录信息
		SysUser user = WebContextUtils.getSessionUserInfo();
//		 SysUser user=sysService.checkLogin("283", "123");
		report.setStatus(MnisQmConstants.STATUS_YX);// 有效状态
		report.setCreatePerson(user.getUserName());// 创建人
		report.setUpdatePerson(user.getUserName());// 更新人
		report.setUserCode(user.getUserCode());// 上报员工编号
		if (StringUtils.isEmpty(user.getUserName())) {
			report.setUserName(user.getUserCode());// 上报员工姓名
		} else {
			report.setUserName(user.getUserName());// 上报员工姓名
		}
		service.saveEventReport(report);
		outVo.setCode(Constants.Success);
		return outVo;
	}

	/**
	 * 对不良事件进行处理
	 * 
	 * @param report
	 * @param opType
	 *            submitEventReportToHeadNurse 提交至护士长
	 *            submitEventReportToNurseDept 提交不良事件到护理部
	 *            trackEventReport 不良事件审核通过，开始追踪
	 *            terminateEventReport 终止不良事件
	 *            returnEventReportToNurse 打回不良事件至护士，重新修改 (护士长的操作)
	 *            returnEventReportToHeadNurse 打回不良事件至护士长，重新修改 (护理部的操作)
	 * @return
	 */
	@RequestMapping(value = "handleEventReport",method=RequestMethod.POST )
	public @ResponseBody BaseDataVo handleEventReport(@RequestBody EventVo eventVo) {
		BaseDataVo outVo = new BaseDataVo();
		String reportCode=eventVo.getReportCode();
		String opType=eventVo.getOpType();
		// 从session中获取登录信息
		SysUser user = WebContextUtils.getSessionUserInfo();
		// SysUser user=sysService.checkLogin("332", "332");
		// 通过reportCode查到report
		EventReport r = new EventReport();
		r.setReportCode(reportCode);
		List<EventReport> reports = service.getEventReport(r, user.getUserCode());
		EventReport report = null;
		if (null != reports) {
			System.out.println("不良事件记录条数："+reports.size());
			report = reports.get(0);
		}
		//加入提交是否合理的 判断   状态校验
		SysTask task=new SysTask();
		SysTaskLog log=new SysTaskLog();
		// 处理不良事件时，主要是根据执行的操作类型 相应更改 对应任务的字段，并新增相关日志
		switch (opType) {
		case MnisQmConstants.OP_TYPE_01:
			// 提交至护士长，
			//判断 新提交-taskCode为空 (为report设置taskCode)   打回之后再提交(使用之前的reportCode)
			if(null==report.getTaskCode()){
				report.setTaskCode(CodeUtils.getSysInvokeId());
				// 更新report主表
				service.saveEventReport(report);
				// 开启任务，为task设置参数 任务编号为不良事件中的taskCode 状态为新提交 处理人为护士长
				task.setTaskCode(report.getTaskCode());
				task.setCreatePerson(user.getUserCode());
				task.setStatus(MnisQmConstants.STATUS_YX);
			}else{
				task=service.getSysTaskByTaskCode(report.getTaskCode());
			}
			task.setTaskStatus(MnisQmConstants.TASK_STATUS_01);
			//找到 应该上报的部门，此时为 提交事件者 所在部门
			task.setHandleDept(report.getDeptCode()); //设置处理部门为事件发生部门,需要改成总护士长所在部门
			// 处理日志
			log.setLogStatus(MnisQmConstants.TASK_STATUS_01);
			break;
		case MnisQmConstants.OP_TYPE_02:
			// 提交不良事件到护理部
			//判断 新提交-taskCode为空 还是打回之后再提交
			if(null==report.getTaskCode()){
				report.setTaskCode(CodeUtils.getSysInvokeId());
				// 更新report主表
				service.saveEventReport(report);
				//创建任务
				task.setTaskCode(report.getTaskCode());
				task.setCreatePerson(user.getUserCode());
				task.setStatus(MnisQmConstants.STATUS_YX);
			}else{
				// 根据reportCode找到对应的任务
				task = service.getSysTaskByTaskCode(report.getTaskCode());
				task.setUpdatePerson(user.getUserCode());
			}
			task.setTaskStatus(MnisQmConstants.TASK_STATUS_02);
			task.setHandleDept("0410");//设置处理部门为 0410
			log.setLogStatus(MnisQmConstants.TASK_STATUS_02);
			break;
		case MnisQmConstants.OP_TYPE_03:
			// 不良事件审核通过，开始追踪
			task = service.getSysTaskByTaskCode(report.getTaskCode());
			task.setTaskStatus(MnisQmConstants.TASK_STATUS_03);
			task.setHandleDept(null);
			task.setUpdatePerson(user.getUserCode());
			log.setLogStatus(MnisQmConstants.TASK_STATUS_03);
			break;
		case MnisQmConstants.OP_TYPE_04:
			// 打回不良事件至护士，重新修改 (护士长的操作)
			task = service.getSysTaskByTaskCode(report.getTaskCode());
			task.setTaskStatus(MnisQmConstants.TASK_STATUS_04);
			//设置handleDept为空,设置处理人为任务创建者
			task.setHandleDept(null);
			task.setHandlePerson(task.getCreatePerson());// 最先创建任务的人
			task.setUpdatePerson(user.getUserCode());
			log.setLogStatus(MnisQmConstants.TASK_STATUS_04);
			break;
		case MnisQmConstants.OP_TYPE_05:
			// 打回不良事件至护士长，重新修改 (护理部的操作)
			task = service.getSysTaskByTaskCode(report.getTaskCode());
			task.setTaskStatus(MnisQmConstants.TASK_STATUS_05);
			task.setHandleDept(report.getDeptCode());
			task.setUpdatePerson(user.getUserCode());
			log.setLogStatus(MnisQmConstants.TASK_STATUS_05);
			break;
		case MnisQmConstants.OP_TYPE_06:
			// 终止不良事件
			task = service.getSysTaskByTaskCode(report.getTaskCode());
			task.setTaskStatus(MnisQmConstants.TASK_STATUS_06);
			task.setHandleDept(null);
			task.setHandlePerson(null);//
			task.setUpdatePerson(user.getUserCode());
			log.setLogStatus(MnisQmConstants.TASK_STATUS_06);
			break;

		default:
			throw new CommRuntimeException("非法提交");
		}
		// 保存task
		service.saveSysTask(task);
		log.setTaskCode(task.getTaskCode());
		log.setStatus(MnisQmConstants.STATUS_YX);
		log.setCreatePerson(user.getUserCode());
		service.saveSysTaskLog(log);
		outVo.setCode(Constants.Success);
		return outVo;
	}

	/**
	 * 通过上报编号查询上报信息
	 * 
	 * @param reportCode
	 * @return
	 */
	@RequestMapping(value = "getEventReport")
	public @ResponseBody BaseDataVo getEventReport(String reportCode) {
		BaseDataVo vo = new BaseDataVo();
		// 数据查询
		EventReport report = new EventReport();
		report.setReportCode(reportCode);
		SysUser user = WebContextUtils.getSessionUserInfo();
//		SysUser user=sysService.checkLogin("283", "123");
		List<EventReport> reports = service.getEventReport(report,user.getUserCode());
		if (null != reports && !reports.isEmpty()) {
			vo.setData(reports.get(0));
		}
		// 数据返回
		vo.setCode(Constants.Success);
		return vo;
	}

	/**
	 * 删除不良事件上报信息
	 * 
	 * @param report
	 * @return
	 */
	@RequestMapping(value = "deleteEventReport")
	public @ResponseBody BaseDataVo deleteEventReport(
			@RequestBody EventReport report) {
		BaseDataVo outVo = new BaseDataVo();
		SysUser user = WebContextUtils.getSessionUserInfo();
		report.setStatus(MnisQmConstants.STATUS_WX);// 无效状态
		report.setUpdatePerson(user.getUserName());// 更新人
		service.deleteEventReport(report);
		return outVo;
	}

	/**
	 * 保存不良事件调查信息
	 * 
	 * @param detail
	 * @return
	 */
	@RequestMapping(value = "saveReportDetail")
	public @ResponseBody BaseDataVo saveReportDetail(
			@RequestBody List<EventReportDetail> details) {

		BaseDataVo outVo = new BaseDataVo();
		SysUser user = WebContextUtils.getSessionUserInfo();
		if (details != null) {
			for (EventReportDetail dtl : details) {
				dtl.setCreatePerson(user.getUserCode());
				dtl.setUpdatePerson(user.getUserCode());
				dtl.setStatus(MnisQmConstants.STATUS_YX);
			}
		}
		service.saveReportDetail(details);

		// 返回状态
		outVo.setCode(Constants.Success);
		return outVo;
	}

	/**
	 * 查询不良事件调查信息
	 * 
	 * @param detail
	 * @return
	 */
	@RequestMapping(value = "getReportDetail")
	public @ResponseBody BaseMapVo getReportDetail(String reportCode) {
		BaseMapVo outVo = new BaseMapVo();
		outVo.addData("lst", service.getReportDetails(reportCode));

		// 返回状态
		outVo.setCode(Constants.Success);
		return outVo;
	}

	/**
	 * 保存不良事件措施
	 * 
	 * @param detail
	 * @return
	 */
	@RequestMapping(value = "saveEventMeasures")
	public @ResponseBody BaseDataVo saveEventMeasures(
			@RequestBody EventMeasures measures) {
		BaseDataVo outVo = new BaseDataVo();
		SysUser user = WebContextUtils.getSessionUserInfo();
		measures.setStatus(MnisQmConstants.STATUS_YX);
		measures.setMeaCode(CodeUtils.getSysInvokeId());
		measures.setCreatePerson(user.getUserName());
		measures.setUpdatePerson(user.getUserName());
		service.saveEventMeasures(measures);
		// 更新状态
		outVo.setCode(Constants.Success);
		return outVo;
	}

	/**
	 * 查询不良事件措施
	 * 
	 * @param detail
	 * @return
	 */
	@RequestMapping(value = "getEventMeasures")
	public @ResponseBody BaseMapVo getEventMeasures(String reportCode) {
		BaseMapVo outVo = new BaseMapVo();

		EventMeasures measures = new EventMeasures();
		measures.setReportCode(reportCode);
		outVo.addData("lst", service.getEventMeasures(measures));
		// 数据返回
		outVo.setCode(Constants.Success);
		return outVo;
	}

	/**
	 * 删除不良事件整改要求
	 * 
	 * @param detail
	 * @return
	 */
	@RequestMapping(value = "deleteEventMeasures")
	public @ResponseBody BaseDataVo deleteEventMeasures(
			@RequestBody EventMeasures measures) {
		BaseDataVo outVo = new BaseDataVo();
		SysUser user = WebContextUtils.getSessionUserInfo();
		measures.setUpdatePerson(user.getUserName());// 更改人
		service.deleteEventMeasures(measures);

		// 数据返回
		outVo.setCode(Constants.Success);
		return outVo;
	}

	/**
	 * 保存不良事件要求
	 * 
	 * @param detail
	 * @return
	 */
	@RequestMapping(value = "saveEventRequirement")
	public @ResponseBody BaseDataVo saveEventRequirement(
			@RequestBody EventRequirement required) {
		BaseDataVo outVo = new BaseDataVo();
		SysUser user = WebContextUtils.getSessionUserInfo();
		required.setCreatePerson(user.getUserName());
		required.setUpdatePerson(user.getUserName());
		required.setStatus(MnisQmConstants.STATUS_YX);// 有效状态
		required.setReqCode(CodeUtils.getSysInvokeId());
		service.saveEventRequirement(required);
		outVo.setCode(Constants.Success);
		return outVo;
	}

	/**
	 * 不良事件要求查询
	 * 
	 * @param detail
	 * @return
	 */
	@RequestMapping(value = "getEventRequirement")
	public @ResponseBody BaseMapVo getEventRequirement(String reportCode) {
		BaseMapVo outVo = new BaseMapVo();
		EventRequirement req = new EventRequirement();
		req.setReportCode(reportCode);
		outVo.addData("lst", service.getEventRequirement(req));
		// 数据返回
		outVo.setCode(Constants.Success);
		return outVo;
	}

	/**
	 * 删除不良事件要求
	 * 
	 * @param detail
	 * @return
	 */
	@RequestMapping(value = "deleteEventRequirement")
	public @ResponseBody BaseDataVo deleteEventRequirement(
			@RequestBody EventRequirement required) {
		BaseDataVo outVo = new BaseDataVo();
		SysUser user = WebContextUtils.getSessionUserInfo();
		required.setUpdatePerson(user.getUserName());
		service.deleteEventRequirement(required);
		// 数据返回
		outVo.setCode(Constants.Success);
		return outVo;
	}
	
	/**
	 * 保存不良事件评估
	 * @param assessment
	 * @return
	 */
	@RequestMapping(value = "saveEventAssessment")
	public @ResponseBody  BaseDataVo saveEventAssessment(@RequestBody EventAssessment assessment){
		BaseDataVo outVo = new BaseDataVo();
		SysUser user = WebContextUtils.getSessionUserInfo();
		assessment.setUserCode(user.getUserCode());
		assessment.setStatus(MnisQmConstants.STATUS_YX);
		assessment.setCreatePerson(user.getUserCode());
		assessment.setUpdatePerson(user.getUserCode());
		service.saveEventAssessment(assessment);
		outVo.setCode(Constants.Success);
		return outVo;
	}
	
	/**
	 * 不良事件措施评估查询
	 * 
	 * @param detail
	 * @return
	 */
	@RequestMapping(value = "getEventAssessmentList")
	public @ResponseBody BaseMapVo getEventAssessmentList(String meaCode) {
		BaseMapVo outVo = new BaseMapVo();
		EventAssessment assessment=new EventAssessment();
		assessment.setMeaCode(meaCode);
		outVo.addData("lst", service.getEventAssessmentList(assessment));
		// 数据返回
		outVo.setCode(Constants.Success);
		return outVo;
	}
	
	/**
	 * 删除不良事件评估
	 * 
	 * @param detail
	 * @return
	 */
	@RequestMapping(value = "deleteEventAssessment")
	public @ResponseBody BaseDataVo deleteEventAssessment(
			@RequestBody EventAssessment assessment) {
		BaseDataVo outVo = new BaseDataVo();
		SysUser user = WebContextUtils.getSessionUserInfo();
		assessment.setUpdatePerson(user.getUserCode());
		service.deleteEventAssessment(assessment);
		outVo.setCode(Constants.Success);
		return outVo;
	}

	/**
	 * 获取风险管理主界面信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/queryRiskManage")
	public @ResponseBody BaseDataVo queryRiskManage() {
		BaseDataVo outVo = new BaseDataVo();
		// 数据查询
		outVo.setData(service.selectAllRiskManage());
		// 数据返回
		outVo.setCode(Constants.Success);
		return outVo;
	}

	/**
	 * 获取不良事件类别分析信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/queryEventAnalysis")
	public @ResponseBody BaseDataVo queryEventAnalysis() {
		BaseDataVo outVo = new BaseDataVo();
		// 数据查询
		outVo.setData(service.selectAllEventAnalysis());
		// 数据返回
		outVo.setCode(Constants.Success);
		return outVo;
	}

	/**
	 * 获取原因分析及干预措施建议信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/queryAnalysisMeasures")
	public @ResponseBody BaseDataVo queryAnalysisMeasures() {
		BaseDataVo outVo = new BaseDataVo();
		// 数据查询
		AnalysisInfo analysisInfo = new AnalysisInfo();
		analysisInfo.setTitle("1、护理人员资质");
		analysisInfo.setAnalysisMeasuresList(service
				.selectAllAnalysisMeasures());
		List<AnalysisInfo> list = new ArrayList<AnalysisInfo>();
		list.add(analysisInfo);
		outVo.setData(list);
		// 数据返回
		outVo.setCode(Constants.Success);
		return outVo;
	}

	/**
	 * 获取临床护理信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/queryNursingInfo")
	public @ResponseBody BaseDataVo queryNursingInfo() {
		BaseDataVo outVo = new BaseDataVo();
		// 数据查询
		outVo.setData(service.selectAllNursingInfo());
		// 数据返回
		outVo.setCode(Constants.Success);
		return outVo;
	}

	/**
	 * 获取临床护理措施信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/queryNursingMeasures")
	public @ResponseBody BaseDataVo queryNursingMeasures() {
		BaseDataVo outVo = new BaseDataVo();
		// 数据查询
		outVo.setData(service.selectAllNursingMeasures());
		// 数据返回
		outVo.setCode(Constants.Success);
		return outVo;
	}

	/**
	 * 获取风险趋势信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/queryRiskTrend")
	public @ResponseBody BaseDataVo queryRiskTrend() {
		BaseDataVo outVo = new BaseDataVo();
		// 数据查询
		RiskTrendDate riskTrendDate = new RiskTrendDate();
		riskTrendDate.setDeptName("手足口");
		riskTrendDate.setRiskTrendList(service.selectAllRiskTrend());
		List<RiskTrendDate> riskTrendDateList = new ArrayList<RiskTrendDate>();
		riskTrendDateList.add(riskTrendDate);
		outVo.setData(riskTrendDateList);
		// 数据返回
		outVo.setCode(Constants.Success);
		return outVo;
	}
}
