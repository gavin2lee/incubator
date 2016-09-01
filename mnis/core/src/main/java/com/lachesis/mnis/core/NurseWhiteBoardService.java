package com.lachesis.mnis.core;

import java.util.List;
import java.util.Map;

import com.lachesis.mnis.core.identity.entity.SysUser;
import com.lachesis.mnis.core.whiteBoard.entity.NurseWhiteBoardInBo;
import com.lachesis.mnis.core.whiteBoard.entity.NurseWhiteBoardMetadata;
import com.lachesis.mnis.core.whiteBoard.entity.NurseWhiteBoardMetadataDic;
import com.lachesis.mnis.core.whiteBoard.entity.NurseWhiteBoardTemplate;
import com.lachesis.mnis.core.whiteBoard.entity.NurseWhiteBoardMetadataTV;
import com.lachesis.mnis.core.whiteBoard.entity.NurseWhiteBoardRecord;
import com.lachesis.mnis.core.whiteBoard.entity.NurseWhiteBoardRecordInfo;
import com.lachesis.mnis.core.whiteBoard.entity.NurseWhiteBoardRecordItem;
import com.lachesis.mnis.core.whiteBoard.entity.NurseWhiteBoardRecordItemInfo;

public interface NurseWhiteBoardService {
	/*******************************数据源******************************************/
	/**
	 * 小白板配置字典表
	 * @param deptCode
	 * @return
	 */
	List<NurseWhiteBoardMetadataDic> getNwbMetadataDics(String deptCode);
	/**
	 * 保存小白板元数据
	 * @param nurseWhiteBoardMetadata
	 * @return
	 */
	void batchSaveNwbMetadata(List<NurseWhiteBoardMetadata> nurseWhiteBoardMetadatas,NurseWhiteBoardTemplate master,boolean isItems);
	/**
	 * 保存小白板元数据
	 * @param nurseWhiteBoardMetadata
	 * @return
	 */
	int saveNwbMetadata(NurseWhiteBoardMetadata nurseWhiteBoardMetadata,boolean isAddRow);
	/**
	 * 修改小白板元数据
	 * @param nurseWhiteBoardMetadata
	 * @return
	 */
	int updateNwbMetadata(NurseWhiteBoardMetadata nurseWhiteBoardMetadata);
	/**
	 * 软删除小白板元数据
	 * @param id
	 * @return
	 */
	int deleteNwbMetadataById(String id);
	
	/**
	 * 获取部门的所有数据源
	 * @param deptCode
	 * @return
	 */
	List<NurseWhiteBoardMetadata> getNurseWhiteBoardMetadatas(String deptCode,String code , boolean isCache);
	
	/**
	 * 获取部门的顶层数据源
	 * @param deptCode
	 * @return
	 */
	List<NurseWhiteBoardMetadata> getNurseWhiteBoardTopMetadatas(String deptCode,String code);
	/*******************************小白板模板******************************************/
	/*******************************小白板记录******************************************/
	/**
	 * 获取小白板记录
	 * @param deptCode
	 * @param code
	 * @param recordDate
	 * @return
	 */
	List<NurseWhiteBoardRecord> getNurseWhiteBoardRecords(String deptCode,String code,String recordDate,String nurseCode);
	
	/**
	 * 批量插入小白板记录
	 * @param nurseWhiteBoardRecords
	 * @return
	 */
	int insertNurseWhiteBoardRecordInfos(List<NurseWhiteBoardRecordInfo> nurseWhiteBoardRecordInfos);
	int updateNurseWhiteBoardRecordInfos(List<NurseWhiteBoardRecordInfo> nurseWhiteBoardRecordInfos);
	List<NurseWhiteBoardRecord> getNurseWhiteBoardDynamicRecords(String deptCode,String code);
	/**
	 * 获取小白板子项记录
	 * @param itemId
	 * @return
	 */
	List<NurseWhiteBoardRecordItem> getNurseWhiteBoardRecordItems(String deptCode,String recordDate);

	/**
	 *批量插入小白板子项记录
	 * @param nurseWhiteBoardRecordItem
	 * @return
	 */
	int insertNurseWhiteBoardRecordItemInfos(List<NurseWhiteBoardRecordItemInfo> nurseWhiteBoardRecordItemInfos);
	
	/**
	 * 护理小白板执行
	 */
//	void execWhiteBoard(List<NurseWhiteBoardRecordInfo> recordList);
	void execWhiteBoardItem(String recordId);
	
	/**
	 * 获取元数据
	 * @return
	 */
	Map<String,String> getMetadataName(String deptCode,String code);

	Map<String,Object> getNurseWhiteBoardMetaValues();
	/**
	 * 保存一个小白板项目记录
	 * @param nurseWhiteBoardInBo
	 * @return
	 */
	int saveNurseWhiteBoardRecord(NurseWhiteBoardInBo nurseWhiteBoardInBo,boolean isOnlySave,SysUser user,String deptCode);
	/**
	 * 保存多个小白板项目记录
	 * @param nurseWhiteBoardInBos
	 * @return
	 */
	int batchSaveNurseWhiteBoardRecord(List<NurseWhiteBoardInBo> nurseWhiteBoardInBos,boolean isOnlySave,SysUser user,String deptCode);
	/**
	 * 获取生命体征子项目
	 * @param deptCode
	 * @return
	 */
	Map<String, Object> getSubMetadatas(String deptCode);
	
	List<String> getFreqMetadataCodes(String deptCode);
	
	List<NurseWhiteBoardMetadataTV> getNurseWhiteBoardTVMetadatas(String deptCode,String templateId ,boolean isCache);
	
	List<NurseWhiteBoardTemplate> getNwbTemplates(String deptCode,String id,boolean isCache);
	
	int saveNwbTemplate(NurseWhiteBoardTemplate template);
	
	int updateNwbTemplate(NurseWhiteBoardTemplate template);
	
	int deleteNwbTemplate(String deptCode,String id);
	
	Map<String, String> getNwbOrderDataMap(String deptCode,String lastRecordDate);
}
