package com.lachesis.mnis.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lachesis.mnis.core.duty.DutyService;
import com.lachesis.mnis.web.common.ResultCst;
import com.lachesis.mnis.web.common.vo.BaseListVo;

@Controller
@RequestMapping("/nur/duty")
public class DutyAction {

	@Autowired
	private DutyService dutyService;

	/**
	 * 查询值班信息
	 * 
	 * @return
	 */
	@RequestMapping("/list")
	public @ResponseBody
	BaseListVo dutyList(@RequestParam(value = "deptId", required = true) String deptId) {
		BaseListVo vo = new BaseListVo();
		vo.setList(dutyService.getDutyByDeptId(deptId));
		vo.setRslt(ResultCst.SUCCESS);
		return vo;
	}
	
	@RequestMapping(value = "/setDuty")
	public @ResponseBody String setPhoneInfo(
			@RequestParam(value = "zbys") String zbys,
			@RequestParam(value = "zbhs") String zbhs,
			@RequestParam(value = "zbhszdh") String zbhszdh,
			@RequestParam(value = "bwkdh") String bwkdh,
			@RequestParam(value = "xzzbdh") String xzzbdh,
			@RequestParam(value = "zbysdh") String zbysdh,
			@RequestParam(value = "zbhsdh") String zbhsdh,
			@RequestParam(value = "hlbdh") String hlbdh,
			@RequestParam(value = "sssdh") String sssdh) {
		dutyService.setUserName(zbys, "d_dc");
		dutyService.setTel(zbysdh, "d_dc");
		dutyService.setUserName(zbhs, "d_nr");
		dutyService.setTel(zbhsdh, "d_nr");
		dutyService.setTel(zbhszdh, "d_nL");
		dutyService.setTel(hlbdh, "h_nd");
		dutyService.setTel(bwkdh, "h_pt");
		dutyService.setTel(sssdh, "h_op");
		dutyService.setTel(xzzbdh, "h_ad");
		return "保存成功！";
	}
}
