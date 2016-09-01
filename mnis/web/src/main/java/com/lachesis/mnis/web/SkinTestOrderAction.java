package com.lachesis.mnis.web;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lachesis.mnis.core.IdentityService;
import com.lachesis.mnis.core.SkinTestService;
import com.lachesis.mnis.core.barcode.BarcodeService;
import com.lachesis.mnis.core.barcode.InpatientWithOrders;
import com.lachesis.mnis.core.constants.MnisConstants;
import com.lachesis.mnis.core.exception.AlertException;
import com.lachesis.mnis.core.identity.entity.UserInformation;
import com.lachesis.mnis.core.skintest.entity.SkinTestInfoLx;
import com.lachesis.mnis.core.skintest.entity.SkinTestItem;
import com.lachesis.mnis.core.util.GsonUtils;
import com.lachesis.mnis.core.util.StringUtil;
import com.lachesis.mnis.web.common.ResultCst;
import com.lachesis.mnis.web.common.util.WebContextUtils;
import com.lachesis.mnis.web.common.vo.BaseMapVo;

@Controller
@RequestMapping("/nur/skinTestOrder")
public class SkinTestOrderAction {

	@Autowired
	SkinTestService skinTestService;
	@Autowired
	private IdentityService identityService;
	@Autowired
	private BarcodeService barcodeService;

	/**
	 * 根据病人Id或扫描barCode获取皮试医嘱列表信息
	 * 
	 * @param patId
	 * @param barcode
	 * @param typeCode
	 *            '0'或null为病人patId，'1'为扫描barCode
	 * @return
	 */
	@RequestMapping("/getSkinTestInofsForPatId")
	@ResponseBody
	public BaseMapVo getSkinTestInfosForNdaByPatientId(
			@RequestParam(value = "patId", required = false) String patId,
			@RequestParam(value = "barcode", required = false) String barcode,
			@RequestParam(value = "typeCode", required = false) String typeCode,
			@RequestParam(value = "selectDate", required = false) String selectDate) {
		BaseMapVo vo = new BaseMapVo();
		if (StringUtils.isNotBlank(typeCode) && "1".equals(typeCode)) {
			// 条码进入
			if (StringUtils.isBlank(barcode)) {
				vo.setRslt(ResultCst.INVALID_PARAMETER);
				vo.setMsg("病人条码不能为空-barcodeId");
				return vo;
			}
			// 获取条码对应的patId
			InpatientWithOrders inpatientWithOrders = barcodeService
					.getPatientInfoByBarcode(barcode);

			if (inpatientWithOrders == null
					|| inpatientWithOrders.getInpatientInfo() == null) {
				vo.setRslt(ResultCst.ALERT_ERROR);
				vo.setMsg("没有此患者");
				return vo;
			}
			patId = inpatientWithOrders.getInpatientInfo().getPatId();
		}

		List<SkinTestInfoLx> list = skinTestService.getSkinTestInfos(patId,
				null, null, selectDate);

		vo.addData("list", list);
		vo.setRslt(ResultCst.SUCCESS);
		return vo;
	}

	/**
	 * 根据护士或科室Id获取皮试医嘱列表信息
	 * 
	 * @param date
	 * @param patId
	 * @return
	 */
	@RequestMapping("/getSkinTestInfoForOrder")
	@ResponseBody
	public BaseMapVo getSkinTestInfosForNda(
			@RequestParam(value = "nurseId", required = false) String nurseId,
			@RequestParam(value = "deptId", required = false) String deptId,
			@RequestParam(value = "selectDate", required = false) String selectDate) {
		List<SkinTestInfoLx> list = skinTestService.getSkinTestInfos(null,
				nurseId, deptId, selectDate);
		BaseMapVo vo = new BaseMapVo();
		vo.addData("list", list);
		vo.setRslt(ResultCst.SUCCESS);
		return vo;
	}

	/**
	 * 执行皮试
	 * 
	 * @param stId
	 * @param execNurseId
	 * @param execNurseName
	 * @param stBeforeImg
	 * @param testDate医嘱开立时间
	 * @return
	 */
	@RequestMapping("/execSkinTestInfo")
	@ResponseBody
	public BaseMapVo execSkinTestForNda(
			@RequestParam(value = "skinTestItem", required = false) String skinTestItem,
			@RequestParam(value = "stBeforeImg", required = false) String stBeforeImg,
			@RequestParam(value = "testDate", required = false) String testDate) {
		BaseMapVo vo = new BaseMapVo();

		SkinTestItem skinTestItemObj = GsonUtils.fromJson(skinTestItem,
				SkinTestItem.class);
		if (skinTestItemObj == null) {
			vo.setRslt(ResultCst.FAILURE);
			vo.setMsg("传入的皮试信息为NULL！");
			return vo;
		}
		UserInformation sessionUserInfo = WebContextUtils.getSessionUserInfo();
		try {
			skinTestService.execSkinTestInfo(skinTestItemObj,
					sessionUserInfo.getCode(), sessionUserInfo.getName(),
					stBeforeImg);
			List<SkinTestInfoLx> skinTestInfoLxs = skinTestService
					.getSkinTestInfos(skinTestItemObj.getPatId(), null, null,
							testDate);
			vo.addData("list", skinTestInfoLxs);
			vo.setRslt(ResultCst.SUCCESS);
		} catch (AlertException e) {
			vo.setRslt(ResultCst.ALERT_ERROR);
			vo.setMsg(e.getMessage());
		}
		return vo;
	}

