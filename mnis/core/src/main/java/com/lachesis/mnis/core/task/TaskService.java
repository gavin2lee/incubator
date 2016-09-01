package com.lachesis.mnis.core.task;

import java.util.List;


public interface TaskService {


	
	/**
	 * 添加白板记录
	 * @param record
	 */
	void addWhiteBoardRecord(WhiteBoardRecord record);

	/**
	 * 获取白板记录
	 * @param deptId
	 * @param showDate
	 * @return
	 */
	List<WhiteBoardRecord> getWhiteList(String deptId,String showDate);
}
