package com.lachesis.mnisqm.module.profSkillDoc.dao;

import java.util.List;

import com.lachesis.mnisqm.module.profSkillDoc.domain.AcademicConference;

public interface AcademicConferenceMapper {
	
	/**
	 * 增加逻辑
	 * @param academicConference
	 * @return
	 */
	int insert(AcademicConference academicConference);
	
	/**
	 * 删除逻辑
	 * @param academicConference
	 * @return
	 */
	int deleteByPrimaryKey(AcademicConference academicConference);
	
	/**
	 * 更新逻辑
	 * @param academicConference
	 * @return
	 */
	int update(AcademicConference academicConference);
	
	/**
	 * 查询逻辑
	 * @param academicConference
	 * @return
	 */
	List<AcademicConference> select(AcademicConference academicConference);
}
