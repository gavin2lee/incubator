package com.lachesis.mnisqm.module.schedule.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lachesis.mnisqm.constants.MnisQmConstants;
import com.lachesis.mnisqm.constants.MnisQmConstants.SysDicConstants;
import com.lachesis.mnisqm.core.CommRuntimeException;
import com.lachesis.mnisqm.core.utils.CodeUtils;
import com.lachesis.mnisqm.core.utils.DateUtils;
import com.lachesis.mnisqm.core.utils.StringUtils;
import com.lachesis.mnisqm.module.schedule.dao.ScheduleChangeClassMapper;
import com.lachesis.mnisqm.module.schedule.dao.ScheduleDeptClassMapper;
import com.lachesis.mnisqm.module.schedule.dao.ScheduleLeaveMapper;
import com.lachesis.mnisqm.module.schedule.dao.ScheduleMapper;
import com.lachesis.mnisqm.module.schedule.dao.ScheduleRecordDetailMapper;
import com.lachesis.mnisqm.module.schedule.dao.ScheduleRecordMapper;
import com.lachesis.mnisqm.module.schedule.dao.ScheduleRuleMapper;
import com.lachesis.mnisqm.module.schedule.domain.Schedule;
import com.lachesis.mnisqm.module.schedule.domain.ScheduleChangeClass;
import com.lachesis.mnisqm.module.schedule.domain.ScheduleCount;
import com.lachesis.mnisqm.module.schedule.domain.ScheduleDeptClass;
import com.lachesis.mnisqm.module.schedule.domain.ScheduleDetail;
import com.lachesis.mnisqm.module.schedule.domain.ScheduleLeave;
import com.lachesis.mnisqm.module.schedule.domain.ScheduleRecord;
import com.lachesis.mnisqm.module.schedule.domain.ScheduleRecordDetail;
import com.lachesis.mnisqm.module.schedule.domain.ScheduleRule;
import com.lachesis.mnisqm.module.schedule.service.IScheduleService;
import com.lachesis.mnisqm.module.schedule.util.ScheduleUtil;
import com.lachesis.mnisqm.module.system.domain.SysDate;
import com.lachesis.mnisqm.module.system.service.ICacheService;
import com.lachesis.mnisqm.module.user.dao.ComBedGroupMapper;
import com.lachesis.mnisqm.module.user.dao.ComDeptBedMapper;
import com.lachesis.mnisqm.module.user.dao.ComDeptNurseMapper;
import com.lachesis.mnisqm.module.user.dao.ComUserMapper;
import com.lachesis.mnisqm.module.user.domain.ComBedGroup;
import com.lachesis.mnisqm.module.user.domain.ComDept;
import com.lachesis.mnisqm.module.user.domain.ComDeptBed;
import com.lachesis.mnisqm.module.user.domain.ComDeptNurse;

@Service
public class ScheduleServiceImpl implements IScheduleService{
	
	@Autowired
	private ScheduleLeaveMapper scheduleLeave;
	
	@Autowired
	private ComUserMapper comUserMapper;
	
	@Autowired
	private ComBedGroupMapper groupMapper;
	
	@Autowired
	private ScheduleDeptClassMapper classMapper;
	
	@Autowired
	private ComDeptBedMapper bedMapper;
	
	@Autowired
	private ScheduleRuleMapper ruleMapper;
	
	@Autowired
	private ScheduleChangeClassMapper changeClassMapper;
	
	@Autowired
	private ScheduleRecordDetailMapper recordDetailMapper;
	
	@Autowired
	private ScheduleRecordMapper recordMapper;
	
	@Autowired
	private ICacheService cacheService;
	
	@Autowired
	private ComDeptNurseMapper nurserMapper;
	
	@Autowired
	private ScheduleMapper scheduleMapper;
	
