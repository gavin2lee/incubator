package com.lachesis.mnis.board.nurse.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lachesis.mnis.board.constants.BoardConstants;
import com.lachesis.mnis.board.dto.NwbCodeDto;
import com.lachesis.mnis.board.dto.NwbRecordAttachDto;
import com.lachesis.mnis.board.dto.NwbRecordDto;
import com.lachesis.mnis.board.dto.NwbRecordItemDto;
import com.lachesis.mnis.board.entity.NwbHisCode;
import com.lachesis.mnis.board.entity.NwbHisCodeItem;
import com.lachesis.mnis.board.entity.NwbMetadata;
import com.lachesis.mnis.board.entity.NwbRecord;
import com.lachesis.mnis.board.entity.NwbTemplate;
import com.lachesis.mnis.board.entity.NwbWhiteBoardItem;
import com.lachesis.mnis.board.exception.MnisException;
import com.lachesis.mnis.board.his.service.NurseWhiteBoardHisService;
import com.lachesis.mnis.board.nurse.mapper.NurseWhiteBoardNurseMapper;
import com.lachesis.mnis.board.nurse.service.NurseWhiteBoardNurseService;

/**
 * 处理移动护理接口
 * 
 * @author ThinkPad
 *
 */
@Service
public class NurseWhiteBoardNurseServiceImpl implements
		NurseWhiteBoardNurseService {
	private static Logger LOG = LoggerFactory
			.getLogger(NurseWhiteBoardNurseServiceImpl.class);

	@Autowired
	private NurseWhiteBoardHisService nurseWhiteBoardHisService;

	@Autowired
	private NurseWhiteBoardNurseMapper nurseWhiteBoardNurseMapper;

	@PostConstruct
	void init() {
		// 缓存医嘱code映射
		List<NwbHisCode> nwbHisCodes = nurseWhiteBoardNurseMapper
				.getHisCodeMapping();

		for (NwbHisCode nwbHisCode : nwbHisCodes) {
			BoardConstants.ORDER_CODES_CACHE.put(nwbHisCode.getTemplateId(),
					nwbHisCode);
			BoardConstants.ORDER_CODE_MAPPING_CACHE.put(
					nwbHisCode.getTemplateId(), setOrderCodeCache(nwbHisCode));
		}
	}

	/**
	 * 设置Code与HisCode映射
	 * 
	 * @param nwbHisCode
	 * @return
	 */
	private HashMap<String, String> setOrderCodeCache(NwbHisCode nwbHisCode) {
		HashMap<String, String> codeMap = new HashMap<String, String>();
		if (null == nwbHisCode) {
			return codeMap;
		}
		List<NwbHisCodeItem> nwbHisCodeItems = nwbHisCode.getNwbHisCodeItems();
		if (null == nwbHisCodeItems || nwbHisCodeItems.isEmpty()) {
			return codeMap;
		}

		for (NwbHisCodeItem nwbHisCodeItem : nwbHisCodeItems) {
			// 多个code可能对应一个HisCode(频次不一致)
			codeMap.put(nwbHisCodeItem.getCode(), nwbHisCodeItem.getHisCode());
		}

		return codeMap;

	}

	
	@Override
	public List<NwbHisCode> getHisCodeMapping() {
		return nurseWhiteBoardNurseMapper
				.getHisCodeMapping();
	}
	
	@Override
	public List<NwbCodeDto> getNwbCodeDto(String deptCode, String templateId) {
		if (StringUtils.isBlank(deptCode)) {
			LOG.debug("NurseWhiteBoardNurseServiceImpl getNwbCodeDto deptCode is null");
			throw new MnisException("科室为空!");
		}

		if (StringUtils.isBlank(templateId)) {
			LOG.debug("NurseWhiteBoardNurseServiceImpl getNwbCodeDto templateId is null");
			throw new MnisException("模板编号为空!");
		}
		return nurseWhiteBoardNurseMapper.getNwbCodeDto(deptCode, templateId);
	}

	@Override
	public List<NwbTemplate> getNwbTemplates(String deptCode) {
		if (StringUtils.isBlank(deptCode)) {
			LOG.debug("NurseWhiteBoardNurseServiceImpl getNwbTemplates deptCode is null");
			throw new MnisException("科室为空!");
		}

		return nurseWhiteBoardNurseMapper.getNwbTemplates(deptCode);
	}

	@Override
	public void saveNwbTemplate(NwbTemplate nwbTemplate) {
		if (nwbTemplate == null) {
			LOG.debug("NurseWhiteBoardNurseServiceImpl saveNwbTemplate nwbTemplate is null");
			throw new MnisException("模板数据为空!");
		}

		if (StringUtils.isBlank(nwbTemplate.getId())) {
			// 新增
			nurseWhiteBoardNurseMapper.insertNwbTemplate(nwbTemplate);
		} else {

			if (nurseWhiteBoardNurseMapper.getNwbTemplateCountById(nwbTemplate
					.getId()) > 0) {
				// 修改
				nurseWhiteBoardNurseMapper.updateNwbTemplate(nwbTemplate);
			} else {
				// 新增
				nurseWhiteBoardNurseMapper.insertNwbTemplate(nwbTemplate);
			}
		}
	}

	@Override
	public List<NwbMetadata> getNwbMetadatas(String deptCode, String templateId) {
		if (StringUtils.isBlank(deptCode)) {
			LOG.debug("NurseWhiteBoardNurseServiceImpl getNwbMetadatas deptCode is null");
			throw new MnisException("科室为空!");
		}

		if (StringUtils.isBlank(templateId)) {
			LOG.debug("NurseWhiteBoardNurseServiceImpl getNwbMetadatas templateId is null");
			throw new MnisException("模板编号不存在!");
		}

		return nurseWhiteBoardNurseMapper.getNwbMetadatas(deptCode, templateId);
	}

	@Override
	public void saveNwbMetadata(NwbMetadata nwbMetadata) {
		if (nwbMetadata == null) {
			LOG.debug("NurseWhiteBoardNurseServiceImpl saveNwbTemplate nwbMetadata is null");
			throw new MnisException("元数据为空!");
		}

		if (StringUtils.isBlank(nwbMetadata.getId())) {
			// 新增
			nurseWhiteBoardNurseMapper.insertNwbMetadata(nwbMetadata);
		} else {

			if (nurseWhiteBoardNurseMapper.getNwbMetadataCountById(nwbMetadata
					.getId()) > 0) {
				// 修改
				nurseWhiteBoardNurseMapper.updateNwbMetadata(nwbMetadata);
			} else {
				// 新增
				nurseWhiteBoardNurseMapper.insertNwbMetadata(nwbMetadata);
			}
		}
	}

	@Transactional(value = "boardTransactionManager")
	@Override
	public void batchSaveNwbMetadata(List<NwbMetadata> nwbMetadatas) {
		for (NwbMetadata nwbMetadata : nwbMetadatas) {
			saveNwbMetadata(nwbMetadata);
		}
	}

	@Override
	public void deleteNwbMetadata(String metadataId) {
		nurseWhiteBoardNurseMapper.deleteNwbMetadata(metadataId);
	}

	/**
	 * 获取小白板数据,正常情况 his数据无法修改
	 */
	@Override
	public List<NwbRecordDto> getNwbRecordDtos(String deptCode,
			String templateId) {

		if (StringUtils.isBlank(deptCode)) {
			LOG.debug("NurseWhiteBoardNurseServiceImpl getNwbRecordDtos deptCode is null");
			throw new MnisException("科室为空!");
		}

		if (StringUtils.isBlank(templateId)) {
			LOG.debug("NurseWhiteBoardNurseServiceImpl getNwbRecordDtos templateId is null");
			throw new MnisException("模板编号为空!");
		}
		// 1.获取his数据
//		List<NwbRecordDto> nwbHisRecordDtos = nurseWhiteBoardHisService
//				.getNurseWhiteBoardsByDeptCode(deptCode, templateId);
		// 2.获取小白板自身数据
		List<NwbRecordDto> nwbRecordDtos = nurseWhiteBoardNurseMapper
				.getNwbRecordDtosByDeptCode(deptCode, templateId);

//		nwbRecordDtos.addAll(nwbHisRecordDtos);

		return nwbRecordDtos;
	}

	@Override
	public void saveNwbRecord(NwbRecord nwbRecord) {
		if (nwbRecord == null) {
			LOG.debug("NurseWhiteBoardNurseServiceImpl saveNwbRecord nwbRecord is null");
			throw new MnisException("记录信息为空!");
		}

		if (isSaveRecord(nwbRecord.getRecordId())) {
			nurseWhiteBoardNurseMapper.insertNwbRecord(nwbRecord);
		} else {
			nurseWhiteBoardNurseMapper.updateNwbRecord(nwbRecord);
		}
	}

	@Override
	public void batchSaveNwbRecord(List<NwbRecordDto> nwbRecordDtos,
			NwbRecordAttachDto nwbRecordAttachDto) {

		if (nwbRecordDtos == null) {
			LOG.debug("NurseWhiteBoardNurseServiceImpl batchSaveNwbRecord nwbRecordDtos is null");
			throw new MnisException("批量记录信息为空!");
		}
		//将业务传输数据转为逻辑数据保存
		List<NwbRecord> nwbRecords = convertRecordDtoToRecord(nwbRecordDtos,
				nwbRecordAttachDto);
		for (NwbRecord nwbRecord : nwbRecords) {
			saveNwbRecord(nwbRecord);
		}
	}

	@Override
	public void deleteNwbRecord(String recordId) {
		nurseWhiteBoardNurseMapper.deleteNwbRecord(recordId);
	}

	/**
	 * 是否保存记录
	 * 
	 * @param recordId
	 * @return
	 */
	private boolean isSaveRecord(String recordId) {
		if (StringUtils.isBlank(recordId)
				|| nurseWhiteBoardNurseMapper.getNwbRecordCountById(recordId) == 0) {
			return true;
		}
		return false;
	}

	/**
	 * 业务逻辑数据转换
	 * @param nwbRecordDtos
	 * @param nwbRecordAttachDto
	 * @return
	 */
	private List<NwbRecord> convertRecordDtoToRecord(
			List<NwbRecordDto> nwbRecordDtos, NwbRecordAttachDto nwbRecordAttachDto) {
		List<NwbRecord> nwbRecords = new ArrayList<NwbRecord>();

		for (NwbRecordDto nwbRecordDto : nwbRecordDtos) {

			for (NwbRecordItemDto nwbRecordtItemDto : nwbRecordDto
					.getNwbRecordItemDtos()) {
				NwbRecord nwbRecord = new NwbRecord();
				nwbRecord.setRecordId(nwbRecordtItemDto.getRecordId());
				nwbRecord.setBedCode(nwbRecordtItemDto.getBedCode());
				nwbRecord.setPatId(nwbRecordtItemDto.getPatId());

				nwbRecord.setDeptCode(nwbRecordDto.getDeptCode());
				
				nwbRecord.setTemplateId(nwbRecordAttachDto.getTemplateId());
				nwbRecord.setNurseCode(nwbRecordAttachDto.getNurseCode());
				nwbRecord.setNurseName(nwbRecordAttachDto.getNurseName());
				nwbRecord.setCreateTime(nwbRecordAttachDto.getCreateTime());
				nwbRecords.add(nwbRecord);
			}

		}
		return nwbRecords;

	}

	@Override
	public List<NwbWhiteBoardItem> queryAllWhiteBoardItem() {
		return nurseWhiteBoardNurseMapper.queryAllWhiteBoardItem();
	}

	@Override
	public List<String> queryTempIdByDeptCode(String deptCode) {
		return nurseWhiteBoardNurseMapper.queryTempIdByDeptCode(deptCode);
	}

}
