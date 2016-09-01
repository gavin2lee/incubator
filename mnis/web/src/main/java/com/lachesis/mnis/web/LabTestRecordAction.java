/**
 * (C) Copyright. LACHESIS All rights reserved.
 *
 */
package com.lachesis.mnis.web;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lachesis.mnis.core.LabTestService;
import com.lachesis.mnis.core.constants.MnisConstants;
import com.lachesis.mnis.core.exception.AlertException;
import com.lachesis.mnis.core.identity.entity.UserInformation;
import com.lachesis.mnis.core.labtest.entity.LabTestSendRecord;
import com.lachesis.mnis.core.labtest.entity.LabTestSendStatistic;
import com.lachesis.mnis.core.util.DateUtil;
import com.lachesis.mnis.core.util.DateUtil.DateFormat;
import com.lachesis.mnis.web.common.ResultCst;
import com.lachesis.mnis.web.common.util.WebContextUtils;
import com.lachesis.mnis.web.common.vo.BaseMapVo;
import com.lachesis.mnis.web.common.vo.BaseVo;


/**
 * The class LabTestRecordAction.
 * 
 * 查询检验报告
 *
 * @author: yanhui.wang
 * @since: 2014-6-23	
 * @version: $Revision$ $Date$ $LastChangedBy$
 *
 */
@Controller
@RequestMapping("/nur/labTestRecord")
public class LabTestRecordAction {
	static final Logger LOGGER = LoggerFactory.getLogger(LabTestRecordAction.class);

	@Autowired
	private LabTestService labTestService;
	
	/**
	 * 
	 * 进入检验报告主界面
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/labTestRecordMain")
	public String labTestRecordMain(String patientId, Model model) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" >>>>>>>>>>>>> redirect to lab test record maintiance page.");
		}

		model.addAttribute("currentDate", DateUtil.format(DateFormat.YMD));
		model.addAttribute(MnisConstants.ID, patientId);
		return "/nur/labTestRecord";
	}
	
	/**
	 * 
	 * 查询患者当天或者全部的检验报告
	 *
	 */
	@RequestMapping("/labTestRecords")
	@ResponseBody
	public BaseMapVo getLabTestRecordsForNda(
			@RequestParam(value = "token", required = true) String token, 
			@RequestParam(value = "date", required = false) String date, 
			@RequestParam(value = "patId", required = false) String patId){
		BaseMapVo vo = new BaseMapVo();
		vo.addData(BaseVo.VO_KEY, labTestService.getLabTestRecord(date, patId,null,null));
		vo.setRslt(ResultCst.SUCCESS);
		return vo;
	}
	
