package com.lachesis.mnis.core.infusionmonitor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lachesis.mnis.core.IdentityService;
import com.lachesis.mnis.core.InfusionMonitorService;
import com.lachesis.mnis.core.PatientService;
import com.lachesis.mnis.core.constants.MnisConstants;
import com.lachesis.mnis.core.event.InfusionManagerEvent;
import com.lachesis.mnis.core.event.MnisEventPublisher;
import com.lachesis.mnis.core.event.entity.InfusionManagerEventEntity;
import com.lachesis.mnis.core.exception.AlertException;
import com.lachesis.mnis.core.exception.MnisException;
import com.lachesis.mnis.core.infusionmonitor.entity.InfusionMonitorInfo;
import com.lachesis.mnis.core.infusionmonitor.entity.InfusionMonitorRecord;
import com.lachesis.mnis.core.infusionmonitor.repository.InfusionMonitorRepository;
import com.lachesis.mnis.core.task.TaskService;
import com.lachesis.mnis.core.util.DateUtil;
import com.lachesis.mnis.core.util.OrderUtil;
import com.lachesis.mnis.core.util.DateUtil.DateFormat;

@Service
public class InfusionMonitorServiceImpl implements InfusionMonitorService {
	@Autowired
	private InfusionMonitorRepository infusionMonitorRepository;

	@Autowired
	private TaskService taskService;

	@Autowired
	private PatientService patientService;

	@Autowired
	private IdentityService identityService;

	@Autowired
	private MnisEventPublisher mnisEventPublisher;
	static final Logger LOGGER = Logger
			.getLogger(InfusionMonitorServiceImpl.class);

	@Override
	public int saveInfusionMonitor(InfusionMonitorInfo infusionMonitorInfo) {
		if (infusionMonitorInfo == null
				|| infusionMonitorInfo.getOrderExecId() == null) {
			LOGGER.error("saveInfusionMonitor error:InfusionMonitorInfo.orderExecId 必须赋值！");
			throw new AlertException("参数错误!");
		}

		if (infusionMonitorInfo.getCurrentRecord().getRecordDate() == null) {
			Date recordDate = new Date(); // 默认为当前时间
			infusionMonitorInfo.getCurrentRecord().setRecordDate(recordDate);
		}
		// 更新医嘱执行表的对应执行的上次滴速
		/*
		 * orderRepository.updateOrderDeliverSpeed(
		 * infusionMonitorInfo.getOrderExecId(),
		 * infusionMonitorInfo.getDeliverSpeed());
		 */
		
		Integer isExistCount = infusionMonitorRepository.getMonitorCountByOrderExecId(infusionMonitorInfo.getOrderExecId(),null);
		int count = 0;
		if(null != isExistCount && isExistCount > 0){
			count = infusionMonitorRepository.updateInfusionMonitor(infusionMonitorInfo);
		}else{
			count = infusionMonitorRepository
					.saveInfusionMonitor(infusionMonitorInfo);
		}
		
		if (count > 0) {
			infusionMonitorInfo.getCurrentRecord().setOrderExecId(
					infusionMonitorInfo.getOrderExecId());
			count = infusionMonitorRepository
					.addInfusionMonitorItem(infusionMonitorInfo
							.getCurrentRecord());
		}

		return count;
	}

	@Override
	public List<InfusionMonitorInfo> getInfusionMonitor(String patientId,
			Date startDate, Date endDate, String isFinishType,
			String orderTypeCode) {
		List<InfusionMonitorInfo> infList = new ArrayList<InfusionMonitorInfo>();

		if (StringUtils.isEmpty(patientId)) {
			throw new MnisException("病人ID不能为空");
		} else {
			if (startDate == null || endDate == null) {
				Date[] dates = DateUtil.getQueryRegionDates(new Date());
				startDate = dates[0];
				endDate = dates[1];
			}
			if (StringUtils.isBlank(orderTypeCode)) {
				orderTypeCode = null;
			} else {
				orderTypeCode = OrderUtil.longTermType(orderTypeCode);
			}

			infList.addAll(infusionMonitorRepository.selectInfusionMonitor(
					patientId, startDate, endDate, isFinishType, orderTypeCode));
		}
		return infList;
	}

	@Override
	public InfusionMonitorInfo getMonitorLogForOrderExec(String orderExecId) {
		if (StringUtils.isNotBlank(orderExecId)) {
			return infusionMonitorRepository
					.selectInfusionMonitorForOrderExec(orderExecId);
		} else {
			throw new MnisException("执行医嘱ID不能为空");
		}
	}

