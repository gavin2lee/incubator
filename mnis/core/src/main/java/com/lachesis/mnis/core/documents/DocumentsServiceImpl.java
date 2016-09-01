package com.lachesis.mnis.core.documents;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lachesis.mnis.core.DocumentsService;
import com.lachesis.mnis.core.OrderService;
import com.lachesis.mnis.core.documents.entity.DocumentInfo;
import com.lachesis.mnis.core.documents.repository.DocumentsRepository;
import com.lachesis.mnis.core.exception.AlertException;
import com.lachesis.mnis.core.order.repository.OrderRepository;
import com.lachesis.mnis.core.util.DateUtil;
import com.lachesis.mnis.core.util.DateUtil.DateFormat;

@Service("documentsService")
public class DocumentsServiceImpl implements DocumentsService {

	@Autowired
	private DocumentsRepository documentsRepository;
	
	@Autowired
	private OrderService orderService;

	@Override
	public List<DocumentInfo> selectLiquorDocument(String patientId, String time) {

		if (StringUtils.isEmpty(patientId)) {
			throw new AlertException("患者ID为空。");
		}

		// 默认给出当天日期
		Date queryDate = null;
		if (StringUtils.isEmpty(time)) {
			queryDate = new Date();
		} else {
			queryDate = DateUtil.parse(time.substring(0, 10), DateFormat.YMD);
		}
		Date[] dates = DateUtil.getQueryRegionDates(queryDate);
		return documentsRepository.selectLiquorDocument(patientId, dates[0],
				dates[1]);

	}

	@Override
	public List<DocumentInfo> selectPersralDocument(String patientId,
			String time) {
		if (StringUtils.isEmpty(patientId)) {
			throw new AlertException("患者ID为空。");
		}
		List<String> patIds = new ArrayList<String>();
		patIds.add(patientId);
		Date queryDate = null;
		if (StringUtils.isEmpty(time)) {
			queryDate = new Date();
		} else {
			queryDate = DateUtil.parse(time.substring(0, 10), DateFormat.YMD);
		}
		Date[] dates = DateUtil.getQueryRegionDates(queryDate);
		
		List<String> orderSubNos = orderService.getOrderSubNoFromDrugBag(null, patIds, dates[0],  dates[1]);
		
		List<DocumentInfo> documentInfos = documentsRepository.selectPersralDocument(orderSubNos,patientId, dates[0],
				dates[1]);
		
		List<DocumentInfo> drugBagDocumentInfos = documentsRepository.selectDrugBagDocument( patientId, dates[0], dates[1]);
		
		documentInfos.addAll(drugBagDocumentInfos);
		return documentInfos;
	}

}
