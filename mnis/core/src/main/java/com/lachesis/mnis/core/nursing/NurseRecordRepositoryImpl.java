package com.lachesis.mnis.core.nursing;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.lachesis.mnis.core.constants.MnisConstants;
import com.lachesis.mnis.core.mybatis.mapper.NurseRecordMapper;

@Repository("nurseRecordRepository")
public class NurseRecordRepositoryImpl implements NurseRecordRepository {
	private static final Logger LOGGER = LoggerFactory.getLogger(NurseRecordRepositoryImpl.class);
	@Autowired
	private NurseRecordMapper nurseRecordMapper;
	
	@Override
	public int save(NurseRecord nurseRecord) {
		NurseRecordEntity nrEntity = new NurseRecordEntity();
		nrEntity.setNurseRecord(nurseRecord);
		
		if(!nrEntity.adapt() || !nrEntity.validate() ) { // 数据不符合要求
			LOGGER.error("NurseRecordEntity is does not pass validation!!");
			return MnisConstants.CODE_NULL_ARGUMENT;
		}
		
		return nurseRecordMapper.insertNurseRecord(nrEntity);
	}

	@Override
	public List<NurseRecordSpecItem> selectNurseRecordSpecItems(Object object) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<NurseRecordSpecItem> selectNurseRecordTreeItems(Object object) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int insertNurseRecord(NurseRecordEntity nrEntity) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<NurseRecordEntity> selectNurseRecord(String patientId,
			String string, String string2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteNurseRecord(String masRecordId) {
		// TODO Auto-generated method stub
		
	}

}
