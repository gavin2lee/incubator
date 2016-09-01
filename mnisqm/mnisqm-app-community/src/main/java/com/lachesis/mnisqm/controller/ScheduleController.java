package com.lachesis.mnisqm.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lachesis.mnisqm.BaseDataVo;
import com.lachesis.mnisqm.BaseMapVo;
import com.lachesis.mnisqm.common.WebContextUtils;
import com.lachesis.mnisqm.constants.Constants;
import com.lachesis.mnisqm.constants.MnisQmConstants;
import com.lachesis.mnisqm.core.CommRuntimeException;
import com.lachesis.mnisqm.core.utils.StringUtils;
import com.lachesis.mnisqm.module.schedule.domain.Schedule;
import com.lachesis.mnisqm.module.schedule.domain.ScheduleChangeClass;
import com.lachesis.mnisqm.module.schedule.domain.ScheduleDeptClass;
import com.lachesis.mnisqm.module.schedule.domain.ScheduleLeave;
import com.lachesis.mnisqm.module.schedule.domain.ScheduleRule;
import com.lachesis.mnisqm.module.schedule.service.IScheduleService;
import com.lachesis.mnisqm.module.system.domain.SysUser;
import com.lachesis.mnisqm.module.user.domain.ComBedGroup;
import com.lachesis.mnisqm.module.user.domain.ComDeptBed;
import com.lachesis.mnisqm.module.user.domain.ComDeptNurse;

@RequestMapping("/schedule")
@Controller
public class ScheduleController {
	Logger log = LoggerFactory.getLogger(ScheduleController.class);
	
	@Autowired
	private IScheduleService service;
	
	/**
	 * 保存排班规则
	 */
	@RequestMapping("/saveRule")
	public @ResponseBody BaseMapVo saveRule(@RequestBody ScheduleRule rule) {
		
		//数据校验
		if(rule == null){
			throw new CommRuntimeException("数据为空！");
		}
		if(StringUtils.isEmpty(rule.getRuleName())){
			throw new CommRuntimeException("规则名称不允许为空！");
		}
		
		if(StringUtils.isEmpty(rule.getDeptCode())){
			throw new CommRuntimeException("科室编号不允许为空！");
		}
		if(StringUtils.isEmpty(rule.getClassCode())){
			throw new CommRuntimeException("班次不允许为空！");
		}
		if(StringUtils.isEmpty(rule.getUserCount())){
			throw new CommRuntimeException("班次最少次数不允许为空！");
		}
		BaseMapVo outVo = new BaseMapVo();
		//获取session中的用户信息
		SysUser user = WebContextUtils.getSessionUserInfo();
		
		rule.setCreatePerson(user.getUserCode());//创建人
		rule.setUpdatePerson(user.getUserCode());//更新人
		rule.setStatus(MnisQmConstants.STATUS_YX);//状态
		service.saveRule(rule);
		// 数据返回
		outVo.setCode(Constants.Success);
		return outVo;
	}
	
	/**
	 * 保存排班规则
	 */
	@RequestMapping("/deleteRule")
	public @ResponseBody BaseMapVo deleteRule(@RequestBody ScheduleRule rule) {
		//数据校验
		if(rule == null){
			throw new CommRuntimeException("数据为空！");
		}
		if(rule.getSeqId() == null){
			throw new CommRuntimeException("seqId不允许为空！");
		}
		
		BaseMapVo outVo = new BaseMapVo();
		//获取session数据
		SysUser user = WebContextUtils.getSessionUserInfo();
		
		rule.setUpdatePerson(user.getUserCode());//更新人
		rule.setStatus(MnisQmConstants.STATUS_WX);//状态为删除
		service.deleteRule(rule);
		// 数据返回
		outVo.setCode(Constants.Success);
		return outVo;
	}
	
