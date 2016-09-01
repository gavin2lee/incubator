package com.lachesis.mnisqm;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.lachesis.mnisqm.constants.MnisQmConstants;
import com.lachesis.mnisqm.controller.ScheduleController;
import com.lachesis.mnisqm.core.utils.GsonUtils;
import com.lachesis.mnisqm.module.schedule.domain.ScheduleChangeClass;
import com.lachesis.mnisqm.module.schedule.domain.ScheduleDeptClass;
import com.lachesis.mnisqm.module.schedule.domain.ScheduleLeave;
import com.lachesis.mnisqm.module.schedule.domain.ScheduleRule;
import com.lachesis.mnisqm.module.user.domain.ComBedGroup;

@RunWith(value = SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class ScheduleControllerTest {

	@Autowired
	ScheduleController controller;

	/**
	 * 查询分组
	 */
	// @Test
	public void getGroupList() {
		String deptCode = "5042";
		controller.getGroupList(deptCode);
	}

	/**
	 * 保存分组
	 */
	// @Test
	public void saveGroup() {
		ComBedGroup group = new ComBedGroup();
		// group.setSeqId(new Long(1));
		group.setDeptCode("5042");
		group.setGroupName("第一组");
		System.out.println(GsonUtils.toJson(group));
		controller.saveGroup(group);
	}

	/**
	 * 删除分组
	 */
	// @Test
	public void deleteGroup() {
		ComBedGroup group = new ComBedGroup();
		group.setSeqId(new Long(1));
		System.out.println(GsonUtils.toJson(group));
		controller.deleteGroup(group);
	}

	/**
	 * 获取床位列表
	 */
	// @Test
	public void getClassList() {
		GsonUtils.toJson(controller.getClassList("5042"));
	}

	/**
	 * 新增床位
	 */
	// @Test
	public void saveClass() {
		ScheduleDeptClass classs = new ScheduleDeptClass();
		classs.setClassName("A班");
		classs.setDeptCode("5042");
		controller.saveClass(classs);
	}

	/**
	 * 删除床位
	 */
	// @Test
	public void deleteClass() {
		ScheduleDeptClass classs = new ScheduleDeptClass();
		classs.setSeqId(new Long(1));
		controller.deleteClass(classs);
	}

	/**
	 * 查询排班规则列表
	 */
	// @Test
	public void getRuleList() {
		controller.getRuleList("5042");
	}

	// @Test
	public void saveRule() {
		ScheduleRule rule = new ScheduleRule();
		;
		rule.setRuleName("规则一");
		rule.setStatus(MnisQmConstants.STATUS_YX);
		controller.saveRule(rule);
	}

	// @Test
	public void deleteRule() {
		ScheduleRule rule = new ScheduleRule();
		;
		rule.setSeqId(new Long(1));
		controller.deleteRule(rule);
	}

	/**
	 * 查询请假信息
	 */
	// @Test
	public void getLeaveList() {
		//controller.getLeaveList("5042");
	}

	/**
	 * 保存请假信息
	 */
	//@Test
	public void saveLeave() {
		ScheduleLeave leave = new ScheduleLeave();
		leave.setDeptCode("5042");
		controller.saveScheduleLeave(leave);
	}
	
	/**
	 * 删除请假信息
	 */
	//@Test
	public void deleteLeave() {
		ScheduleLeave leave = new ScheduleLeave();
		leave.setSeqId(new Long(1));
		controller.deleteScheduleLeave(leave);
	}
	
	//@Test
	public void saveChangeClass() {
		ScheduleChangeClass change = new ScheduleChangeClass();
		change.setApplyUserCode("123");
		controller.saveChangeClass(change);
	}
	
	//@Test
	public void getChangeClass() {
		controller.getChangeClass("5042");
	}
	
	//@Test
	public void delete(){
		ScheduleChangeClass change = new ScheduleChangeClass();
		change.setSeqId(new Long(1));
		controller.deleteChangeClass(change);
	}
	
	//@Test
	public void getLeave(){
		controller.getLeaveList("5042", null);
	}
	
	//@Test
	public void saveLeave2(){
		ScheduleLeave leave = new ScheduleLeave();
		leave.setSeqId(10L);
		leave.setApprovePersonCode("000283");
		leave.setUpdatePerson("csq");
		leave.setApproveStatus("02");
		controller.apprvScheduleLeave(leave);
	}
}
