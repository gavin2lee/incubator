package com.lachesis.mnis.board;


import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.lachesis.mnis.board.dto.NwbCodeDto;
import com.lachesis.mnis.board.dto.NwbRecordDto;
import com.lachesis.mnis.board.entity.NwbHisCode;
import com.lachesis.mnis.board.entity.NwbMetadata;
import com.lachesis.mnis.board.entity.NwbTemplate;
import com.lachesis.mnis.board.nurse.service.NurseWhiteBoardNurseService;


public class NurseWhiteBoardNurseServiceTest extends BaseTest {
	@Autowired
	private NurseWhiteBoardNurseService nurseWhiteBoardNurseService;
	
	@Test
	public void testGetHisCodeMapping(){
		List<NwbHisCode>  nwbHisCodes = nurseWhiteBoardNurseService.getHisCodeMapping();
		Assert.assertNotNull(nwbHisCodes);
	}
	
	@Test
	public void testGetNwbCodeDto(){
		List<NwbCodeDto>  nwbHisCodes = nurseWhiteBoardNurseService.getNwbCodeDto("5042", "1");
		Assert.assertNotNull(nwbHisCodes);
	}
	
	@Test
	public void testGetNwbTemplates(){
		List<NwbTemplate>  templates = nurseWhiteBoardNurseService.getNwbTemplates("5042");
		Assert.assertNotNull(templates);
	}
	
	@Test
	public void testGetNwbMetadatas(){
		List<NwbMetadata>  templates = nurseWhiteBoardNurseService.getNwbMetadatas("5042","1");
		Assert.assertNotNull(templates);
	}
	
	@Test
	public void testGetNwbRecordDtos(){
		List<NwbRecordDto>  nwbRecordDtos = nurseWhiteBoardNurseService.getNwbRecordDtos("5042","1");
		Assert.assertNotNull(nwbRecordDtos);
	}
}