	/**
	 * 确认皮试
	 * 
	 * @param skinTestItem
	 * @param patientId
	 * @param approveUserLoginName
	 * @param approveNursePwd
	 * @param approveTypeCode
	 *            确认类型0或null为密码，1为指纹
	 * @param stImgAfter
	 * @param testDate医嘱开立时间
	 * @return
	 */
	@RequestMapping("/approveSkinTestInfo")
	@ResponseBody
	public BaseMapVo approveSkinTestForNda(
			@RequestParam(value = "skinTestItem", required = false) String skinTestItem,
			@RequestParam(value = "patientId", required = false) String patientId,
			@RequestParam(value = "approveUserLoginName", required = false) String approveUserLoginName,
			@RequestParam(value = "approveNursePwd", required = false) String approveNursePwd,
			@RequestParam(value = "approveTypeCode", required = false) int approveTypeCode,
			@RequestParam(value = "stImgAfter", required = false) String stImgAfter,
			@RequestParam(value = "testDate", required = false) String testDate) {

		BaseMapVo vo = new BaseMapVo();

		SkinTestItem skinTestItemObj = GsonUtils.fromJson(skinTestItem,
				SkinTestItem.class);
		if (skinTestItemObj == null) {
			vo.setRslt(ResultCst.FAILURE);
			vo.setMsg("传入的皮试信息为NULL!");
			return vo;
		}
		UserInformation sessionUserInfo = WebContextUtils.getSessionUserInfo(); 

		try {
			int execType = skinTestService.approveSkinTestInfo(skinTestItemObj, stImgAfter,
					sessionUserInfo,approveUserLoginName,approveNursePwd);
			
			if(execType == MnisConstants.DOUBLE_CHECK_PASSOWRD_ERR){
				vo.setRslt(ResultCst.NOT_EXIST_USER);
				vo.setMsg("用户名或密码错误!");
				return vo;
			}else if(execType == MnisConstants.DOUBLE_CHECK_NOT_SAME_DEPT_ERR){
				vo.setRslt(ResultCst.NOT_EXIST_USER);
				vo.setMsg("双核护士部门不一致!");
				return vo;
			}else if(execType == MnisConstants.DOUBLE_CHECK_SAME_NURSER_ERR){
				vo.setRslt(ResultCst.NOT_EXIST_USER);
				vo.setMsg("双核不能为同一护士!");
				return vo;
			}
			
			List<SkinTestInfoLx> skinTestInfoLxs = skinTestService
					.getSkinTestInfos(patientId, null, null, testDate);
			vo.addData("list", skinTestInfoLxs);
			vo.setRslt(ResultCst.SUCCESS);
		} catch (AlertException e) {
			vo.setRslt(ResultCst.ALERT_ERROR);
			vo.setMsg("皮试无法确认");
		}

		return vo;
	}

	/**
	 * 修改皮试图片
	 * 
	 * @param stItemId
	 * @param imageString
	 * @param imageType
	 * @return
	 */
	@RequestMapping("/updateSkinTestImg")
	@ResponseBody
	public BaseMapVo updateSkinTestImg(
			@RequestParam(value = "patId", required = false) String patId,
			@RequestParam(value = "stItemId", required = false) String stItemId,
			@RequestParam(value = "imageString", required = false) String imageString,
			@RequestParam(value = "imageType", required = false) int imageType) {
		BaseMapVo vo = new BaseMapVo();
		try {
			skinTestService.updateSkinTestImg(patId, stItemId, imageString,
					imageType);
			vo.setRslt(ResultCst.SUCCESS);
		} catch (AlertException e) {
			vo.setRslt(ResultCst.ALERT_ERROR);
			vo.setMsg(e.getMessage());
		}
		return vo;
	}

	/**
	 * 获取皮试图片信息
	 * 
	 * @param stId
	 * @param imageNo
	 *            0或不传表示皮试前图片，1表示皮试后图片
	 * @return
	 */
	@RequestMapping("/getSkinTestImg")
	@ResponseBody
	public void getSkinTestImg(
			@RequestParam(value = "stId", required = false) String stId,
			@RequestParam(value = "imageNo", required = false) String imageNo,
			HttpServletResponse response) {
		byte[] imageData = skinTestService.getSkinTestItemImg(stId, imageNo);

		response.setContentType("image/jpeg");
		response.setCharacterEncoding("UTF-8");
		try {
			OutputStream outputSream = response.getOutputStream();
			InputStream in = new ByteArrayInputStream(imageData);
			int len = 0;
			byte[] buf = new byte[1024];
			while ((len = in.read(buf, 0, 1024)) != -1) {
				outputSream.write(buf, 0, len);
			}
			outputSream.close();
		} catch (IOException e) {
			throw new AlertException("图片显示异常!");
		}

	}

	/**
	 * Nda获取皮试图片信息
	 * 
	 * @param stId
	 * @param imageNo
	 *            0或不传表示皮试前图片，1表示皮试后图片
	 * @return
	 */
	@RequestMapping("/getSkinTestImgForNDA")
	@ResponseBody
	public BaseMapVo getSkinTestImgForNDA(
			@RequestParam(value = "stId", required = false) String stId,
			@RequestParam(value = "imageNo", required = false) String imageNo,
			HttpServletResponse response) {
		byte[] imageData = skinTestService.getSkinTestItemImg(stId, imageNo);

		BaseMapVo vo = new BaseMapVo();
		String data = "";
		if (imageData != null) {
			data = StringUtil.byteToString(imageData);
		}

		vo.addData("data", data);
		vo.setRslt(ResultCst.SUCCESS);
		return vo;
	}
}