	@Override
	public List<InfusionMonitorInfo> selectInfusionMonitorList(
			String workUnitCode, String nurseCode, String date) {
		/*
		 * if (StringUtils.isBlank(nurseCode) ||
		 * StringUtils.isBlank(workUnitCode)) { throw new
		 * MnisException("部门ID和护士ID参数至少需要一个"); }
		 * 
		 * List<String> patientList = patientService.getAttention(nurseCode,
		 * workUnitCode);
		 */
		List<String> patientList = null;

		if (StringUtils.isBlank(nurseCode) && StringUtils.isEmpty(workUnitCode)) {
			throw new MnisException("部门ID和护士ID参数至少需要一个");
		}
		// 获取病人列表
		patientList = patientService.getPatientByDeptCodeOrUserCode(nurseCode,
				workUnitCode);
		if (patientList == null || patientList.size() == 0) {
			patientList = new ArrayList<String>();
			patientList.add("-1");
		}
		Date queryDate = null;
		if (date == null) {
			queryDate = new Date();

		} else {
			queryDate = DateUtil.parse(date.substring(0, 10), DateFormat.YMD);
		}

		Date[] dates = DateUtil.getQueryRegionDates(queryDate);
		return infusionMonitorRepository.selectInfusionMonitorList(patientList,
				dates[0], dates[1]);
	}

	@Override
	public InfusionMonitorRecord updateInfusionMonitorItem(
			InfusionMonitorRecord infusion) {
		InfusionMonitorInfo info = new InfusionMonitorInfo();
		info.setOrderExecId(infusion.getOrderExecId());
		info.setStatus(infusion.getStatus());
		// 主表改变状态
		InfusionMonitorInfo selected = infusionMonitorRepository
				.selectInfusionMonitorForOrderExec(infusion.getOrderExecId());
		if (selected == null) {
			throw new MnisException("没有此巡视记录");
		}
		int updateCount = updateInfusionMonitor(info, selected.getStatus(),
				selected.getDeptId(), selected.getPatientId(),
				selected.getBedNo());
		// 记录表改变状态
		updateCount = infusionMonitorRepository
				.updateInfusionMonitorItem(infusion);

		if (updateCount == 0) {
			throw new MnisException("新增巡视记录失败!");
		}
		InfusionMonitorRecord record = infusionMonitorRepository
				.getInfusionMonitorRecordById(String.valueOf(infusion.getId()));
		return record;
	}

	@Override
	public InfusionMonitorRecord addInfusionMonitorItem(
			InfusionMonitorRecord infusion) {

		if (infusion == null || infusion.getOrderExecId() == null) {
			throw new MnisException("执行医嘱ID不能为空");
		}
		InfusionMonitorInfo selected = infusionMonitorRepository
				.selectInfusionMonitorForOrderExec(infusion.getOrderExecId());
		if (selected == null) {
			throw new MnisException("没有此巡视记录");
		}

		if (MnisConstants.ORDER_STATUS_FINISHED.equals(selected.getStatus())) {
			throw new MnisException("输液已完成，不能再巡视");
		}

		InfusionMonitorInfo info = new InfusionMonitorInfo();
		info.setOrderExecId(infusion.getOrderExecId());
		info.setStatus(infusion.getStatus());
		// 修改主表
		int updateCount = updateInfusionMonitor(info, selected.getStatus(),
				selected.getDeptId(), selected.getPatientId(),
				selected.getBedNo());

		infusion.setAbnormal(false);
		if (null == infusion.getRecordDate()) {
			infusion.setRecordDate(new Date());
		}
		
		if(StringUtils.isBlank(infusion.getSpeedUnit())){
			infusion.setSpeedUnit(MnisConstants.INFUSION_DEFAULT_SPEED_UNIT);
		}

		//设置剩余量和滴数
		if (selected.getRecords() != null
				&& (0 == infusion.getDeliverSpeed() || 0 == infusion
						.getResidue())) {
			int selectSize = (selected.getRecords().size() > 0) ? (selected
					.getRecords().size() - 1) : 0;
			InfusionMonitorRecord selectRecord = selected.getRecords().get(
					selectSize);
			int deliverSpeed = selectRecord.getDeliverSpeed();
			infusion.setDeliverSpeed(deliverSpeed);
			infusion.setResidue(getInfusionResidue(selectRecord.getResidue(),
					deliverSpeed, selectRecord.getRecordDate(),
					infusion.getRecordDate(),infusion.getSpeedUnit()));
		}

		updateCount = infusionMonitorRepository
				.addInfusionMonitorItem(infusion);
		if (updateCount == 0) {
			throw new MnisException("新增巡视记录失败!");
		}
		InfusionMonitorRecord record = infusionMonitorRepository
				.getInfusionMonitorRecordById(String.valueOf(infusion.getId()));
		return record;
	}

	@Override
	public int updateInfusionMonitor(InfusionMonitorInfo infusionMonitorInfo,
			String existStatus, String deptCode, String patId, String bedCode) {
		// TODO 当暂停,停止,拔针时，输出数据给输液监视
		if (identityService.isOpenIs()) {
			execInfusionManager(infusionMonitorInfo, existStatus, deptCode,
					patId, bedCode);
		}

		return infusionMonitorRepository
				.updateInfusionMonitor(infusionMonitorInfo);
	}

