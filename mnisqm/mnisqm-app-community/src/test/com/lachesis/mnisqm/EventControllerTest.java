package com.lachesis.mnisqm;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.lachesis.mnisqm.controller.EventController;
import com.lachesis.mnisqm.core.utils.GsonUtils;
import com.lachesis.mnisqm.module.event.domain.EventLevel;
import com.lachesis.mnisqm.module.event.domain.EventMeasures;
import com.lachesis.mnisqm.module.event.domain.EventReport;
import com.lachesis.mnisqm.module.event.domain.EventReportDetail;
import com.lachesis.mnisqm.module.event.domain.EventRequirement;

@RunWith(value=SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class EventControllerTest {
	
	 @Autowired
	 EventController controller;

	 //@Test
	 public void saveEventLevel(){
		EventLevel level = new EventLevel();
		level.setDamageLevelName("332");
		level.setFlow("flow");
		System.out.println(GsonUtils.toJson(level));
		controller.saveEventLevel(level);
	 }
	 
	//@Test
	 public void deleteEventLevel(){
		EventLevel level = new EventLevel();
		level.setSeqId(new Long(1));
		System.out.println(GsonUtils.toJson(level));
		controller.deleteEventLevel(level);
	 }
	 
	 //@Test
	 public void getEventLevels(){
		EventLevel level = new EventLevel();
		level.setSeqId(new Long(1));
		level.setDamageLevelName("332");
		level.setFlow("flow");
		List<EventLevel> levels = new ArrayList<EventLevel>();
		levels.add(level);
		
		BaseMapVo vo = new BaseMapVo();
		vo.addData("lst", levels);
		System.out.println(GsonUtils.toJson(vo));
		System.out.println(controller.getEventLevels());
	 }
	 
	 //@Test
	 public void saveEventReport(){
		EventReport report = new EventReport();
		report.setReportCode("33");
		report.setDamageLevel("123");
		report.setDeptCode("123");
		report.setDeptName("44");
		report.setEventTypeCode("eventTypeCode");
		report.setUserCode("userCode");
		report.setUserName("123");
		report.setCreatePerson("123");
		report.setUpdatePerson("123");
		System.out.println(GsonUtils.toJson(report));
		controller.saveEventReport(report);
	 }
	 
	 //@Test
	 public void getEventList(){
		System.out.println(GsonUtils.toJson(controller.getEventList("123","01","userCode","userCode")));
	 }
	 
	//@Test
	 public void getEventReport(){
		System.out.println(GsonUtils.toJson(controller.getEventReport("123")));
	 }
	 
	 //@Test
	 public void saveEventReportDetail(){
		 List<EventReportDetail> details = new ArrayList<EventReportDetail>();
		 EventReportDetail detail = new EventReportDetail();
		 detail.setReportCode("code");
		 detail.setItemCode("2");
		 detail.setItemName("2");
		 detail.setItemValue("3");
		 detail.setCreatePerson("4");
		 detail.setUpdatePerson("5");
		 detail.setStatus("01");
		 details.add(detail);
		 System.out.println(GsonUtils.toJson(details));
		 controller.saveReportDetail(details);
	 }
	 
	 //@Test
	 public void getEventReportDetail(){
		System.out.println(GsonUtils.toJson(controller.getReportDetail("code")));
	 }
	 
	 //不良事件要求信息查询
	 //@Test
	 public void getEventRequirement(){
		 System.out.println(GsonUtils.toJson(controller.getEventRequirement("code")));
	 }
	 
	 //@Test
	 public void saveEventRequirement(){
		 EventRequirement required = new EventRequirement();
		 required.setReportCode("code2");
		 required.setCreatePerson("123");
		 required.setUpdatePerson("123");
		 required.setReqTime(new Date());
		 required.setUserCode("123");
		 required.setReqContent("严格");
		 //required.setRemark("3");
		 System.out.println(GsonUtils.toJson(required));
		 controller.saveEventRequirement(required);
	 }
	 
	 //@Test
	 public void saveEventMeasures(){
		 EventMeasures mea = new EventMeasures();
		 mea.setReportCode("code2");
		 mea.setCreatePerson("123");
		 mea.setUpdatePerson("123");
		 mea.setUserCode("123");
		 mea.setMeaTime(new Date());
		 mea.setMeaContent("33");
		 System.out.println(GsonUtils.toJson(mea));
		 controller.saveEventMeasures(mea);
	 }
	 
	 @Test
	 public void getEventMeasures(){
		 System.out.println(GsonUtils.toJson(controller.getEventMeasures("code2")));
	 }
}
