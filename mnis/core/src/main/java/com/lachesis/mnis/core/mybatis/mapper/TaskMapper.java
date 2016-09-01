package com.lachesis.mnis.core.mybatis.mapper;

import java.util.Date;
import java.util.List;

import com.lachesis.mnis.core.task.TaskEvent;
import com.lachesis.mnis.core.task.WhiteBoardAtt;
import com.lachesis.mnis.core.task.WhiteBoardRecord;

public interface TaskMapper {

	public void addWhiteBoardRecord(WhiteBoardRecord record);

	public void addWhiteBoardRecordItem(WhiteBoardAtt item);

	public List<WhiteBoardRecord> getWhiteList(String deptId,String showDate);
	
	public List<WhiteBoardRecord> getNoticeList(String nurseId, String deptId,
			Date startDate, Date endDate);

	public WhiteBoardAtt getWhiteAtt(String attId);

	public List<TaskEvent> selectPatientEvent(String deptCode, String startDate,
			String endDate);

	public WhiteBoardRecord getWhiteBoard(String itemCode, String deptId,
			String showDate);

	public void deleteWhiteBoard(String itemCode, String deptId, String showDate);
	
	/*以下delete函数用于盛京医院演示时，清除测试数据*/
	public void deleteInfuMonitor();
	public void deleteInfuMonitorItem();
	public void deleteLiquorTable();
	public void deleteSkinTestItem();
	public void deleteWardPatrol();
	public void deleteOrderExec();
}
