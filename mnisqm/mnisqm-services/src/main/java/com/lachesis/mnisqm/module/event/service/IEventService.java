package com.lachesis.mnisqm.module.event.service;

import java.util.List;

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
import com.lachesis.mnisqm.module.system.domain.SysTask;
import com.lachesis.mnisqm.module.system.domain.SysTaskLog;


public interface IEventService {
	/**
	 * 获取不良事件类型
	 * @return
	 */
	public List<EventType> getEventType();
	
	/**
	 * 保存不良事件类型
	 * @param eventType
	 */
	public void saveEventType(EventType eventType);
	
	/**
	 * 删除不良事件类型
	 * @param eventType
	 */
	public void deleteEventType(EventType eventType);
	
	/**
	 * 查询不良事件分级列表
	 * @return
	 */
	public List<EventLevel> getEventLevels();
	
	/**
	 * 保存事件分级
	 * @param level
	 */
	public void saveEventLevel(EventLevel level);
	
	/**
	 * 删除事件分级
	 * @param level
	 */
	public void deleteEventLevel(EventLevel level);
	
	/**
	 * 保存不良事件
	 * @param report
	 */
	public void saveEventReport(EventReport report);
	
	/**
	 * 保存任务
	 * @param task
	 */
	public void saveSysTask(SysTask task);
	
	/**
	 * 保存日志
	 * @param log
	 */
	public void saveSysTaskLog(SysTaskLog log);
	
	/**
	 * 根据taskCode找到对应的任务
	 * @param taskCode
	 * @return
	 */
	public SysTask getSysTaskByTaskCode(String taskCode);
	
	
	/**
	 * 删除不良事件上报
	 * @param report
	 */
	public void deleteEventReport(EventReport report);
	
	
	/**
	 * 查询不良事件上报信息
	 * @param report
	 * @param apprvUserCode 
	 */
	public List<EventReport> getEventReport(EventReport report, String apprvUserCode);
	
	/**
	 * 查询不良事件调查信息
	 * @param reportCode
	 * @return
	 */
	public List<EventReportDetail> getReportDetails(String reportCode);
	
	/**
	 * 保存调查信息
	 * @param details
	 */
	public void saveReportDetail(List<EventReportDetail> details);
	
	/**
	 * 保存不良事件处理
	 * @param measures
	 */
	public void saveEventMeasures(EventMeasures measures);
	
	/**
	 * 查询不良事件措施
	 * @param measures
	 */
	public List<EventMeasures> getEventMeasures(EventMeasures measures);
	
	/**
	 * 不良事件措施删除
	 * @param measures
	 */
	public void deleteEventMeasures(EventMeasures measures);
	
	/**
	 * 保存不良事件要求信息
	 * @param req
	 */
	public void saveEventRequirement(EventRequirement req);
	
	/**
	 * 查询不良事件要求
	 * @param req
	 * @return
	 */
	public List<EventRequirement> getEventRequirement(EventRequirement req);
	
	/**
	 * 删除不良事件要求
	 * @param req
	 */
	public void deleteEventRequirement(EventRequirement req);
	
	/**
	 * 新增或修改 事件评估
	 * @param assessment
	 */
	public void saveEventAssessment(EventAssessment assessment);
	
	/**
	 * 删除不良事件评估
	 * @param assessment
	 */
	public void deleteEventAssessment(EventAssessment assessment);
	
	/**
	 * 根据措施编号查询评估了列表
	 * @param assessment
	 * @return
	 */
	public List<EventAssessment> getEventAssessmentList(EventAssessment assessment);
	
	
	
	/**
	 * 获取风险管理主界面信息
	 * @return
	 */
	public List<RiskManage> selectAllRiskManage();
	
	/**
	 * 获取不良事件类别分析信息
	 * @return
	 */
	public List<EventAnalysis> selectAllEventAnalysis();
	
	/**
	 * 获取原因分析及干预措施建议信息
	 * @return
	 */
	public List<AnalysisMeasures> selectAllAnalysisMeasures();
	
	/**
	 * 获取临床护理信息
	 * @return
	 */
	public List<NursingInfo> selectAllNursingInfo();
	
	/**
	 * 获取临床护理措施信息
	 * @return
	 */
	public List<NursingMeasures> selectAllNursingMeasures();
	
	/**
	 * 获取风险趋势信息
	 * @return
	 */
	List<RiskTrend> selectAllRiskTrend();
	
	/**
	 * 保存事件元素
	 * @param eventElement
	 */
	public void saveEventElement(EventElement eventElement);
	
	/**
	 * 获取不良事件元素
	 * @return
	 */
	List<EventElement> selectAllEventElement();
	
	/**
	 * 根据不良事件类型代码获得事件元素列表
	 * @param eventElement
	 * @return
	 */
	List<EventElement> getEventElementByTypeCode(String eventTypeCode);
	
	/**
	 * 根据id删除事件元素
	 * @param eventElement
	 */
	public void deleteEventElementByPrimaryKey(EventElement eventElement);
	
}
