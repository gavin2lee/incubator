package com.lachesis.mnis.core.redismsg;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lachesis.mnis.core.CriticalValueService;
import com.lachesis.mnis.core.InspectionService;
import com.lachesis.mnis.core.LabTestService;
import com.lachesis.mnis.core.OrderService;
import com.lachesis.mnis.core.RedisMsgService;
import com.lachesis.mnis.core.SkinTestService;
import com.lachesis.mnis.core.WardPatrolService;
import com.lachesis.mnis.core.critical.entity.CriticalValueRecord;
import com.lachesis.mnis.core.exception.AlertException;
import com.lachesis.mnis.core.inspection.entity.InspectionRecord;
import com.lachesis.mnis.core.labtest.entity.LabTestRecord;
import com.lachesis.mnis.core.order.entity.HisOrderGroup;
import com.lachesis.mnis.core.patrol.entity.WardPatrolInfo;
import com.lachesis.mnis.core.redismsg.entity.RedisMsgRecord;
import com.lachesis.mnis.core.redismsg.repository.RedisMsgRepository;
import com.lachesis.mnis.core.skintest.entity.SkinTestInfoLx;
import com.lachesis.mnis.core.util.DateUtil;

@Service
public class RedisMsgServiceImpl implements RedisMsgService{
	private static Logger LOGGER = LoggerFactory.getLogger(RedisMsgServiceImpl.class);
	@Autowired
	private RedisMsgRepository redisMsgRepository;
	
	@Autowired
	private CriticalValueService criticalValueService;
	@Autowired
	private LabTestService labTestService;
	@Autowired
	private InspectionService inspectionService;
	@Autowired
	private SkinTestService skinTestService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private WardPatrolService wardPatrolService;
	
	@Transactional
	@Override
	public int batchSaveRedisMsgRecords(List<RedisMsgRecord> redisMsgRecords) {
		int count = 0;
		Date date = new Date();
		for (RedisMsgRecord redisMsgRecord : redisMsgRecords) {
			if(redisMsgRecord.getOperDate() == null){
				redisMsgRecord.setOperDate(date);
			}
			if(redisMsgRepository.getSysMsgPublishByOperId(redisMsgRecord.getOperId(), redisMsgRecord.getOperType()) > 0){
				continue;
			}
			count = redisMsgRepository.saveRedisMsgRecord(redisMsgRecord);
		}
		return count;
	}

	@Override
	public List<CriticalValueRecord> getCriticalValueRecords(String deptCode) throws Exception {
		if(StringUtils.isBlank(deptCode)){
			LOGGER.error("错误信息异常：RedisMsgServiceImpl getCriticalValueRecords---部门为空!");
			throw new AlertException("错误信息异常：部门为空!");
		}
		return criticalValueService.getCriticalValueRecord(deptCode, null, null, null, false);
	}

	@Override
	public List<LabTestRecord> getPublishLabTests(String deptCode) {
		if(StringUtils.isBlank(deptCode)){
			LOGGER.error("错误信息异常：RedisMsgServiceImpl getPublishLabTests 部门为空!");
			throw new AlertException("错误信息异常：部门为空!");
		}
		Date dates[] = getQueryDate();
		return labTestService.getPublishLabTests(deptCode, dates[0], dates[1]);
	}

	@Override
	public List<InspectionRecord> getPublishInspections(String deptCode) {
		if(StringUtils.isBlank(deptCode)){
			LOGGER.error("错误信息异常：RedisMsgServiceImpl getPublishInspections 部门为空!");
			throw new AlertException("错误信息异常：部门为空!");
		}
//		Date dates[] = getQueryDate();
		Date dates[] = new Date[2];
		dates[0] = DateUtil.parse("2016-04-26");
		dates[1] = DateUtil.parse("2016-04-31");
		return inspectionService.getPublishInspections(deptCode, dates[0], dates[1]);
	}

	@Override
	public List<WardPatrolInfo> getPublishWardPatrols(String deptCode) {
		if(StringUtils.isBlank(deptCode)){
			LOGGER.error("错误信息异常：RedisMsgServiceImpl getPublishWardPatrols 部门为空!");
			throw new AlertException("错误信息异常：部门为空!");
		}
		Date dates[] = getQueryDate();
		return wardPatrolService.getPublishWardPatrols(deptCode, dates[0], dates[1]);
	}

	@Override
	public List<SkinTestInfoLx> getPublishSkinTests(String deptCode) {
		if(StringUtils.isBlank(deptCode)){
			LOGGER.error("错误信息异常：RedisMsgServiceImpl getPublishSkinTests 部门为空!");
			throw new AlertException("错误信息异常：部门为空!");
		}
		return skinTestService.getPublishSkinTests(deptCode, null, null);
	}
	
	private Date[] getQueryDate(){
		Date[] dates = new Date[2];
		Date date = new Date();
		dates[0] = DateUtils.addHours(date, -2);
		dates[1] = DateUtils.addHours(date, 2);
		return dates;
	}

	@Override
	public List<HisOrderGroup> getPublishChangeOrders(String deptCode)
			throws Exception {
		if(StringUtils.isBlank(deptCode)){
			LOGGER.error("错误信息异常：RedisMsgServiceImpl getPublishSkinTests 部门为空!");
			throw new AlertException("错误信息异常：部门为空!");
		}
		Date dates[] = getQueryDate();
		return orderService.getPublishOriginalOrders(deptCode, dates[0],  dates[1]);
	}

}
