package com.lachesis.mnisqm.module.system.dao;

import java.util.List;

import com.lachesis.core.persistence.service.ISearchableDAO;
import com.lachesis.mnisqm.module.system.domain.SysDic;

public interface SysDicMapperExt extends ISearchableDAO {
	
	/**
	 * 查询所有的数据库字典
	 * @return
	 */
	public List<SysDic> selectAllDics();
	
	/**
	 * 获取所有字典类型
	 * @return
	 */
	List<String> queryDicTypes();
}