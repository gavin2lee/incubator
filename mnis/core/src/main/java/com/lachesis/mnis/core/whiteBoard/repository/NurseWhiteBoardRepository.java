package com.lachesis.mnis.core.whiteBoard.repository;

import java.util.Date;
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

public interface NurseWhiteBoardRepository {
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
	 * @return
	 */
	int getNwbMetadataCount(String detpCode,String code,String rowNo);
	String getNwbMetadataRowNo(String deptCode,String code);
	
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
	int updateNwbMetadataByRowNo(String deptCode,int newRowNo,int oldRowNo);
	/**
	 * 软删除小白板元数据
	 * @param id
	 * @return
	 */
	int deleteNwbMetadata(String id,String code,String deptCode);
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
	boolean isExistNwbRecord(String recordCode,String patId, String deptCod);
	
	List<String> getExistNwbRecordCodes(String deptCode);
	
	/**
	 * 获取小白板视图记录
	 * @param deptCode
	 * @return
	 */
	List<String> getExistNwbViewRecordCodes(String deptCode,String lastRecordDate);
	/**
	 * 获取小白板记录
	 * @param deptCode	部门
	 * @param codes		code集合
	 * @param notCodes	排除code集合
	 * @param patIds	患者集合
	 * @param startDate	开始日期
	 * @param endDate	结束日期
	 * @return
	 */
	List<NurseWhiteBoardRecord> getNurseWhiteBoardRecords(
			String deptCode,String code,List<String> patIds,Date startDate, Date endDate);
	
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
			NurseWhiteBoardRecordInfo nurseWhiteBoardRecordInfo);

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
			NurseWhiteBoardRecordInfo nurseWhiteBoardRecordInfos);
	/**
	 * 获取小白板子项记录
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
			NurseWhiteBoardRecordItemInfo nurseWhiteBoardRecordItemInfo);
	
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

	/**
	 * 小白板执行功能
	 * @param nurseWhiteBoardTemplateItem
	 */
	void execWhiteBoard(NurseWhiteBoardRecordItemInfo item);
	
	/**
	 * 获取元数据的编号和名称对应
	 * @return
	 */
	Map<String,String> getMetadataName(String deptCode,String code);
	
	/**
	 * 获取频次元数据
	 * @return
	 */
	Map<String,Object> getMetadataFreq();
	
	void execWhiteBoardItem(String recordId);
	/**
	 * 删除记录
	 * @param recordCode 
	 * @param patId
	 * @return
	 */
	int delete(String recordCode, String patId,String deptCode);
	/**
	 * 获取生命体征子项目
	 * @param deptCode
	 * @return
	 */
	Map<String, String> getSubMetadatas(String deptCode,String parentCode);
	List<String> getParentMetadatas(String deptCode);
	List<String> getFreqMetadataCodes(String deptCode);
	
	List<NurseWhiteBoardMetadataTV> getNurseWhiteBoardTVMetadatas(String deptCode,String templateId);
	
	List<NurseWhiteBoardTemplate> getNwbTemplates(String deptCode,String id);
	
	String saveNwbTemplate(NurseWhiteBoardTemplate master);
	
	int updateNwbTemplate(NurseWhiteBoardTemplate master);
	
	int deleteNwbTemplate(String deptCode,String id);
	/**
	 * 获取包含剂量code
	 * @param deptCode
	 * @return
	 */
	List<String> getMetadataCodes(String deptCode,String type);
	
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
