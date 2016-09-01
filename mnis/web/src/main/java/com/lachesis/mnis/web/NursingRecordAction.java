/**
 * (C) Copyright. LACHESIS All rights reserved.
 *
 */
package com.lachesis.mnis.web;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lachesis.mnis.core.identity.entity.UserInformation;
import com.lachesis.mnis.core.nursing.NurseRecord;
import com.lachesis.mnis.core.nursing.NurseRecordService;
import com.lachesis.mnis.web.common.ResultCst;
import com.lachesis.mnis.web.common.util.WebContextUtils;
import com.lachesis.mnis.web.common.vo.BaseDataVo;
import com.lachesis.mnis.web.common.vo.BaseVo;

/**
 * The class NursingRecordAction.
 * 
 *  护理记录
 *  
 * @author: yanhui.wang
 * @since: 2014-6-24	
 * @version: $Revision$ $Date$ $LastChangedBy$
 *
 */
@Controller
@RequestMapping("/nur/nursingRecord")
public class NursingRecordAction {
	static final Logger LOGGER = LoggerFactory.getLogger(NursingRecordAction.class);

	@Autowired
	private NurseRecordService nurseRecordService;

	/**
	 * 
	 * 进入护理记录主界面
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/nursingRecord")
	public String patientGlanceMain() {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" >>>>>>>>>>>>> redirect to nursing record maintiance page.");
		}

		return "/nur/nursingRecord";
	}

	/**
	 * 根据查询日期，患者编号查询护理记录列表
	 * 
	 * @param date 查询日期
	 * @param patientId 患者编号 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/getNurseRecord")
	public @ResponseBody
	BaseDataVo getNurseRecord(@RequestParam String date, @RequestParam String patientId) throws Exception {
		BaseDataVo vo = new BaseDataVo();
		vo.setData(nurseRecordService.getNurseRecord(date, patientId));
		vo.setRslt(ResultCst.SUCCESS);
		return vo;
	}

	/**
	 * 根据当前登录用户所在的科室，获取录入护理记录的数据
	 * 
	 * @param workUnitCode 部门编号
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/getNurseRecordConfig")
	public @ResponseBody
	BaseDataVo getNurseRecordConfig(@RequestParam(required = false) String workUnitCode) throws Exception {
		BaseDataVo vo = new BaseDataVo();

		if (StringUtils.isEmpty(workUnitCode)) {
			UserInformation sessionUserInfo = WebContextUtils.getSessionUserInfo();
			workUnitCode = sessionUserInfo.getDeptCode();
		}
		vo.setData(nurseRecordService.getNurseRecordConfig(workUnitCode));
		vo.setRslt(ResultCst.SUCCESS);
		return vo;
	}

	/**
	 * 保存护理记录
	 * 
	 * @param record
	 * @param isModify
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/saveNurseRecord")
	public @ResponseBody
	BaseVo saveNurseRecord(@RequestParam NurseRecord record, Boolean isModify) throws Exception {
		BaseVo vo = new BaseVo();
		if (nurseRecordService.saveNurseRecord(record, isModify) <= 0) {
			vo.setRslt(ResultCst.FAILURE);
			vo.setMsg("传入的护理记录有误");
			LOGGER.error(vo.getMsg()); 
		} else {
			vo.setRslt(ResultCst.SUCCESS);
		}
		
		return vo;
	}
}
