package com.lachesis.mnisqm.module.research.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lachesis.mnisqm.module.research.dao.ResearchItemMapper;
import com.lachesis.mnisqm.module.research.dao.ResearchPaperMapper;
import com.lachesis.mnisqm.module.research.domain.ResearchItem;
import com.lachesis.mnisqm.module.research.domain.ResearchPaper;
import com.lachesis.mnisqm.module.research.service.IResearchService;

@Service
public class ResearchServiceImpl implements IResearchService{

	@Autowired
	private ResearchItemMapper itemMapper;
	
	@Autowired
	private ResearchPaperMapper paperMapper;

	@Override
	public List<ResearchItem> getResearchItems(String deptCode) {
		ResearchItem item = new ResearchItem();
		item.setDeptCode(deptCode);
		return itemMapper.selectItemsByDept(item);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void saveResearchItem(ResearchItem item) {
		if(item.getSeqId() == null){
			itemMapper.insert(item);
		}else{
			itemMapper.updateByPrimaryKey(item);
		}
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void deleteResearchItem(ResearchItem item) {
		itemMapper.deleteByPrimaryKey(item.getSeqId());
	}

	@Override
	public List<ResearchPaper> getResearchPapers(String deptCode) {
		ResearchPaper paper = new ResearchPaper();
		paper.setDeptCode(deptCode);
		return paperMapper.selectPaperByDept(paper);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void saveResearchPaper(ResearchPaper paper) {
		if(paper.getSeqId() ==null){
			paperMapper.insert(paper);
		}else{
			paperMapper.updateByPrimaryKey(paper);
		}
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void deleteResearchPaper(ResearchPaper paper) {
		paperMapper.deleteByPrimaryKey(paper);
	}
}