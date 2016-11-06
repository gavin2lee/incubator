package com.harmazing.openbridge.sys.config.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.harmazing.framework.common.dao.IBaseMapper;
import com.harmazing.openbridge.sys.config.model.SysConfig;

public interface SysConfigMapper extends IBaseMapper {
	@Select("select * from sys_config")
	List<SysConfig> getAllConfig();

	@Delete("delete from sys_config")
	void cleanConfig();

	@Insert("insert into sys_config(conf_id,conf_key,conf_value) values(#{confId},#{confKey},#{confValue})")
	void insertConfig(SysConfig conifg);

	@Update("update sys_config set conf_key=#{confKey},conf_value=#{confValue} where conf_id=#{confId}")
	void updateConfig(SysConfig conifg);

	@Select("select * from sys_config where conf_id=#{confId}")
	SysConfig selectConfig(@Param("confId") String confId);

	@Select("select * from sys_core_config")
	List<Map<String, String>> getSysCoreConfig();

	@Update("update sys_core_config set `value`=#{value} where `key`=#{key}")
	void updateSysCoreConfig(@Param("key") String key,
			@Param("value") String value);

	@Insert("insert into sys_core_config(`key`,`value`) values(#{key},#{value})")
	void saveSysCoreConfig(@Param("key") String key,
			@Param("value") String value);
}
