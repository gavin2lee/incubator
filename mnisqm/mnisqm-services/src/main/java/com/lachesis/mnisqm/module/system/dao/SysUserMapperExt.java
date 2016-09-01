package com.lachesis.mnisqm.module.system.dao;

import java.util.Map;

import com.lachesis.core.persistence.service.ISearchableDAO;
import com.lachesis.mnisqm.module.system.domain.SysUser;

public interface SysUserMapperExt extends ISearchableDAO {
	/**
	 * 查询登录账号查询用户信息
	 * @param param
	 * @return
	 */
	public SysUser selectSysUserByName(Map<String,Object> param);
}