package com.lachesis.mnisqm.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lachesis.mnisqm.BaseDataVo;
import com.lachesis.mnisqm.BaseMapVo;
import com.lachesis.mnisqm.common.WebContextUtils;
import com.lachesis.mnisqm.constants.Constants;
import com.lachesis.mnisqm.constants.MnisQmConstants;
import com.lachesis.mnisqm.core.CommRuntimeException;
import com.lachesis.mnisqm.core.utils.StringUtils;
import com.lachesis.mnisqm.module.research.domain.ResearchItem;
import com.lachesis.mnisqm.module.research.domain.ResearchPaper;
import com.lachesis.mnisqm.module.research.service.IResearchService;
import com.lachesis.mnisqm.module.system.domain.SysUser;

@RequestMapping("/research")
@Controller
public class ResearchController {
	Logger log = LoggerFactory.getLogger(ResearchController.class);
	
	@Autowired
	private IResearchService service ;
	
	/**
	 * 查询科研项目
	 * @param paper
	 * @return
	 */
	@RequestMapping("/getResearchItems")
	public @ResponseBody BaseMapVo getResearchItems(String deptCode){
		BaseMapVo vo = new BaseMapVo();
		
		vo.addData("lst", service.getResearchItems(deptCode));
		//数据返回
		vo.setCode(Constants.Success);
		return vo;
	}
	
	/**
	 * 保存科研项目
	 * @param paper
	 * @return
	 */
	@RequestMapping("/saveResearchItem")
	public @ResponseBody BaseDataVo saveResearchItem(@RequestBody ResearchItem item){
		//数据校验
		if(item == null){
			throw new CommRuntimeException("数据不允许为空！");
		}
		if(StringUtils.isEmpty(item.getDeptCode())){
			throw new CommRuntimeException("科室不允许为空！");
		}
		
		BaseDataVo vo = new BaseDataVo();
		SysUser user = WebContextUtils.getSessionUserInfo();
		
		
		item.setCreatePerson(user.getUserCode());//创建人
		item.setUpdatePerson(user.getUserCode());//更新人
		item.setStatus(MnisQmConstants.STATUS_YX);//状态
		
		service.saveResearchItem(item);
		//数据返回
		vo.setCode(Constants.Success);
		return vo;
	}
	
	/**
	 * 删除科研项目
	 * @param paper
	 * @return
	 */
	@RequestMapping("/deleteResearchItem")
	public @ResponseBody BaseDataVo deleteResearchItem(@RequestBody ResearchItem item){
		BaseDataVo vo = new BaseDataVo();
		SysUser user= WebContextUtils.getSessionUserInfo();
		
		item.setUpdatePerson(user.getUserCode());
		item.setStatus(MnisQmConstants.STATUS_WX);
		service.deleteResearchItem(item);
		//数据返回
		vo.setCode(Constants.Success);
		return vo;
	}
	
	/**
	 * 查询科研论文
	 * @param paper
	 * @return
	 */
	@RequestMapping("/getResearchPapers")
	public @ResponseBody BaseMapVo getResearchPapers(String deptCode){
		BaseMapVo vo = new BaseMapVo();
		vo.addData("lst", service.getResearchPapers(deptCode));
		//数据返回
		vo.setCode(Constants.Success);
		return vo;
	}
	
	/**
	 * 保存科研论文
	 * @param paper
	 * @return
	 */
	@RequestMapping("/saveResearchPaper")
	public @ResponseBody BaseDataVo saveResearchPaper(@RequestBody ResearchPaper paper){
		//数据校验
		if(paper == null){
			throw new CommRuntimeException("数据为空！");
		}
		if(StringUtils.isEmpty(paper.getDeptCode())){
			throw new CommRuntimeException("科室不允许为空！");
		}
		
		
		BaseDataVo vo = new BaseDataVo();
		SysUser user = WebContextUtils.getSessionUserInfo();
		paper.setStatus(MnisQmConstants.STATUS_YX);
		paper.setUpdatePerson(user.getUserCode());
		paper.setCreatePerson(user.getUserCode());
		service.saveResearchPaper(paper);
		//数据返回
		vo.setCode(Constants.Success);
		return vo;
	}
	
	/**
	 * 删除科研论文
	 * @param paper
	 * @return
	 */
	@RequestMapping("/deleteResearchPaper")
	public @ResponseBody BaseDataVo deleteResearchPaper(@RequestBody ResearchPaper paper){
		BaseDataVo vo = new BaseDataVo();
		SysUser user = WebContextUtils.getSessionUserInfo();
		paper.setStatus(MnisQmConstants.STATUS_WX);
		paper.setUpdatePerson(user.getUserCode());
		service.deleteResearchPaper(paper);
		//数据返回
		vo.setCode(Constants.Success);
		return vo;
	}
}
