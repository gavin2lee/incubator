package com.lachesis.mnis.web;

import java.text.ParseException;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lachesis.mnis.core.nursing.NurseItemCategory;
import com.lachesis.mnis.core.nursing.NurseItemRecord;
import com.lachesis.mnis.core.nursing.NurseItemService;
import com.lachesis.mnis.web.common.ResultCst;
import com.lachesis.mnis.web.common.vo.BaseMapVo;
import com.lachesis.mnis.web.common.vo.BaseVo;

@Controller
@RequestMapping("/nur/nurseItem")
public class NurseItemAction {
	@Resource(name="nurseItemService")
	private NurseItemService nurseItemService;
	/**
	 * 获取记录项数据
	 * @param token
	 * @param workUnitCode
	 * @return
	 */
	@RequestMapping("/getNurseItemConfig")
	public @ResponseBody
	BaseVo getNurseItemConfig(String token, String workUnitCode){
		BaseMapVo vo = new BaseMapVo();
		
		List<NurseItemCategory> list = nurseItemService.getNurseItemConfig(workUnitCode);
		vo.addData("lst",list);
		vo.setRslt(ResultCst.SUCCESS);
		
		return vo;
	}
	
	/**
	 * 保存护理记录
	 * @param token
	 * @param nurseItemRecord
	 * @return
	 */
	@RequestMapping("/saveNurseItem")
	public @ResponseBody
	BaseVo saveNurseItem(String token, @RequestBody NurseItemRecord nurseItemRecord){
		BaseVo vo = new BaseVo();
		
		int resultCode = nurseItemService.saveNurseItemRecord(nurseItemRecord);
		if(resultCode<= 0){
			vo.setRslt(ResultCst.INVALID_PARAMETER);
			vo.setMsg("传入的护理项目记录有误！");
		}
		else{
			vo.setRslt(ResultCst.SUCCESS);
		}
		
		return vo;
	}
	
	/**
	 * 获取患者某天的护理项
	 * @param token
	 * @param date
	 * @param patientId
	 * @return
	 */
	@RequestMapping("/getNurseItemRecord")
	public @ResponseBody
	BaseVo getNurseItemRecord(String token, String date, String patientId) {
		BaseMapVo vo = new BaseMapVo();
		
		try {
			List<NurseItemRecord> list = nurseItemService.getNurseItemRecords(patientId, date);
			vo.addData("lst",list);
			
			vo.setRslt(ResultCst.SUCCESS);
		} catch (ParseException e) {
			vo.setRslt(ResultCst.INVALID_PARAMETER);
			vo.setMsg("传入日期格式有误");
		}
		
		return vo;
	}
}
