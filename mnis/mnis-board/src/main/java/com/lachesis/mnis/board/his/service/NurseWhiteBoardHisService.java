package com.lachesis.mnis.board.his.service;

import java.util.List;

import com.lachesis.mnis.board.dto.NwbRecordDto;

public interface NurseWhiteBoardHisService {
	/**
	 * 获取小白板记录
	 * @param deptCode
	 * @param templateId
	 * @return
	 */
	List<NwbRecordDto> getNurseWhiteBoardsByDeptCode(String deptCode,String templateId);
}