	/**
	 * 获取排班规则列表
	 * @param deptCode
	 */
	@RequestMapping("/getRuleList")
	public @ResponseBody
	BaseMapVo getRuleList(String deptCode) {
		// 数据校验
		if (StringUtils.isEmpty(deptCode)) {
			throw new CommRuntimeException("科室编号不允许为空！");
		}
		BaseMapVo outVo = new BaseMapVo();

		outVo.addData("lst", service.getRuleList(deptCode));
		// 数据返回
		outVo.setCode(Constants.Success);
		return outVo;
	}
	
	/**
	 * 获取床位分组列表
	 */
	@RequestMapping("/getGroupList")
	public @ResponseBody BaseMapVo getGroupList(String deptCode){
		//数据校验
		if(StringUtils.isEmpty(deptCode)){
			throw new CommRuntimeException("科室编号不允许为空！");
		}
		BaseMapVo outVo = new BaseMapVo();
		
		outVo.addData("lst", service.getGroupList(deptCode));
		//数据返回
		outVo.setCode(Constants.Success);
		return outVo;
	}
	
	/**
	 * 保存床位分组信息
	 */
	@RequestMapping("/saveGroup")
	public @ResponseBody BaseDataVo saveGroup(@RequestBody ComBedGroup group){
		BaseDataVo outVo = new BaseDataVo();
		//数据校验
		if(group == null){
			throw new CommRuntimeException("数据为空！");
		}
		if(StringUtils.isEmpty(group.getDeptCode())){
			throw new CommRuntimeException("科室编号不允许为空！");
		}
		if(StringUtils.isEmpty(group.getGroupName())){
			throw new CommRuntimeException("分组名称不允许为空！");
		}
		SysUser user = WebContextUtils.getSessionUserInfo();
		
		group.setStatus(MnisQmConstants.STATUS_YX);//有效状态
		group.setCreatePerson(user.getUserCode());//创建人
		group.setUpdatePerson(user.getUserCode());//更新人
		//保存分组
		service.saveGroup(group);
		//数据返回
		outVo.setCode(Constants.Success);
		return outVo;
	}
	
	/**
	 * 删除科室床位分组
	 * @param group
	 * @return
	 */
	@RequestMapping("/deleteGroup")
	public @ResponseBody BaseDataVo deleteGroup(@RequestBody ComBedGroup group){
		BaseDataVo outVo = new BaseDataVo();
		if(null == group || group.getSeqId() ==null){
			throw new CommRuntimeException("seqId为空！");
		}
		//获取session中的用户信息
		SysUser user = WebContextUtils.getSessionUserInfo();
		group.setUpdatePerson(user.getUserCode());
		//保存分组
		service.deleteGroup(group);
		//数据返回
		outVo.setCode(Constants.Success);
		return outVo;
	}

	
	/**
	 * 保存床位信息
	 */
	@RequestMapping("/saveBed")
	public @ResponseBody BaseDataVo saveBed(@RequestBody ComDeptBed bed){
		BaseDataVo outVo = new BaseDataVo();
		
		//保存分组
		service.saveBed(bed);
		//数据返回
		outVo.setCode(Constants.Success);
		return outVo;
	}
	
	/**
	 * 获取床位信息列表
	 */
	@RequestMapping("/getBedList")
	public @ResponseBody BaseMapVo getBedList(String deptCode){
		BaseMapVo outVo = new BaseMapVo();
		
		outVo.addData("lst", service.getBedList(deptCode));
		//数据返回
		outVo.setCode(Constants.Success);
		return outVo;
	}
	
