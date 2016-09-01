package com.lachesis.mnisqm.module.event.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lachesis.mnisqm.constants.MnisQmConstants;
import com.lachesis.mnisqm.constants.MnisQmConstants.SysDicConstants;
import com.lachesis.mnisqm.core.utils.CodeUtils;
import com.lachesis.mnisqm.core.utils.DateTimeUtils;
import com.lachesis.mnisqm.core.utils.StringUtils;
import com.lachesis.mnisqm.module.event.dao.EventAssessmentMapper;
import com.lachesis.mnisqm.module.event.dao.EventElementMapper;
import com.lachesis.mnisqm.module.event.dao.EventLevelMapper;
import com.lachesis.mnisqm.module.event.dao.EventMeasuresMapper;
import com.lachesis.mnisqm.module.event.dao.EventReportDetailMapper;
import com.lachesis.mnisqm.module.event.dao.EventReportMapper;
import com.lachesis.mnisqm.module.event.dao.EventRequirementMapper;
import com.lachesis.mnisqm.module.event.dao.EventTypeMapper;
import com.lachesis.mnisqm.module.event.dao.EventTypeMapperExt;
import com.lachesis.mnisqm.module.event.domain.AnalysisMeasures;
import com.lachesis.mnisqm.module.event.domain.EventAnalysis;
import com.lachesis.mnisqm.module.event.domain.EventAssessment;
import com.lachesis.mnisqm.module.event.domain.EventElement;
import com.lachesis.mnisqm.module.event.domain.EventLevel;
import com.lachesis.mnisqm.module.event.domain.EventMeasures;
import com.lachesis.mnisqm.module.event.domain.EventReport;
import com.lachesis.mnisqm.module.event.domain.EventReportDetail;
import com.lachesis.mnisqm.module.event.domain.EventRequirement;
import com.lachesis.mnisqm.module.event.domain.EventType;
import com.lachesis.mnisqm.module.event.domain.NursingInfo;
import com.lachesis.mnisqm.module.event.domain.NursingMeasures;
import com.lachesis.mnisqm.module.event.domain.RiskManage;
import com.lachesis.mnisqm.module.event.domain.RiskTrend;
import com.lachesis.mnisqm.module.event.service.IEventService;
import com.lachesis.mnisqm.module.remote.event.dao.AnalysisMeasuresMapper;
import com.lachesis.mnisqm.module.remote.event.dao.EventAnalysisMapper;
import com.lachesis.mnisqm.module.remote.event.dao.NursingInfoMapper;
import com.lachesis.mnisqm.module.remote.event.dao.NursingMeasuresMapper;
import com.lachesis.mnisqm.module.remote.event.dao.RiskManageMapper;
import com.lachesis.mnisqm.module.remote.event.dao.RiskTrendMapper;
import com.lachesis.mnisqm.module.system.dao.SysTaskLogMapper;
import com.lachesis.mnisqm.module.system.dao.SysTaskMapper;
import com.lachesis.mnisqm.module.system.domain.SysTask;
import com.lachesis.mnisqm.module.system.domain.SysTaskLog;
import com.lachesis.mnisqm.module.system.service.ICacheService;
import com.lachesis.mnisqm.module.user.domain.ComDept;

@Service
public class EventServiceImpl implements IEventService{
	
	@Autowired
	private ICacheService cacheService;
	
	@Autowired
	private EventTypeMapperExt eventTypeMapperExt;
	
	@Autowired
	private EventTypeMapper eventTypeMapper;
	
	@Autowired
	private EventElementMapper eventElementMapper;
	
	@Autowired
	private EventLevelMapper eventLevelMapper;
	
	@Autowired
	private EventReportMapper eventReportMapper;
	
	@Autowired
	private EventReportDetailMapper eventReportDetailMapper;
	
	@Autowired
	private EventMeasuresMapper eventMeasuresMapper;
	
	@Autowired
	private EventRequirementMapper eventRequirementMapper;
	
	@Autowired
	private EventAssessmentMapper eventAssessmentMapper;
	
	@Autowired
	private RiskManageMapper riskManageMapper;
	
	@Autowired
	private EventAnalysisMapper eventAnalysisMapper;
	
