package com.lachesis.mnisqm.module.system.dao;

import java.util.List;
import java.util.Map;

import com.lachesis.core.persistence.service.ISearchableDAO;
import com.lachesis.mnisqm.module.system.domain.SysFunction;

public interface SysFunctionMapperExt extends ISearchableDAO {
	public List<SysFunction> selectFunByRole(Map<String,Object> param);
	
	/**
	 * 角色未有功能菜单
	 * @param param
	 * @return
	 */
	public List<SysFunction> selectNotHaveRole(Map<String,Object> param);
}