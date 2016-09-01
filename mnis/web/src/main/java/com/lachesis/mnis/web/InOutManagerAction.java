package com.lachesis.mnis.web;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lachesis.mnis.core.InOutManagerService;
import com.lachesis.mnis.core.inoutmanager.entity.InOutManager;
import com.lachesis.mnis.core.inoutmanager.entity.InOutManagerStatistic;
import com.lachesis.mnis.core.util.DateUtil;
import com.lachesis.mnis.core.util.GsonUtils;
import com.lachesis.mnis.web.common.ResultCst;
import com.lachesis.mnis.web.common.vo.BaseDataVo;
import com.lachesis.mnis.web.common.vo.BaseMapVo;


/**
 * 出入量管理
 * @author ThinkPad
 *
 */
@Controller
@RequestMapping("/nur/inOutManager")
public class InOutManagerAction{
	@Autowired
	private InOutManagerService inOutManagerService;
	
	/**
	 * 进入出入量界面
	 * 
	 * @param modelMap
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/inOutManagerMain" )
	public String inOutManagerMain(ModelMap modelMap, HttpServletRequest request) throws Exception {
		return "nur/inOutManagerMain";
	}
	
	
	@RequestMapping("/getInOutManagers")
	@ResponseBody
	public BaseMapVo getInOutManagers(String patId,String deptCode,String startTime,String endTime){
		BaseMapVo vo = new BaseMapVo();
		
		Date startDate = null;
		Date endDate = null;
		if(!StringUtils.isBlank(startTime) && !StringUtils.isBlank(endTime)){
			startDate = DateUtil.parse(startTime);
			endDate = DateUtil.parse(endTime);
		}
		
		List<InOutManager> inOutManagers = inOutManagerService.getInOutManagers(patId, deptCode, startDate, endDate);
		vo.addData("list",inOutManagers);
		InOutManagerStatistic inOutManagerStatistic = inOutManagerService.getInOutManagerStatistic(patId, deptCode, startDate, endDate);
		vo.addData("inOutStatic", inOutManagerStatistic);
		vo.setRslt(ResultCst.SUCCESS);
		return vo;
	}
	
	@RequestMapping("/getInOutManagerStatistic")
	@ResponseBody
	public BaseDataVo getInOutManagerStatistic(String patId,String deptCode,String startTime,String endTime){
		BaseDataVo vo = new BaseDataVo();
		
		Date startDate = null;
		Date endDate = null;
		if(!StringUtils.isBlank(startTime) && !StringUtils.isBlank(endTime)){
			startDate = DateUtil.parse(startTime);
			endDate = DateUtil.parse(endTime);
		}
		
		InOutManagerStatistic inOutManagerStatistic = inOutManagerService.getInOutManagerStatistic(patId, deptCode, startDate, endDate);
		vo.setData(inOutManagerStatistic);
		vo.setRslt(ResultCst.SUCCESS);
		return vo;
	}
	
	@RequestMapping("/getInOutManagerById")
	@ResponseBody
	public BaseDataVo getInOutManagerById(int id){
		BaseDataVo vo = new BaseDataVo();
		InOutManager inOutManager = inOutManagerService.getInOutManagerById(id);
		vo.setData(inOutManager);
		vo.setRslt(ResultCst.SUCCESS);
		return vo;
	}
	
	@RequestMapping("/saveInOutManager")
	@ResponseBody
	public BaseDataVo saveInOutManager(String inOutManager,boolean isSave){
		BaseDataVo vo = new BaseDataVo();
		
		if(StringUtils.isBlank(inOutManager)){
			vo.setRslt(ResultCst.FAILURE);
			vo.setMsg("请录入出入量!");
			return vo;
		}
		
		InOutManager inOutManagerObject = GsonUtils.fromJson(inOutManager, InOutManager.class);
		if(null == inOutManagerObject){
			vo.setRslt(ResultCst.FAILURE);
			vo.setMsg("请录入出入量!");
			return vo;
		}
		
		if(isSave){
			inOutManagerService.insertInOutManager(inOutManagerObject);
			
		}else{
			inOutManagerService.updateInOutManager(inOutManagerObject);
			
		}
		vo.setData(inOutManagerService.getInOutManagerById(inOutManagerObject.getId()));
		vo.setMsg("保存成功!");
		vo.setRslt(ResultCst.SUCCESS);
		return vo;
	}
	
	@RequestMapping("/deleteInOutManagerById")
	@ResponseBody
	public BaseDataVo deleteInOutManagerById(int id){
		BaseDataVo vo = new BaseDataVo();
		int deleteCount = inOutManagerService.deleteById(id);
		if(deleteCount > 0){
			vo.setRslt(ResultCst.SUCCESS);
			vo.setMsg("删除成功!");
		}else{
			vo.setRslt(ResultCst.FAILURE);
			vo.setMsg("删除失败");
		}
		
		return vo;
	}
	
	/**
	 * 获取性状和颜色字典
	 * @return
	 */
	@RequestMapping("/getOutDics")
	@ResponseBody
	public BaseMapVo getOutDics(){
		BaseMapVo vo = new BaseMapVo();
		Map<String, String> shapeDics = inOutManagerService.getOutDics(0);
		Map<String, String> colorDics = inOutManagerService.getOutDics(1);
		
		vo.addData("shapeDics", shapeDics);
		vo.addData("colorDics", colorDics);
		vo.setRslt(ResultCst.SUCCESS);
		return vo;
	}
	
}
