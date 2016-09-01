package com.lachesis.mnisqm.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lachesis.mnisqm.BaseMapVo;
import com.lachesis.mnisqm.vo.smart.MnisVo;
import com.lachesis.mnisqm.vo.smart.MnisVoList;

@RequestMapping(value = "/smart")
@Controller
public class SmartController {
	
	@RequestMapping("/getMnisStatis")
	public @ResponseBody BaseMapVo getMnisStatis(){
		BaseMapVo vo = new BaseMapVo();
		MnisVo mnis = new MnisVo();
		mnis.setModel("7");
		int OrderNum = 40318+56381+19443;
		mnis.setOrderNum(OrderNum+"");//总数
		int errOrderNum = 1501+582+129;
		mnis.setErrOrderNum(errOrderNum+"");//错误数
		
		mnis.setDocNum("24");
		mnis.setHj("319701");
		mnis.setPgd("11958");
		mnis.setTys("4858");
		
		List<MnisVoList> voList = new ArrayList<MnisVoList>();
		MnisVoList ls1 = new MnisVoList();
		ls1.setLc("输液");
		ls1.setLcfg("4");
		ls1.setOrderNum("128990");
		ls1.setExecNum("40318");
		ls1.setDocName("肝病护理记录单");
		ls1.setcNum("182778");
		ls1.setZcNum("143406");
		voList.add(ls1);
		
		MnisVoList ls2 = new MnisVoList();
		ls2.setLc("口服药");
		ls2.setLcfg("3");
		ls2.setOrderNum("184377");
		ls2.setExecNum("56381");
		ls2.setDocName("内科护理记录单");
		ls2.setcNum("18902");
		ls2.setZcNum("15443");
		voList.add(ls2);
		
		MnisVoList ls3 = new MnisVoList();
		ls3.setLc("采血");
		ls3.setLcfg("2");
		ls3.setOrderNum("70069");
		ls3.setExecNum("19443");
		ls3.setDocName("感染科护理记录单");
		ls3.setcNum("37496");
		ls3.setZcNum("32859");
		voList.add(ls3);
		
		MnisVoList ls4 = new MnisVoList();
		ls4.setLc("化验");
		ls4.setLcfg("5");
		ls4.setOrderNum("30069");
		ls4.setExecNum("8334");
		ls4.setDocName("外科护理记录单");
		ls4.setcNum("26391");
		ls4.setZcNum("19065");
		voList.add(ls4);
		
		MnisVoList ls5 = new MnisVoList();
		ls5.setLc("皮试");
		ls5.setLcfg("5");
		ls5.setOrderNum("2601");
		ls5.setExecNum("1093");
		ls5.setDocName("儿科护理记录单");
		ls5.setcNum("13928");
		ls5.setZcNum("12047");
		voList.add(ls5);
		
		MnisVoList ls6 = new MnisVoList();
		ls6.setLc("其他处置单");
		ls6.setLcfg("1");
		ls6.setOrderNum("242023");
		ls6.setExecNum("87846");
		ls6.setDocName("肺科护理记录单");
		ls6.setcNum("39092");
		ls6.setZcNum("33816");
		voList.add(ls6);
		mnis.setLst(voList);
		vo.addData("statis", mnis);
		
		return vo;
	}
}