	@Autowired
	private AnalysisMeasuresMapper analysisMeasuresMapper;
	
	@Autowired
	private NursingInfoMapper nursingInfoMapper;
	
	@Autowired
	private NursingMeasuresMapper nursingMeasuresMapper;
	
	@Autowired
	private RiskTrendMapper riskTrendMapper;
	
	
	@Autowired
	private SysTaskMapper sysTaskMapper;
	
	@Autowired
	private SysTaskLogMapper sysTaskLogMapper;
	
	@Override
	public List<EventType> getEventType(){
	
		return eventTypeMapperExt.selectEventTypeList();
	}
	
	@Override
	public List<EventLevel> getEventLevels(){
		return eventLevelMapper.selectAll();
	}
	
	@Override
	public void saveEventLevel(EventLevel level){
		if(null == level.getSeqId()){
			//数据插入
			level.setDamageLevel(CodeUtils.getSysInvokeId());
			eventLevelMapper.insert(level);
		}else{
			eventLevelMapper.updateByPrimaryKey(level);
		}
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void deleteEventLevel(EventLevel level){
		eventLevelMapper.logicDelete(level);
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void saveEventReport(EventReport report){
		if(null == report.getSeqId()){
			//此时为新增状态，生成reportCode
			report.setReportCode(CodeUtils.getSysInvokeId());
			eventReportMapper.insert(report);
		}else{
			// 主键有值，为修改不良事件
			eventReportMapper.updateByPrimaryKey(report);
		}
	}
	
	
	
	@Override
	public void saveSysTask(SysTask task) {
		if(null==task.getSeqId()){
			//新增
			sysTaskMapper.insert(task);
		}else{
			sysTaskMapper.updateByPrimaryKey(task);
		}
	}

	@Override
	public void saveSysTaskLog(SysTaskLog log) {
		sysTaskLogMapper.insert(log);
	}

	
	@Override
	public SysTask getSysTaskByTaskCode(String taskCode) {
		Map<String, Object> param=new HashMap<String, Object>();
		param.put("taskCode", taskCode);
		SysTask task = sysTaskMapper.selectByTaskCode(param);
		return task;
	}


	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void deleteEventReport(EventReport report){
		eventReportMapper.deleteByPrimaryKey(report);
	}
	
	@Override
	public List<EventReport> getEventReport(EventReport report,String apprvUserCode){
		List<EventReport> reports = new ArrayList<EventReport>();
		reports = eventReportMapper.selectEventList(report);
		if(reports != null){
			for(EventReport r : reports){
				//时间显示处理
				r.setEventTime(r.getEventTime().substring(0,r.getEventTime().lastIndexOf(":")));
				//患者性别
				r.setPatientGenderName(cacheService.getSysDicValue(SysDicConstants.gender, r.getPatientGender()));
				String permission=apprvPermission(apprvUserCode, r);
				//设置权限
				r.setPermission(permission);
			}
		}
		return reports;
	}

	private String apprvPermission(String apprvUserCode, EventReport r) {
		String permission=MnisQmConstants.APPRV_04;
		//判断是不是 未提交的
		if(StringUtils.isEmpty(r.getTaskCode())){
			//本人创建的
			if(apprvUserCode.equals(r.getUserCode())){
				permission=MnisQmConstants.APPRV_STATUS_01;
			}
		}else{
			//已经提交
			Map<String, Object> param=new HashMap<String, Object>();
			param.put("taskCode", r.getTaskCode());
			//找到绑定的sysTask
			SysTask task=sysTaskMapper.selectByTaskCode(param);
			//判断task的taskStatus
			if(MnisQmConstants.TASK_STATUS_01.equals(task.getTaskStatus())||MnisQmConstants.TASK_STATUS_05.equals(task.getTaskStatus())){
				//提交至护士长或打回至护士长
				//判断apprvUserCode是否是task的handleDept的护士长
				//找到report对应的科室长
				ComDept dept=eventReportMapper.selectDeptHeader(r);
				String deptHeader="";
				if(dept!=null){
					deptHeader=dept.getDeptNurseHeader();
				}
				if(apprvUserCode.equals(deptHeader)){
					permission=MnisQmConstants.APPRV_02;
				}
			}else if(MnisQmConstants.TASK_STATUS_02.equals(task.getTaskStatus())){
				//提交到护理部
				r.setDeptCode("0410");
				ComDept dept=eventReportMapper.selectDeptHeader(r);
				String deptHeader="";
				if(dept!=null){
					deptHeader=dept.getDeptNurseHeader();
				}
				if(apprvUserCode.equals(deptHeader)){
					permission=MnisQmConstants.APPRV_03;
				}
			}else if(MnisQmConstants.TASK_STATUS_04.equals(task.getTaskStatus())){
				//打回至护士
				if(apprvUserCode.equals(r.getUserCode())){
					permission=MnisQmConstants.APPRV_01;
				}
			}else{
				//通过或者终止的 设置 04,前端不能进行操作
				permission=MnisQmConstants.APPRV_04;
			}
		}
		return permission;
	}
	
	@Override
	public List<EventReportDetail> getReportDetails(String reportCode){
		//查询条件-上报的编号
		EventReportDetail detail = new EventReportDetail();
		detail.setReportCode(reportCode);
		//数据查询
		return eventReportDetailMapper.selectReportDetailByCode(detail);
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void saveReportDetail(List<EventReportDetail> details){
		if(null == details || details.isEmpty()){
			return ;
		}
		//查询条件-上报的编号
		EventReportDetail detail = new EventReportDetail();
		detail.setReportCode(details.get(0).getReportCode());
		//数据查询
		List<EventReportDetail> dbList = eventReportDetailMapper.selectReportDetailByCode(detail);
		
		//如果数据库中已经存在有
		if(null != dbList && !dbList.isEmpty()){
			Set<String> exitSet = new HashSet<String>();
			for(EventReportDetail d : details){
				exitSet.add(d.getItemCode());
			}
			for(EventReportDetail d : dbList){
				if(!exitSet.contains(d.getItemCode())){
					exitSet.add(d.getItemCode());
					d.setStatus(MnisQmConstants.STATUS_WX);//删除--无效
					details.add(d);
				}
			}
			//释放缓存
			exitSet = null;
			dbList = null;
		}
		
		//数据保存
		for(EventReportDetail d : details){
			if(d.getSeqId() == null){
				eventReportDetailMapper.insert(d);
			}else{
				eventReportDetailMapper.updateByPrimaryKey(d);
			}
		}
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void saveEventMeasures(EventMeasures measures){
		if(measures.getSeqId() == null){
			eventMeasuresMapper.insert(measures);
		}else{
			eventMeasuresMapper.updateByPrimaryKey(measures);
		}
	}
	
	@Override
	public List<EventMeasures> getEventMeasures(EventMeasures measures){
		return eventMeasuresMapper.selectEventMeasuresList(measures);
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void deleteEventMeasures(EventMeasures measures){
		eventMeasuresMapper.deleteByPrimaryKey(measures.getSeqId());
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void saveEventRequirement(EventRequirement req){
		if(req.getSeqId() == null){
			eventRequirementMapper.insert(req);
		}else{
			eventRequirementMapper.updateByPrimaryKey(req);
		}
	}
	
	@Override
	public List<EventRequirement> getEventRequirement(EventRequirement req){
		return eventRequirementMapper.selectRequireList(req);
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void deleteEventRequirement(EventRequirement req){
		eventRequirementMapper.deleteByPrimaryKey(req.getSeqId());
	}
	

	@Override
	public void saveEventAssessment(EventAssessment assessment) {
		if(assessment.getSeqId()==null){
			//生成评估编号
			assessment.setAssessCode(CodeUtils.getSysInvokeId());
			eventAssessmentMapper.insert(assessment);
		}else{
			eventAssessmentMapper.updateByPrimaryKey(assessment);
		}
		
	}

	@Override
	public void deleteEventAssessment(EventAssessment assessment) {
		eventAssessmentMapper.deleteByPrimaryKey(assessment.getSeqId());
	}

	@Override
	public List<EventAssessment> getEventAssessmentList(
			EventAssessment assessment) {
		return eventAssessmentMapper.selectEventAssessmentList(assessment);
	}

	@Override
	public List<RiskManage> selectAllRiskManage() {
		return riskManageMapper.selectAll();
	}

	@Override
	public List<EventAnalysis> selectAllEventAnalysis() {
		/*List<EventAnalysis> resultList = new ArrayList<EventAnalysis>();
		List<String> nameList = eventAnalysisMapper.queryAllEventName();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("threeD", DateTimeUtils.formatDate(DateTimeUtils.getDateByDay(new Date(), -30), "yyyy-MM-dd HH:mm:ss"));
		map.put("threeM", DateTimeUtils.formatDate(DateTimeUtils.getDateByMonth(new Date(), -3), "yyyy-MM-dd HH:mm:ss"));
		map.put("threeY", DateTimeUtils.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss").substring(0,4) + "-01-01 00:00:01");
		map.put("endTime", DateTimeUtils.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
		
		Date lastYearDate = DateTimeUtils.getDateByMonth(new Date(), -12);
		map.put("threeDL", DateTimeUtils.formatDate(DateTimeUtils.getDateByDay(lastYearDate, -30), "yyyy-MM-dd HH:mm:ss"));
		map.put("threeML", DateTimeUtils.formatDate(DateTimeUtils.getDateByMonth(lastYearDate, -3), "yyyy-MM-dd HH:mm:ss"));
		map.put("threeYL", DateTimeUtils.formatDate(lastYearDate, "yyyy-MM-dd HH:mm:ss").substring(0,4) + "-01-01 00:00:01");
		map.put("endTimeL", DateTimeUtils.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss").substring(0,4) + "-12-31 23:59:59");
		for (String eventName : nameList) {
			map.put("eventName", eventName);
			resultList.add(eventAnalysisMapper.getEventSumByNameAndDate(map));
		}*/
		return eventAnalysisMapper.selectAll();
	}

	@Override
	public List<AnalysisMeasures> selectAllAnalysisMeasures() {
		return analysisMeasuresMapper.selectAll();
	}

	@Override
	public List<NursingInfo> selectAllNursingInfo() {
		return nursingInfoMapper.selectAll();
	}

	@Override
	public List<NursingMeasures> selectAllNursingMeasures() {
		return nursingMeasuresMapper.selectAll();
	}

	@Override
	public List<RiskTrend> selectAllRiskTrend() {
		return riskTrendMapper.selectAll();
	}

	@Override
	public void saveEventType(EventType eventType) {
		if(eventType.getSeqId()==null){
			eventType.setStatus(MnisQmConstants.STATUS_YX);
			eventTypeMapper.insert(eventType);
		}else{
			eventType.setStatus(MnisQmConstants.STATUS_YX);
			eventTypeMapper.updateByPrimaryKey(eventType);
		}
	}

	@Override
	public void deleteEventType(EventType eventType) {
		EventType e = eventTypeMapper.selectByPrimaryKey(eventType.getSeqId());
		e.setUpdatePerson(eventType.getUpdatePerson());
		e.setStatus(MnisQmConstants.STATUS_WX);
		eventTypeMapper.updateByPrimaryKey(e);
	}

	@Override
	public void saveEventElement(EventElement eventElement) {
		eventElement.setStatus(MnisQmConstants.STATUS_YX);
		if(null==eventElement.getSeqId()){
			eventElement.setCreateTime(new Date());
			eventElement.setUpdateTime(new Date());
			eventElementMapper.insert(eventElement);
		}else{
			eventElement.setUpdateTime(new Date());
			eventElementMapper.updateByPrimaryKey(eventElement);
		}
	}

	@Override
	public List<EventElement> selectAllEventElement() {
		return eventElementMapper.selectAll();
	}

	@Override
	public List<EventElement> getEventElementByTypeCode(String eventTypeCode) {
		return eventElementMapper.selectByEventTypeCode(eventTypeCode);
	}

	@Override
	public void deleteEventElementByPrimaryKey(EventElement eventElement) {
		EventElement e = eventElementMapper.selectByPrimaryKey(eventElement.getSeqId());
		e.setStatus(MnisQmConstants.STATUS_WX);
		e.setUpdatePerson(eventElement.getUpdatePerson());
		e.setUpdateTime(new Date());
		eventElementMapper.updateByPrimaryKey(e);
	}
}