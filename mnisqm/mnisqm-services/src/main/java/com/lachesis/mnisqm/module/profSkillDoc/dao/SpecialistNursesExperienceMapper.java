package com.lachesis.mnisqm.module.profSkillDoc.dao;

import java.util.List;

import com.lachesis.mnisqm.module.profSkillDoc.domain.SpecialistNursesExperience;

public interface SpecialistNursesExperienceMapper {

	/**
	 * 增加逻辑
	 * @param specialistNursesExperience
	 * @return
	 */
	int insert(SpecialistNursesExperience specialistNursesExperience);
	
	/**
	 * 删除逻辑
	 * @param specialistNursesExperience
	 * @return
	 */
	int deleteByPrimaryKey(SpecialistNursesExperience specialistNursesExperience);
	
	/**
	 * 更新逻辑
	 * @param specialistNursesExperience
	 * @return
	 */
	int update(SpecialistNursesExperience specialistNursesExperience);
	
	/**
	 * 查询逻辑
	 * @param specialistNursesExperience
	 * @return
	 */
	List<SpecialistNursesExperience> select(SpecialistNursesExperience specialistNursesExperience);
}
