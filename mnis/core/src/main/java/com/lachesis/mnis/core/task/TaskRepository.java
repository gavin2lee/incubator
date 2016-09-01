package com.lachesis.mnis.core.task;

import java.util.List;


public interface TaskRepository {

	WhiteBoardRecord getWhiteBoard(String itemCode, String deptId, String showDate);

	void deleteWhiteBoard(String itemCode, String deptId, String showDate);

	void addWhiteBoardRecord(WhiteBoardRecord record);
	
	List<WhiteBoardRecord> getWhiteList(String deptId, String showDate);
	
	/*以下delete函数用于盛京医院演示时，清除测试数据*/
	public void deleteForShow();
}
