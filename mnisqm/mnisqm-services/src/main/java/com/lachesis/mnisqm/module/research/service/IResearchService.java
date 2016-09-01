package com.lachesis.mnisqm.module.research.service;

import java.util.List;

import com.lachesis.mnisqm.module.research.domain.ResearchItem;
import com.lachesis.mnisqm.module.research.domain.ResearchPaper;

public interface IResearchService {
	/**
	 * 获取科研项目
	 * @param deptCode
	 * @return
	 */
	public List<ResearchItem> getResearchItems(String deptCode);
	
	/**
	 * 保存科研项目
	 * @param item
	 */
	public void saveResearchItem(ResearchItem item);
	
	/**
	 * 删除科研项目
	 * @param item
	 */
	public void deleteResearchItem(ResearchItem item);
	
	/**
	 * 查询科研论文
	 * @param deptCode
	 * @return
	 */
	public List<ResearchPaper> getResearchPapers(String deptCode);
	
	/**
	 * 保存科研论文
	 * @param paper
	 */
	public void saveResearchPaper(ResearchPaper paper);
	
	/**
	 * 删除科研论文
	 * @param paper
	 */
	public void deleteResearchPaper(ResearchPaper paper);
}
