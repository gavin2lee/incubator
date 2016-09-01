package com.lachesis.mnis.core.critical.repository.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.lachesis.mnis.core.critical.entity.CriticalOperRecord;
import com.lachesis.mnis.core.critical.entity.CriticalValue;
import com.lachesis.mnis.core.critical.entity.CriticalValueRecord;
import com.lachesis.mnis.core.critical.repository.CriticalRepository;
import com.lachesis.mnis.core.identity.entity.UserInformation;
import com.lachesis.mnis.core.mybatis.mapper.CriticalServiceMapper;
import com.lachesis.mnis.core.mybatis.mapper.IdentityMapper;

@Repository("criticalRepository")
public class CriticalRepositoryImpl implements CriticalRepository {
	@Autowired
	CriticalServiceMapper criticalServiceMapper;
	
	@Autowired
	IdentityMapper identityMapper;
	
	@Override
	public List<CriticalValue> getCriticalValue(Map<String, Object> paramMap) {
		return criticalServiceMapper.getCriticalValue(paramMap);
	}

	@Override
	public CriticalValue getCriticalValueById(String criticalId) {
		return criticalServiceMapper.getCriticalValueById(criticalId);
	}

	@Override
	public int configCritical(String criticalId, String nurseId) {
		UserInformation user = identityMapper.getUserByCode(nurseId);
		return criticalServiceMapper.updateCritical(criticalId, nurseId, user.getName(), new Date());
	}

	@Override
	public Integer getCritiveRecordCount(String barcode) {
		return criticalServiceMapper.getCritiveRecordCount(barcode);
	}

	@Override
	public int insertCriticalOperRecord(CriticalOperRecord criticalOperRecord) {
		return criticalServiceMapper.insertCriticalOperRecord(criticalOperRecord);
	}

	@Override
	public List<CriticalValueRecord> getCriticalValueRecord(Map<String, Object> params) {
		return criticalServiceMapper.getCriticalValueRecord(params);
	}


}
