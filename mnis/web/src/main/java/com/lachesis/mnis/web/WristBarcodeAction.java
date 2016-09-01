package com.lachesis.mnis.web;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.lachesis.mnis.core.IdentityService;
import com.lachesis.mnis.core.PatientService;
import com.lachesis.mnis.core.constants.MnisConstants;
import com.lachesis.mnis.core.identity.entity.UserInformation;
import com.lachesis.mnis.core.nursing.NurseShiftService;
import com.lachesis.mnis.core.patient.entity.ChargeType;
import com.lachesis.mnis.core.patient.entity.Patient;
import com.lachesis.mnis.core.patient.entity.WorkUnitStat;
import com.lachesis.mnis.core.util.DateUtil;
import com.lachesis.mnis.core.util.DateUtil.DateFormat;
import com.lachesis.mnis.core.util.StringUtil;
import com.lachesis.mnis.web.common.util.PatientUtils;
import com.lachesis.mnis.web.common.util.WebContextUtils;
import com.lachesis.mnis.web.vo.BedPatientInfo;
import com.lachesis.mnis.web.vo.BedPatientInfoResult;


/**
 * 腕带打印
 * @author linyun.zheng
 *
 */
@Controller
@RequestMapping("/nur/wristBarcode")
public class WristBarcodeAction{
	@Autowired
	private PatientService patientService;
	@Autowired
	private NurseShiftService nurseShiftService;
	@Autowired
	private IdentityService identityService;
	private List<BedPatientInfo> deptAllBedPatList; // 全科病床列表
	private Set<String> selectedBedPatSet; // 责任护士的病床列表
	
	
	/**
	 * 进入腕带打印主界面
	 * 
	 * @param modelMap
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/wristBarcodeMain" )
	public String wristBarcodeMain(ModelMap modelMap, HttpServletRequest request) throws Exception {
		UserInformation userInfo = WebContextUtils.getSessionUserInfo();
		deptAllBedPatList = new ArrayList<BedPatientInfo>();
		selectedBedPatSet = new HashSet<String>();
		
		if (userInfo != null) {
			BedPatientInfoResult result = getBedPatientList(1, userInfo.getDeptCode());
			if (result != null && !result.getBedInfoList().isEmpty()) {
				deptAllBedPatList.addAll(result.getBedInfoList());
				// 选择全科为关注信息
				for (BedPatientInfo bedInfo : result.getBedInfoList()) {
					selectedBedPatSet.add(bedInfo.getBedCode());
				}
				modelMap.addAttribute("selectedBedPatSet", selectedBedPatSet);
				modelMap.addAttribute("bedList", result.getBedInfoList());
				modelMap.addAttribute("workUnitStatis",
						result.getWorkUnitStatistics());
				modelMap.put("today", DateUtil.format(new Date(), DateFormat.YMD));
				modelMap.put("deptCode", userInfo.getDeptCode());
				modelMap.put("deptName", userInfo.getDeptName());
				modelMap.put("hospitalName", identityService.getHospitalName());
				modelMap.put("user", userInfo);
				modelMap.put("checked", "checked");
			}
		}
		
		return "nur/wristBarcodeMain";
	}
	
	/**
	 * 
	 * 获取床位列表
	 * 
	 * @param workUnitType
	 * @param workUnitCode
	 * @param showEmptyBeds
	 * @return
	 */
	//TODO 可以考虑移除该方法，莫名其妙
	private BedPatientInfoResult getBedPatientList(Integer workUnitType,
			String workUnitCode) {
		List<Patient> list = patientService.getWardPatientList(workUnitType, workUnitCode);
		
		//TODO stat查询出错，之后在调整
		WorkUnitStat stat = 
				patientService.getPatientStatisticByDeptCode(workUnitCode);

		return handleDataConvertDetail(list, stat);
	}
	
	/**
	 * 
	 * 根据查询的数据结果，处理相关输出，供界面数据显示
	 * 
	 * @param reult
	 * @return
	 */
	private BedPatientInfoResult handleDataConvertDetail(List<Patient> list, WorkUnitStat stat) {
		BedPatientInfoResult rst = new BedPatientInfoResult();
		List<BedPatientInfo> bedPatList = new ArrayList<BedPatientInfo>();

		String currentMinDate = DateUtil.format(DateUtil.getCurDateWithMinTime(), DateFormat.YMDHM);
		String currentMaxDate = DateUtil.format(DateUtil.getCurDateWithMaxTime(), DateFormat.YMDHM);

		BedPatientInfo bedPatientInfo = null;
		if (list != null && list.size() > 0) {
			for (Patient patient : list) {
				bedPatientInfo = PatientUtils.copyBedPatientInfoProperties(patient, currentMinDate, currentMaxDate);
				bedPatList.add(bedPatientInfo);
			}
		}

		rst.setBedInfoList(bedPatList);
		rst.setWorkUnitStatistics(stat);
		return rst;
	}

	/**
	 * 瓶签界面
	 * @return
	 */
	@RequestMapping("/labelBarcode")
	public String labelBarcode() {
		String redirectUrl = "/nur/labelBarcodeHorizontal";
		boolean isVeritalLabelBarcode = identityService.isVeritalLabelBarcode();
		
		//test
//		labelBarcodeType = "1";
		//0:横向打印;1:纵向打印
		if(isVeritalLabelBarcode){
			redirectUrl = "/nur/labelBarcodeVerital";
		}
		return redirectUrl;
	}
}
