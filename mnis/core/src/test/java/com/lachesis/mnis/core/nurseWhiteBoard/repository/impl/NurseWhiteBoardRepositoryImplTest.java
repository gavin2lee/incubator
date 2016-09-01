package com.lachesis.mnis.core.nurseWhiteBoard.repository.impl;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
import com.lachesis.mnis.core.SpringTest;
import com.lachesis.mnis.core.whiteBoard.entity.NurseWhiteBoardRecord;
import com.lachesis.mnis.core.whiteBoard.entity.NurseWhiteBoardMetadata;
import com.lachesis.mnis.core.whiteBoard.entity.NurseWhiteBoardRecordInfo;
import com.lachesis.mnis.core.whiteBoard.entity.NurseWhiteBoardRecordItem;
import com.lachesis.mnis.core.whiteBoard.entity.NurseWhiteBoardRecordItemInfo;
import com.lachesis.mnis.core.whiteBoard.repository.NurseWhiteBoardRepository;

import javax.swing.plaf.ProgressBarUI;

public class NurseWhiteBoardRepositoryImplTest extends SpringTest {
	@Autowired
	private NurseWhiteBoardRepository nurseWhiteBoardRepository;
	String deptCode = null;
	String nurseWhiteBoardTemplateString = null;
	NurseWhiteBoardRecordInfo nurseWhiteBoardRecordInfo = null;
	List<NurseWhiteBoardRecordItemInfo> nurseWhiteBoardRecordItemInfos = new ArrayList<NurseWhiteBoardRecordItemInfo>();

	List<NurseWhiteBoardMetadata> nurseWhiteBoardMetadatas = new ArrayList<NurseWhiteBoardMetadata>();

	private List<NurseWhiteBoardRecordItemInfo> setNurseWhiteBoardRecordItemInfos() {
		NurseWhiteBoardRecordItemInfo nurseWhiteBoardRecordItemInfo1 = new NurseWhiteBoardRecordItemInfo();
		nurseWhiteBoardRecordItemInfo1.setRecordItemCode("weight");
		nurseWhiteBoardRecordItemInfo1.setRecordItemDate(new Date());
		nurseWhiteBoardRecordItemInfo1.setRecordItemName("体重");
		nurseWhiteBoardRecordItemInfo1.setRecordItemValue("75");

		NurseWhiteBoardRecordItemInfo nurseWhiteBoardRecordItemInfo2 = new NurseWhiteBoardRecordItemInfo();
		nurseWhiteBoardRecordItemInfo2.setRecordId(1);
		nurseWhiteBoardRecordItemInfo2.setRecordItemCode("height");
		nurseWhiteBoardRecordItemInfo2.setRecordItemDate(new Date());
		nurseWhiteBoardRecordItemInfo2.setRecordItemName("身高");
		nurseWhiteBoardRecordItemInfo2.setRecordItemValue("175");

		nurseWhiteBoardRecordItemInfos.add(nurseWhiteBoardRecordItemInfo1);
		nurseWhiteBoardRecordItemInfos.add(nurseWhiteBoardRecordItemInfo2);
		return nurseWhiteBoardRecordItemInfos;
	}

	private NurseWhiteBoardRecordInfo setNurseWhiteBoardRecordInfo() {
		NurseWhiteBoardRecordInfo nurseWhiteBoardRecordInfo = new NurseWhiteBoardRecordInfo();
		nurseWhiteBoardRecordInfo.setDeptCode("H250005");
		nurseWhiteBoardRecordInfo.setOrderNo(1);
		nurseWhiteBoardRecordInfo.setPatId("1234");
		nurseWhiteBoardRecordInfo.setRecordCode("tizheng");
		nurseWhiteBoardRecordInfo.setRecordName("体征");
		nurseWhiteBoardRecordInfo.setRecordValue("506-王 weight,height 8:00,16:00");
		nurseWhiteBoardRecordInfo.setRecordDate(new Date());
		nurseWhiteBoardRecordInfo.setRecordNurseName("唐..");
		nurseWhiteBoardRecordInfo.setRecordNurseCode("tsl1");
		nurseWhiteBoardRecordInfo
				.setNurseWhiteBoardRecordItemInfos(setNurseWhiteBoardRecordItemInfos());
		return nurseWhiteBoardRecordInfo;
	}
	