	/**
	 * 根据起始时间，检验状态查询检验报告
	 * 
	 * @param patientIds 患者编号或者条码号,多个以,号分隔
	 * @param startDate 执行开始日期
	 * @param endDate  执行结束日期
	 * @param status
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/getLabTestRdsByCond") 
	public @ResponseBody BaseMapVo getLabTestRdsByCond(String patientIds, String startDate, String endDate, String status) {
		BaseMapVo vo = new BaseMapVo();
		if(StringUtils.isBlank(startDate)){
			vo.setMsg("起始时间不能为空");
			vo.setMsg(ResultCst.INVALID_PARAMETER);
			return vo;
		}
		if(StringUtils.isBlank(endDate)){
			vo.setMsg("终止时间不能为空");
			vo.setMsg(ResultCst.INVALID_PARAMETER);
			return vo;
		}
		if(StringUtils.isBlank(patientIds)){
			vo.setMsg("病人ID不能为空");
			vo.setMsg(ResultCst.INVALID_PARAMETER);
			return vo;
		}
		vo.addData(BaseVo.VO_KEY, labTestService.getLabTestRecord(startDate, endDate, patientIds,status,null));
		vo.setRslt(ResultCst.SUCCESS);
		return vo;	
	}
	
	/**
	 * 更新检验报告优先标志
	 * @param id
	 * @param priFlag
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/setLabTestRdsPriFlag")
	public @ResponseBody BaseVo setLabTestRdsPriFlag(String id, Integer priFlag){
		BaseVo vo = new BaseVo();
		if(labTestService.updateLabTestRecordPriFlag(id,priFlag)>0){
			vo.setRslt(ResultCst.SUCCESS);
		}else{
			vo.setRslt(ResultCst.FAILURE);
		}
		
		return vo;
	}
	
	/**
	 * 待送检统计
	 * @param deptCode
	 * @return
	 */
	@RequestMapping("/getLabTestSendStatistic")
	@ResponseBody
	public BaseMapVo getLabTestSendStatistic(String deptCode,String startDate,String endDate,boolean isAll){
		BaseMapVo vo = new BaseMapVo();
		List<LabTestSendStatistic> labTestSendStatistics;
		try {
			labTestSendStatistics = labTestService.getLabTestSendStatistic(deptCode, startDate, endDate, isAll);
			vo.addData("statistic", labTestSendStatistics);
			vo.setRslt(ResultCst.SUCCESS);
		} 
		catch(AlertException e){
			LOGGER.error("getLabTestSendStatistic error:" + e.getMessage());
			vo.setMsg(e.getMessage());
			vo.setRslt(ResultCst.ALERT_ERROR);
		}
		catch (Exception e) {
			LOGGER.error("getLabTestSendStatistic error:" + e.getMessage());
			vo.setMsg("获取失败!");
			vo.setRslt(ResultCst.ALERT_ERROR);

		}
		
		return vo;
	}
	
	/**
	 * 根据条码执行送检
	 * @param barcode
	 * @return
	 */
	@RequestMapping("/execLabTestSendByBarcode")
	@ResponseBody
	public BaseMapVo execLabTestSendByBarcode(String barcode){
		BaseMapVo vo = new BaseMapVo();
		
		UserInformation user = WebContextUtils.getSessionUserInfo();
		List<LabTestSendRecord> records;
		try {
			records = labTestService.execLabTestSendByBarcode(barcode, user);
			vo.addData("records", records);
			vo.setRslt(ResultCst.SUCCESS);
		} catch(AlertException e){
			LOGGER.error("getLabTestSendStatistic error:" + e.getMessage());
			vo.setMsg(e.getMessage());
			vo.setRslt(ResultCst.ALERT_ERROR);
		}
		catch (Exception e) {
			LOGGER.error("getLabTestSendStatistic error:" + e.getMessage());
			vo.setMsg("执行失败!");
			vo.setRslt(ResultCst.ALERT_ERROR);

		}
		
		return vo;
	}
	
	/**
	 * 查询科室所有送检信息
	 * @param deptCode
	 * @return
	 */
	@RequestMapping("/getLabTestSendRecords")
	@ResponseBody
	public BaseMapVo getLabTestSendRecords(String deptCode,String startDate,String endDate,boolean isAll){
		BaseMapVo vo = new BaseMapVo();
		List<LabTestSendRecord> records;
		List<LabTestSendStatistic> labTestSendStatistics;
		try {
			records = labTestService.getLabTestSendRecords(deptCode, null, startDate, endDate, isAll);
			labTestSendStatistics = labTestService.getLabTestSendStatistic(deptCode, startDate, endDate, isAll);
			vo.addData("statistic", labTestSendStatistics);
			vo.addData("records", records);
			vo.setRslt(ResultCst.SUCCESS);
		} catch(AlertException e){
			LOGGER.error("getLabTestSendStatistic error:" + e.getMessage());
			vo.setMsg(e.getMessage());
			vo.setRslt(ResultCst.ALERT_ERROR);
		}
		catch (Exception e) {
			LOGGER.error("getLabTestSendStatistic error:" + e.getMessage());
			vo.setMsg("获取失败!");
			vo.setRslt(ResultCst.ALERT_ERROR);

		}
		return vo;
	}

}
