package com.lachesis.mnis.core.mybatis.mapper;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lachesis.mnis.core.whiteBoard.entity.NurseWhiteBoardEditType;
import com.lachesis.mnis.core.whiteBoard.entity.NurseWhiteBoardMetadata;
import com.lachesis.mnis.core.whiteBoard.entity.NurseWhiteBoardMetadataDic;
import com.lachesis.mnis.core.whiteBoard.entity.NurseWhiteBoardTemplate;
import com.lachesis.mnis.core.whiteBoard.entity.NurseWhiteBoardMetadataTV;
import com.lachesis.mnis.core.whiteBoard.entity.NurseWhiteBoardRecord;
import com.lachesis.mnis.core.whiteBoard.entity.NurseWhiteBoardRecordInfo;
import com.lachesis.mnis.core.whiteBoard.entity.NurseWhiteBoardRecordItem;
import com.lachesis.mnis.core.whiteBoard.entity.NurseWhiteBoardRecordItemInfo;

public interface NurseWhiteBoardMapper {
	
	/**
	 * 小白板配置字典表
	 * @param deptCode
	 * @return
	 */
	List<NurseWhiteBoardMetadataDic> getNwbMetadataDics(String deptCode);
	/******************** 元数据 ****************/
	List<NurseWhiteBoardMetadata> getNurseWhiteBoardTopMetadatas(
			String deptCode,String code);

	List<NurseWhiteBoardMetadata> getNurseWhiteBoardMetadatasByIds(
			String id, String parentId);
	/**
	 * 根据个数判断元数据是否存在
	 * @param detpCode
	 * @param code
	 * @param rowNo
	 * @return
	 */
	int getNwbMetadataCount(String deptCode,String code,String rowNo);
	String getNwbMetadataRowNo(String deptCode,String code);
	/**
	 * 获取包含剂量code
	 * @param deptCode
	 * @param type: edit:是否编辑, dosage:是否剂量
	 * @return
	 */
	List<String> getMetadataCodes(String deptCode,String type);
	/**
	 * 根据个数判断编辑类型是否存在
	 * @param type
	 * @param code
	 * @param metadataCode
	 * @return
	 */
	int getNwbEditTypeCount(String type,String code,String metadataCode,String templateId);
	
	/**
	 * 保存小白板元数据
	 * @param nurseWhiteBoardMetadata
	 * @return
	 */
	int saveNwbMetadata(NurseWhiteBoardMetadata nurseWhiteBoardMetadata);
	/**
	 * 修改小白板元数据
	 * @param nurseWhiteBoardMetadata
	 * @return
	 */
	int updateNwbMetadata(NurseWhiteBoardMetadata nurseWhiteBoardMetadata);
	/**
	 * 根据行更新元数据行
	 * @param deptCode
	 * @param rowNo
	 * @return
	 */
	int updateNwbMetadataByRowNo(HashMap<String, Object> params);
	/**
	 * 软删除小白板元数据
	 * @param id
	 * @param code
	 * @param detpCode
	 * @return
	 */
	int deleteNwbMetadata(String id,String code,String detpCode);
	/**
	 * 保存小白板编辑类型
	 * @param nurseWhiteBoardEditType
	 * @return
	 */
	int saveNwbEditType(NurseWhiteBoardEditType nurseWhiteBoardEditType);
	/**
	 * 修改小白板编辑类型
	 * @param nurseWhiteBoardEditType
	 * @return
	 */
	int updateNwbEditType(NurseWhiteBoardEditType nurseWhiteBoardEditType);
	
	int deleteNwbEditType(String metaCode,String templateId);

	/******************** 数据记录 ****************/
	/**
	 * 获取小白板基本记录
	 * 
	 * @param deptCode
	 * @param itemCode
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	List<NurseWhiteBoardRecord> getNurseWhiteBoardRecords(HashMap<String, Object> params);
	
	int isExistNwbRecord(String recordCode,String patId,String deptCode);
	/**
	 * 获取小白板记录
	 * @param deptCode
	 * @return
	 */
	List<String> getExistNwbRecordCodes(String deptCode);
	/**
	 * 获取小白板视图记录
	 * @param deptCode
	 * @return
	 */
	List<String> getExistNwbViewRecordCodes(String deptCode,String lastRecordDate);
	
	List<NurseWhiteBoardRecord> getNurseWhiteBoardDynamicRecords(String deptCode,String code);
	
