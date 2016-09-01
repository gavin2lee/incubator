package com.lachesis.mnisqm.module.profSkillDoc.dao;

import java.util.List;

import com.lachesis.mnisqm.module.profSkillDoc.domain.ContinuingEducation;

public interface ContinuingEducationMapper {

	/**
	 * 增加逻辑
	 * @param continuingEducation
	 * @return
	 */
	int insert(ContinuingEducation continuingEducation);
	
	/**
	 * 删除逻辑
	 * @param continuingEducation
	 * @return
	 */
	int deleteByPrimaryKey(ContinuingEducation continuingEducation);
	
	/**
	 * 更新逻辑
	 * @param continuingEducation
	 * @return
	 */
	int update(ContinuingEducation continuingEducation);
	
	/**
	 * 查询逻辑
	 * @param continuingEducation
	 * @return
	 */
	List<ContinuingEducation> select(ContinuingEducation continuingEducation);
}
