package com.lachesis.mnis.web.task;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(value=SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext-web.xml")
public class NurseNotifyTaskTest extends AbstractJUnit4SpringContextTests{
	
	@Test
	public void registerListBean(){
		NurseNotifyTask task = applicationContext.getBean("nurseNotifyTask", NurseNotifyTask.class);
		
		task.notifyBodySign();
		task.notifyNurses();
	}
}