	/**
	 * 输液监控数据
	 * 
	 * @param infusionMonitorInfo
	 * @param existStatus
	 */
	private void execInfusionManager(InfusionMonitorInfo infusionMonitorInfo,
			String existStatus, String deptCode, String patId, String bedCode) {
		InfusionManagerEventEntity[] infusionManagerEventEntities = new InfusionManagerEventEntity[2];
		if (MnisConstants.ORDER_STATUS_PAUSED.equals(infusionMonitorInfo
				.getStatus())
				|| MnisConstants.ORDER_STATUS_STOPPED
						.equals(infusionMonitorInfo.getStatus())
				|| MnisConstants.ORDER_STATUS_END.equals(infusionMonitorInfo
						.getStatus())) {

			// 结束输液监控
			InfusionManagerEventEntity endInfusionManagerEventEntity = getInfusionManagerEventEntity(
					infusionMonitorInfo, deptCode, patId, bedCode);

			infusionManagerEventEntities[1] = endInfusionManagerEventEntity;
		} else if (MnisConstants.ORDER_STATUS_EXECUTED
				.equals(infusionMonitorInfo.getStatus())
				&& MnisConstants.ORDER_STATUS_PAUSED.equals(existStatus)) {
			// 重新开始输液监视
			InfusionManagerEventEntity execInfusionManagerEventEntity = getInfusionManagerEventEntity(
					infusionMonitorInfo, deptCode, patId, bedCode);
			infusionManagerEventEntities[0] = execInfusionManagerEventEntity;
		}

		if (infusionManagerEventEntities[0] != null
				|| infusionManagerEventEntities[1] != null) {
			// TODO 输出数据
			mnisEventPublisher.publish(new InfusionManagerEvent(this,
					infusionManagerEventEntities));
		}
	}

	/**
	 * 获取输液监视实体
	 * 
	 * @param infusionMonitorInfo
	 * @param deptCode
	 * @param patId
	 * @param bedCode
	 * @return
	 */
	private InfusionManagerEventEntity getInfusionManagerEventEntity(
			InfusionMonitorInfo infusionMonitorInfo, String deptCode,
			String patId, String bedCode) {
		InfusionManagerEventEntity infusionManagerEventEntity = new InfusionManagerEventEntity();
		infusionManagerEventEntity.setOrderGroupNo(infusionMonitorInfo
				.getOrderExecId());
		infusionManagerEventEntity.setInfusionStatus(infusionMonitorInfo
				.getStatus());
		infusionManagerEventEntity.setPatId(patId);
		infusionManagerEventEntity.setBedCode(bedCode);
		infusionManagerEventEntity.setDeptCode(deptCode);
		return infusionManagerEventEntity;
	}

	@Override
	public List<InfusionMonitorRecord> getInfusionMonitorRecordList(
			String patientId, List<String> orderExecIds, Date startDate,
			Date endDate) {
		List<String> patientIdList = null;
		if (patientId != null) {
			patientIdList = new ArrayList<String>();
			patientIdList.add(patientId);
		}

		if (startDate == null || endDate == null) {
			Date queryDate = new Date();
			startDate = DateUtil.setDateToDay(queryDate);
			endDate = DateUtil.setNextDayToDate(queryDate);
		}

		return infusionMonitorRepository.selectInfusionMonitorRecordList(
				patientIdList, orderExecIds, null, null);
	}

	/**
	 * 计算最新的剩余量
	 * 
	 * @param upResidue
	 * @param upDeliverSpeed
	 * @param upInfusionDate
	 * @param nowInfusionDate
	 * @return
	 */
	private int getInfusionResidue(int upResidue, int upDeliverSpeed,
			Date upInfusionDate, Date nowInfusionDate,String speedUnit) {
		long second = (nowInfusionDate.getTime() - upInfusionDate.getTime()) / 1000;
		//默认滴每分钟消耗多少ml
		int mlPerMin = (60 * MnisConstants.INFUSION_SPEED_PER_ML);
		//ml/时
		if(!StringUtils.isBlank(speedUnit) && !MnisConstants.INFUSION_DEFAULT_SPEED_UNIT.equals(speedUnit)){
			mlPerMin = 60*60;
		}
		long nowResidue = upResidue - (second * upDeliverSpeed)
				/ mlPerMin;
		return Integer.valueOf(String.valueOf(nowResidue > 0 ? nowResidue : 0));
	}

	@Override
	public boolean isPauseMonitorByOrderExecId(String orderExecId) {
		Integer countInteger = infusionMonitorRepository.getMonitorCountByOrderExecId(orderExecId,"1");
		return  null != countInteger && countInteger > 0? true:false;
	}
	
	@Override
	public void endInfusionMonitorByOrderExecId(String endOrderExecId,String userName,String userCode,Date recordDate){
		InfusionMonitorRecord endInfusionRecord = new InfusionMonitorRecord();

		endInfusionRecord.setRecordDate(recordDate == null ?new Date():recordDate);
		endInfusionRecord.setStatus(MnisConstants.ORDER_STATUS_FINISHED);
		endInfusionRecord.setOrderExecId(endOrderExecId);
		endInfusionRecord.setRecordNurseId(userCode);
		endInfusionRecord.setRecordNurseName(userName);
		addInfusionMonitorItem(endInfusionRecord);
	}
}
