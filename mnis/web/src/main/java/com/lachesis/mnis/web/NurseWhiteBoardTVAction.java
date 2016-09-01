package com.lachesis.mnis.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lachesis.mnis.core.IdentityService;
import com.lachesis.mnis.core.NurseWhiteBoardService;
import com.lachesis.mnis.core.PatientService;
import com.lachesis.mnis.core.constants.MnisConstants;
import com.lachesis.mnis.core.exception.AlertException;
import com.lachesis.mnis.core.identity.entity.SysDept;
import com.lachesis.mnis.core.nursing.NurseShiftService;
import com.lachesis.mnis.core.patient.entity.Patient;
import com.lachesis.mnis.core.patient.entity.WorkUnitStat;
import com.lachesis.mnis.core.util.DateUtil;
import com.lachesis.mnis.core.util.SuperCacheUtil;
import com.lachesis.mnis.core.util.DateUtil.DateFormat;
import com.lachesis.mnis.core.whiteBoard.entity.NurseWhiteBoardMetadata;
import com.lachesis.mnis.core.whiteBoard.entity.NurseWhiteBoardTemplate;
import com.lachesis.mnis.core.whiteBoard.entity.NurseWhiteBoardMetadataTV;
import com.lachesis.mnis.core.whiteBoard.entity.NurseWhiteBoardRecord;
import com.lachesis.mnis.web.common.ResultCst;
import com.lachesis.mnis.web.common.util.PatientUtils;
import com.lachesis.mnis.web.common.vo.BaseMapVo;
import com.lachesis.mnis.web.vo.BedPatientInfo;
import com.lachesis.mnis.web.vo.BedPatientInfoResult;

/**
 * 小白板,任务清单
 * 
 * @author ThinkPad
 *
 */
@Controller
@RequestMapping("/nur/nurseWhiteBoardTV")
public class NurseWhiteBoardTVAction {
	static final Logger log = Logger.getLogger(NurseWhiteBoardAction.class);
	@Autowired
	private NurseWhiteBoardService nurseWhiteBoardService;

	@Autowired
	private IdentityService identityService;

	@Autowired
	private PatientService patientService;
	@Autowired
	private NurseShiftService nurseShiftService;

	@RequestMapping(value = "/noteTVShow")
	public String noteShow(String deptCode,ModelMap model, HttpServletRequest request)
			throws Exception {
		List<SysDept> depts = identityService.getDeptmentsByCode(null, deptCode);
		String deptName = "";
		if(null != depts && depts.size()>0){
			deptName = depts.get(0).getName();
		}
		Set<String> beds = null;
		// BedPatientInfoResult 完全没有存在的必要，真心复杂
		BedPatientInfoResult result = getBedPatientList(1, deptCode);

		List<BedPatientInfo> bedList = result.getBedInfoList();
		if (bedList != null && bedList.size() > 0) {
			beds = new HashSet<>(bedList.size());
			// 选择全科为关注信息
			for (BedPatientInfo bedInfo : bedList) {
				beds.add(bedInfo.getBedCode());
			}
			model.addAttribute("selectedBedPatSet", beds);
			model.addAttribute("deptCode", deptCode);
			model.addAttribute("deptName", deptName);
			model.addAttribute("bedList", bedList);
			model.addAttribute("workUnitStatis", result.getWorkUnitStatistics());

			model.addAttribute("hospitalName",
					identityService.getHospitalName());
		}
		Date date = new Date();
		String today = DateUtil.format(date, DateFormat.YMDC)
				+ MnisConstants.EMPTY + DateUtil.getWeek(date);
		model.addAttribute("today", today);
		model.addAttribute("currentMinDate", DateUtil.format(
				DateUtil.getCurDateWithMinTime(), DateFormat.YMDHM));
		model.addAttribute("currentMaxDate", DateUtil.format(
				DateUtil.getCurDateWithMaxTime(), DateFormat.YMDHM));

		// TODO 传入界面需要的数据
		return "/nur/noteTVShow";
	}
	
