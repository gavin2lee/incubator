package com.lachesis.mnis.core.infusionmonitor.repository.impl;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.lachesis.mnis.core.SpringTest;
import com.lachesis.mnis.core.infusionmonitor.entity.InfusionMonitorInfo;
import com.lachesis.mnis.core.infusionmonitor.entity.InfusionMonitorRecord;
import com.lachesis.mnis.core.infusionmonitor.repository.InfusionMonitorRepository;
import com.lachesis.mnis.core.util.DateUtil;
import com.lachesis.mnis.core.util.DateUtil.DateFormat;

public class InfusionMonitorRepositoryTest extends SpringTest {

	private static final String patId = "test1";
	@Autowired
	InfusionMonitorRepository infusionMonitorRepository;

	@Test
	public void testGetInfusionMonitorList() {
		List<String> patientList = new ArrayList<String>();
		patientList.add(patId);
		String[] dates = DateUtil.getTimeEndPoints("2014-11-18");
		infusionMonitorRepository
				.selectInfusionMonitorList(patientList,
						DateUtil.parse(dates[0], DateFormat.FULL), 
						DateUtil.parse(dates[1], DateFormat.FULL));
	}

	@Test
	public void testAddInfusionMonitor() {
		InfusionMonitorInfo info = new InfusionMonitorInfo();
		InfusionMonitorRecord record = new InfusionMonitorRecord();
		info.setCurrentRecord(record);
		record.setAbnormal(true);
		record.setAnomalyDisposal("已处置");
		record.setAnomalyMsg("过敏");
		record.setDeliverSpeed(60);
		record.setRecordDate(DateUtil.parse("2014-07-5 08:30:30", DateFormat.FULL));
		record.setRecordNurseId("000156");
		record.setRecordNurseName("测试护士1");
		record.setResidue(0);
		record.setStatus("N");
		info.setDeptId("1005");
		info.setOrderExecId("I1234567890");
		info.setPatientId(patId);
		info.setBedNo("1床");
		info.setPatientName("测试病人1");

		int count = infusionMonitorRepository.saveInfusionMonitor(info);
		Assert.isTrue(info.getCurrentRecord().getId() > 0);
		Assert.isTrue(count == 1);

	}

	@Test
	public void testSelectInfusionMonitorByExecId() {
		InfusionMonitorInfo info = infusionMonitorRepository
				.selectInfusionMonitorForOrderExec("I123456789");
		Assert.notNull(info);
	}

	@Test
	public void testModifyInfusionMonitor() {
		InfusionMonitorRecord record = new InfusionMonitorRecord();
		record.setAbnormal(false);
		record.setAnomalyDisposal("已处置2");
		record.setAnomalyMsg("过敏2");
		record.setDeliverSpeed(60);
		record.setResidue(100);
		record.setStatus("N");
		record.setId(1);

		int count = infusionMonitorRepository.updateInfusionMonitorItem(record);
		Assert.isTrue(count == 1);
	}
}
