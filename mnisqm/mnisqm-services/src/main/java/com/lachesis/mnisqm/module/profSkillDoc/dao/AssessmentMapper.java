package com.lachesis.mnisqm.module.profSkillDoc.dao;

import java.util.List;

import com.lachesis.mnisqm.module.profSkillDoc.domain.Assessment;

public interface AssessmentMapper {

	/**
	 * 增加逻辑
	 * @param assessment
	 * @return
	 */
	int insert(Assessment assessment);
	
	/**
	 * 删除逻辑
	 * @param assessment
	 * @return
	 */
	int deleteByPrimaryKey(Assessment assessment);
	
	/**
	 * 更新逻辑
	 * @param assessment
	 * @return
	 */
	int update(Assessment assessment);
	
	/**
	 * 查询逻辑
	 * @param assessment
	 * @return
	 */
	List<Assessment> select(Assessment assessment);
}