	/**
	 * 根据部门和指定item类型获取元数据
	 * 
	 * @param deptCode
	 * @param code
	 * @return
	 */
	@RequestMapping(value = "/getNwbTVDatas")
	@ResponseBody
	public BaseMapVo getNurseWhiteBoardDatas(String deptCode, String code) {
		BaseMapVo vo = new BaseMapVo();
		if (StringUtils.isBlank(deptCode)) {
			throw new AlertException("部门参数不能为空!");
		}
		List<NurseWhiteBoardMetadata> nurseWhiteBoardMetadatas = nurseWhiteBoardService
				.getNurseWhiteBoardMetadatas(deptCode, code,false);

		Map<String,Object> metaValues = nurseWhiteBoardService.getNurseWhiteBoardMetaValues();
		
		List<NurseWhiteBoardRecord> nurseWhiteBoardRecords = nurseWhiteBoardService
				.getNurseWhiteBoardDynamicRecords(deptCode,code);
		
		Date date = new Date();
		String today = DateUtil.format(date, DateFormat.YMDC) + MnisConstants.EMPTY + DateUtil.getWeek(date);
		vo.addData("metadatas", nurseWhiteBoardMetadatas);
		vo.addData("freq", metaValues);
		vo.addData("records", nurseWhiteBoardRecords);
		vo.addData("today", today);
		vo.setRslt(ResultCst.SUCCESS);
		return vo;
	}

	// 可以考虑移除该方法，莫名其妙
	private BedPatientInfoResult getBedPatientList(Integer workUnitType,
			String workUnitCode) {
		List<Patient> list = patientService.getWardPatientList(workUnitType,
				workUnitCode);

		// stat查询出错，之后在调整
		WorkUnitStat stat = patientService
				.getPatientStatisticByDeptCode(workUnitCode);
		// nurseShiftService.calcWorkUnitStatistics(workUnitType, workUnitCode,
		// 0);

		return handleDataConvertDetail(list, stat);
	}

	/**
	 * 
	 * 根据查询的数据结果，处理相关输出，供界面数据显示
	 * 
	 * @param reult
	 * @return
	 */
	private BedPatientInfoResult handleDataConvertDetail(List<Patient> list,
			WorkUnitStat stat) {
		BedPatientInfoResult rst = new BedPatientInfoResult();
		List<BedPatientInfo> bedPatList = new ArrayList<BedPatientInfo>();

		String currentMinDate = DateUtil.format(
				DateUtil.getCurDateWithMinTime(), DateFormat.YMDHM);
		String currentMaxDate = DateUtil.format(
				DateUtil.getCurDateWithMaxTime(), DateFormat.YMDHM);

		BedPatientInfo bedPatientInfo = null;
		if (list != null && list.size() > 0) {
			for (Patient patient : list) {
				bedPatientInfo = PatientUtils.copyBedPatientInfoProperties(
						patient, currentMinDate, currentMaxDate);
				bedPatList.add(bedPatientInfo);
			}
		}

		rst.setBedInfoList(bedPatList);
		rst.setWorkUnitStatistics(stat);
		return rst;
	}
	
	/**
	 * 根据部门和指定item类型获取元数据
	 * 
	 * @param deptCode
	 * @param code
	 * @return
	 */
	@RequestMapping(value = "/getNwbTVDataList")
	@ResponseBody
	public BaseMapVo getNurseWhiteBoardTVDatas(String deptCode, String templateId,int dataType) {
		BaseMapVo vo = new BaseMapVo();
		if (StringUtils.isBlank(deptCode)) {
			throw new AlertException("部门参数不能为空!");
		}
		
		List<NurseWhiteBoardMetadataTV> nurseWhiteBoardMetadatas = null;
		List<NurseWhiteBoardTemplate> masters = null;
		List<NurseWhiteBoardRecord> nurseWhiteBoardRecords = null;
		
		switch (dataType) {
		case 0:
			nurseWhiteBoardMetadatas = nurseWhiteBoardService
				.getNurseWhiteBoardTVMetadatas(deptCode, templateId , false);
			masters = nurseWhiteBoardService.getNwbTemplates(deptCode, templateId , false);
			vo.addData("metadatas", nurseWhiteBoardMetadatas);
			vo.addData("masters", masters);
			break;
		case 1:
			Date date = new Date();
			String dateStr = DateUtil.format(date,DateFormat.FULL);
			//上一次查询时间
			String preQueryOrderDate =(String) SuperCacheUtil.CACHE_DATA.
					get(MnisConstants.QUERY_NWB_DATE + deptCode);
			
			if( null == preQueryOrderDate){
				preQueryOrderDate = dateStr;
			}
			SuperCacheUtil.CACHE_DATA.put(MnisConstants.PRE_QUERY_NWB_DATE + deptCode, 
					preQueryOrderDate);
			
			SuperCacheUtil.CACHE_DATA.put(MnisConstants.QUERY_NWB_DATE + deptCode, 
					dateStr);
			
			nurseWhiteBoardRecords = nurseWhiteBoardService
				.getNurseWhiteBoardDynamicRecords(deptCode,null);
			vo.addData("records", nurseWhiteBoardRecords);
			break;
		default:
			break;
		}
		vo.setRslt(ResultCst.SUCCESS);
		return vo;
	}
	
}