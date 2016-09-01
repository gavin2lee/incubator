package com.lachesis.mnis.core.skintest.repository.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.lachesis.mnis.core.mybatis.mapper.SkinTestMapper;
import com.lachesis.mnis.core.skintest.entity.SkinTestInfoLx;
import com.lachesis.mnis.core.skintest.entity.SkinTestItem;
import com.lachesis.mnis.core.skintest.repository.SkinTestRepository;

@Repository("skinTestRepository")
public class SkinTestRepositoryImpl implements SkinTestRepository {

	@Autowired
	SkinTestMapper skinTestMapper;
	
	@Override
	public List<SkinTestInfoLx> getSkinTestInfos(Map<String, Object> paramMap) {
		return this.skinTestMapper.getSkinTestInfos(paramMap);
	}
	
	@Override
	public SkinTestInfoLx getSkinTestInfoByStId(String stId) {
		return this.skinTestMapper.getSkinTestInfoByStId(stId);
	}
	
	@Override
	public int saveSkinTestItem(SkinTestItem skinTestItem) {
		return this.skinTestMapper.saveSkinTestItem(skinTestItem);
	}
	@Override
	public int updateSkinTestItem(SkinTestItem skinTestItem) {
		return this.skinTestMapper.updateSkinTestItem(skinTestItem);
	}
	@Override
	public SkinTestItem getSkinTestItemByStItemId(String skItemId) {
		return this.skinTestMapper.getSkinTestItemByStItemId(skItemId);
	}
	
	@Override
	public int updateSkinTestImg(SkinTestItem skinTestItem){
		return this.skinTestMapper.updateSkinTestImg(skinTestItem);
	}

	@Override
	public SkinTestItem getImgBeforeByStId(String stId) {
		return this.skinTestMapper.getImgBeforeByStId(stId);
	}

	@Override
	public SkinTestItem getImgAfterByStId(String stId) {
		return this.skinTestMapper.getImgAfterByStId(stId);
	}

	@Override
	public SkinTestItem getSkinTestItemByStId(String stId) {
		return this.skinTestMapper.getSkinTestItemByStId(stId);
	}

	@Override
	public int saveSkinTestItemImg(SkinTestItem skinTestItem) {
		return this.skinTestMapper.saveSkinTestItemImg(skinTestItem);
	}

	@Override
	public void deleteAllergy(String patId) {
		skinTestMapper.deleteAllergy(patId);
	}
	
	public void updateAllergy(String patId,String drugCode){
		skinTestMapper.updateAllergy(patId,drugCode);
	}

	@Override
	public List<SkinTestInfoLx> getPublishSkinTests(
			HashMap<String, Object> params) {
		return skinTestMapper.getPublishSkinTests(params);
	}
}
