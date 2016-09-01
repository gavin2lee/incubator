package com.lachesis.mnis.board.nurse.service;

import java.util.List;

import com.lachesis.mnis.board.dto.NwbCodeDto;
import com.lachesis.mnis.board.dto.NwbRecordAttachDto;
import com.lachesis.mnis.board.dto.NwbRecordDto;
import com.lachesis.mnis.board.entity.NwbHisCode;
import com.lachesis.mnis.board.entity.NwbMetadata;
import com.lachesis.mnis.board.entity.NwbRecord;
import com.lachesis.mnis.board.entity.NwbTemplate;
import com.lachesis.mnis.board.entity.NwbWhiteBoardItem;

public interface NurseWhiteBoardNurseService {
	
	List<NwbHisCode> getHisCodeMapping();
	/**
	 * 获取小白板项目
	 * @param deptCode
	 * @param templateId
	 * @return
	 */
	List<NwbCodeDto> getNwbCodeDto(String deptCode,String templateId);
	/**
	 * 获取模版数据
	 */
	List<NwbTemplate> getNwbTemplates(String deptCode);
	
	void saveNwbTemplate(NwbTemplate nwbTemplate);
	
	/**
	 * 根据部门和模版编号获取模板
	 * @param deptCode
	 * @param temmplateId
	 * @return
	 */
	List<NwbMetadata> getNwbMetadatas(String deptCode,String templateId);
	
	void saveNwbMetadata(NwbMetadata nwbMetadata);
	
	void deleteNwbMetadata(String metadataId);
	
	void batchSaveNwbMetadata(List<NwbMetadata> nwbMetadatas);
	
	/**
	 * 根据科室和模板获取数据
	 * @param deptCode
	 * @param templateId
	 * @return
	 */
	List<NwbRecordDto> getNwbRecordDtos(String deptCode,String templateId);
	
	void saveNwbRecord(NwbRecord nwbRecord);
	
	void deleteNwbRecord(String recordId);
	
	void batchSaveNwbRecord(List<NwbRecordDto> nwbRecordDtos,NwbRecordAttachDto nwbRecordAttachDto);
	
	/**
	 * 获取本地与his的code对应信息
	 * @return
	 */
	List<NwbWhiteBoardItem> queryAllWhiteBoardItem();
	
	
	/**
	 * 获取所有指定部门的模板id
	 * @param deptCode 部门code
	 * @return
	 */
	List<String> queryTempIdByDeptCode(String deptCode);
	
}
