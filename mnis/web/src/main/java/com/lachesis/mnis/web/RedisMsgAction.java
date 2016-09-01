package com.lachesis.mnis.web;

import java.lang.reflect.Type;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.reflect.TypeToken;
import com.lachesis.mnis.core.RedisMsgService;
import com.lachesis.mnis.core.critical.entity.CriticalValueRecord;
import com.lachesis.mnis.core.inspection.entity.InspectionRecord;
import com.lachesis.mnis.core.labtest.entity.LabTestRecord;
import com.lachesis.mnis.core.order.entity.HisOrderGroup;
import com.lachesis.mnis.core.patrol.entity.WardPatrolInfo;
import com.lachesis.mnis.core.redismsg.entity.RedisMsgRecord;
import com.lachesis.mnis.core.skintest.entity.SkinTestInfoLx;
import com.lachesis.mnis.core.util.GsonUtils;
import com.lachesis.mnis.web.common.ResultCst;
import com.lachesis.mnis.web.common.vo.BaseMapVo;

import org.springframework.web.bind.annotation.ResponseBody;
/**
 * redis消息展示action
 * @author ThinkPad
 *
 */

@RequestMapping("/nur/redisMsg")
@Controller
public class RedisMsgAction {
	private static Logger LOGGER = LoggerFactory.getLogger(RedisMsgAction.class);
	@Autowired
	private RedisMsgService redisMsgService;
	
	/**
	 * 消息推送获取危急值信息
	 * @param deptCode
	 * @return
	 */
	@RequestMapping("/getCriticalValueRecords")
	@ResponseBody
	public BaseMapVo getCriticalValueRecords(String deptCode){
		BaseMapVo vo = new BaseMapVo();
		
		try {
			List<CriticalValueRecord> criticalValueRecords = redisMsgService
					.getCriticalValueRecords(deptCode);
			vo.addData("list", criticalValueRecords);
			vo.setRslt(ResultCst.SUCCESS);
		} catch (Exception e) {
			vo.setMsg(e.getMessage());
			vo.setRslt(ResultCst.ALERT_ERROR);
		}
		
		return vo;
	}
	
	/**
	 * 检验报告信息
	 * @param deptCode
	 * @return
	 */
	@RequestMapping("/getPublishLabTests")
	@ResponseBody
	public BaseMapVo getPublishLabTests(String deptCode){
		BaseMapVo vo = new BaseMapVo();
		
		try {
			List<LabTestRecord> labTestRecords = redisMsgService
					.getPublishLabTests(deptCode);
			vo.addData("list", labTestRecords);
			vo.setRslt(ResultCst.SUCCESS);
		} catch (Exception e) {
			vo.setMsg(e.getMessage());
			vo.setRslt(ResultCst.ALERT_ERROR);
		}
		
		return vo;
	}
	
	/**
	 * 检查报告信息
	 * @param deptCode
	 * @return
	 */
	@RequestMapping("/getPublishInspections")
	@ResponseBody
	public BaseMapVo getPublishInspections(String deptCode){
		BaseMapVo vo = new BaseMapVo();
		
		try {
			List<InspectionRecord> inspectionRecords = redisMsgService
					.getPublishInspections(deptCode);
			vo.addData("list", inspectionRecords);
			vo.setRslt(ResultCst.SUCCESS);
		} catch (Exception e) {
			vo.setMsg(e.getMessage());
			vo.setRslt(ResultCst.ALERT_ERROR);
		}
		
		return vo;
	}
	
	/**
	 * 病房巡视信息
	 * @param deptCode
	 * @return
	 */
	@RequestMapping("/getPublishWardPatrols")
	@ResponseBody
	public BaseMapVo getPublishWardPatrols(String deptCode){
		BaseMapVo vo = new BaseMapVo();
		
		try {
			List<WardPatrolInfo> wardPatrolInfos = redisMsgService.getPublishWardPatrols(deptCode);
			vo.addData("list", wardPatrolInfos);
			vo.setRslt(ResultCst.SUCCESS);
		} catch (Exception e) {
			vo.setMsg(e.getMessage());
			vo.setRslt(ResultCst.ALERT_ERROR);
		}
		
		return vo;
	}
	
	/**
	 * 皮试执行后
	 * @param deptCode
	 * @return
	 */
	@RequestMapping("/getPublishSkinTests")
	@ResponseBody
	public BaseMapVo getPublishSkinTests(String deptCode){
		BaseMapVo vo = new BaseMapVo();
		
		try {
			List<SkinTestInfoLx> skinTestInfoLxs = redisMsgService.
					getPublishSkinTests(deptCode);
			vo.addData("list", skinTestInfoLxs);
			vo.setRslt(ResultCst.SUCCESS);
		} catch (Exception e) {
			vo.setMsg(e.getMessage());
			vo.setRslt(ResultCst.ALERT_ERROR);
		}
		
		return vo;
	}
	
	/**
	 * 改变的医嘱信息(停止和新开)
	 * @param deptCode
	 * @return
	 */
	@RequestMapping("/getPublishChangeOrders")
	@ResponseBody
	public BaseMapVo getPublishChangeOrders(String deptCode){
		BaseMapVo vo = new BaseMapVo();
		
		try {
			List<HisOrderGroup> hisOrderGroups = redisMsgService
					.getPublishChangeOrders(deptCode);
			vo.addData("list", hisOrderGroups);
			vo.setRslt(ResultCst.SUCCESS);
		} catch (Exception e) {
			vo.setMsg(e.getMessage());
			vo.setRslt(ResultCst.ALERT_ERROR);
		}
		
		return vo;
	}
	
	@RequestMapping("/batchSaveRedisMsgRecords")
	@ResponseBody
	public BaseMapVo batchSaveRedisMsgRecords(String redisMsgRecords){
		BaseMapVo vo = new BaseMapVo();
		if(StringUtils.isBlank(redisMsgRecords)){
			vo.setMsg("保存失败,参数为空!");
			vo.setRslt(ResultCst.ALERT_ERROR);
		}
		
		try {
			Type type = new TypeToken<List<RedisMsgRecord>>(){}.getType();
			List<RedisMsgRecord> redisMsgs = GsonUtils.fromJson(redisMsgRecords, type);
			if(null == redisMsgs){
				vo.setMsg("保存失败,参数为空!");
				vo.setRslt(ResultCst.ALERT_ERROR);
			}
			
			redisMsgService.batchSaveRedisMsgRecords(redisMsgs);
			vo.setMsg("保存成功!");
			vo.setRslt(ResultCst.SUCCESS);
		} catch (Exception e) {
			LOGGER.error("RedisMsgAction batchSaveRedisMsgRecords error:" + e.getMessage());
			vo.setMsg("保存失败!");
			vo.setRslt(ResultCst.ALERT_ERROR);
		}
		return vo;
	}
	
}


