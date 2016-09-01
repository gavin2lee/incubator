package com.lachesis.mnisqm;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.lachesis.mnisqm.controller.SystemController;
import com.lachesis.mnisqm.core.utils.GsonUtils;
import com.lachesis.mnisqm.module.system.domain.SysRole;
import com.lachesis.mnisqm.module.system.domain.SysRoleFun;

@RunWith(value=SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class SystemControllerTest {
	
	 @Autowired
	 SystemController controller;

	 @Test
	 public void saveSysRoleFuns(){
		 //TEST	护士长	01	萨达撒沙发	2016-04-07 11:06:59.477	NULL	123	000283
		 SysRole sysRole = new SysRole();
		 sysRole.setRoleCode("TEST");
		 sysRole.setSeqId(2L);
		 sysRole.setRoleName("护士长");
		 sysRole.setRemark("萨达撒沙发");
		 sysRole.setStatus("01");
		 
		 SysRoleFun sysRoleFun = new SysRoleFun();
		 sysRoleFun.setFunCode("FUN0000001");

		 SysRoleFun sysRoleFun1 = new SysRoleFun();
		 sysRoleFun1.setFunCode("FUN0000002");
		 
		 SysRoleFun sysRoleFun2 = new SysRoleFun();
		 sysRoleFun2.setFunCode("FUN0000003");
		 
		 SysRoleFun sysRoleFun3 = new SysRoleFun();
		 sysRoleFun3.setFunCode("FUN0000090");
		 
		 List<SysRoleFun> list = new ArrayList<SysRoleFun>();
		 
		 list.add(sysRoleFun);
		 list.add(sysRoleFun1);
		 list.add(sysRoleFun2);
		 list.add(sysRoleFun3);
		 sysRole.setFuns(list);
		 
		System.out.println(GsonUtils.toJson(sysRole));
		 
		 controller.saveSysRoleFuns(sysRole);
	 }
}