	/**
	 * 保存班次信息
	 */
	@RequestMapping("/saveClass")
	public @ResponseBody BaseDataVo saveClass(@RequestBody ScheduleDeptClass deptClass){
		BaseDataVo outVo = new BaseDataVo();
		//数据校验
		if(deptClass == null){
			throw new CommRuntimeException("数据为空！");
		}
		if(StringUtils.isEmpty(deptClass.getDeptCode())){
			throw new CommRuntimeException("科室编号不允许为空!");
		}
		if(StringUtils.isEmpty(deptClass.getClassName())){
			throw new CommRuntimeException("班次名称不允许为空！");
		}
		if(StringUtils.isEmpty(deptClass.getStartTime())){
			throw new CommRuntimeException("起始时间不允许为空！");
		}
		if(deptClass.getStartTime().length()!=5){
			throw new CommRuntimeException("起始时间格式错误！");
		}
		if (StringUtils.isEmpty(deptClass.getEndTime())) {
			throw new CommRuntimeException("结束时间不允许为空！");
		}
		if(deptClass.getEndTime().length()!=5){
			throw new CommRuntimeException("结束时间格式错误！");
		}
		//获取操作人
		SysUser user = WebContextUtils.getSessionUserInfo();
		deptClass.setCreatePerson(user.getUserCode());//创建人
		deptClass.setUpdatePerson(user.getUserCode());//更新人
		deptClass.setStatus(MnisQmConstants.STATUS_YX);//有效状态
		service.saveClass(deptClass);
		//数据返回
		outVo.setCode(Constants.Success);
		return outVo;
	}
	
	/**
	 * 获取排班信息
	 */
	@RequestMapping("/deleteClass")
	public @ResponseBody BaseMapVo deleteClass(@RequestBody ScheduleDeptClass deptClass){
		//数据校验
		if(deptClass == null){
			throw new CommRuntimeException("数据为空！");
		}
		if(deptClass.getSeqId() == null){
			throw new CommRuntimeException("SEQID不允许为空！");
		}
		BaseMapVo outVo = new BaseMapVo();
		//获取session的用户
		SysUser user = WebContextUtils.getSessionUserInfo();
		deptClass.setUpdatePerson(user.getUserCode());
		deptClass.setStatus(MnisQmConstants.STATUS_WX);
		service.deleteClass(deptClass);
		//数据返回
		outVo.setCode(Constants.Success);
		return outVo;
	}
	
	/**
	 * 获取班次列表
	 */
	@RequestMapping("/getClassList")
	public @ResponseBody BaseMapVo getClassList(String deptCode){
		//数据校验
		if(StringUtils.isEmpty(deptCode)){
			throw new CommRuntimeException("科室编号为空！");
		}
		
		BaseMapVo outVo = new BaseMapVo();
		
		outVo.addData("lst", service.getClassList(deptCode));
		//数据返回
		outVo.setCode(Constants.Success);
		return outVo;
	}
	
	/**
	 * 保存请假信息
	 */
	@RequestMapping("/saveScheduleLeave")
	public @ResponseBody BaseDataVo saveScheduleLeave(@RequestBody ScheduleLeave leave){
		BaseDataVo outVo = new BaseDataVo();
		SysUser user = WebContextUtils.getSessionUserInfo();
		leave.setUpdatePerson(user.getUserCode());
		leave.setCreatePerson(user.getUserCode());
		leave.setUserCode(user.getUserCode());
		leave.setApproveStatus(MnisQmConstants.APPRV_N);//未审批
		//保存请假信息
		service.saveLeave(leave);
		//返回信息
		outVo.setCode(Constants.Success);
		return outVo;
	}
	
	/**
	 * 保存请假信息
	 */
	@RequestMapping("/deleteScheduleLeave")
	public @ResponseBody BaseDataVo deleteScheduleLeave(@RequestBody ScheduleLeave leave){
		BaseDataVo outVo = new BaseDataVo();
		leave.setApproveStatus("09");
		//保存请假信息
		service.deleteLeave(leave);
		//返回信息
		outVo.setCode(Constants.Success);
		return outVo;
	}
	
	/**
	 * 审批请假
	 */
	@RequestMapping("/apprvScheduleLeave")
	public @ResponseBody BaseDataVo apprvScheduleLeave(@RequestBody ScheduleLeave leave){
		BaseDataVo outVo = new BaseDataVo();
		SysUser user = WebContextUtils.getSessionUserInfo();
		leave.setUpdatePerson(user.getUserCode());
		leave.setStatus(MnisQmConstants.STATUS_YX);
		service.updateScheduleApproveStatus(leave);
		//返回信息
		outVo.setCode(Constants.Success);
		return outVo;
	}
	