	@Before
	public void init() {
		deptCode = "H250005";
		nurseWhiteBoardRecordInfo = setNurseWhiteBoardRecordInfo();
		nurseWhiteBoardRecordItemInfos.clear();
		nurseWhiteBoardRecordItemInfos = setNurseWhiteBoardRecordItemInfos();
	}

	/********************************* 记录实现方法 ********************************************************/
	
	@Test
	public void testGetNurseWhiteBoardRecords() {
		List<NurseWhiteBoardRecord> nurseWhiteBoardRecords = nurseWhiteBoardRepository
				.getNurseWhiteBoardRecords("5042", null, null,null, null);
		for (NurseWhiteBoardRecord nurseWhiteBoardRecord : nurseWhiteBoardRecords) {
//			NurseWhiteBoardUtil.clearNurseWhiteBoardRecordItemInfos(nurseWhiteBoardRecord.getNurseWhiteBoardRecordInfos());
		}
//		nurseWhiteBoardBaseRecords = clearNurseWhiteBoardRecordItems(nurseWhiteBoardBaseRecords);
		Gson gson = new Gson();
		String gsonString = gson.toJson(nurseWhiteBoardRecords);
		writeStrToFile(gsonString,"D:/getNurseWhiteBoardRecords.txt");
		Assert.assertNotNull(gsonString);
	}



	@Test
	public void testInsertNurseWhiteBoardRecordInfo() {
		
		int insertCount = nurseWhiteBoardRepository
				.insertNurseWhiteBoardRecordInfo(nurseWhiteBoardRecordInfo);
		Assert.assertEquals(1, insertCount);
	}

	@Test
	public void testInsertNurseWhiteBoardRecordInfoAndItems() {
		int insertCount = nurseWhiteBoardRepository
				.insertNurseWhiteBoardRecordInfoAndItems(nurseWhiteBoardRecordInfo);
		Assert.assertEquals(2, insertCount);
	}

	@Test
	public void testUpdateNurseWhiteBoardRecordInfo() {
		nurseWhiteBoardRepository
				.insertNurseWhiteBoardRecordInfo(nurseWhiteBoardRecordInfo);
		nurseWhiteBoardRecordInfo.setRecordNurseName("王");
		int insertCount = nurseWhiteBoardRepository
				.updateNurseWhiteBoardRecordInfo(nurseWhiteBoardRecordInfo);
		Assert.assertEquals(1, insertCount);
	}

	@Test
	public void testGetNurseWhiteBoardRecordItems() {
		List<NurseWhiteBoardRecordItem> nurseWhiteBoardRecordItems = nurseWhiteBoardRepository
				.getNurseWhiteBoardRecordItems("H250005", null, null, null);
		Gson gson = new Gson();
		String gsonString = gson.toJson(nurseWhiteBoardRecordItems);
		writeStrToFile(gsonString,"D:/getNurseWhiteBoardRecordItems.txt");
		Assert.assertNotNull(gsonString);
	}

	@Test
	public void testInsertNurseWhiteBoardRecordItemInfo() {
		int insertCount = nurseWhiteBoardRepository
				.insertNurseWhiteBoardRecordItemInfo(nurseWhiteBoardRecordItemInfos
						.get(0));
		Assert.assertEquals(1, insertCount);
	}

	@Test
	public void testUpdateNurseWhiteBoardRecordItemInfo() {
		NurseWhiteBoardRecordItemInfo nurseWhiteBoardRecordItemInfo = nurseWhiteBoardRecordItemInfos
				.get(0);
		nurseWhiteBoardRepository
				.insertNurseWhiteBoardRecordItemInfo(nurseWhiteBoardRecordItemInfo);
		nurseWhiteBoardRecordItemInfo.setRecordItemValue("19:00");

		int insertCount = nurseWhiteBoardRepository
				.updateNurseWhiteBoardRecordItemInfo(nurseWhiteBoardRecordItemInfo);
		Assert.assertEquals(1, insertCount);
	}
	
