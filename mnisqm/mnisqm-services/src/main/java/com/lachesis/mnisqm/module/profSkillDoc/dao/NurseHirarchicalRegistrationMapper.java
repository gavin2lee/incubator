package com.lachesis.mnisqm.module.profSkillDoc.dao;

import java.util.List;

import com.lachesis.mnisqm.module.profSkillDoc.domain.NurseHirarchicalRegistration;


public interface NurseHirarchicalRegistrationMapper {

	/**
	 * 增加逻辑
	 * @param nurseHirarchicalRegistration
	 * @return
	 */
	int insert(NurseHirarchicalRegistration nurseHirarchicalRegistration);
	
	/**
	 * 删除逻辑
	 * @param nurseHirarchicalRegistration
	 * @return
	 */
	int deleteByPrimaryKey(NurseHirarchicalRegistration nurseHirarchicalRegistration);
	
	/**
	 * 更新逻辑
	 * @param nurseHirarchicalRegistration
	 * @return
	 */
	int update(NurseHirarchicalRegistration nurseHirarchicalRegistration);
	
	/**
	 * 查询逻辑
	 * @param nurseHirarchicalRegistration
	 * @return
	 */
	List<NurseHirarchicalRegistration> select(NurseHirarchicalRegistration nurseHirarchicalRegistration);
}
