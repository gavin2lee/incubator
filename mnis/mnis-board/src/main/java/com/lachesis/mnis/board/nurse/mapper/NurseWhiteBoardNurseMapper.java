package com.lachesis.mnis.board.nurse.mapper;

import java.util.List;

import com.lachesis.mnis.board.dto.NwbCodeDto;
import com.lachesis.mnis.board.dto.NwbRecordDto;
import com.lachesis.mnis.board.entity.NwbHisCode;
import com.lachesis.mnis.board.entity.NwbMetadata;
import com.lachesis.mnis.board.entity.NwbRecord;
import com.lachesis.mnis.board.entity.NwbTemplate;
import com.lachesis.mnis.board.entity.NwbWhiteBoardItem;

public interface NurseWhiteBoardNurseMapper {
	/**
	 * 获取code映射
	 * @return
	 */
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
	
	void insertNwbTemplate(NwbTemplate nwbTemplate);
	
	void updateNwbTemplate(NwbTemplate nwbTemplate);
	
	Integer getNwbTemplateCountById(String templateId);
	/**
	 * 根据部门和模版编号获取模板
	 * @param deptCode
	 * @param temmplateId
	 * @return
	 */
	List<NwbMetadata> getNwbMetadatas(String deptCode,String templateId);
	
	void insertNwbMetadata(NwbMetadata nwbMetadata);
	
	void updateNwbMetadata(NwbMetadata nwbMetadata);
	
	void deleteNwbMetadata(String metadataId);
	
	Integer getNwbMetadataCountById(String metadataId);
	
	List<NwbRecordDto> getNwbRecordDtosByDeptCode(String deptCode,String templateId);
	
	Integer getNwbRecordCountById(String recordId);
	
	void insertNwbRecord(NwbRecord nwbRecord);
	
	void updateNwbRecord(NwbRecord nwbRecord);
	
	void deleteNwbRecord(String recordId);
	
	List<NwbWhiteBoardItem> queryAllWhiteBoardItem();
	
	List<String> queryTempIdByDeptCode(String deptCode);
}
