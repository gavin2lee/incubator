package com.lachesis.mnis.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.lachesis.mnis.core.constants.MnisConstants;
import com.lachesis.mnis.core.util.DateUtil;
import com.lachesis.mnis.core.util.DateUtil.DateFormat;

@Controller
@RequestMapping("/nur/medicalOrder")
public class MedicalOrderAction {
	/**
	 * 进入医嘱单主页面
	 * @param id
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/{id}/medicalOrderMain")
	public String labTestRecordMain(@PathVariable String id, Model model) {
		model.addAttribute("currentDate", DateUtil.format(DateFormat.YMD));
		model.addAttribute(MnisConstants.ID, id);
		return "/nur/medicalOrder";
	}

}
