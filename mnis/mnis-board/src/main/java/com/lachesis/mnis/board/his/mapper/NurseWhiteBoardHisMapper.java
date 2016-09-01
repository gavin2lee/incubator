package com.lachesis.mnis.board.his.mapper;

import java.util.List;

import com.lachesis.mnis.board.dto.NwbRecordDto;

public interface NurseWhiteBoardHisMapper {
	List<NwbRecordDto> getNurseWhiteBoardsByDeptCode(String deptCode,
			List<String> codes);
}
