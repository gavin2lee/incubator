package com.lachesis.mnis.core.nurseWhiteBoard.service.impl;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.gson.Gson;
import com.lachesis.mnis.core.NurseWhiteBoardService;
import com.lachesis.mnis.core.SpringTest;
import com.lachesis.mnis.core.identity.entity.SysOperate;
import com.lachesis.mnis.core.whiteBoard.NurseWhiteBoardUtil;
import com.lachesis.mnis.core.whiteBoard.entity.NurseWhiteBoardInBo;
import com.lachesis.mnis.core.whiteBoard.entity.NurseWhiteBoardItemInBo;
import com.lachesis.mnis.core.whiteBoard.entity.NurseWhiteBoardMetadata;
import com.lachesis.mnis.core.whiteBoard.entity.NurseWhiteBoardRecord;
import com.lachesis.mnis.core.whiteBoard.entity.NurseWhiteBoardRecordInfo;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class NurseWhiteBoardServiceImplTest  {

	@Autowired
	private NurseWhiteBoardService nurseWhiteBoardService;
	
	private List<NurseWhiteBoardInBo> setNurseWhiteBoardInBo() {

		List<NurseWhiteBoardMetadata> nurseWhiteBoardMetadatas = nurseWhiteBoardService
				.getNurseWhiteBoardTopMetadatas("5042", null);
		List<NurseWhiteBoardInBo> nurseWhiteBoardInBos = new ArrayList<NurseWhiteBoardInBo>();
		for (NurseWhiteBoardMetadata nurseWhiteBoardMetadata : nurseWhiteBoardMetadatas) {
			NurseWhiteBoardInBo nurseWhiteBoardInBo = new NurseWhiteBoardInBo();
			nurseWhiteBoardInBo.setDeptCode("5042");
			nurseWhiteBoardInBo
					.setRecordCode(nurseWhiteBoardMetadata.getCode());
			nurseWhiteBoardInBo
					.setRecordName(nurseWhiteBoardMetadata.getName());
			nurseWhiteBoardInBo.setRecordNurseCode("111");
			nurseWhiteBoardInBo.setRecordNurseName("明");
			nurseWhiteBoardInBo.setRecordDate("2015-12-15");

			List<String> patientItemList = Arrays.asList(new String[] { "cy",
					"ry", "bw", "bz", "bodysign", "bloodGlu", "in-output",
					"urine", "abdominalCir", "weight" });

			List<NurseWhiteBoardItemInBo> nurseWhiteBoardItemInBoList1 = new ArrayList<NurseWhiteBoardItemInBo>();
			// 患者
			if (patientItemList.contains(nurseWhiteBoardMetadata.getCode())) {

				NurseWhiteBoardItemInBo nurseWhiteBoardItemInBo1 = new NurseWhiteBoardItemInBo();
				nurseWhiteBoardItemInBo1.setItemCode("patient");
				nurseWhiteBoardItemInBo1.setItemName("患者");
				List<String> patIds = new ArrayList<String>();
				patIds.add("p1-01床|p2-02床|p3-03床|p4-04床");
				patIds.add("p5-05床|p6-06床");
				nurseWhiteBoardItemInBo1.setItemValues(patIds);
				nurseWhiteBoardItemInBoList1.add(nurseWhiteBoardItemInBo1);

				if( "bloodGlu".equals(nurseWhiteBoardMetadata.getCode())){
					NurseWhiteBoardItemInBo nurseWhiteBoardItemInBo2 = new NurseWhiteBoardItemInBo();
					nurseWhiteBoardItemInBo2.setItemCode("content");
					nurseWhiteBoardItemInBo2.setItemName("内容");
					List<String> contents = new ArrayList<String>();
					contents.add("三餐前,三餐后");
					contents.add("睡前 bid,空腹 tid");
					nurseWhiteBoardItemInBo2.setItemValues(contents);
					nurseWhiteBoardItemInBoList1.add(nurseWhiteBoardItemInBo2);
				}

				
				// item
				if ("bodysign".equals(nurseWhiteBoardMetadata.getCode())) {
					NurseWhiteBoardItemInBo nurseWhiteBoardItemInBo3 = new NurseWhiteBoardItemInBo();
					nurseWhiteBoardItemInBo3.setItemCode("item");
					nurseWhiteBoardItemInBo3.setItemName("体征项目");
					List<String> items = new ArrayList<String>();
					items.add("breathe|pulse");
					items.add("puls");
					nurseWhiteBoardItemInBo3.setItemValues(items);
					nurseWhiteBoardItemInBoList1.add(nurseWhiteBoardItemInBo3);

					NurseWhiteBoardItemInBo nurseWhiteBoardItemInBo4 = new NurseWhiteBoardItemInBo();
					nurseWhiteBoardItemInBo4.setItemCode("freq");
					nurseWhiteBoardItemInBo4.setItemName("频次");
					List<String> freqs = new ArrayList<String>();
					freqs.add("QD");
					freqs.add("tid");
					nurseWhiteBoardItemInBo4.setItemValues(freqs);
					nurseWhiteBoardItemInBoList1.add(nurseWhiteBoardItemInBo4);
				}
			} else {
				NurseWhiteBoardItemInBo nurseWhiteBoardItemInBo2 = new NurseWhiteBoardItemInBo();
				nurseWhiteBoardItemInBo2.setItemCode("content");
				nurseWhiteBoardItemInBo2.setItemName("内容");
				List<String> contents = new ArrayList<String>();
				contents.add(nurseWhiteBoardMetadata
						.getName());
				contents.add(nurseWhiteBoardMetadata
						.getName() + 1);
				nurseWhiteBoardItemInBo2.setItemValues(contents);
				nurseWhiteBoardItemInBoList1.add(nurseWhiteBoardItemInBo2);
			}

			nurseWhiteBoardInBo.setItemList(nurseWhiteBoardItemInBoList1);

			nurseWhiteBoardInBos.add(nurseWhiteBoardInBo);
		}

		return nurseWhiteBoardInBos;
	}
	
	@Test
	public void testInsertNurseWhiteBoardRecordInfos(){
		List<NurseWhiteBoardInBo> nurseWhiteBoardInBos = setNurseWhiteBoardInBo();
		
		int saveCount = nurseWhiteBoardService.batchSaveNurseWhiteBoardRecord(nurseWhiteBoardInBos,true,null,null);
		
		Assert.assertTrue(saveCount >=1 ? true:false);
	}
	
	@Test
	public void testGetNurseWhiteBoardRecord(){
		List<NurseWhiteBoardRecord> nurseWhiteBoardRecords = nurseWhiteBoardService
				.getNurseWhiteBoardRecords("H250005",null, null,null);
		for (NurseWhiteBoardRecord nurseWhiteBoardRecord : nurseWhiteBoardRecords) {
//			NurseWhiteBoardUtil.clearNurseWhiteBoardRecordItemInfos(nurseWhiteBoardRecord.getNurseWhiteBoardRecordInfos());
		}
//		nurseWhiteBoardBaseRecords = clearNurseWhiteBoardRecordItems(nurseWhiteBoardBaseRecords);
		Gson gson = new Gson();
		String gsonString = gson.toJson(nurseWhiteBoardRecords);
		System.out.println(gsonString);
		Assert.assertNotNull(gsonString);
	}
	

	
	@Test
	public void testSaveNwbMetadata(){
		
		int saveCount = nurseWhiteBoardService.saveNwbMetadata(getNurseWhiteBoardMetadata(),true);
		Assert.assertEquals(1, saveCount);
		
	}
	
	@Test
	public void testUpdateNwbMetadata(){
//		nurseWhiteBoardService.saveNwbMetadata(getNurseWhiteBoardMetadata());
		int saveCount = nurseWhiteBoardService.updateNwbMetadata(getNurseWhiteBoardMetadata());
		Assert.assertEquals(1, saveCount);
		
	}
	
	@Test
	public void testDeleteNwbMetadataById(){
//		nurseWhiteBoardRepository.saveNwbMetadata(getNurseWhiteBoardMetadata());
		int saveCount = nurseWhiteBoardService.deleteNwbMetadataById("1");
		Assert.assertEquals(1, saveCount);
		
	}	
	
	
	private NurseWhiteBoardMetadata getNurseWhiteBoardMetadata(){
		String jsonString = "{ \"id\": 13,\"parentId\": \"\",\"deptCode\": \"5042\",\"code\": \"pCount\",\"name\": \"病人总数\", \"level\": 0,\"inputType\": \"TEXT\",\"isAuto\": true,\"isShow\": false,\"rowNo\": 1,\"columnNo\": 1,\"columnType\": \"0\",\"showType\": \"singleText\", \"isEdit\": false,\"isBedCode\": true,\"isColspan\": false,\"nurseWhiteBoardEditTypes\": [{\"id\": \"1\", \"code\": \"\",\"type\": \"text\",\"isMulti\": false,\"isInner\": false,\"isBedCode\": true}],\"items\": []}";
		Gson gson = new Gson();
		NurseWhiteBoardMetadata nurseWhiteBoardMetadata = gson.fromJson(jsonString, NurseWhiteBoardMetadata.class);
		return nurseWhiteBoardMetadata;
	}
	
}