	/**
	 * 获取请假列表
	 */
	@RequestMapping("/getLeaveList")
	public @ResponseBody BaseMapVo getLeaveList(String deptCode,String userCode){
		if(StringUtils.isEmpty(deptCode)){
			throw new CommRuntimeException("科室不允许为空!");
		}
		SysUser user = WebContextUtils.getSessionUserInfo();
		String apprvUserCode = user.getUserCode();
		BaseMapVo outVo = new BaseMapVo();
		outVo.addData("lst", service.getLeaveList(deptCode,apprvUserCode,userCode));
		//数据返回
		outVo.setCode(Constants.Success);
		return outVo;
	}
	
	/**
	 * 通过请假做统计
	 * @param deptCode
	 * @param month
	 * @return
	 */
	@RequestMapping("/getCountByLeave")
	public @ResponseBody BaseMapVo getCountLeaves(String deptCode,String month){
		BaseMapVo outVo = new BaseMapVo();
		outVo.addData("lst", service.CountScheduleByLeave(deptCode, month));
		//数据返回
		return outVo;
	}
	
	/**
	 * 通过日期类型做统计
	 * @param deptCode
	 * @param mounth
	 * @return
	 */
	@RequestMapping("/getCountByDays")
	public @ResponseBody BaseMapVo getCountByDays(String deptCode,String mounth,String startDate,String endDate){
		BaseMapVo outVo = new BaseMapVo();
		outVo.addData("lst", service.CountScheduleByDays(deptCode, mounth));
		
		//数据返回
		return outVo;
	}
	
	/**
	 * 通过班次类型做统计
	 * @param deptCode
	 * @param mounth
	 * @return
	 */
	@RequestMapping("/getCountByClass")
	public @ResponseBody BaseMapVo getCountByClass(String deptCode,String mounth,String startDate,String endDate){
		BaseMapVo outVo = new BaseMapVo();
		outVo.addData("lst", service.CountScheduleByClass(deptCode, mounth));
		
		//数据返回
		return outVo;
	}
	
	
	/**
	 * 获取排班信息
	 */
	@RequestMapping("/getSchedule")
	public @ResponseBody BaseDataVo getSchedule(String deptCode,String weeks, String year){
		//数据校验
		if(StringUtils.isEmpty(deptCode)){
			throw new CommRuntimeException("科室不允许为空！");
		}
		
		BaseDataVo outVo = new BaseDataVo();
		//获取数据的排班信息
		Schedule schedule = service.getSchedule(deptCode, weeks, year);
		//如果数据库不存在排班，那么新增排班
		if(schedule == null){
			schedule = service.newSchedule(deptCode,weeks);
		}
		outVo.setData(schedule);
		//数据返回
		outVo.setCode(Constants.Success);
		return outVo;
	}
	
	
	/**
	 * 获取排班信息
	 */
	@RequestMapping("/copySchedule")
	public @ResponseBody BaseDataVo copySchedule(String deptCode,String srcWeek,String outWeek, String year){
		//数据校验
		if(StringUtils.isEmpty(deptCode)){
			throw new CommRuntimeException("科室不允许为空！");
		}
		
		BaseDataVo outVo = new BaseDataVo();
		
		outVo.setData(service.copySchedule(deptCode, srcWeek, outWeek, year));
		//数据返回
		outVo.setCode(Constants.Success);
		return outVo;
	}
	
	/**
	 * 科室排班
	 */
	@RequestMapping("/newSchedule")
	public @ResponseBody BaseDataVo newSchedule(String deptCode,String week) {
		if(StringUtils.isEmpty(deptCode)){
			throw new CommRuntimeException("科室编号不允许为空！");
		}
		BaseDataVo outVo = new BaseDataVo();

		outVo.setData(service.newSchedule(deptCode,week));
		// 数据返回
		outVo.setCode(Constants.Success);
		return outVo;
	}
	
