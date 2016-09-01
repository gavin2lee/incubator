package com.lachesis.mnis.core.skintest.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lachesis.mnis.core.skintest.entity.SkinTestInfoLx;
import com.lachesis.mnis.core.skintest.entity.SkinTestItem;


public interface SkinTestRepository {
	/**
	 * 获取皮试信息(当前日期，护士Id，护士部门，病人Id)
	 * @param paramMap
	 * @return
	 */
	List<SkinTestInfoLx> getSkinTestInfos(Map<String, Object> paramMap);
	
	SkinTestInfoLx getSkinTestInfoByStId(String stId);
	
	/**
	 * 插入皮试相关信息
	 * @param skinTestItem
	 * @return
	 */
	int saveSkinTestItem(SkinTestItem skinTestItem);
	
	/**
	 * 保存皮试前图片
	 * @param skinTestItem
	 * @return
	 */
	int saveSkinTestItemImg(SkinTestItem skinTestItem);
	
	/**
	 * 修改皮试信息
	 * @param skinTestItem
	 * @return
	 */
	int updateSkinTestItem(SkinTestItem skinTestItem);
	
	int updateSkinTestImg(SkinTestItem skinTestItem);
	
	/**
	 * 根据皮试Id获取皮试信息
	 * @param skItemId
	 * @return
	 */
	SkinTestItem getSkinTestItemByStItemId(String stItemId);
	/**
	 * 根据医嘱id获取皮试信息
	 * @param stId
	 * @return
	 */
	SkinTestItem getSkinTestItemByStId(String stId);
	
	SkinTestItem getImgBeforeByStId(String stId);
	SkinTestItem getImgAfterByStId(String stId);
	
	/**
	 * 删除皮试为阴性的过敏信息
	 */
	public void deleteAllergy(String patId);
	
	public void updateAllergy(String patId,String drugCode);
	
	List<SkinTestInfoLx> getPublishSkinTests(HashMap<String, Object> params);
}
