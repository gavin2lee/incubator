package com.lachesis.mnisqm.module.qualityForm.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lachesis.mnisqm.module.qualityForm.domain.QualityTeam;

public interface QualityTeamMapper {
	
	/**
	 * 插入
	 * @param qualityTeam
	 * @return
	 */
	int insert(QualityTeam qualityTeam);
	
	/**
	 * 根据组名查找质控小组
	 * @param teamName
	 * @return
	 */
	List<QualityTeam> selectByTeamCode(@Param(value="teamCode") String teamCode,
			@Param(value="userCode") String userCode);
	
	/**
	 * 更新质控小组
	 * @param qualityTeam
	 * @return
	 */
	int update(QualityTeam qualityTeam);
	
	/**
	 * 删除质控小组
	 * @param seqId
	 * @return
	 */
	int deleteByPrimaryKey(QualityTeam qualityTeam);
	
	List<QualityTeam> selectUniqueTeamCode();
}
