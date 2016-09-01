package com.lachesis.mnis.core.task;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.lachesis.mnis.core.mybatis.mapper.TaskMapper;

@Repository("taskRepository")
public class TaskRepositoryImpl implements TaskRepository {
	@Autowired
	private TaskMapper taskMapper;

	@Override
	public WhiteBoardRecord getWhiteBoard(String itemCode, String deptId,
			String showDate) {
		return taskMapper.getWhiteBoard(itemCode, deptId, showDate);
	}

	@Override
	public void deleteWhiteBoard(String itemCode, String deptId, String showDate) {
		taskMapper.deleteWhiteBoard(itemCode, deptId, showDate);
	}

	@Override
	public void addWhiteBoardRecord(WhiteBoardRecord record) {
		taskMapper.addWhiteBoardRecord(record);
	}

	@Override
	public List<WhiteBoardRecord> getWhiteList(String deptId, String showDate) {
		return taskMapper.getWhiteList(deptId, showDate);
	}

	@Override
	public void deleteForShow() {
		taskMapper.deleteInfuMonitorItem();
		taskMapper.deleteInfuMonitor();
		taskMapper.deleteLiquorTable();
		taskMapper.deleteSkinTestItem();
		taskMapper.deleteWardPatrol();
		taskMapper.deleteOrderExec();
	}

}
