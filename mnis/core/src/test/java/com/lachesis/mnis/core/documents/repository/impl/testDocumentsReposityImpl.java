package com.lachesis.mnis.core.documents.repository.impl;

import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.lachesis.mnis.core.SpringTest;
import com.lachesis.mnis.core.documents.entity.DocumentInfo;
import com.lachesis.mnis.core.documents.repository.DocumentsRepository;
import com.lachesis.mnis.core.util.DateUtil;
import com.lachesis.mnis.core.util.DateUtil.DateFormat;

public class testDocumentsReposityImpl extends SpringTest {

	private String patId = null;
	private Date startDate = null;
	private Date endDate = null;

	@Autowired
	DocumentsRepository documentsRepository;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(testDocumentsReposityImpl.class);

	@Before
	public void init() {
		patId = "ZA1001803_1";
		startDate = DateUtil.setDateToDay(new Date());
		endDate = DateUtil.setNextDayToDate(startDate);
	}

	@Test
	public void testInit() {
		Assert.assertNotNull(documentsRepository);
	}

	@Test
	public void testLiquor() {
		List<DocumentInfo> items = this.documentsRepository
				.selectLiquorDocument(patId, startDate, endDate);
		Assert.assertNotNull(items);
	}

	@Test
	public void testPersral() {
		List<DocumentInfo> items = this.documentsRepository
				.selectPersralDocument(null,patId, startDate, endDate);
		Assert.assertNotNull(items);
	}

}