	/************************************* 数据源实现方法 ***************************************************/
	@Test
	public void testGetNurseWhiteBoardMetadatas() {
		List<NurseWhiteBoardMetadata> nurseWhiteBoardTopMetadatas = nurseWhiteBoardRepository
				.getNurseWhiteBoardTopMetadatas(deptCode, null);

		List<NurseWhiteBoardMetadata> nurseWhiteBoardMetadatas = getNurseWhiteBoardChildrenMetadata(nurseWhiteBoardTopMetadatas);
		Gson gson = new Gson();
		String gsonString = gson.toJson(nurseWhiteBoardMetadatas);
		writeStrToFile(gsonString,"D:/getNurseWhiteBoardMetadatas.txt");
		Assert.assertNotNull(gsonString);
	}

	private List<NurseWhiteBoardMetadata> getNurseWhiteBoardChildrenMetadata(
			List<NurseWhiteBoardMetadata> nurseWhiteBoardMetadatas) {

		for (NurseWhiteBoardMetadata nurseWhiteBoardMetadata : nurseWhiteBoardMetadatas) {
			String parentItemCode = nurseWhiteBoardMetadata.getCode();
			List<NurseWhiteBoardMetadata> nurseWhiteBoardChildrenMetadatas = nurseWhiteBoardRepository
					.getNurseWhiteBoardMetadatasByIds(null, parentItemCode);
			nurseWhiteBoardMetadata.setItems(nurseWhiteBoardChildrenMetadatas);
			getNurseWhiteBoardChildrenMetadata(nurseWhiteBoardChildrenMetadatas);
		}
		return nurseWhiteBoardMetadatas;
	}

	@Test
	public void testGetNurseWhiteBoardTopMetadatas() {
		List<NurseWhiteBoardMetadata> nurseWhiteBoardMetadatas = nurseWhiteBoardRepository
				.getNurseWhiteBoardTopMetadatas(deptCode, null);
		Gson gson = new Gson();
		String gsonString = gson.toJson(nurseWhiteBoardMetadatas);
		writeStrToFile(gsonString,"D:/getNurseWhiteBoardTopMetadatas.txt");
		Assert.assertNotNull(gsonString);
	}

	
	@Test
	public void testSaveNwbMetadata(){
		
		int saveCount = nurseWhiteBoardRepository.saveNwbMetadata(getNurseWhiteBoardMetadata());
		Assert.assertEquals(1, saveCount);
		
	}
	
	@Test
	public void testUpdateNwbMetadata(){
		nurseWhiteBoardRepository.saveNwbMetadata(getNurseWhiteBoardMetadata());
		int saveCount = nurseWhiteBoardRepository.updateNwbMetadata(getNurseWhiteBoardMetadata());
		Assert.assertEquals(1, saveCount);
		
	}
	
	@Test
	public void testDeleteNwbMetadataById(){
//		nurseWhiteBoardRepository.saveNwbMetadata(getNurseWhiteBoardMetadata());
		int saveCount = nurseWhiteBoardRepository.deleteNwbMetadata("1",null,null);
		Assert.assertEquals(1, saveCount);
		
	}
	
	@Test
	public void testGetNwbMetadataRowNo(){
		String rowNo = nurseWhiteBoardRepository.getNwbMetadataRowNo("0203", "rdd");
		Assert.assertNull(rowNo);
	}
	
	private void writeStrToFile(String writeStr,String fileName){
		 FileWriter writer;
	        try {
	            writer = new FileWriter(fileName);
	            writer.write(writeStr);
	            writer.flush();
	            writer.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	}
	
	private NurseWhiteBoardMetadata getNurseWhiteBoardMetadata(){
		String jsonString = "{ \"id\": 13,\"parentId\": \"\",\"deptCode\": \"5042\",\"code\": \"pCount\",\"name\": \"病人总数\", \"level\": 0,\"inputType\": \"TEXT\",\"isAuto\": true,\"isShow\": false,\"rowNo\": 1,\"columnNo\": 1,\"columnType\": \"0\",\"showType\": \"singleText\", \"isEdit\": false,\"isBedCode\": true,\"isColspan\": false,\"nurseWhiteBoardEditTypes\": [{\"id\": \"1\", \"code\": \"\",\"type\": \"text\",\"isMulti\": false,\"isInner\": false,\"isBedCode\": true}],\"items\": []}";
		Gson gson = new Gson();
		NurseWhiteBoardMetadata nurseWhiteBoardMetadata = gson.fromJson(jsonString, NurseWhiteBoardMetadata.class);
		return nurseWhiteBoardMetadata;
	}
}