	/**
	 * 保存排班
	 */
	@RequestMapping("/saveSchedule")
	public @ResponseBody BaseDataVo saveSchedule(@RequestBody Schedule schedule){
		//数据校验
		if(schedule == null){
			throw new CommRuntimeException("数据为空！");
		}
		BaseDataVo vo = new BaseDataVo();
		//获取操作人
		SysUser user = WebContextUtils.getSessionUserInfo();
		schedule.setUserCode(user.getUserCode());
		service.saveSchedule(schedule);
		//数据返回
		vo.setCode(Constants.Success);
		return vo ;
	}
	
	/**
	 * 查询调班信息列表
	 * @param deptCode
	 * @return
	 */
	@RequestMapping("/getChangeClass")
	public @ResponseBody BaseMapVo getChangeClass(String deptCode) {
		BaseMapVo outVo = new BaseMapVo();

		SysUser user = WebContextUtils.getSessionUserInfo();
		String userCode = user.getUserCode();
		outVo.addData("lst", service.getChangeClass(deptCode,userCode));
		// 数据返回
		outVo.setCode(Constants.Success);
		return outVo;
	}
	
	/**
	 * 保存调班信息
	 * @param deptCode
	 * @return
	 */
	@RequestMapping("/saveChangeClass")
	public @ResponseBody BaseMapVo saveChangeClass(@RequestBody ScheduleChangeClass change) {
		BaseMapVo outVo = new BaseMapVo();

		service.saveChangeClass(change);
		// 数据返回
		outVo.setCode(Constants.Success);
		return outVo;
	}
	
	/**
	 * 审批调班信息
	 * @param deptCode
	 * @return
	 */
	@RequestMapping("/apprvChangeClass")
	public @ResponseBody BaseMapVo apprvChangeClass(@RequestBody ScheduleChangeClass change) {
		BaseMapVo outVo = new BaseMapVo();

		service.saveChangeClass(change);
		// 数据返回
		outVo.setCode(Constants.Success);
		return outVo;
	}
	
	/**
	 * 删除调班信息
	 * @param deptCode
	 * @return
	 */
	@RequestMapping("/deleteChangeClass")
	public @ResponseBody BaseMapVo deleteChangeClass(@RequestBody ScheduleChangeClass change) {
		BaseMapVo outVo = new BaseMapVo();

		service.deleteByKey(change);
		// 数据返回
		outVo.setCode(Constants.Success);
		return outVo;
	}
	
	@RequestMapping("/getUserNurses")
	public @ResponseBody BaseMapVo getUserNurses(String deptCode,String userCode){
		//数据校验
		if(StringUtils.isEmpty(deptCode)){
			throw new CommRuntimeException("科室编号不允许为空!");
		}
		BaseMapVo outVo = new BaseMapVo();

		//数据查询
		outVo.addData("lst", service.getUserNurse(deptCode, userCode));
		// 数据返回
		outVo.setCode(Constants.Success);
		return outVo;
	}
	
	@RequestMapping("/batchSaveUserNurse")
	public  @ResponseBody BaseMapVo batchSaveUserNurse(@RequestBody List<ComDeptNurse> nurses){
		if(nurses==null || nurses.isEmpty()){
			throw new CommRuntimeException("数据为空!");
		}
		BaseMapVo outVo = new BaseMapVo();
		//数据处理
		SysUser user = WebContextUtils.getSessionUserInfo();
		for(ComDeptNurse nurse : nurses){
			if(StringUtils.isEmpty(nurse.getDeptCode())){
				throw new CommRuntimeException("科室编号不允许为空！");
			}
			if(StringUtils.isEmpty(nurse.getUserCode())){
				throw new CommRuntimeException("员工编号不允许为空！");
			}
			if(StringUtils.isEmpty(nurse.getUserName())){
				throw new CommRuntimeException("员工姓名不允许为空！");
			}
			nurse.setCreatePerson(user.getUserCode());//创建人
			nurse.setUpdatePerson(user.getUpdatePerson());//更新人
			nurse.setStatus(MnisQmConstants.STATUS_YX);//有效状态
		}
		//保存
		service.saveUserNurse(nurses);
		//是否缓存
		nurses = null;
		//数据返回
		outVo.setCode(Constants.Success);
		return outVo;
	}
}