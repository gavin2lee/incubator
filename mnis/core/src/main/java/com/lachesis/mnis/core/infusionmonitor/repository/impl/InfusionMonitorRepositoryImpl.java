package com.lachesis.mnis.core.infusionmonitor.repository.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.lachesis.mnis.core.infusionmonitor.entity.InfusionMonitorInfo;
import com.lachesis.mnis.core.infusionmonitor.entity.InfusionMonitorRecord;
import com.lachesis.mnis.core.infusionmonitor.repository.InfusionMonitorRepository;
import com.lachesis.mnis.core.mybatis.mapper.InfusionMonitorMapper;

@Repository("infusionMonitorRepository")
public class InfusionMonitorRepositoryImpl implements InfusionMonitorRepository {
	@Autowired
	private InfusionMonitorMapper infusionMonitorMapper;

	@Override
	public List<InfusionMonitorInfo> selectInfusionMonitor(String patientId,
			Date startDate, Date endDate,String isFinishType,String orderTypeCode) {
		return infusionMonitorMapper.selectInfusionMonitor(patientId,
				startDate, endDate,isFinishType, orderTypeCode);
	}

	@Override
	public int saveInfusionMonitor(InfusionMonitorInfo infusionMonitorInfo) {
		return infusionMonitorMapper.saveInfusionMonitor(infusionMonitorInfo);
	}

	@Override
	public InfusionMonitorInfo selectInfusionMonitorForOrderExec(
			String orderExecId) {
		return infusionMonitorMapper
				.selectInfusionMonitorForOrderExec(orderExecId);
	}

	@Override
	public List<InfusionMonitorInfo> selectInfusionMonitorList(List<String> patientList, Date startDate,
			Date endDate) {
		return infusionMonitorMapper.selectInfusionMonitorList(patientList, startDate, endDate);
	}

	@Override
	public int updateInfusionMonitorItem(InfusionMonitorRecord record) {
		return infusionMonitorMapper.updateInfusionMonitorItem(record);
	}

	@Override
	public int addInfusionMonitorItem(InfusionMonitorRecord record) {
		return infusionMonitorMapper.addInfusionMonitorItem(record);
	}

	@Override
	public int updateInfusionMonitor(InfusionMonitorInfo infusionMonitorInfo) {
		return infusionMonitorMapper.updateInfusionMonitor(infusionMonitorInfo);
	}

	@Override
	public List<InfusionMonitorRecord> selectInfusionMonitorRecordList(
			List<String> patientList,List<String> orderExecIds, Date startDate, Date endDate) {
		return infusionMonitorMapper.selectInfusionMonitorRecordList(patientList,orderExecIds, startDate, endDate);
	}

	@Override
	public InfusionMonitorRecord getInfusionMonitorRecordById(String id) {
		return infusionMonitorMapper.getInfusionMonitorRecordById(id);
	}

	@Override
	public int getMonitorCountByOrderExecId(String orderExecId,String isPause) {
		return infusionMonitorMapper.getMonitorCountByOrderExecId(orderExecId,isPause);
	}

}
