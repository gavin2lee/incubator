package com.lachesis.mnis.core.whiteBoard.repository.impl;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.lachesis.mnis.core.mybatis.mapper.NurseWhiteBoardMapper;
import com.lachesis.mnis.core.mybatis.mapper.OrderMapper;
import com.lachesis.mnis.core.whiteBoard.entity.NurseWhiteBoardEditType;
import com.lachesis.mnis.core.whiteBoard.entity.NurseWhiteBoardMetadataDic;
import com.lachesis.mnis.core.whiteBoard.entity.NurseWhiteBoardTemplate;
import com.lachesis.mnis.core.whiteBoard.entity.NurseWhiteBoardMetadataTV;
import com.lachesis.mnis.core.whiteBoard.entity.NurseWhiteBoardRecord;
import com.lachesis.mnis.core.whiteBoard.entity.NurseWhiteBoardMetadata;
import com.lachesis.mnis.core.whiteBoard.entity.NurseWhiteBoardRecordInfo;
import com.lachesis.mnis.core.whiteBoard.entity.NurseWhiteBoardRecordItem;
import com.lachesis.mnis.core.whiteBoard.entity.NurseWhiteBoardRecordItemInfo;
import com.lachesis.mnis.core.whiteBoard.repository.NurseWhiteBoardRepository;

@Repository("nurseWhiteBoardRepository")
public class NurseWhiteBoardRepositoryImpl implements NurseWhiteBoardRepository {

	@Autowired
	private NurseWhiteBoardMapper nurseWhiteBoardMapper;
	/***********************小白板元数据*******************************/
	@Override
	public List<NurseWhiteBoardMetadataDic> getNwbMetadataDics(
			String deptCode) {
		return nurseWhiteBoardMapper.getNwbMetadataDics(deptCode);
	}
	@Override
	public List<NurseWhiteBoardMetadata> getNurseWhiteBoardTopMetadatas(
			String deptCode, String code) {
		return nurseWhiteBoardMapper.getNurseWhiteBoardTopMetadatas(deptCode,
				code);
	}

	@Override
	public List<NurseWhiteBoardMetadata> getNurseWhiteBoardMetadatasByIds(
			String id, String parentId) {
		return nurseWhiteBoardMapper.getNurseWhiteBoardMetadatasByIds(id,
				parentId);
	}
	@Override
	public int getNwbMetadataCount(String deptCode, String code,String rowNo) {
		return nurseWhiteBoardMapper.getNwbMetadataCount(deptCode, code,rowNo);
	}
	
	@Override
	public String getNwbMetadataRowNo(String deptCode,String code){
		return nurseWhiteBoardMapper.getNwbMetadataRowNo(deptCode, code);
	}
	
	@Override
	public int getNwbEditTypeCount(String type,String code,String metadataCode,String templateId){
		return nurseWhiteBoardMapper.getNwbEditTypeCount(type, code, metadataCode,templateId);
	}
	
	@Override
	public int saveNwbMetadata(NurseWhiteBoardMetadata nurseWhiteBoardMetadata) {
		return nurseWhiteBoardMapper.saveNwbMetadata(nurseWhiteBoardMetadata);
	}

	@Override
	public int updateNwbMetadata(NurseWhiteBoardMetadata nurseWhiteBoardMetadata) {
		return nurseWhiteBoardMapper.updateNwbMetadata(nurseWhiteBoardMetadata);
	}
	
	@Override
	public int updateNwbMetadataByRowNo(String deptCode,int newRowNo,int oldRowNo){
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("deptCode", deptCode);
		params.put("newRowNo", newRowNo);
		params.put("oldRowNo", oldRowNo);
		
		return nurseWhiteBoardMapper.updateNwbMetadataByRowNo(params);
	}

	@Override
	public int deleteNwbMetadata(String id,String code,String deptCode) {
		return nurseWhiteBoardMapper.deleteNwbMetadata(id,code,deptCode);
	}
	
	@Override
	public int saveNwbEditType(NurseWhiteBoardEditType nurseWhiteBoardEditType) {
		return nurseWhiteBoardMapper.saveNwbEditType(nurseWhiteBoardEditType);
	}

	@Override
	public int updateNwbEditType(NurseWhiteBoardEditType nurseWhiteBoardEditType) {
		return nurseWhiteBoardMapper.updateNwbEditType(nurseWhiteBoardEditType);
	}
	
