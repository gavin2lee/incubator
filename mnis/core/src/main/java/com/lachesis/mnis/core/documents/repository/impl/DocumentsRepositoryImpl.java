package com.lachesis.mnis.core.documents.repository.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.lachesis.mnis.core.documents.entity.DocumentInfo;
import com.lachesis.mnis.core.documents.repository.DocumentsRepository;
import com.lachesis.mnis.core.mybatis.mapper.DocumentsMapper;

@Repository("documentsRepository")
public class DocumentsRepositoryImpl implements DocumentsRepository{
	
	@Autowired
	private DocumentsMapper documentsMapper;

	@Override
	public List<DocumentInfo> selectLiquorDocument(String patientId,
			Date startDate,Date endDate) {
		return documentsMapper.selectLiquorDocument(patientId, startDate,endDate);
	}

	@Override
	public List<DocumentInfo> selectPersralDocument(List<String> orderSubNos,String patientId,
			Date startDate,Date endDate) {
		return documentsMapper.selectPersralDocument(orderSubNos,patientId, startDate,endDate);
	}

	@Override
	public List<DocumentInfo> selectDrugBagDocument(
			String patientId, Date startDate, Date endDate) {
		return documentsMapper.selectDrugBagDocument( patientId, startDate, endDate);
	}

}
