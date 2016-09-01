package com.lachesis.mnisqm.module.profSkillDoc.dao;

import java.util.List;

import com.lachesis.mnisqm.module.profSkillDoc.domain.CprAcls;


public interface CprAclsMapper {
	
	/**
	 * 增加逻辑
	 * @param cprAcls
	 * @return
	 */
	int insert(CprAcls cprAcls);
	
	/**
	 * 删除逻辑
	 * @param cprAcls
	 * @return
	 */
	int deleteByPrimaryKey(CprAcls cprAcls);
	
	/**
	 * 更新逻辑
	 * @param cprAcls
	 * @return
	 */
	int update(CprAcls cprAcls);
	
	/**
	 * 查询逻辑
	 * @param cprAcls
	 * @return
	 */
	List<CprAcls> select(CprAcls cprAcls);

}