	@Override
	public int deleteNwbEditType(String metaCode,String templateId) {
		return nurseWhiteBoardMapper.deleteNwbEditType(metaCode,templateId);
	}
	/***********************小白板记录*******************************/

	@Override
	public List<NurseWhiteBoardRecord> getNurseWhiteBoardRecords(
			String deptCode, String code,List<String> patIds,Date startDate, Date endDate) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("deptCode", deptCode);
		params.put("code", code);
		params.put("patIds", patIds);
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		return nurseWhiteBoardMapper.getNurseWhiteBoardRecords(params);
	}

	@Override
	public int insertNurseWhiteBoardRecordInfo(
			NurseWhiteBoardRecordInfo nurseWhiteBoardRecordInfo) {
		return nurseWhiteBoardMapper
				.insertNurseWhiteBoardRecordInfo(nurseWhiteBoardRecordInfo);
	}

	@Override
	public int insertNurseWhiteBoardRecordInfoAndItems(
			NurseWhiteBoardRecordInfo nurseWhiteBoardRecordInfos) {
		return nurseWhiteBoardMapper
				.insertNurseWhiteBoardRecordInfoAndItems(nurseWhiteBoardRecordInfos);
	}

	@Override
	public int updateNurseWhiteBoardRecordInfo(
			NurseWhiteBoardRecordInfo nurseWhiteBoardRecordInfo) {
		return nurseWhiteBoardMapper
				.updateNurseWhiteBoardRecordInfo(nurseWhiteBoardRecordInfo);
	}

	@Override
	public int updateNurseWhiteBoardRecordInfoAndItems(
			NurseWhiteBoardRecordInfo nurseWhiteBoardRecordInfos) {
		return nurseWhiteBoardMapper
				.updateNurseWhiteBoardRecordInfoAndItems(nurseWhiteBoardRecordInfos);
	}

	@Override
	public int insertNurseWhiteBoardRecordItemInfo(
			NurseWhiteBoardRecordItemInfo nurseWhiteBoardRecordItemInfo) {
		return nurseWhiteBoardMapper
				.insertNurseWhiteBoardRecordItemInfo(nurseWhiteBoardRecordItemInfo);
	}

	@Override
	public int updateNurseWhiteBoardRecordItemInfo(
			NurseWhiteBoardRecordItemInfo nurseWhiteBoardRecordItemInfo) {
		return nurseWhiteBoardMapper
				.updateNurseWhiteBoardRecordItemInfo(nurseWhiteBoardRecordItemInfo);
	}

	@Override
	public int deleteNurseWhiteBoardRecordInfoById(String recordId) {
		return nurseWhiteBoardMapper.deleteNurseWhiteBoardRecordInfoById(recordId);
	}

	@Override
	public int deleteNurseWhiteBoardRecordItemInfoById(String itemId) {
		return nurseWhiteBoardMapper
				.deleteNurseWhiteBoardRecordItemInfoById(itemId);
	}


	@Override
	public List<NurseWhiteBoardRecordItem> getNurseWhiteBoardRecordItems(
			String deptCode, String code, Date startDate, Date endDate) {
		return nurseWhiteBoardMapper.getNurseWhiteBoardRecordItems(deptCode, code, startDate, endDate);
	}

	
	@Override
	public void execWhiteBoard(NurseWhiteBoardRecordItemInfo item){
		nurseWhiteBoardMapper.updateNurseWhiteBoardRecordItem(item);
	}
	
	@Override
	public Map<String,String> getMetadataName(String deptCode,String code){
		Map<String,String> rs = new HashMap<String,String>();
		List<Map<String,Object>> metadas = nurseWhiteBoardMapper.selectMetadataName(deptCode,code);
		for(int i=0;i<metadas.size();i++){
			Map<String,Object> data = metadas.get(i);
			rs.put((String)data.get("code"), (String)data.get("name"));
		}
		return rs;
	}
	
	@Override
	public Map<String,Object> getMetadataFreq(){
		Map<String,Object> rs = new HashMap<String,Object>();
		List<String> freqTypes = nurseWhiteBoardMapper.getFreqTypes();
		
		for (String freqType : freqTypes) {
			Map<String, String> map = new HashMap<String, String>();
			List<Map<String,Object>> metadas = nurseWhiteBoardMapper.getMetadataNameValue(freqType);
			for(int i=0;i<metadas.size();i++){
				Map<String,Object> data = metadas.get(i);
				map.put((String)data.get("code"), (String)data.get("name"));
			}
			
			rs.put(freqType, map);
		}
		return rs;
	}

	@Override
	public void execWhiteBoardItem(String recordId) {
		nurseWhiteBoardMapper.execWhiteBoardItem(recordId);
	}

	@Override
	public int delete(String recordCode, String patId,String deptCode) {
		return nurseWhiteBoardMapper.delete(recordCode, patId, deptCode);
	}

	@Override
	public Map<String, String> getSubMetadatas(String deptCode,String parentCode) {
		Map<String,String> rs = new HashMap<String,String>();
		List<Map<String,Object>> metadas = nurseWhiteBoardMapper.getSubMetadatas(deptCode,parentCode);
		for(int i=0;i<metadas.size();i++){
			Map<String,Object> data = metadas.get(i);
			rs.put((String)data.get("code"), (String)data.get("name"));
		}
		return rs;
	}

	@Override
	public List<String> getFreqMetadataCodes(String deptCode) {
		return nurseWhiteBoardMapper.getFreqMetadataCodes(deptCode);
	}

	@Override
	public List<NurseWhiteBoardRecord> getNurseWhiteBoardDynamicRecords(
			String deptCode, String code) {
		return nurseWhiteBoardMapper.getNurseWhiteBoardDynamicRecords(deptCode, code);
	}
	@Override
	public List<String> getParentMetadatas(String deptCode) {
		return nurseWhiteBoardMapper.getParentMetadatas(deptCode);
	}
	@Override
	public String getNwbRecordLastRecordDate(String deptCode) {
		return nurseWhiteBoardMapper.getNwbRecordLastRecordDate(deptCode);
	}
	@Override
	public List<NurseWhiteBoardMetadataTV> getNurseWhiteBoardTVMetadatas(
			String deptCode, String templateId) {
		return nurseWhiteBoardMapper.getNurseWhiteBoardTVMetadatas(deptCode, templateId);
	}
	@Override
	public List<NurseWhiteBoardTemplate> getNwbTemplates(
			String deptCode, String id) {
		return nurseWhiteBoardMapper.getNwbTemplates(deptCode, id);
	}
	@Override
	public String saveNwbTemplate(NurseWhiteBoardTemplate master) {
		nurseWhiteBoardMapper.saveNwbTemplate(master);
		return master.getId();
	}
	@Override
	public int updateNwbTemplate(NurseWhiteBoardTemplate master) {
		return nurseWhiteBoardMapper.updateNwbTemplate(master);
	}
	@Override
	public int deleteNwbTemplate(String deptCode, String id) {
		return nurseWhiteBoardMapper.deleteNwbTemplate(deptCode, id);
	}
	@Override
	public boolean isExistNwbRecord(String recordCode, String patId, String deptCode) {
		return nurseWhiteBoardMapper.isExistNwbRecord(recordCode, patId,deptCode) > 0 ? true: false;
	}
	@Override
	public List<String> getExistNwbRecordCodes(String deptCode) {
		return nurseWhiteBoardMapper.getExistNwbRecordCodes(deptCode);
	}
	@Override
	public List<String> getExistNwbViewRecordCodes(String deptCode,String lastRecordDate) {
		return nurseWhiteBoardMapper.getExistNwbViewRecordCodes(deptCode, lastRecordDate);
	}
	@Override
	public List<String> getMetadataCodes(String deptCode,String type) {
		return nurseWhiteBoardMapper.getMetadataCodes(deptCode,type);
	}
	
	
	
	@Override
	public String getDosageByData(String deptCode, String patId, String code) {
		return nurseWhiteBoardMapper.getDosageByData(deptCode, patId, code);
	}
	@Override
	public List<Map<String, String>> getNwbOrderDataMap(String deptCode,
			String lastRecordDate) {
		
		return nurseWhiteBoardMapper.getNwbOrderDataMap(deptCode, lastRecordDate);
	}
	@Override
	public List<String> getQueryOrderCodes() {
		return nurseWhiteBoardMapper.getQueryOrderCodes();
	}
	
}
