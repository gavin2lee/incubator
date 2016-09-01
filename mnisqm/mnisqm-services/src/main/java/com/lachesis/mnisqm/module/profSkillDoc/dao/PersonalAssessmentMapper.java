package com.lachesis.mnisqm.module.profSkillDoc.dao;

import java.util.List;

import com.lachesis.mnisqm.module.profSkillDoc.domain.PersonalAssessment;

public interface PersonalAssessmentMapper {

	/**
	 * 增加逻辑
	 * @param personalAssessment
	 * @return
	 */
	int insert(PersonalAssessment personalAssessment);
	
	/**
	 * 删除逻辑
	 * @param personalAssessment
	 * @return
	 */
	int deleteByPrimaryKey(PersonalAssessment personalAssessment);
	
	/**
	 * 更新逻辑
	 * @param personalAssessment
	 * @return
	 */
	int update(PersonalAssessment personalAssessment);
	
	/**
	 * 查询逻辑
	 * @param personalAssessment
	 * @return
	 */
	List<PersonalAssessment> select(PersonalAssessment personalAssessment);
}
