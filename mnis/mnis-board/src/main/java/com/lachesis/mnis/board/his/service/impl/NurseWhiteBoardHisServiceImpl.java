package com.lachesis.mnis.board.his.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lachesis.mnis.board.constants.BoardConstants;
import com.lachesis.mnis.board.dto.NwbRecordDto;
import com.lachesis.mnis.board.dto.NwbRecordItemDto;
import com.lachesis.mnis.board.entity.NwbHisCode;
import com.lachesis.mnis.board.entity.NwbHisCodeItem;
import com.lachesis.mnis.board.exception.MnisException;
import com.lachesis.mnis.board.his.mapper.NurseWhiteBoardHisMapper;
import com.lachesis.mnis.board.his.service.NurseWhiteBoardHisService;

/**
 * 处理his数据库接口
 * 
 * @author ThinkPad
 *
 */
@Service
public class NurseWhiteBoardHisServiceImpl implements NurseWhiteBoardHisService {
	private static Logger LOG = LoggerFactory
			.getLogger(NurseWhiteBoardHisServiceImpl.class);
	@Autowired
	private NurseWhiteBoardHisMapper nurseWhiteBoardHisMapper;

	@Override
	public List<NwbRecordDto> getNurseWhiteBoardsByDeptCode(String deptCode,
			String templateId) {
		if (StringUtils.isBlank(deptCode)) {
			LOG.debug("NurseWhiteBoardHisServiceImpl getNurseWhiteBoardsByDeptCode deptCode is null");
			throw new MnisException("科室为空!");
		}
		if (StringUtils.isBlank(templateId)) {
			LOG.debug("NurseWhiteBoardHisServiceImpl getNurseWhiteBoardsByDeptCode templateId is null");
			throw new MnisException("模板编号为空!");
		}
		// 获取映射code
		HashMap<String, String> codeMappingMap = BoardConstants.ORDER_CODE_MAPPING_CACHE
				.get(templateId);

		if (codeMappingMap == null || codeMappingMap.isEmpty()) {
			// 抛出异常给前端,配置信息无
			LOG.debug("NurseWhiteBoardHisServiceImpl getNurseWhiteBoardsByDeptCode codeMappingMap is null");
			throw new MnisException("编号映射配置为空!");
		}

		// 部门对应hisCode实体
		NwbHisCode nwbHisCode = BoardConstants.ORDER_CODES_CACHE
				.get(templateId);

		if (nwbHisCode == null) {
			LOG.debug("NurseWhiteBoardHisServiceImpl getNurseWhiteBoardsByDeptCode nwbHisCode is null");
			throw new MnisException("编号映射配置为空!");
		}
		List<String> hisCodes = new ArrayList<String>();
		// hisCode对应所有值
		for (String hisCode : codeMappingMap.values()) {
			if (hisCodes.contains(hisCode))
				continue;
			hisCodes.add(hisCode);
		}
		// 获取his数据
		List<NwbRecordDto> nwbRecordDtos = nurseWhiteBoardHisMapper
				.getNurseWhiteBoardsByDeptCode(deptCode, hisCodes);

		return convertDataFromHis(nwbRecordDtos,
				nwbHisCode.getNwbHisCodeItems());
	}

	/**
	 * 将his相关code转自身需要code
	 * 
	 * @param nwbRecords
	 * @param codeMap
	 * @return
	 */
	private List<NwbRecordDto> convertDataFromHis(
			List<NwbRecordDto> nwbRecordDtos,
			List<NwbHisCodeItem> nwbHisCodeItems) {
		List<NwbRecordDto> records = nwbRecordDtos;
		if (records == null || records.isEmpty()) {
			return records;
		}
		for (NwbRecordDto nwbRecordDto : records) {
			for (NwbHisCodeItem nwbHisCodeItem : nwbHisCodeItems) {
				// code与hisCode匹配转换
				if (isCodeMapping(nwbRecordDto.getCode(),
						nwbHisCodeItem.getHisCode(), nwbRecordDto.getFreq(),
						nwbHisCodeItem.getFreq())) {
					// 设置转换后的code值
					nwbRecordDto.setCode(nwbHisCodeItem.getCode());
					for (NwbRecordItemDto nwbRecordItemDto : nwbRecordDto
							.getNwbRecordItemDtos()) {
						nwbRecordItemDto.setCode(nwbHisCodeItem.getCode());
					}
				}
			}

		}

		return records;
	}

	/**
	 * 判断code与hisCode是否匹配
	 * 
	 * @param recordCode
	 * @param hisCode
	 * @param recordFreq
	 * @param freq
	 * @return
	 */
	private boolean isCodeMapping(String recordCode, String hisCode,
			String recordFreq, String freq) {
		boolean isCodeMapping = false;
		if (recordCode.equals(hisCode)) {
			if (StringUtils.isBlank(recordFreq) || StringUtils.isBlank(freq)) {
				isCodeMapping = true;
			} else {
				if (recordFreq.equals(freq)) {
					isCodeMapping = true;
				}
			}
		}
		return isCodeMapping;
	}

}