	@Override
	public List<ScheduleLeave> getLeaveList(String deptCode,String apprvUserCode,String userCode){
		ScheduleLeave leave = new ScheduleLeave();
		leave.setDeptCode(deptCode);
		leave.setUserCode(userCode);
		List<ScheduleLeave> leaves = scheduleLeave.selectLeave(leave);
		ComDept dept = scheduleLeave.selectDeptHeader(leave);
		String deptHeader ="";
		if(dept!=null){
			deptHeader= dept.getDeptNurseHeader();
		}
		if(leaves != null){
			for(ScheduleLeave l : leaves){
				String status = apprvPermission(l.getApproveStatus(), l.getUserCode(), apprvUserCode, deptHeader);
				l.setPermission(status);//审核状态
			}
		}
		return leaves;
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void saveLeave(ScheduleLeave leave){
		leave.setStatus(MnisQmConstants.STATUS_YX);//状态
		
		if(leave.getSeqId() == null){
			scheduleLeave.insert(leave);
		}else{
			scheduleLeave.updateByKey(leave);
		}
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void deleteLeave(ScheduleLeave leave){
			scheduleLeave.deleteLeaveByKey(leave.getSeqId());
	}
	
	@Override
	public List<ComBedGroup> getGroupList(String deptCode){
		ComBedGroup group = new ComBedGroup();
		group.setDeptCode(deptCode);
		return groupMapper.selectByCode(group);
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void saveGroup(ComBedGroup group){
		if(group.getSeqId() == null){
			group.setGroupCode(CodeUtils.getSysInvokeId());
			groupMapper.insert(group);
		}else{
			groupMapper.updateByPrimaryKey(group);
		}
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void deleteGroup(ComBedGroup group){
		group.setStatus(MnisQmConstants.STATUS_WX);
		groupMapper.deleteByPrimaryKey(group);
	}
	
	@Override
	public List<ScheduleDeptClass> getClassList(String deptCode){
		ScheduleDeptClass classes = new ScheduleDeptClass();
		classes.setDeptCode(deptCode);
		return classMapper.selectAllByCode(classes);
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void saveClass(ScheduleDeptClass deptClass){
		if(deptClass.getSeqId() == null){
			deptClass.setClassCode(CodeUtils.getSysInvokeId());//获取班次编号
			classMapper.insert(deptClass);
		}else{
			classMapper.updateByPrimaryKey(deptClass);
		}
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void deleteClass(ScheduleDeptClass deptClass){
		classMapper.deleteByPrimaryKey(deptClass);
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void saveBed(ComDeptBed bed){
		bedMapper.insert(bed);
	}
	
	@Override
	public List<ComDeptBed> getBedList(String deptCode){
		return bedMapper.selectAll();
	}
	
	@Override
	public List<ScheduleRule> getRuleList(String deptCode){
		ScheduleRule rule = new ScheduleRule();
		rule.setDeptCode(deptCode);
		List<ScheduleRule> rules =  ruleMapper.selectRuleByDept(rule);
		if(rules != null){
			for(ScheduleRule r : rules){
				r.setDateTypeName(cacheService.getSysDicValue(SysDicConstants.dateType, r.getDateType()));//日期类型
			}
		}
		return rules;
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void saveRule(ScheduleRule rule){
		//如果有seqId为更新，没有未插入
		if(rule.getSeqId() == null){
			rule.setRuleCode(CodeUtils.getSysInvokeId());
			ruleMapper.insert(rule);
		}else {
			ruleMapper.updateRuleByKey(rule);
		}
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void deleteRule(ScheduleRule rule){
		ruleMapper.deleteRuleByKey(rule);
	}
	
	@Override
	public List<ScheduleChangeClass> getChangeClass(String deptCode,String loginUser){
		List<ScheduleChangeClass> changeLst = changeClassMapper.selectAll(deptCode);
		ScheduleLeave leave = new ScheduleLeave();
		leave.setDeptCode(deptCode);
		ComDept dept = scheduleLeave.selectDeptHeader(leave);
		String deptHeader ="";
		if(dept!=null){
			deptHeader= dept.getDeptNurseHeader();
		}
		if(changeLst != null){
			for(ScheduleChangeClass change : changeLst){
				String status = apprvPermission(change.getStatus(), change.getApplyUserCode(), loginUser, deptHeader);
				change.setPermission(status);//审核状态
			}
		}
		return changeClassMapper.selectAll(deptCode);
	}
	
	/**
	 * 获取审批权限
	 * @return
	 */
	private String apprvPermission(String apprvStatus,String createUser,String loginUser,String headerUser){
		String rsStatus = "";
		if(MnisQmConstants.APPRV_01.equals(apprvStatus)){
			/*
			 * 新建
			 * 新建状态：如果登录人是创建人，那么允许修改和提交
			 */
			if(loginUser.equals(createUser)){
				rsStatus = MnisQmConstants.APPRV_01;
			}
		}else if(MnisQmConstants.APPRV_02.equals(apprvStatus)){
			/*
			 * 已提交
			 * 已提交状态：如果登录人事科室护士长，那么允许审批
			 */
			if(loginUser.equals(createUser)){
				Map<String,Object> parm = new HashMap<String,Object>();
				parm.put("createUser", createUser);
				parm.put("loginUser", loginUser);
				if(comUserMapper.selectDeptByUser(parm)>0){
					rsStatus = MnisQmConstants.APPRV_02;
				}
			}else if(loginUser.equals(headerUser)){
				rsStatus = MnisQmConstants.APPRV_02;
			}
		}else if (MnisQmConstants.APPRV_02.equals(apprvStatus)){
			/*
			 * 已审批
			 * 已审批状态：如果登录人是护士长，那么允许撤销
			 */
			if(loginUser.equals(createUser)){
				Map<String,Object> parm = new HashMap<String,Object>();
				parm.put("createUser", createUser);
				parm.put("loginUser", loginUser);
				if(comUserMapper.selectDeptByUser(parm)>0){
					rsStatus = MnisQmConstants.APPRV_03;
				}
			}else if(loginUser.equals(headerUser)){
				rsStatus = MnisQmConstants.APPRV_03;
			}
		}
		return rsStatus;
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void saveChangeClass(ScheduleChangeClass change){
		if(change.getSeqId()!=null){
			changeClassMapper.update(change);
		}else{
			changeClassMapper.insert(change);
		}
		
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void deleteByKey(ScheduleChangeClass change){
		changeClassMapper.deleteByKey(change.getSeqId());
	}
	
	/**
	 * 新增排班接口
	 */
	@Override
	public Schedule newSchedule(String deptCode,String week){
		int weeks = 0;
		if(!StringUtils.isEmpty(week)){
			try{
				weeks = Integer.parseInt(week);
			}catch (Exception e) {
				throw new CommRuntimeException("周数错误！");
			}
		}else{
			//如果周数为空，那么使用当前日期所在周为周数
			Calendar c = Calendar.getInstance();
			weeks = c.get(Calendar.WEEK_OF_YEAR);
		}
		//新增本周排班
		return getNullSchedule(deptCode,weeks);
	}
	
	/**
	 * 新增排班
	 * @param deptCode  部门编号
	 * @param week  周数
	 * @return
	 */
	private Schedule newSchedule(String deptCode,int week){
		//获取排班周的表头
		List<SysDate> dateList = ScheduleUtil.getWeekDateList(week);
		//初始化表头
		Schedule schedule = ScheduleUtil.initSchedule(dateList,week);
		
		//获取科室所有的护理人员
		Map<String,String> parm = new HashMap<String,String>();
		parm.put("deptCode", deptCode);
		List<ComDeptNurse> userList = nurserMapper.selectUserNurseByDepot(parm);
		
		//查询所有的排班规则
		List<ScheduleRule> ruleList = getRuleList(deptCode);
		
		//处理排班规则
		Map<String,List<ScheduleRule>> ruleMap = classifyRule(ruleList);
		
		//根据排班规则获取所有的班次
		Map<String,List<ScheduleRecordDetail>> details = ScheduleUtil.getScheduleRecordDetail(ruleMap, dateList);
		
		//根据排班信息和用户获取排班
		List<ScheduleDetail> scheduleList = ScheduleUtil.getScheduleDetail(details, userList);
		
		schedule.setDtls(scheduleList);
		return schedule;
	}
	
	/**
	 * 新增排班
	 * @param deptCode  部门编号
	 * @param week  周数
	 * @return
	 */
	private Schedule getNullSchedule(String deptCode,int week){
		//获取排班周的表头
		List<SysDate> dateList = ScheduleUtil.getWeekDateList(week);
		//初始化表头
		Schedule schedule = ScheduleUtil.initSchedule(dateList,week);
		
		//获取科室所有的护理人员
		Map<String,String> parm = new HashMap<String,String>();
		parm.put("deptCode", deptCode);
		List<ComDeptNurse> userList = nurserMapper.selectUserNurseByDepot(parm);
		
		//根据排班规则获取所有的班次
		Map<String,List<ScheduleRecordDetail>> details = ScheduleUtil.getScheduleRecordDetail(new HashMap<String,List<ScheduleRule>>(), dateList);
		
		//根据排班信息和用户获取排班
		List<ScheduleDetail> scheduleList = ScheduleUtil.getScheduleDetail(details, userList);
		
		schedule.setDtls(scheduleList);
		return schedule;
	}
	
	/**
	 * 排班规则分类,按所属日期类型分类
	 * 比如：工作日，周末
	 * @return
	 */
	private Map<String,List<ScheduleRule>> classifyRule(List<ScheduleRule> ruleList){
		Map<String,List<ScheduleRule>> ruleMap = new HashMap<String,List<ScheduleRule>>();
		for(ScheduleRule rule : ruleList){
			String dateType = rule.getDateType();
			List<ScheduleRule> rs = ruleMap.get(dateType);
			if(rs == null){
				rs = new ArrayList<ScheduleRule>();
			}
			rs.add(rule);
			ruleMap.put(dateType, rs);
		}
		return ruleMap;
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void saveSchedule(Schedule schedule){
		String recordCode = schedule.getRecordCode();//记录编号
		//数据处理
		ScheduleRecord record = parseScheduleRecord(schedule);
		//状态更新
		record.setStatus(MnisQmConstants.STATUS_YX);//有效状态
		//明细数据处理
		List<ScheduleRecordDetail> details = parseScheduleRecordDetail(schedule);
		//主记录录入
		String userCode = schedule.getUserCode();
		//保存前的排班积假
		List<ComDeptNurse> oldLeave = null;
		if(StringUtils.isEmpty(recordCode)){
			recordCode = CodeUtils.getSysInvokeId();
			record.setRecordCode(recordCode);
			record.setCreatePerson(userCode);//创建人
			record.setUpdatePerson(userCode);//更新人
			
			ScheduleRecord exitsRecord = recordMapper.selectByWeeks(record);
			if(exitsRecord != null){
				exitsRecord.setStatus(MnisQmConstants.STATUS_WX);
				exitsRecord.setUpdatePerson(userCode);
				recordMapper.deleteByPrimaryKey(exitsRecord);
			}
			
			recordMapper.insert(record);
			//明细插入
			for(ScheduleRecordDetail detail : details){
				detail.setRecordCode(recordCode);
				detail.setRecordDetailCode(CodeUtils.getSysInvokeId());
				detail.setCreatePerson(userCode);//创建人
				detail.setUpdatePerson(userCode);//更新人
				recordDetailMapper.insert(detail);
			}
		}else{
			/**
			 * 获取更新前的本班次的积假
			 */
			Map<String,Object> parm = new HashMap<String,Object>();
			parm.put("recordCode", recordCode);
			oldLeave = recordMapper.selectUserLeave(parm);
			
			record.setUpdatePerson(userCode);//更新人
			recordMapper.updateByPrimaryKey(record);
			recordDetailMapper.updateByRecordCode(record);
			for(ScheduleRecordDetail detail : details){
				detail.setCreatePerson(userCode);//创建人
				detail.setUpdatePerson(userCode);//更新人
				if(!StringUtils.isEmpty(detail.getRecordDetailCode())){
					recordDetailMapper.updateByPrimaryKey(detail);
				}else{
					//新增
					detail.setRecordCode(recordCode);
					detail.setRecordDetailCode(CodeUtils.getSysInvokeId());
					detail.setCreatePerson(userCode);//创建人
					detail.setUpdatePerson(userCode);//更新人
					recordDetailMapper.insert(detail);
				}
			}
		}
		/**
		 * 获取更新后的本班次的积假
		 */
		Map<String,Object> parm = new HashMap<String,Object>();
		parm.put("recordCode", recordCode);
		List<ComDeptNurse> newLeave = recordMapper.selectUserLeave(parm);
		//计算积假
		Map<String,ComDeptNurse> leaveMap = ScheduleUtil.countLeave(oldLeave, newLeave);
		//保存积假
		if(leaveMap!=null && !leaveMap.isEmpty()){
			for(Iterator<ComDeptNurse> iter = leaveMap.values().iterator();iter.hasNext();){
				ComDeptNurse nurse = iter.next();
				nurserMapper.saveUserLeave(nurse);
			}
		}
	}
	
	/**
	 * 转换为排班主记录
	 * @param schedule
	 * @return
	 */
	private ScheduleRecord parseScheduleRecord(Schedule schedule){
		ScheduleRecord record = new ScheduleRecord();
		record.setStartDate(schedule.getStartDate());//开始日期
		record.setEndDate(schedule.getEndDate());//结束日期
		record.setWeeks(schedule.getWeeks());//周数
		record.setDeptCode(schedule.getDeptCode());//科室编号
		record.setDeptName(schedule.getDeptName());//科室名称
		record.setSeqId(schedule.getSeqId());
		record.setWeek1(schedule.getWeek1());
		record.setWeek2(schedule.getWeek2());
		record.setWeek3(schedule.getWeek3());
		record.setWeek4(schedule.getWeek4());
		record.setWeek5(schedule.getWeek5());
		record.setWeek6(schedule.getWeek6());
		record.setWeek7(schedule.getWeek7());
		
		return record;
	}
	
	/**
	 * 转换排班字记录
	 * @param schedules
	 * @return
	 */
	private List<ScheduleRecordDetail>  parseScheduleRecordDetail(Schedule sch){
		List<ScheduleDetail> schedules = sch.getDtls();
		String strDate = sch.getStartDate();
		Date startD = DateUtils.parseDate(strDate, DateUtils.YMD);//日期
		List<ScheduleRecordDetail> details = new ArrayList<ScheduleRecordDetail>();
		for(ScheduleDetail schedule : schedules){
			String userCode = schedule.getUserCode();//员工编号
			String userName = schedule.getUserName();//员工姓名
			//星期1
			Calendar c1 = Calendar.getInstance();
			c1.setTime(startD);//日期
			for(ScheduleRecordDetail detail : schedule.getWeek1()){
				detail.setStatus(MnisQmConstants.STATUS_YX);
				detail.setUserCode(userCode);
				detail.setUserName(userName);
				detail.setRecordDate(DateUtils.format(c1.getTime(), DateUtils.YMD));//记录日期
				detail.setBeds(schedule.getBeds());//床位信息
				details.add(detail);
			}
			Calendar c2 = Calendar.getInstance();
			c2.setTime(startD);//日期
			c2.add(Calendar.DATE, 1);//记录日期+1
			//星期2
			for(ScheduleRecordDetail detail : schedule.getWeek2()){
				detail.setStatus(MnisQmConstants.STATUS_YX);
				detail.setUserCode(userCode);
				detail.setUserName(userName);
				detail.setRecordDate(DateUtils.format(c2.getTime(), DateUtils.YMD));//记录日期
				detail.setBeds(schedule.getBeds());//床位信息
				details.add(detail);
			}
			Calendar c3 = Calendar.getInstance();
			c3.setTime(startD);//日期
			c3.add(Calendar.DATE, 2);//记录日期+1
			//星期3
			for(ScheduleRecordDetail detail : schedule.getWeek3()){
				detail.setStatus(MnisQmConstants.STATUS_YX);
				detail.setUserCode(userCode);
				detail.setUserName(userName);
				detail.setRecordDate(DateUtils.format(c3.getTime(), DateUtils.YMD));//记录日期
				detail.setBeds(schedule.getBeds());//床位信息
				details.add(detail);
			}
			Calendar c4 = Calendar.getInstance();
			c4.setTime(startD);//日期
			c4.add(Calendar.DATE, 3);//记录日期+1
			//星期4
			for(ScheduleRecordDetail detail : schedule.getWeek4()){
				detail.setStatus(MnisQmConstants.STATUS_YX);
				detail.setUserCode(userCode);
				detail.setUserName(userName);
				detail.setRecordDate(DateUtils.format(c4.getTime(), DateUtils.YMD));//记录日期
				detail.setBeds(schedule.getBeds());//床位信息
				details.add(detail);
			}
			Calendar c5 = Calendar.getInstance();
			c5.setTime(startD);//日期
			c5.add(Calendar.DATE, 4);//记录日期+1
			//星期5
			for(ScheduleRecordDetail detail : schedule.getWeek5()){
				detail.setStatus(MnisQmConstants.STATUS_YX);
				detail.setUserCode(userCode);
				detail.setUserName(userName);
				detail.setRecordDate(DateUtils.format(c5.getTime(), DateUtils.YMD));//记录日期
				detail.setBeds(schedule.getBeds());//床位信息
				details.add(detail);
			}
			Calendar c6 = Calendar.getInstance();
			c6.setTime(startD);//日期
			c6.add(Calendar.DATE, 5);//记录日期+1
			//星期六
			for(ScheduleRecordDetail detail : schedule.getWeek6()){
				detail.setStatus(MnisQmConstants.STATUS_YX);
				detail.setUserCode(userCode);
				detail.setUserName(userName);
				detail.setRecordDate(DateUtils.format(c6.getTime(), DateUtils.YMD));//记录日期
				detail.setBeds(schedule.getBeds());//床位信息
				details.add(detail);
			}
			Calendar c7 = Calendar.getInstance();
			c7.setTime(startD);//日期
			c7.add(Calendar.DATE, 6);//记录日期+1
			//星期天
			for(ScheduleRecordDetail detail : schedule.getWeek7()){
				detail.setStatus(MnisQmConstants.STATUS_YX);
				detail.setUserCode(userCode);
				detail.setUserName(userName);
				detail.setRecordDate(DateUtils.format(c7.getTime(), DateUtils.YMD));//记录日期
				detail.setBeds(schedule.getBeds());//床位信息
				details.add(detail);
			}
		}
		return details;
	}
	
	/**
	 * 获取科室排班信息
	 * @param deptCode
	 * @param weeks
	 * @return
	 */
	@Override
	public Schedule getSchedule(String deptCode,String week, String year){
		int weeks = 0;
		if(!StringUtils.isEmpty(week)){
			try{
				weeks = Integer.parseInt(week);
			}catch (Exception e) {
				throw new CommRuntimeException("周数错误！");
			}
		}else{
			//如果周数为空，那么使用当前日期所在周为周数
			Calendar c = Calendar.getInstance();
			weeks = c.get(Calendar.WEEK_OF_YEAR);
		}
		//查询条件
		ScheduleRecord recordParm = new ScheduleRecord();
		recordParm.setDeptCode(deptCode);
		recordParm.setWeeks(weeks);
		//年份查询参数放到到startDate
		recordParm.setStartDate(year);
		//获取主记录数据
		ScheduleRecord record = recordMapper.selectByWeeks(recordParm);
		//如果数据库不存在排班
		if(record == null){
			return null;
		}
		
		//获取明细数据
		List<ScheduleRecordDetail> dtls = recordDetailMapper.selectByRecordCode(record.getRecordCode());
		
		//数据转换
		Schedule schedule = ScheduleUtil.parseSchedule(record);
		//明细数据转换
		List<ScheduleDetail> details = ScheduleUtil.parseScheduleDetail(dtls);
		//查询新增人员
		Map<String,Object> parm = new HashMap<String,Object>();
		parm.put("deptCode", deptCode);
		parm.put("recordCode", record.getRecordCode());
		List<ComDeptNurse> addUsers = nurserMapper.selectUserAddNurseByDepot(parm);
		if(addUsers!=null && !addUsers.isEmpty()){
			//获取日期
			List<SysDate> dateList = ScheduleUtil.getWeekDateList(weeks);
			//获取排班
			Map<String,List<ScheduleRecordDetail>> dtlMap = ScheduleUtil.getScheduleRecordDetail(new HashMap<String,List<ScheduleRule>>(), dateList);
			//获取排班结果
			details.addAll(ScheduleUtil.getScheduleDetail(dtlMap,addUsers));
			
		}
		
		schedule.setDtls(details);
		return schedule;
	}
	
	/**
	 * 周排班复制
	 * @param deptCode
	 * @param srcWeek
	 * @param outWeek
	 * @return
	 */
	@Override
	public Schedule copySchedule(String deptCode,String srcWeek,String outWeek, String year){
		//获取对应周的排班
		Schedule srcRchedule = getSchedule(deptCode, srcWeek, year);
		if(srcRchedule == null){
			throw new CommRuntimeException("第["+srcWeek+"]周不存在排班！");
		}
		//周数
		int weeks = Integer.parseInt(outWeek);
		//获取排班周的表头
		List<SysDate> dateList = ScheduleUtil.getWeekDateList(weeks);
		//初始化表头
		Schedule schedule = ScheduleUtil.initSchedule(dateList,weeks);
		
		for(ScheduleDetail dtl : srcRchedule.getDtls()){
			if(dtl.getWeek1()!=null){
				for(ScheduleRecordDetail rd : dtl.getWeek1()){
					rd.setSeqId(null);
					rd.setRecordCode(null);
					rd.setRecordDetailCode(null);
				}
			}
			if(dtl.getWeek2()!=null){
				for(ScheduleRecordDetail rd : dtl.getWeek2()){
					rd.setSeqId(null);
					rd.setRecordCode(null);
					rd.setRecordDetailCode(null);
				}
			}
			if(dtl.getWeek3()!=null){
				for(ScheduleRecordDetail rd : dtl.getWeek3()){
					rd.setSeqId(null);
					rd.setRecordCode(null);
					rd.setRecordDetailCode(null);
				}
			}
			if(dtl.getWeek4()!=null){
				for(ScheduleRecordDetail rd : dtl.getWeek4()){
					rd.setSeqId(null);
					rd.setRecordCode(null);
					rd.setRecordDetailCode(null);
				}
			}
			if(dtl.getWeek5()!=null){
				for(ScheduleRecordDetail rd : dtl.getWeek5()){
					rd.setSeqId(null);
					rd.setRecordCode(null);
					rd.setRecordDetailCode(null);
				}
			}
			if(dtl.getWeek6()!=null){
				for(ScheduleRecordDetail rd : dtl.getWeek6()){
					rd.setSeqId(null);
					rd.setRecordCode(null);
					rd.setRecordDetailCode(null);
				}
			}
			if(dtl.getWeek7()!=null){
				for(ScheduleRecordDetail rd : dtl.getWeek7()){
					rd.setSeqId(null);
					rd.setRecordCode(null);
					rd.setRecordDetailCode(null);
				}
			}
		}
		schedule.setDtls(srcRchedule.getDtls());
		return schedule;
	}
	
	@Override
	public List<ComDeptNurse> getUserNurse(String deptCode,String userCode){
		ComDeptNurse nurse = new ComDeptNurse();
		nurse.setDeptCode(deptCode);
		nurse.setUserCode(userCode);
		List<ComDeptNurse> nurses = nurserMapper.selectUserNurses(nurse);
		return nurses;
	}
	
	@Override
	public void saveUserNurse(List<ComDeptNurse> nurses){
		for(ComDeptNurse nurse : nurses){
			if(nurse.getSeqId()==null){
				nurserMapper.insert(nurse);
			}else{
				nurserMapper.updateByPrimaryKey(nurse);
			}
		}
	}
	
	@Override
	public List<ScheduleCount> CountScheduleByLeave(String deptCode,String month){
		ScheduleLeave parm = new ScheduleLeave();
		parm.setDeptCode(deptCode);//科室编号
		//parm.setMounth(month);//月份
		List<ScheduleLeave> leaveList = scheduleMapper.getScheduleCount(parm);
		
		Map<String,ScheduleCount> countMap = new HashMap<String,ScheduleCount>();
		if(leaveList != null){
			for(ScheduleLeave leave : leaveList){
				String userCode = leave.getUserCode();//用户名
				ScheduleCount c = countMap.get(userCode);
				if(c == null){
					c = new ScheduleCount();
					c.setDeptCode(deptCode);
					c.setUserCode(userCode);
					c.setHisCode(leave.getHisCode());
					c.setUserName(leave.getUserName());
				}
				ScheduleUtil.copyToLeaveCount(c, leave);
				countMap.put(userCode, c);
			}
		}
		//数据返回
		List<ScheduleCount> rsList = new ArrayList<ScheduleCount>();
		rsList.addAll(countMap.values());
		return rsList;
	}
	
	@Override
	public List<ScheduleCount> CountScheduleByDays(String deptCode,String mounth){
		
		//日期维度统计节假日
		Map<String,Object> parmM = new HashMap<String,Object>();
		parmM.put("deptCode", deptCode);
		return scheduleMapper.getWorkDays(parmM);
	}
	
	@Override
	public List<ScheduleCount> CountScheduleByClass(String deptCode,String mounth){
		
		//日期维度统计节假日
		Map<String,Object> parmM = new HashMap<String,Object>();
		parmM.put("deptCode", deptCode);
		ScheduleDeptClass classes = new ScheduleDeptClass();
		classes.setDeptCode(deptCode);
		List<ScheduleCount> countList = scheduleMapper.getCountByClass(parmM);
		List<ScheduleCount> rsList = new ArrayList<ScheduleCount>();
		Map<String,ScheduleCount> rsMap = new HashMap<String,ScheduleCount>();
		for(ScheduleCount count:countList){
			ScheduleCount rsCount = rsMap.get(count.getUserCode());
			if(rsCount == null){
				rsCount = count;
				List<ScheduleDeptClass> classList = classMapper.selectAllByCode(classes);
				rsCount.getClassList().addAll(classList);
			}
			for(ScheduleDeptClass cla : rsCount.getClassList()){
				if(cla.getClassCode().equals(rsCount.getClassCode())){
					cla.setCount(rsCount.getYb());
				}
			}
			rsMap.put(count.getUserCode(), rsCount);
		}
		rsList.addAll(rsMap.values());
		return rsList;
	}

	@Override
	public void updateScheduleApproveStatus(ScheduleLeave leave) {
		scheduleLeave.updateApproveStatus(leave);
	}
}