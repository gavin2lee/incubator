package com.lachesis.mnisqm.module.profSkillDoc.dao;

import java.util.List;

import com.lachesis.mnisqm.module.profSkillDoc.domain.AwardPunishmentInfo;


public interface AwardPunishmentInfoMapper {

	/**
	 * 增加逻辑
	 * @param awardPunishmentInfo
	 * @return
	 */
	int insert(AwardPunishmentInfo awardPunishmentInfo);
	
	/**
	 * 删除逻辑
	 * @param awardPunishmentInfo
	 * @return
	 */
	int deleteByPrimaryKey(AwardPunishmentInfo awardPunishmentInfo);
	
	/**
	 * 更新逻辑
	 * @param awardPunishmentInfo
	 * @return
	 */
	int update(AwardPunishmentInfo awardPunishmentInfo);
	
	/**
	 * 查询逻辑
	 * @param awardPunishmentInfo
	 * @return
	 */
	List<AwardPunishmentInfo> select(AwardPunishmentInfo awardPunishmentInfo);
}
