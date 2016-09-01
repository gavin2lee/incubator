package com.lachesis.mnis.web;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lachesis.mnis.core.InfusionMonitorService;
import com.lachesis.mnis.core.constants.MnisConstants;
import com.lachesis.mnis.core.exception.MnisException;
import com.lachesis.mnis.core.infusionmonitor.entity.InfusionMonitorInfo;
import com.lachesis.mnis.core.infusionmonitor.entity.InfusionMonitorRecord;
import com.lachesis.mnis.core.util.GsonUtils;
import com.lachesis.mnis.web.common.ResultCst;
import com.lachesis.mnis.web.common.vo.BaseDataVo;
import com.lachesis.mnis.web.common.vo.BaseMapVo;
import com.lachesis.mnis.web.common.vo.BaseVo;

@Controller
@RequestMapping("/nur/infusion")
public class InfusionMonitorAction  {
	static final Logger LOGGER = LoggerFactory.getLogger(InfusionMonitorAction.class);

	@Autowired
	private InfusionMonitorService infusionMonitorService;

	/**
	 * 输液巡视_获取所有输液巡视信息
	 * 
	 * @param token
	 * @param workUnitCode
	 * @param nurseCode
	 * @return
	 */
	@RequestMapping("/getInfusionMonitorList")
	@ResponseBody
	public BaseMapVo getInfusionMonitorList(
			@RequestParam(value = "workUnitCode" , required = false) String workUnitCode,
			@RequestParam(value = "nurseCode", required = false) String nurseCode,
			@RequestParam(value = "date", required = false) String date) {
		BaseMapVo vo = new BaseMapVo();

		List<InfusionMonitorInfo> list = infusionMonitorService
				.selectInfusionMonitorList(workUnitCode, nurseCode,date);
		vo.addData("list",list);
		vo.setRslt(ResultCst.SUCCESS);

		return vo;
	}

	/**
	 * 护理单据_获取输液巡视单数据
	 * 
	 * @param token
	 * @param patientId
	 * @param date
	 * @return
	 */
	/*@RequestMapping(value = ActionCst.MAPPING_GET_INFUSION_MONITOR_LOG)
	@ResponseBody
	public BaseMapVo getInfusionMonitorLog(
			@RequestParam(value = "token", required = false) String token,
			@RequestParam(value = "patientId", required = false) String patientId,
			@RequestParam(value = "date", required = false) String date) {
		BaseMapVo vo = new BaseMapVo();
		List<InfusionMonitorInfo> infList = null;
		try {
			infList = infusionMonitorService
					.getInfusionMonitor(patientId, date);
			vo.setRslt(ResultCst.SUCCESS);
		} catch (ParseException e) {
			vo.setRslt(ResultCst.ILLEGAL_DATE_PARAMETER);
		}

		vo.addData(CommonCst.VO_KEY, infList);
		return vo;
	}*/

	/**
	 * NDA获取某个医嘱的所有巡视信息
	 * 
	 * @param token
	 * @param orderExecId
	 * @return
	 */
	@RequestMapping("/getMonitorLogForOrderExec")
	@ResponseBody
	public BaseMapVo getMonitorLogForOrderExec(String token, String orderExecId) {
		BaseMapVo vo = new BaseMapVo();
		vo.setRslt(ResultCst.SUCCESS);
		vo.addData(BaseVo.VO_KEY,
				infusionMonitorService.getMonitorLogForOrderExec(orderExecId));
		return vo;
	}

	/**
	 * 输液巡视_保存输液巡视记录
	 * 
	 * @param token
	 * @param infusionMonitorInfo
	 * @return
	 */
	@RequestMapping("/saveInfusionMonitorRecord")
	@ResponseBody
	public BaseDataVo saveInfusionMonitorRecord(String infusionMonitorRecord,String endOrderExecId) {
		BaseDataVo vo = new BaseDataVo();
		
		if (StringUtils.isBlank(infusionMonitorRecord)) {
			vo.setRslt(ResultCst.NOT_NULL_PARAMETER);
			vo.setMsg("传入参数不能为空");
			return vo;
		}
		
		try {
			infusionMonitorRecord = URLDecoder.decode(infusionMonitorRecord, MnisConstants.DEFAULT_CHARSET);
		} catch (UnsupportedEncodingException e) {
			LOGGER.error("UnsupportedEncodingException", e);
			vo.setRslt(ResultCst.SYSTEM_ERROR);
			vo.setMsg("传入参数错误");
			return vo;
		}
				
		InfusionMonitorRecord infusion = GsonUtils.fromJson(infusionMonitorRecord, InfusionMonitorRecord.class);
		if (infusion == null) {
			vo.setRslt(ResultCst.NOT_NULL_PARAMETER);
			vo.setMsg("巡视对象参数不能为空");
			return vo;
		}
		InfusionMonitorRecord record = null;
		try {
			if(!StringUtils.isBlank(endOrderExecId)){
				infusionMonitorService.endInfusionMonitorByOrderExecId(endOrderExecId, 
						infusion.getRecordNurseName(), infusion.getRecordNurseId(),infusion.getRecordDate());
			}
			if(infusion.getId() == null){
				record = infusionMonitorService.addInfusionMonitorItem(infusion);
			}else{
				record = infusionMonitorService.updateInfusionMonitorItem(infusion);
			}
			if (null == record) {
				// 保存输液巡视失败
				vo.setRslt(ResultCst.SYSTEM_ERROR);
				vo.setMsg("保存输液巡视失败");
			}else{
				vo.setRslt(ResultCst.SUCCESS);
				//返回操作的巡视记录信息
				vo.setData(record);
			}
		} catch (MnisException e) {
			vo.setRslt(ResultCst.FAILURE);
			vo.setMsg(e.getMessage());
		}
		return vo;
	}
}
