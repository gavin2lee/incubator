package com.lachesis.mnis.web;


import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.lachesis.mnis.core.bloodSugarMonitor.entity.PatBloodSugarMonitor;
import com.lachesis.mnis.core.util.GsonUtils;

@RunWith(value=SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext-web.xml")
public class BloodSugarMonitorActionTest extends AbstractJUnit4SpringContextTests{
	
	@Autowired
	BloodSugarMonitorAction action;
	
	@Test
	public void queryBloodPress(){
		action.queryBloodSugar("5042", "ZY010000146676", "2016-02-02", "02");
	}
	
	//@Test
	public void saveBloodPress(){
		List<PatBloodSugarMonitor> lst = new ArrayList<PatBloodSugarMonitor>();
		PatBloodSugarMonitor press = new PatBloodSugarMonitor();
		press.setPatId("ZY010000146676");
		press.setDeptCode("5042");
		press.setItemCode("bq");
		press.setItemValue("120/44");
		press.setRecordTime("2016-02-02 08:00:01");
		lst.add(press);
		String txt = GsonUtils.toJson(lst);
		action.saveBloodSugar(txt);
	}
}
