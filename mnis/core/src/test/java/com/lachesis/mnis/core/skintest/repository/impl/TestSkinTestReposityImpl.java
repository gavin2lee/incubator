package com.lachesis.mnis.core.skintest.repository.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
import com.lachesis.mnis.core.PatientService;
import com.lachesis.mnis.core.SpringTest;
import com.lachesis.mnis.core.skintest.entity.SkinTestInfoLx;
import com.lachesis.mnis.core.skintest.entity.SkinTestItem;
import com.lachesis.mnis.core.skintest.repository.SkinTestRepository;
import com.lachesis.mnis.core.task.TaskService;

public class TestSkinTestReposityImpl extends SpringTest {
	@Autowired
	SkinTestRepository skinTestRepository;
	@Autowired
	PatientService patientService;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(TestSkinTestReposityImpl.class);

	private Map<String, Object> paramMap = new HashMap<String, Object>();
	private SkinTestItem skinTestItem;
	private String patientId = null;
	private List<String> patientIds = new ArrayList<>();
	private String stOrderId = null;
	private Gson gson = null;

	@Before
	public void init() {
		patientId = "QA0029297_1";
		stOrderId = "IQA0029297*32*1";
		patientIds.add(patientId);
		paramMap.put("patId", patientId);
		gson = new Gson();
		
		skinTestItem = new SkinTestItem();
		skinTestItem.setDeptCode("");
		skinTestItem.setDrugCode("0102010IJ5");
		skinTestItem.setDrugName("*^头孢哌酮钠他唑巴坦钠粉针(乐灵)");
		skinTestItem.setHospNo("QA0029297");
		skinTestItem.setPatId(patientId);
		skinTestItem.setSkinTestId(stOrderId);
		skinTestItem.setSkinTestItemResult("p");
		skinTestItem.setSkinTestItemStatus("2");
	}

	@Test
	public void testInit() {
		Assert.assertNotNull(skinTestRepository);
	}

	@Test
	public void testGetSkinTestInfos() {
		List<SkinTestInfoLx> skInfos = this.skinTestRepository
				.getSkinTestInfos(paramMap);

		
		LOGGER.debug( gson.toJson(skInfos));
		Assert.assertNotNull(skInfos);
	}

	@Test
	public void testGetSkinTestByStId() {
		skinTestRepository.saveSkinTestItem(skinTestItem);
		SkinTestInfoLx skinTestInfoLx = skinTestRepository
				.getSkinTestInfoByStId(stOrderId);
		LOGGER.debug( gson.toJson(skinTestInfoLx));
		Assert.assertNotNull(skinTestInfoLx);
	}

	@Test
	public void testSaveSkinTestItem() {
		Assert.assertEquals(1,
				skinTestRepository.saveSkinTestItem(skinTestItem));
	}

	@Test
	public void testUpdateSkinTestItem() {
		skinTestRepository.saveSkinTestItem(skinTestItem);
		skinTestItem.setSkinTestItemResult("n");
		Assert.assertEquals(1,
				skinTestRepository.updateSkinTestItem(skinTestItem));
	}

	@Test
	public void testGetSkinTestItem() {
		skinTestRepository.saveSkinTestItem(skinTestItem);
		SkinTestItem item = skinTestRepository
				.getSkinTestItemByStItemId(skinTestItem.getSkinTestItemId());
		Assert.assertNotNull(item);
	}

	@Test
	public void testGetImgBeforeByStItemId() {
		skinTestRepository.saveSkinTestItem(skinTestItem);
		SkinTestItem item = skinTestRepository
				.getImgBeforeByStId(skinTestItem.getSkinTestId());
		Assert.assertNotNull(item);
	}

	@Test
	public void testGetImgAfterByStItemId() {
		skinTestRepository.saveSkinTestItem(skinTestItem);
		SkinTestItem item = skinTestRepository
				.getImgAfterByStId(skinTestItem.getSkinTestId());
		Assert.assertNotNull(item);
	}
	
	@Test
	public void testUpdateSkinTestImg(){
//		this.skinTestRepository.updateSkintTestImg(skinTestItem);
	}

}
