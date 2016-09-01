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

import com.lachesis.mnis.core.InspectionService;
import com.lachesis.mnis.core.constants.MnisConstants;
import com.lachesis.mnis.core.inspection.entity.InspectionRecord;
import com.lachesis.mnis.core.util.DateUtil;
import com.lachesis.mnis.core.util.DateUtil.DateFormat;
import com.lachesis.mnis.web.common.ResultCst;
import com.lachesis.mnis.web.common.vo.BaseMapVo;
import com.lachesis.mnis.web.common.vo.BaseVo;


/**
 * The class InspectionReportAction.
 * 
 * 查询检查报告
 *
 * @author: yanhui.wang
 * @since: 2014-6-23	
 * @version: $Revision$ $Date$ $LastChangedBy$
 *
 */
@Controller
@RequestMapping("/nur/inspectionRpt")
public class InspectionReportAction{
	static final Logger LOGGER = LoggerFactory.getLogger(InspectionReportAction.class);

	@Autowired
	private InspectionService inspectionSypacsService;
	
	/**
	 * 
	 * 进入检查报告主界面
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/inspectionRptMain")
	public String inspectionRptMain(String patientId, Model model) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" >>>>>>>>>>>>> redirect to inspection report maintiance page.");
		}

		model.addAttribute("currentDate", DateUtil.format(DateFormat.YMD));
		model.addAttribute(MnisConstants.ID, patientId);
		
		return "/nur/inspectionRptMain";
	}
	
	
	/**
	 * 检查报告
	 * @param token
	 * @param date
	 * @param patId
	 * @return
	 */
	@RequestMapping("/inspectionRecords")
	@ResponseBody
	public BaseMapVo getInspectionRecordsForNda(
			@RequestParam(value = "token", required = true) String token,
			@RequestParam(value = "date", required = false) String date, 
			@RequestParam(value = "patId", required = false) String patId){
		BaseMapVo vo = new BaseMapVo();
		if(StringUtils.isBlank(patId)){
			vo.setMsg("病人ID不能为空");
			vo.setMsg(ResultCst.INVALID_PARAMETER);
			return vo;
		}
		List<InspectionRecord> list =  inspectionSypacsService.getInspectionRecords(date, patId);
		
		vo.addData(BaseVo.VO_KEY, list);
		vo.setRslt(ResultCst.SUCCESS);
		return vo;
	}
	
	/**
	 * 
	 * 查询患者当天或者全部的检验报告
	 * 
	 * @param patientIds 患者编号或者条码号，多个之间用逗号分隔
	 * @param date 查询当前或者当天的日期（允许为空，为空时查询所有记录）
	 * @param status 查询状态
	 * @return
	 * @throws Exception
	 */
	/*@RequestMapping(value = ActionCst.MAPPING_GET_INSPECTION_RECORDS)
	public @ResponseBody Json getInspectionRecords(String patientIds, String date,String status) throws Exception {
		Json json = new Json();
		
		List<InspectionRecord> result = inspectionSypacsService.getInspectionRecordByPatients(patientIds.split(","),date, date, status);
		json.setLst(result);
		json.setSuccess(true);
		return json;
	}*/

	/**
	 * 根据起始时间，检验状态查询检验报告
	 * 
	 * @param patientIds 患者编号或者条码号，多个之间用逗号分隔
	 * @param startDate 执行开始日期
	 * @param endDate  执行结束日期
	 * @param status 查询状态
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/getInspectionRdsByCond") 
	@ResponseBody 
	public BaseMapVo getInspectionRdsByCond(String patientIds, String startDate, String endDate, String status){
		BaseMapVo vo = new BaseMapVo();
		if(StringUtils.isBlank(startDate)){
			vo.setMsg("起始时间不能为空");
			vo.setRslt(ResultCst.INVALID_PARAMETER);
			return vo;
		}
		if(StringUtils.isBlank(endDate)){
			vo.setMsg("终止时间不能为空");
			vo.setRslt(ResultCst.INVALID_PARAMETER);
			return vo;
		}
		if(StringUtils.isBlank(patientIds)){
			vo.setMsg("病人ID不能为空");
			vo.setRslt(ResultCst.INVALID_PARAMETER);
			return vo;
		}
		vo.addData(BaseVo.VO_KEY, inspectionSypacsService.getInspectionRecords(startDate, endDate, patientIds));
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
	@RequestMapping("/setInspectionRdsPriFlag")
	public @ResponseBody BaseVo setInspectionRdsPriFlag(String id, Integer priFlag) {
		BaseVo vo = new BaseVo();
		if(inspectionSypacsService.updateInspectionPriFlag(id,priFlag) > 0){
			vo.setRslt(ResultCst.SUCCESS);
		}else{
			vo.setRslt(ResultCst.FAILURE);
		}
		
		return vo;
	}
}