	/**
	 * 获取记录的最新时间
	 * @return
	 */
	String getNwbRecordLastRecordDate(String deptCode);
	
	/**
	 * 获取最新时间医嘱同步数据的code和(code+patientId)组合
	 * @param deptCode
	 * @param lastRecordDate
	 * @return
	 */
	List<Map<String, String>> getNwbOrderDataMap(String deptCode,String lastRecordDate);

	/**
	 * 插入小白板记录
	 * 
	 * @param nurseWhiteBoardRecord
	 * @return
	 */
	int insertNurseWhiteBoardRecordInfo(
			NurseWhiteBoardRecordInfo nurseWhiteBoardRecordInfo);

	/**
	 * 插入小白板记录和子项记录
	 * 
	 * @param nurseWhiteBoardRecords
	 * @return
	 */
	int insertNurseWhiteBoardRecordInfoAndItems(
			NurseWhiteBoardRecordInfo nurseWhiteBoardRecordInfos);

	/**
	 * 修改小白板记录
	 * 
	 * @param nurseWhiteBoardRecord
	 * @return
	 */
	int updateNurseWhiteBoardRecordInfo(
			NurseWhiteBoardRecordInfo nurseWhiteBoardRecordInfo);

	/**
	 * 修改小白板记录和子项记录
	 * 
	 * @param nurseWhiteBoardRecords
	 * @return
	 */
	int updateNurseWhiteBoardRecordInfoAndItems(
			NurseWhiteBoardRecordInfo nurseWhiteBoardRecordinfos);

	/**
	 * 获取小白板子项记录(项和子项集合)
	 * 
	 * @param itemId
	 * @return
	 */
	List<NurseWhiteBoardRecordItem> getNurseWhiteBoardRecordItems(
			String deptCode, String code, Date startDate, Date endDate);

	/**
	 * 插入小白板子项记录
	 * 
	 * @param nurseWhiteBoardRecordItem
	 * @return
	 */
	int insertNurseWhiteBoardRecordItemInfo(
			NurseWhiteBoardRecordItemInfo nurseWhiteBoardRecordItemInfo);

	/**
	 * 修改小白板子项记录
	 * 
	 * @param nurseWhiteBoardRecordItem
	 * @return
	 */
	int updateNurseWhiteBoardRecordItemInfo(
			NurseWhiteBoardRecordItemInfo nurseWhiteBoardRecordItem);
	/**
	 *  删除记录
	 * @param recordId
	 * @return
	 */
	int deleteNurseWhiteBoardRecordInfoById(String recordId);
	/**
	 * 删除子项记录
	 * @param itemId
	 * @return
	 */
	int deleteNurseWhiteBoardRecordItemInfoById(String itemId);
	
	int delete(String recordCode, String patId,String deptCode);
	
	//执行小白板
	int updateNurseWhiteBoardRecordItem(NurseWhiteBoardRecordItemInfo item);
	
	/**
	 * 获取元数据的编号和名称
	 * @return
	 */
	List<Map<String,Object>> selectMetadataName(String deptCode,String code);
	
	/**
	 * 获取频次无数据
	 */
	List<Map<String,Object>> getMetadataNameValue(String freqCode);
	
	void execWhiteBoardItem(String recordId);
	/**
	 * 获取生命体征子项目
	 * @param deptCode
	 * @return
	 */
	List<Map<String, Object>> getSubMetadatas(String deptCode,String parentCode);
	
	List<String> getParentMetadatas(String deptCode);
	/**
	 * 获取元数据包含频次的code
	 * @param deptCode
	 * @return
	 */
	List<String> getFreqMetadataCodes(String deptCode);
	/**
	 * 获取元数据值得类型
	 * @return
	 */
	List<String> getFreqTypes();
	
	List<NurseWhiteBoardMetadataTV> getNurseWhiteBoardTVMetadatas(String deptCode,String templateId);
	
	List<NurseWhiteBoardTemplate> getNwbTemplates(String deptCode,String id);
	
	int saveNwbTemplate(NurseWhiteBoardTemplate master);
	
	int updateNwbTemplate(NurseWhiteBoardTemplate master);
	
	int deleteNwbTemplate(String deptCode,String id);
	
	/**
	 * 同步表中获取小白板剂量信息
	 * @param deptCode
	 * @param patId
	 * @param code
	 * @return
	 */
	String getDosageByData(String deptCode,String patId,String code);
	
	List<String> getQueryOrderCodes();
 }
