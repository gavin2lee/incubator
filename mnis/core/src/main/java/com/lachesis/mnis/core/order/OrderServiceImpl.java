package com.lachesis.mnis.core.order;

import com.lachesis.mnis.core.*;
import com.lachesis.mnis.core.constants.MnisConstants;
import com.lachesis.mnis.core.event.DocReportEvent;
import com.lachesis.mnis.core.event.MnisEventPublisher;
import com.lachesis.mnis.core.event.MnisThreadPoolTaskExecutor;
import com.lachesis.mnis.core.event.entity.DocReportEventEntity;
import com.lachesis.mnis.core.event.entity.DocReportEventItemEntity;
import com.lachesis.mnis.core.exception.AlertException;
import com.lachesis.mnis.core.exception.MnisException;
import com.lachesis.mnis.core.identity.entity.UserInformation;
import com.lachesis.mnis.core.infusionmonitor.entity.InfusionMonitorInfo;
import com.lachesis.mnis.core.infusionmonitor.entity.InfusionMonitorRecord;
import com.lachesis.mnis.core.order.entity.*;
import com.lachesis.mnis.core.order.repository.OrderRepository;
import com.lachesis.mnis.core.patient.entity.Patient;
import com.lachesis.mnis.core.patient.entity.PatientBaseInfo;
import com.lachesis.mnis.core.task.TaskService;
import com.lachesis.mnis.core.util.*;
import com.lachesis.mnis.core.util.DateUtil.DateFormat;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Transactional
@Service("orderService")
public class OrderServiceImpl implements OrderService {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(OrderServiceImpl.class);

	@Autowired
	private MnisThreadPoolTaskExecutor mnisThreadPoolTaskExecutor;

	// 医嘱数据操作接口
	@Autowired
	private OrderRepository orderRepository;

	// 任务数据操作接口
	@Autowired
	private TaskService taskService;

	// 输液巡视数据操作接口
	@Autowired
	private InfusionMonitorService infusionMonitorService;

	// 病人数据操作接口
	@Autowired
	private PatientService patientService;

	// 护士数据操作接口
	@Autowired
	private IdentityService identityService;

	@Autowired
	private MnisEventPublisher mnisEventPulisher;

	@Autowired
	private NurseScanService nurseScanService;

	@Override
	public List<OrderExecGroup> getOrderBaseGroupList(String patientId,
			String orderTypeCode, String orderExecTypeCode, String status,
			Date startDate, Date endDate) {
		if (StringUtils.isBlank(patientId)) {
			patientId = null;
		}

		// 获取医嘱query时间
		Date[] queryDates = getOrderQueryDates(startDate, endDate,
				orderTypeCode);

		if (StringUtils.isBlank(orderTypeCode)) {
			orderTypeCode = null;
		} else {
			orderTypeCode = OrderUtil.longTermType(orderTypeCode);
		}

		if (StringUtils.isBlank(orderExecTypeCode)) {
			orderExecTypeCode = null;
		}

		if (StringUtils.isBlank(status)) {
			status = null;
		}

		List<OrderExecGroup> orders = orderRepository
				.selectOrderBaseGroupByPatId(patientId, orderTypeCode,
						orderExecTypeCode, status, queryDates[0], queryDates[1]);
		processOrderGroupExecution(orders);

		return orders;
	}

	/**
	 * 根据患者和医嘱id获取医嘱(扫描医嘱条码)
	 */
	@Override
	public OrderExecGroup getOrderGroupDetail(String orderGroupId,
			String patientId) {
		if (orderGroupId == null) {
			LOGGER.debug("OrderServiceImpl-> getOrderGroupDetail->orderGroupId is null");
			throw new MnisException(StringUtil.createErrorMsg(null,
					MnisConstants.ORD_NOT_EXIST, "条码为空"));
		}

		// 获取医嘱执行类型
		BarCodeVo vo = getOrderBarcodeType(orderGroupId, patientId, true);
		String barType = null;
		if (vo != null) {
			barType = vo.getBarType();
			orderGroupId = vo.getBarCode();
		}

		if (StringUtils.isBlank(barType)) {
			// 请求his
			HttpResponeMsg msg = WebServiceUtil.webServiceRequest(
					identityService.isSyncHis(),
					identityService.getSyncHisIp(),
					MnisConstants.WEB_SERVICE_HIS_ALL_ORDER_DATA, orderGroupId);
			if (msg != null) {
				barType = msg.getMsgType();
			} else {
				LOGGER.error("2天内无此条码","2天内无此条码");
				throw new MnisException(StringUtil.createErrorMsg(null,MnisConstants.ORD_NOT_EXIST, "2天内无此条码"));
			}
		} else {
			barType = barType.split(MnisConstants.LINE)[0];
		}

		// 获取原始医嘱状态
		String originalOrderStatus = orderRepository
				.getOriginalOrderStatusByOrderGroupId(orderGroupId, patientId);
		if (null != originalOrderStatus && "6".equals(originalOrderStatus)) {
			// 表示重整
			LOGGER.debug("OrderServiceImpl-> getOrderGroupDetail->originalOrderStatus 重整");
			throw new MnisException(StringUtil.createErrorMsg(barType,
					MnisConstants.ORD_REFORM, "该医嘱已重整,请重打条码!"));
		}

		OrderExecGroup result = null;
		switch (barType) {
		case MnisConstants.ORDER_EXEC_TYPE_LAB:
			result = orderRepository.getLABOrderGroupDetailByBarcode(
					orderGroupId, patientId);

			break;
		case MnisConstants.ORDER_EXEC_TYPE_ORAL:
			// 如果输液条码为空 查询口服液条码
			result = orderRepository.getORALOrderGroupDetail(orderGroupId,
					patientId);
			break;
		default:
			// 根据orderGroupId获取医嘱
			result = orderRepository.getOrderListScanGroupId(orderGroupId,
					patientId, null);

			if (result != null) {
				processOrderGroupExecution(result);
			}
			break;
		}

		// 计算统计值
		if (result != null) {
			// 统计当前执行时间日期内的医嘱执行类型相同的医嘱
			OrderCount orderCount = orderRepository.getOrderCount(result
					.getPatientId(), result.getOrderGroup()
					.getOrderExecTypeCode(), null, result.getOrderGroup()
					.getPlanExecTime());
			if (orderCount != null) {
				result.setPendingOrderCount(orderCount.getPendingOrderCount());
				// 剩余医嘱
				result.setFinishedOrderCount(orderCount.getFinishedOrderCount());
			}
		}else{
			throw new MnisException("无效条码!");
		}
		return result;
	}

	@Override
	public OrderExecGroup getBloodOrderGroupDetail(String orderGroupId,
			String secBarcode, String patientId) {
		if (null == orderGroupId || null == secBarcode) {
			LOGGER.debug("OrderServiceImpl-> getOrderGroupDetail->orderGroupId is null");
			throw new MnisException(StringUtil.createErrorMsg(
					MnisConstants.ORDER_EXEC_TYPE_BLOOD,
					MnisConstants.ORD_NOT_EXIST, "条码为空"));
		}
		// 获取输血信息
		OrderExecGroup result = orderRepository
				.getBloodOrderGroupDetailByBarcode(orderGroupId, secBarcode,
						patientId);

		if (result == null) {
			// his获取输血数据
			HttpResponeMsg msg = WebServiceUtil.webServiceRequest(
					identityService.isSyncHis(),
					identityService.getSyncHisIp(),
					MnisConstants.WEB_SERVICE_HIS_BLOOD_DATA, orderGroupId
							+ MnisConstants.LINE_SEMI_COLON + secBarcode);
			if (msg != null) {
				result = orderRepository.getBloodOrderGroupDetailByBarcode(
						orderGroupId, secBarcode, patientId);
			}
		}

		// 计算统计值
		if (result != null) {
			// 统计当前执行时间日期内的医嘱执行类型相同的医嘱
			OrderCount orderCount = orderRepository.getBloodOrderCount(result
					.getPatientId(), result.getOrderGroup().getPlanExecTime());
			if (orderCount != null) {
				result.setPendingOrderCount(orderCount.getPendingOrderCount());
				// 剩余医嘱
				result.setFinishedOrderCount(orderCount.getFinishedOrderCount());
			}
		}
		return result;
	}

	@Override
	public List<OrderExecGroup> getPendingOrderGroups(String patientId,
			String orderTypeCode, String orderExecTypeCode, Date startDate,
			Date endDate) {
		if (StringUtils.isBlank(orderTypeCode)) {
			orderTypeCode = null;
		}

		if (StringUtils.isBlank(orderExecTypeCode)) {
			orderExecTypeCode = null;
		}

		List<OrderExecGroup> unExecOrders = new ArrayList<OrderExecGroup>();
		if (patientId != null) {
			// 获取当天数据
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
			unExecOrders = orderRepository.selectUnExecutedOrderGroups(
					patientId, orderTypeCode, orderExecTypeCode, startDate,
					endDate);

			processOrderGroupExecution(unExecOrders);
		}
		return unExecOrders;
	}

	@Override
	public OrderExecGroup execBloodOrderGroupWithPDA(String orderExecId,
			String secBarcode, String patientId, UserInformation user,
			String approveNurseCode, String approveNurseName, Date execDate,
			boolean isScan) {
		// 1.校验医嘱信息
		if (StringUtils.isBlank(orderExecId) || StringUtils.isBlank(secBarcode)) {
			LOGGER.error("execBloodOrderGroupWithPDA exist null: orderExecId="
					+ orderExecId + ";secBarcode:" + secBarcode);
			throw new MnisException(StringUtil.createErrorMsg(
					MnisConstants.ORDER_EXEC_TYPE_BLOOD,
					MnisConstants.BAR_EMPTY, "条码为空"));
		}

		// 判断给定的条码是否执行
		if (orderRepository
				.getOrderExecCountById(orderExecId, secBarcode, null) > 0) {
			LOGGER.error("execOrderGroupWithPDA ：医嘱 orderExecId=" + orderExecId
					+ "已被执行");
			throw new MnisException(StringUtil.createErrorMsg(
					MnisConstants.ORDER_EXEC_TYPE_BLOOD,
					MnisConstants.ORD_EXECED, "医嘱已被执行"));
		}

		// 3.执行医嘱
		OrderExecGroup orderGroup = orderRepository
				.getBloodOrderGroupDetailByBarcode(orderExecId, secBarcode,
						patientId);

		if (null == orderGroup) {
			// his获取输血数据
			HttpResponeMsg msg = WebServiceUtil.webServiceRequest(
					identityService.isSyncHis(),
					identityService.getSyncHisIp(),
					MnisConstants.WEB_SERVICE_HIS_BLOOD_DATA, orderExecId
							+ MnisConstants.LINE_SEMI_COLON + secBarcode);
			if (msg != null) {
				orderGroup = orderRepository.getBloodOrderGroupDetailByBarcode(
						orderExecId, secBarcode, patientId);
			}

			if (null == orderGroup) {
				LOGGER.error("execBloodOrderGroupWithPDA blood order not exist");
				throw new MnisException(StringUtil.createErrorMsg(
						MnisConstants.ORDER_EXEC_TYPE_BLOOD,
						MnisConstants.ORD_NOT_EXIST, "医嘱不存在"));
			}
		}
		// 执行输血条码
		orderGroup = execBloodOrder(orderExecId, secBarcode, orderGroup,
				patientId, user, approveNurseCode, approveNurseName, execDate);

		return orderGroup;
	}

	@Override
	public OrderExecGroup execOrderGroupWithPDA(String orderExecId,
			String patientId, UserInformation user, String endOrderExecId,
			String orderExecType, Date execDate, boolean isScan) {
		// 1.校验医嘱信息
		if (StringUtils.isBlank(orderExecId)) {
			LOGGER.error("execOrderGroupWithPDA exist null: orderExecId="
					+ orderExecId);
			throw new AlertException(StringUtil.createErrorMsg(orderExecType,
					MnisConstants.BAR_EMPTY, "条码为空!"));
		}

		// 获取原始医嘱状态
		String originalOrderStatus = orderRepository
				.getOriginalOrderStatusByOrderGroupId(orderExecId, patientId);
		if (null != originalOrderStatus && "6".equals(originalOrderStatus)) {
			LOGGER.debug("OrderServiceImpl-> execOrderGroupWithPDA->originalOrderStatus 重整");
			throw new AlertException(StringUtil.createErrorMsg(orderExecType,
					MnisConstants.ORD_REFORM, "该医嘱已重整,请重打条码"));
		}

		// 判断给定的条码是否执行
		if (orderRepository.getOrderExecCountById(orderExecId, null, null) > 0) {
			LOGGER.error("execOrderGroupWithPDA ：医嘱 orderExecId=" + orderExecId
					+ "已被执行");
			throw new AlertException(StringUtil.createErrorMsg(orderExecType,
					MnisConstants.ORD_EXECED, "医嘱已被执行"));
		}

		// 2.判断医嘱状态
		String orderStatus = getOrderStatusByOrderGroupId(orderExecId, null,
				patientId);
		// 3.执行医嘱
		OrderExecGroup orderGroup = null;
		// 检验,包药机医嘱状态为4(无停止)
		if (StringUtils.isBlank(orderStatus)) {
			String webServiceMethod = null;
			// 判断是否需要his同步
			if (identityService.isSyncHis()) {

				// 医嘱不存在,根据条码判断医嘱类型
				switch (orderExecType) {
				case MnisConstants.ORDER_EXEC_TYPE_ORAL:
					// 包药机口服药
					webServiceMethod = MnisConstants.WEB_SERVICE_HIS_DRUG_BAG_DATA;
					break;
				case MnisConstants.ORDER_EXEC_TYPE_LAB:
					// 检验
					webServiceMethod = MnisConstants.WEB_SERVICE_HIS_LAB_TEST_DATA;
					break;
				default:
					// 根据待执行医嘱ID获取相应医嘱(判断医嘱是否存在)
					webServiceMethod = MnisConstants.WEB_SERVICE_HIS_ORDER_DATA;
					break;
				}
			}

			HttpResponeMsg msg = WebServiceUtil.webServiceRequest(
					identityService.isSyncHis(),
					identityService.getSyncHisIp(), webServiceMethod,
					orderExecId);

			if (msg == null) {
				LOGGER.error("execOrderGroupWithPDA ：医嘱 orderExecId="
						+ orderExecId + "医嘱条码无效");

				throw new AlertException(StringUtil.createErrorMsg(
						orderExecType, MnisConstants.ORD_HIS_NOT_EXIST,
						"HIS医嘱条码无效"));
			}else{
				//再次获取医嘱状态
				orderStatus = getOrderStatusByOrderGroupId(orderExecId, null,
						patientId);
			}
		} 
		
		if ("3".equals(orderStatus)) {
			LOGGER.error("execOrderGroupWithPDA ：医嘱 orderExecId=" + orderExecId
					+ "医嘱已停止");
			throw new AlertException(StringUtil.createErrorMsg(orderExecType,
					MnisConstants.ORD_STOP, "医嘱已停止"));
		} else {
			switch (orderExecType) {
			case MnisConstants.ORDER_EXEC_TYPE_ORAL:
				if ("4".equals(orderStatus)) {
					// 包药机口服药
					orderGroup = orderRepository.getORALOrderGroupDetail(
							orderExecId, patientId);
				} else {
					// 临时医嘱口服药
					orderGroup = orderRepository.getOrderListScanGroupId(
							orderExecId, patientId, "1");
				}
				break;
			case MnisConstants.ORDER_EXEC_TYPE_LAB:
				// 检验
				orderGroup = orderRepository.getLABOrderGroupDetailByBarcode(
						orderExecId, patientId);
				break;
			default:
				// 根据待执行医嘱ID获取相应医嘱(判断医嘱是否存在)
				orderGroup = orderRepository.getOrderListScanGroupId(
						orderExecId, patientId, "1");
				break;
			}
		}

		// 如果条码无法获取医嘱，那么跑出异常
		if (orderGroup == null) {

			throw new AlertException(StringUtil.createErrorMsg(orderExecType,
					MnisConstants.ORD_NOT_EXIST, "条码不存在"));
		}

		// 条码已存在,获取相关信息,直接执行
		if (MnisConstants.ORDER_EXEC_TYPE_ORAL.equals(orderExecType)
				|| MnisConstants.ORDER_EXEC_TYPE_LAB.equals(orderExecType)) {
			// 执行口服药或者执行检验
			orderGroup = execOralOrLabOrder(orderExecId, orderGroup, patientId,
					user, execDate);

		} else {
			// 2.根据待执行医嘱ID获取相应医嘱(判断医嘱是否存在)
			orderGroup = execOrder(orderGroup, orderExecId, patientId, user,
					endOrderExecId, execDate);
		}
		return orderGroup;
	}

	@Override
	public List<OrderExecGroup> batchExecOrderGroupWithPDA(String orderExecId,
			String patientId, UserInformation user, String approveNurseCode,
			String approveNurseName, String orderType, boolean isScan) {
		List<OrderExecGroup> results = new ArrayList<OrderExecGroup>();
		String[] ids = orderExecId.split(MnisConstants.COMMA);
		for (int i = 0; i < ids.length; i++) {
			String[] barcodes = ids[i].split(MnisConstants.LINE_COLON);
			OrderExecGroup orderGroup = null;
			if (barcodes.length > 1) {
				orderGroup = execBloodOrderGroupWithPDA(barcodes[0],
						barcodes[1], patientId, user, approveNurseCode,
						approveNurseName, null, isScan);
			} else {
				orderGroup = execOrderGroupWithPDA(barcodes[0], patientId,
						user, null, orderType, null, isScan);
			}

			results.add(orderGroup);
		}
		return results;
	}

	/**
	 * 执行医嘱
	 * 
	 * @param orderGroup
	 * @param orderExecId
	 * @param patientId
	 * @param user
	 * @param endOrderExecId
	 * @return
	 */
	private OrderExecGroup execOrder(final OrderExecGroup orderGroup,
			String orderExecId, String patientId, UserInformation user,
			String endOrderExecId, Date execDate) {

		if (StringUtils.isBlank(patientId)) {
			patientId = orderGroup.getOrderGroup().getPatientId();
		}

		// 1.执行一次执行记录
		final OrderExecLog execInfo = saveOrderExecLog(orderExecId, null, user,
				null, null, orderGroup.getOrderGroup(), execDate);

		// 设置执行信息
		orderGroup.setOrderExecLog(execInfo);

		// 2.插入输液类医嘱巡视记录
		if (MnisConstants.ORDER_EXEC_TYPE_INFUSION.equals(execInfo
				.getOrderExecType())) {
			// 执行输液巡视
			execInfusionOrder(orderGroup, execInfo, user, endOrderExecId);
			// 闭环输液数据转抄
			boolean isSynInfusionManager = ("1".equals(SuperCacheUtil.getSYSTEM_CONFIGS().get(
							MnisConstants.SYNC_INFUSION))) ? true
					: false;
			if (isSynInfusionManager) {
				String msg = publishInfusionManagerOrder(orderGroup,
						user.getDeptCode(), user.getUserId());
				LOGGER.debug("httpclientUtils:msg" + msg);
				// String path = PropertiesUtils
				// .findPropertiesKey(MnisConstants.PROP_PATH,MnisConstants.PROP_INFUSION_MANAGER_SAVEEXECORDER_URL);
				String path = SuperCacheUtil.getSYSTEM_CONFIGS().get(
						MnisConstants.INFUSION_IP);
				if (StringUtils.isNotBlank(path)) {
					path = path
							+ MnisConstants.INFUSION_MANAGER_SAVEEXECORDER_URL;
					mnisThreadPoolTaskExecutor
							.execute(new HttpClientUtils(
									MnisConstants.PROP_INFUSION_MANAGER_SAVEEXECORDER_PARAM,
									msg, path));
				} else {
					LOGGER.debug("httpclientUtils: infusion path is null");
				}
			}
		}

		// 发布文书数据
		if (getCopyOrderExecTypes(MnisConstants.COM_DIC_USAGE_COPY)
				.containsKey(execInfo.getOrderExecType())) {
			threadPoolPublishReportEvent(execInfo, orderGroup.getOrderGroup()
					.getOrderItems(),
					getInfusionResidue(orderGroup.getOrderGroup()));
		}

		// 封装医嘱数据
		processOrderGroupExecution(orderGroup);

		if (null != orderGroup.getOrderGroup()
				&& null != orderGroup.getOrderGroup().getOrderExecTypeCode()) {
			// 其他类型的设置为已完成
			if (!MnisConstants.ORDER_EXEC_TYPE_INFUSION.equals(orderGroup
					.getOrderGroup().getOrderExecTypeCode())) {
				orderGroup
						.setOrderExecStatusCode(MnisConstants.ORDER_STATUS_EXECUTED);
				orderGroup
						.setOrderExecStatusName(MnisConstants.ORDER_STATUS_EXECUTED_NAME);
			}
		}
		return orderGroup;

	}

	/**
	 * 线程池发布文书--医嘱
	 * 
	 * @param orderExecLog
	 * @param orderItems
	 * @param inTakeValue
	 */
	private void threadPoolPublishReportEvent(final OrderExecLog orderExecLog,
			final List<OrderItem> orderItems, final int inTakeValue) {
		if (!identityService.isSyncDocReport()) {
			LOGGER.debug("threadPoolPublishReportEvent isSyncDocReport is false");
			return;
		}
		//根据配置情况，如果没有出入量数据，则不执行转抄
		if(identityService.isOrderInOutCopy() && 0>=inTakeValue){
			LOGGER.debug("threadPoolPublishReportEvent isOrderInOutCopy and no inout value.");
			return;
		}
		mnisThreadPoolTaskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					boolean flag = has24InAndOutOrder(
							orderExecLog.getPatientId(), null, null);

					if (flag) {
						publishDocReportEvent(orderExecLog, orderItems,
								inTakeValue);
					}
				} catch (Exception e) {
					LOGGER.error("order publishDocReportEvent:"
							+ e.getMessage());
				}

			}
		});
	}

	/**
	 * 发布 文书入量信息
	 * 
	 * @param orderExecLog
	 * @param inTakeValue
	 */
	private void publishDocReportEvent(OrderExecLog orderExecLog,
			List<OrderItem> orderItems, int inTakeValue) {
		DocReportEventEntity docReportEventEntity = new DocReportEventEntity();
		docReportEventEntity.setCreate_person(orderExecLog.getExecNurseId()
				+ "-" + orderExecLog.getExecNurseName());
		docReportEventEntity.setCreateTime(orderExecLog.getExecDate());
		String execDateStr = DateUtil.format(orderExecLog.getExecDate(),
				DateFormat.FULL);
		String[] execDateStrs = execDateStr.split(" ");
		docReportEventEntity.setDate_list(execDateStrs[0]);
		docReportEventEntity.setTime_list(execDateStrs[1]);
		docReportEventEntity.setInpatient_no(orderExecLog.getPatientId());
		docReportEventEntity.setDept_code(orderExecLog.getDetpId());
		docReportEventEntity.setBarcode(orderExecLog.getOrderExecBarcode());
		docReportEventEntity.setCreate_person(orderExecLog.getExecNurseId()
				+ MnisConstants.LINE + orderExecLog.getExecNurseName());

		List<DocReportEventItemEntity> docReportEventItemEntities = new ArrayList<DocReportEventItemEntity>();
		DocReportEventItemEntity nameDocReportEventItemEntity = new DocReportEventItemEntity();
		StringBuffer orderItemSB = new StringBuffer();
		// 输液药物名称，多个以;隔开
		for (int i = 0; i < orderItems.size(); i++) {
			if (StringUtils.isEmpty(orderItems.get(i).getUsageCode())) { // 如果用法为空，不转抄内容
				continue;
			}

			orderItemSB.append(orderItems.get(i).getOrderName());
			if (MnisConstants.ORDER_EXEC_TYPE_INJECT.equals(orderExecLog
					.getOrderExecType())
					|| MnisConstants.ORDER_EXEC_TYPE_ENEMA.equals(orderExecLog
							.getOrderExecType())) {
				orderItemSB.append(MnisConstants.LINE
						+ orderItems.get(i).getDosage());
				orderItemSB.append(MnisConstants.LINE
						+ orderItems.get(i).getDosageUnit());
			}
			if (i != (orderItems.size() - 1)) {
				orderItemSB.append(MnisConstants.SEMI_COLON);
			}

		}

		// 皮试执行记录 转抄到文书
		if (MnisConstants.ORDER_EXEC_TYPE_SKINTEST.equals(orderExecLog
				.getOrderExecType())) {
			nameDocReportEventItemEntity.setRecord_value("执行皮试："
					+ orderItemSB.toString());
			nameDocReportEventItemEntity.setTemplate_item_id("remark1");
			docReportEventItemEntities.add(nameDocReportEventItemEntity);
		} else {
			nameDocReportEventItemEntity
					.setRecord_value(orderItemSB.toString());
			nameDocReportEventItemEntity
					.setTemplate_item_id(MnisConstants.COPY_DOC_INPUT_NAME);
			docReportEventItemEntities.add(nameDocReportEventItemEntity);

			DocReportEventItemEntity valueDocReportEventItemEntity = new DocReportEventItemEntity();
			valueDocReportEventItemEntity.setRecord_value(String
					.valueOf(inTakeValue));
			valueDocReportEventItemEntity
					.setTemplate_item_id(MnisConstants.COPY_DOC_IN_TAKE);
			docReportEventItemEntities.add(valueDocReportEventItemEntity);

		}
		docReportEventEntity.setData_item(docReportEventItemEntities);
		mnisEventPulisher
				.publish(new DocReportEvent(this, docReportEventEntity));
	}

	/**
	 * 执行输液医嘱，巡视信息处理
	 * 
	 * @param orderGroup
	 * @param execInfo
	 * @param user
	 * @param endOrderExecId
	 */
	private void execInfusionOrder(OrderExecGroup orderGroup,
			OrderExecLog execInfo, UserInformation user, String endOrderExecId) {
		String usageName = orderGroup.getOrderGroup().getUsageName();
		Patient patient = patientService.getPatientByPatId(orderGroup
				.getPatientId());
		InfusionMonitorInfo info = new InfusionMonitorInfo();
		InfusionMonitorRecord record = new InfusionMonitorRecord();
		info.setCurrentRecord(record);
		record.setAbnormal(false);
		record.setRecordDate(execInfo.getExecDate());
		record.setRecordNurseId(user.getUserId());
		record.setRecordNurseName(user.getName());
		// 第一次剩余量为所有ml的剂量
		record.setResidue(getInfusionResidue(orderGroup.getOrderGroup()));
		record.setStatus(MnisConstants.ORDER_STATUS_MONITOR);
		// 设置滴数默认单位
		record.setSpeedUnit(MnisConstants.INFUSION_DEFAULT_SPEED_UNIT);

		info.setDeptId(user.getDeptCode());
		info.setOrderExecId(execInfo.getOrderExecId());
		info.setPatientId(execInfo.getPatientId());
		info.setBedNo(patient.getBedCode());
		info.setPatientName(patient.getName());
		info.setDeliverFreq(orderGroup.getOrderGroup().getDeliverFreq());
		info.setUsageName(usageName);
		info.setStatus(MnisConstants.ORDER_STATUS_MONITOR);
		try {
			infusionMonitorService.saveInfusionMonitor(info);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			LOGGER.info("保存输液巡视失败");
		}
		// 获取待完成输液医嘱ids
		List<String> endOrderExecIds = new ArrayList<String>();
		if (StringUtils.isNotBlank(endOrderExecId)) {
			boolean isPause = infusionMonitorService
					.isPauseMonitorByOrderExecId(endOrderExecId);
			if (!isPause) {
				endOrderExecIds.add(endOrderExecId);
			}
		} else {// 获取该患者其他正在输液医嘱
			List<InfusionMonitorInfo> infusionMonitorInfos = infusionMonitorService
					.getInfusionMonitor(orderGroup.getPatientId(), null, null,
							"0", orderGroup.getOrderGroup().getOrderTypeCode());
			for (InfusionMonitorInfo infusionMonitorInfo : infusionMonitorInfos) {
				// 暂停的不结束
				if (MnisConstants.ORDER_STATUS_PAUSED
						.equals(infusionMonitorInfo.getStatus())) {
					continue;
				}
				endOrderExecIds.add(infusionMonitorInfo.getOrderExecId());
			}
			// 不会结束待执行医嘱
			if (endOrderExecIds.contains(execInfo.getOrderExecId())) {
				endOrderExecIds.remove(execInfo.getOrderExecId());
			}
		}

		// 4 完成已执行输液医嘱
		for (String orderExecId : endOrderExecIds) {
			infusionMonitorService.endInfusionMonitorByOrderExecId(orderExecId,
					user.getName(), user.getUserId(), new Date());
		}
	}

	/**
	 * 执行口服药医嘱
	 * 
	 * @param labBarcode
	 *            :口服药条码
	 * @param orderExecGroup
	 *            :医嘱组实体
	 * @param patientId
	 *            :患者ID
	 * @param user
	 *            :用户实体
	 * @param execDate
	 *            :执行时间
	 * @return
	 */
	private OrderExecGroup execOralOrLabOrder(String oralBarcode,
			OrderExecGroup orderExecGroup, String patientId,
			UserInformation user, Date execDate) {
		if (orderExecGroup == null || StringUtils.isBlank(patientId)) {
			return null;
		}
		// 医嘱执行封装
		OrderExecLog orderExecLog = null;
		try {
			orderExecLog = saveOrderExecLog(oralBarcode, null, user, null,
					null, orderExecGroup.getOrderGroup(), execDate);
		} catch (Exception e) {
			throw new AlertException("ORDER_FAIL-核对失败!");
		}
		orderExecGroup.setOrderExecLog(orderExecLog);
		orderExecGroup
				.setOrderExecStatusCode(MnisConstants.ORDER_STATUS_EXECUTED);
		orderExecGroup.getOrderGroup().setOrderStatusCode(
				MnisConstants.ORDER_STATUS_EXECUTED);

		return orderExecGroup;
	}

	/**
	 * 执行口服药医嘱
	 * 
	 * @param labBarcode
	 *            :口服药条码
	 * @param orderExecGroup
	 *            :医嘱组实体
	 * @param patientId
	 *            :患者ID
	 * @param user
	 *            :用户实体
	 * @param execDate
	 *            :执行时间
	 * @return
	 */
	private OrderExecGroup execBloodOrder(String barcode, String secBarcode,
			final OrderExecGroup orderExecGroup, final String patientId,
			UserInformation user, String approveNurseCode,
			String approveNurseName, Date execDate) {
		if (orderExecGroup == null || StringUtils.isBlank(patientId)) {
			return null;
		}
		// 医嘱执行封装
		OrderExecLog orderExecLog = null;
		try {
			orderExecLog = saveOrderExecLog(barcode, secBarcode, user,
					approveNurseCode, approveNurseName,
					orderExecGroup.getOrderGroup(), execDate);
		} catch (Exception e) {
			throw new AlertException("核对失败!");
		}
		orderExecGroup.setOrderExecLog(orderExecLog);
		orderExecGroup
				.setOrderExecStatusCode(MnisConstants.ORDER_STATUS_EXECUTED);
		orderExecGroup.getOrderGroup().setOrderStatusCode(
				MnisConstants.ORDER_STATUS_EXECUTED);

		// 输血发布文书

		threadPoolPublishReportEvent(orderExecGroup.getOrderExecLog(),
				orderExecGroup.getOrderGroup().getOrderItems(),
				getInfusionResidue(orderExecGroup.getOrderGroup()));

		return orderExecGroup;
	}

	/**
	 * 保存执行信息
	 * 
	 * @param orderExecId
	 * @param patientId
	 * @param user
	 * @param orderGroup
	 * @return
	 */
	private OrderExecLog saveOrderExecLog(String orderExecId,
			String secBarcode, UserInformation user, String approveNurseCode,
			String approveNurseName, OrderGroup orderGroup, Date execDate) {
		// 1.执行一次执行记录
		OrderExecLog execInfo = new OrderExecLog();
		execInfo.setOrderExecId(orderExecId);
		execInfo.setOrderExecBarcode(orderExecId);
		execInfo.setOrderExecGroupNo(orderExecId);
		execInfo.setSecBarcode(secBarcode);
		/**
		 * 医嘱类型，长嘱或临嘱
		 */
		execInfo.setOrderType(orderGroup.getOrderTypeCode());
		execInfo.setOrderExecType(orderGroup.getOrderExecTypeCode());
		execInfo.setPatientId(orderGroup.getPatientId());
		execInfo.setPlanDate(orderGroup.getPlanExecTime());
		execInfo.setExecDate(execDate == null ? new Date() : execDate);

		execInfo.setExecNurseId(user.getUserId());
		execInfo.setExecNurseName(user.getName());
		execInfo.setApproveNurseId(approveNurseCode);
		execInfo.setApproveNurseName(approveNurseName);
		execInfo.setDetpId(user.getDeptCode());

		orderRepository.insertSingleOrderExecution(execInfo);
		return execInfo;
	}

	@Override
	public OrderExecGroup getOrderGroupByExecId(String execId, String patientId) {
		OrderExecGroup orderGroup = orderRepository.getOrderListScanGroupId(
				execId, patientId, "1");
		if (orderGroup != null) {
			processOrderGroupExecution(orderGroup);
		}

		return orderGroup;
	}

	/**
	 * 处理医嘱状态和执行状态
	 * 
	 * @param orderGroup
	 */
	private boolean processOrderGroupExecution(OrderExecGroup orderGroup) {
		if (null == orderGroup.getOrderGroup()
				|| null == orderGroup.getOrderGroup().getOrderExecTypeCode()) {
			return false;
		}
		// 原始医嘱停止,执行医嘱置停止,长嘱
		if (StringUtils.isNotBlank(orderGroup.getOrderGroup()
				.getOrderTypeCode())
				&& MnisConstants.LONG_ORDER.equals(orderGroup.getOrderGroup()
						.getOrderTypeCode())
				&& orderRepository.isStopOrderOriginal(orderGroup
						.getOrderGroup().getOrderNo(), orderGroup
						.getPatientId())) {
			orderGroup.getOrderGroup().setOrderStatusCode(
					MnisConstants.ORDER_STATUS_STOPPED);
			orderGroup.getOrderGroup().setOrderStatusName(
					MnisConstants.ORDER_STATUS_STOPPED_NAME);
		}

		// 设置医嘱执行状态
		if (orderGroup.getOrderGroup().getOrderExecTypeCode()
				.equals(MnisConstants.ORDER_EXEC_TYPE_INFUSION)) {
			InfusionMonitorInfo monitor = infusionMonitorService
					.getMonitorLogForOrderExec(orderGroup.getOrderExecBarcode());
			// 如果是输液医嘱，则需要设置巡视状态
			if (monitor != null
					&& monitor.getStatus() != null
					&& !MnisConstants.ORDER_STATUS_NEW.equals(monitor
							.getStatus())) {
				orderGroup.setOrderExecStatusCode(monitor.getStatus());
				if (MnisConstants.ORDER_STATUS_MONITOR.equals(monitor
						.getStatus())) {
					orderGroup
							.setOrderExecStatusName(MnisConstants.ORDER_STATUS_MONITOR_NAME);
				} else if (MnisConstants.ORDER_STATUS_STOPPED.equals(monitor
						.getStatus())) {
					orderGroup
							.setOrderExecStatusName(MnisConstants.ORDER_STATUS_STOPPED_NAME);
				} else if (MnisConstants.ORDER_STATUS_FINISHED.equals(monitor
						.getStatus())) {
					orderGroup
							.setOrderExecStatusName(MnisConstants.ORDER_STATUS_FINISHED_NAME);
				} else if (MnisConstants.ORDER_STATUS_PAUSED.equals(monitor
						.getStatus())) {
					orderGroup
							.setOrderExecStatusName(MnisConstants.ORDER_STATUS_PAUSED_NAME);
				} else if (MnisConstants.ORDER_STATUS_END.equals(monitor
						.getStatus())) {
					orderGroup
							.setOrderExecStatusName(MnisConstants.ORDER_STATUS_END_NAME);
				}
			}
		}

		return true;
	}

	@Override
	public List<OrderExecGroup> getOrderBaseGroupForTask(
			List<String> patientIdList, String day, String orderExecType) {
		/*
		 * Date startDate = null; Date endDate = null; Date[] dates = new
		 * Date[2]; if (day != null) { Date date = DateUtil.parse(day,
		 * DateFormat.FULL); dates = DateUtil.getQueryRegionDates(date);
		 * 
		 * } else { dates = DateUtil.getQueryRegionDates(new Date()); }
		 * 
		 * startDate = dates[0]; endDate = dates[1];
		 * 
		 * List<OrderExecGroup> results = new ArrayList<OrderExecGroup>(); //
		 * 根据医嘱执行类型，null就是全部 List<OrderExecGroup> result = orderRepository
		 * .selectOrderBaseGroupByPatientIds(patientIdList, null, orderExecType,
		 * null, startDate, endDate); results.addAll(result); // 删除...
		 * 
		 * //查询输液类医嘱 if(orderExecType == null ||
		 * MnisConstants.ORDER_EXEC_TYPE_INFUSION.equals(orderExecType)){
		 * List<OrderExecGroup> result =
		 * orderRepository.selectOrderBaseGroupForTaskForInfu( patientIdList,
		 * startDate, endDate); results.addAll(result); }
		 * 
		 * //查询检验类医嘱 if(orderExecType == null ||
		 * MnisConstants.ORDER_EXEC_TYPE_LAB.equals(orderExecType)){
		 * List<OrderExecGroup> result =
		 * orderRepository.selectOrderBaseGroupForTaskForLab( patientIdList,
		 * startDate, endDate); results.addAll(result); }
		 * 
		 * //查询口服类医嘱 if(orderExecType == null ||
		 * MnisConstants.ORDER_EXEC_TYPE_ORAL.equals(orderExecType)){
		 * List<OrderExecGroup> result =
		 * orderRepository.selectOrderBaseGroupForTaskForPo( patientIdList,
		 * startDate, endDate); results.addAll(result); }
		 * 
		 * //查询查检类医嘱 if(orderExecType == null ||
		 * MnisConstants.ORDER_EXEC_TYPE_INSPECTION.equals(orderExecType)){
		 * List<OrderExecGroup> result =
		 * orderRepository.selectOrderBaseGroupForTaskForUc( patientIdList,
		 * startDate, endDate);
		 * 
		 * results.addAll(result); }
		 * 
		 * //查询治疗类医嘱 if(orderExecType == null ||
		 * MnisConstants.ORDER_EXEC_TYPE_TREATMENT.equals(orderExecType)){
		 * List<OrderExecGroup> result =
		 * orderRepository.selectOrderBaseGroupForTaskForUz( patientIdList,
		 * startDate, endDate);
		 * 
		 * results.addAll(result); }
		 * 
		 * 
		 * processOrderGroupExecution(results);
		 * 
		 * // 移除不可执行医嘱（已停止，已完成，错误） for (Iterator<OrderExecGroup> iterator =
		 * results.iterator(); iterator .hasNext();) { OrderExecGroup
		 * orderExecGroup = (OrderExecGroup) iterator.next(); if
		 * (MnisConstants.ORDER_STATUS_EXECUTED.equals(orderExecGroup
		 * .getOrderGroup().getOrderStatusCode()) ||
		 * MnisConstants.ORDER_STATUS_STOPPED.equals(orderExecGroup
		 * .getOrderGroup().getOrderStatusCode()) ||
		 * MnisConstants.ORDER_STATUS_FINISHED
		 * .equals(orderExecGroup.getOrderGroup() .getOrderStatusCode()))
		 * iterator.remove(); }
		 * 
		 * return results;
		 */
		return null;
	}

	@Override
	public List<OrderExecGroup> getOrderBaseGroupList(List<String> patientIds,
			String orderTypeCode, String state, String startTime, String endTime) {
		/*
		 * Date sTime = null; Date eTime = null; if
		 * (StringUtils.isBlank(startTime) && StringUtils.isBlank(endTime)) {
		 * Date[] dates = DateUtil.getQueryRegionDates(new Date()); sTime =
		 * dates[0]; eTime = dates[1];
		 * 
		 * } else { sTime = DateUtil.parse(startTime, DateFormat.FULL); eTime =
		 * DateUtil.parse(endTime, DateFormat.FULL); }
		 * 
		 * // 获取输入时间 或者 最近1天内的医嘱(48小数, 昨天~今天，新开立或者停止的医嘱) List<OrderExecGroup>
		 * orderList = orderRepository
		 * .selectOrderBaseGroupByPatientIds(patientIds, orderTypeCode, null,
		 * state, sTime, eTime);
		 * 
		 * processOrderGroupExecution(orderList);
		 * 
		 * return orderList;
		 */
		return null;
	}

	/**
	 * 处理医嘱状态和执行状态
	 * 
	 * @param orderList
	 */
	private void processOrderGroupExecution(List<OrderExecGroup> orderList) {

		for (Iterator<OrderExecGroup> iterator = orderList.iterator(); iterator
				.hasNext();) {
			OrderExecGroup orderExecGroup = (OrderExecGroup) iterator.next();
			if (!processOrderGroupExecution(orderExecGroup)) {
				continue;
			}
		}

	}

	private Map<String, Integer> countOrderExecTimes(List<String> patientIds,
			List<String> groupNoList, String defaultExecDate) {
		// 已分解过的医嘱 (当天自动分解已执行的才存在数据)
		List<OrderExecCount> orderExecCountList = new ArrayList<>();
		Map<String, Integer> orderExecCountMap = new HashMap<String, Integer>();
		for (OrderExecCount oec : orderExecCountList) {
			orderExecCountMap.put(oec.getOrderGroupId(),
					oec.getOrderExecTimes());
		}
		return orderExecCountMap;
	}

	public List<OrderExecGroup> decompAndCreateLXOrderExecGroup(
			List<OrderGroup> orderBaseGroups, List<String> patientIds,
			List<String> groupNoList) {
		String defaultPlanDate = DateUtil.format(DateFormat.YMD);
		// 今天执行过医嘱
		Map<String, Integer> orderExecCountMap = countOrderExecTimes(
				patientIds, groupNoList, defaultPlanDate);

		List<OrderExecGroup> orderExecGroups = new ArrayList<>();
		// 保存分解之后的数据
		List<OrderExecLog> execList = new ArrayList<>();
		String orderStatusCode = MnisConstants.ORDER_STATUS_NEW;
		String orderStatusName = "未执行";

		// 处理每个基本医嘱以获得执行计划
		OrderExecGroup execGroup = null;
		for (OrderGroup orderGroup : orderBaseGroups) {
			Integer executedTimes = orderExecCountMap.get(orderGroup
					.getOrderGroupNo());
			int i = executedTimes == null ? 0 : executedTimes.intValue();
			List<OrderExecLog> execInfos = getOrderExecPlanList(orderGroup,
					defaultPlanDate, i);
			execList.addAll(execInfos);
			// 修改数据的结构以方便前端显示；只有仍有执行计划的医嘱才会产生待执行医嘱
			for (OrderExecLog execInfo : execInfos) {
				execGroup = new OrderExecGroup();
				execGroup.setPlanExecTime(execInfo.getPlanDate());
				execGroup.setOrderExecBarcode(execInfo.getOrderExecBarcode());
				execGroup.setOrderExecLog(execInfo);
				execGroup.setOrderExecStatusCode(orderStatusCode);
				execGroup.setOrderExecStatusName(orderStatusName);
				execGroup.setOrderGroup(orderGroup);

				orderExecGroups.add(execGroup);
			}
		}
		return orderExecGroups;
	}

	/**
	 * 获取某医嘱组的执行计划列表（即医嘱分解得到执行计划）
	 * 
	 * @param orderGroup
	 * @return
	 */
	private List<OrderExecLog> getOrderExecPlanList(OrderGroup orderGroup,
			String defaultPlanDate, int orderExecutedTimes) {
		// 自主配药的医嘱按默认执行次数进行分解
		// 根据默认日执行次数和当日已执行次数来判断分解出多少执行计划
		int i = OrderUtil.convertFreqCodeToNumber(orderGroup.getDeliverFreq());
		String planTime = DateUtil.format(
				orderGroup.getPlanExecTime() == null ? new Date() : orderGroup
						.getPlanExecTime(), DateFormat.FULL);
		String[] planTimes = null;
		if (StringUtils.isNotBlank(planTime)) {
			planTimes = planTime.split(MnisConstants.COMMA);
		} else {
			planTimes = OrderUtil.setDefaultOrderPlanTimes(i);
		}

		boolean isLZOrder = orderGroup.getOrderTypeCode().equals(
				OrderUtil.ORDER_TYPE_TEMP);
		boolean isSTOrder = orderGroup.getDeliverFreq().equalsIgnoreCase(
				OrderUtil.ORDER_FREQ_ST);
		List<OrderExecLog> execPlan = new ArrayList<OrderExecLog>();
		if (i > orderExecutedTimes) {
			// 有当天剩余待执行的
			OrderExecLog orderExec = null;
			for (int n = orderExecutedTimes; n < i; n++) {
				orderExec = new OrderExecLog();
				orderExec.setOrderExecBarcode("z"
						+ orderGroup.getOrderGroupNo());
				if (isLZOrder || isSTOrder) {
					// 临时医嘱执行时间的开始时间,立即执行的医嘱
					orderExec.setPlanDate(orderGroup.getCreateDate());
				} else {
					// 首次执行
					if (n == 0
							&& (orderGroup.getOrderStatusCode().equals(
									MnisConstants.ORDER_STATUS_NEW) || orderGroup
									.getOrderStatusCode()
									.equals(MnisConstants.ORDER_STATUS_CONFIRMED))) {
						if (StringUtils.isBlank(orderGroup
								.getPlanFirstExecTime())) {
							// 首次默认执行时间已过,而计划首次执行时间为空，表名减少一次执行
							continue;
						} else {
							orderExec
									.setPlanDate(DateUtil.parse(
											defaultPlanDate
													+ MnisConstants.EMPTY
													+ orderGroup
															.getPlanFirstExecTime(),
											DateFormat.FULL));
						}
					} else {
						orderExec.setPlanDate(DateUtil.parse(defaultPlanDate
								+ MnisConstants.EMPTY + planTimes[n],
								DateFormat.FULL));
					}
				}
				orderExec.setOrderExecGroupNo(orderGroup.getOrderGroupNo());
				execPlan.add(orderExec);
			}
		}

		return execPlan;
	}

	@Override
	public void batchExecMultPatOrderGroupWithPDA(Map<String, String> records,
			UserInformation user, String approveNurseCode,
			String approveNurseName) {
		Set<String> keys = records.keySet();
		for (Iterator<String> iterator = keys.iterator(); iterator.hasNext();) {
			String key = (String) iterator.next();
			String value = records.get(key);
			batchExecOrderGroupWithPDA(value, key, user, approveNurseCode,
					approveNurseName, null, false);
		}
	}

	// 删除...
	@Override
	public List<OrderExecGroup> getShiftOrderList(List<String> patients,
			Date startDate, Date endDate) {
		// List<OrderExecGroup> orders = orderRepository.getShiftOrderList(
		// patients, startDate, endDate);
		// processOrderGroupExecution(orders);
		// return orders;
		return null;
	}

	/**
	 * 判断患者当天是否有24小时出入量
	 */
	@Override
	public boolean has24InAndOutOrder(String patientId, Date beginDate,
			Date endDate) {
		if (beginDate == null) {
			beginDate = DateUtil.getCurDateWithMinTime();
		}
		if (endDate == null) {
			endDate = DateUtil.getCurDateWithMaxTime();
		}
		int count = orderRepository.getInAndOutOrder(patientId, beginDate,
				endDate);
		if (count > 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean has24UrineOrder(String patientId, Date beginDate,
			Date endDate) {
		if (beginDate == null) {
			beginDate = DateUtil.getCurDateWithMinTime();
		}
		if (endDate == null) {
			endDate = DateUtil.getCurDateWithMaxTime();
		}
		int count = orderRepository
				.getUrineOrder(patientId, beginDate, endDate);
		if (count > 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public String getCreateDateTimeOfPatInoutOrder(String patId, String recordDay){
		Date beginDate =  DateUtil.getDateWithMinTime(recordDay);
		Date endDate = DateUtil.getDateWithMaxTime(recordDay);
		return orderRepository.getCreateDateTimeOfPatInoutOrder(patId, beginDate, endDate);

	}

	@Override
	public List<OrderBedInfo> getOrderBedInfoList(List<String> patientIds,
			String deptCode, String orderTypeCode, String orderExecTypeCode,
			Date startTime, Date endTime, boolean isOriginal) {
		if (patientIds == null || patientIds.size() == 0) {
			patientIds = null;
		}

		// 获取医嘱query时间
		Date[] queryDates = getOrderQueryDates(startTime, endTime,
				orderTypeCode);

		if (StringUtils.isBlank(orderTypeCode)) {
			orderTypeCode = null;
		} else {
			orderTypeCode = OrderUtil.longTermType(orderTypeCode);
		}
		if (StringUtils.isBlank(orderExecTypeCode)) {
			orderExecTypeCode = null;
		}

		List<OrderBedInfo> orderBedInfos = null;

		long start = new Date().getTime();
		// 查询原始医嘱床位信息
		if (isOriginal) {

			if ("0".equals(orderTypeCode)) {
				// 2.临嘱
				orderBedInfos = orderRepository
						.getOrderOriginalTempBedInfoList(patientIds, deptCode,
								orderTypeCode, orderExecTypeCode,
								queryDates[0], queryDates[1]);
			} else {
				// 1.长嘱
				orderBedInfos = orderRepository
						.getOrderOriginalLongBedInfoList(patientIds, deptCode,
								orderTypeCode, orderExecTypeCode,
								queryDates[0], queryDates[1]);
			}

		} else {
			// 执行医嘱床位信息
			if ("0".equals(orderTypeCode)) {
				// 2.临嘱
				orderBedInfos = orderRepository.getOrderTempBedInfoList(
						patientIds, deptCode, orderTypeCode, orderExecTypeCode,
						queryDates[0], queryDates[1]);
			} else {
				// 1.长嘱
				orderBedInfos = orderRepository.getOrderLongBedInfoList(
						patientIds, deptCode, orderTypeCode, orderExecTypeCode,
						queryDates[0], queryDates[1]);
			}

		}
		System.err.println("========时间差：======"
				+ (new Date().getTime() - start));
		return orderBedInfos;
	}

	@Override
	public int updateOrderNoByOrderGroupIds(String newOrderGroupId,
			String oldOrderGroupId) {
		// 1.根据医嘱id判断医嘱是否存在
		OrderExecGroup newOrderExecGroup = getOrderGroupByExecId(
				newOrderGroupId, null);
		if (newOrderExecGroup == null
				|| newOrderExecGroup.getOrderGroup() == null) {
			throw new MnisException("医嘱不存在!");
		}
		OrderExecGroup oldOrderExecGroup = getOrderGroupByExecId(
				oldOrderGroupId, null);
		if (oldOrderExecGroup == null
				|| oldOrderExecGroup.getOrderGroup() == null) {
			throw new MnisException("医嘱不存在!");
		}
		// 2.修改医嘱排序号newOrderNo:被排序号,oldOrderNo：待排序号
		String newOrderNo = newOrderExecGroup.getOrderGroup().getOrderSortNo();
		String oldOrderNo = oldOrderExecGroup.getOrderGroup().getOrderSortNo();
		orderRepository
				.updateOrderNoByOrderGroupId(newOrderGroupId, oldOrderNo);
		return orderRepository.updateOrderNoByOrderGroupId(oldOrderGroupId,
				newOrderNo);
	}

	/**
	 * 计算输液医嘱第一次剩余量
	 * 
	 * @param orderGroup
	 * @return
	 */
	private int getInfusionResidue(OrderGroup orderGroup) {
		int residue = 0;
		if (null == orderGroup || null == orderGroup.getOrderItems()
				|| orderGroup.getOrderItems().size() == 0) {
			return residue;
		}
		for (OrderItem item : orderGroup.getOrderItems()) {
			if (StringUtils.isNotBlank(item.getDosageUnit())
					&& MnisConstants.INFUSION_DOSAGE_UNIT.equals(item
							.getDosageUnit().toLowerCase())) {
				residue += item.getDosage();
			}
		}

		return residue;
	}

	/**
	 * 获取原始医嘱信息
	 */
	@Override
	public List<HisOrderGroup> getOriginalOrderList(String patientId,
			String orderTypeCode, String orderExecTypeCode, Date startDate,
			Date endDate) {
		if (StringUtils.isBlank(patientId)) {
			return null;
		}

		List<String> patientIds = new ArrayList<String>();
		patientIds.add(patientId);

		// 获取医嘱query时间
		Date[] queryDates = getOrderQueryDates(startDate, endDate,
				orderTypeCode);
		if (StringUtils.isBlank(orderTypeCode)) {
			orderTypeCode = null;
		} else {
			orderTypeCode = OrderUtil.longTermType(orderTypeCode);
		}
		if (StringUtils.isBlank(orderExecTypeCode)) {
			orderExecTypeCode = null;
		}

		String queryByOrderGroup = identityService
				.getConfigure(MnisConstants.QUERYBYORDERGROUP);
		if ("1".equals(queryByOrderGroup)) {
			return orderRepository.getOriginalOrderListOne(patientIds,
					orderTypeCode, orderExecTypeCode, queryDates[0],
					queryDates[1]);
		} else {
			return orderRepository.getOriginalOrderList(patientIds,
					orderTypeCode, orderExecTypeCode, queryDates[0],
					queryDates[1]);
		}
	}

	@Override
	public String getPatientIdByOrderGroupId(String orderGroupId, String type) {
		return orderRepository.getPatientIdByOrderGroupId(orderGroupId, type);
	}

	@Override
	public List<OrderUnprintStatistic> getUnprintOrderStatisticList(
			String deptCode, String orderTypeCode, Date startDate, Date endDate) {
		HashMap<String, Object> params = new HashMap<String, Object>();

		// 获取医嘱query时间
		Date[] queryDates = getOrderQueryDates(startDate, endDate,
				orderTypeCode);
		if (StringUtils.isBlank(orderTypeCode)) {
			orderTypeCode = null;
		} else {
			orderTypeCode = OrderUtil.longTermType(orderTypeCode);
		}
		params.put("deptCode", deptCode);
		params.put("orderTypeCode", orderTypeCode);
		params.put("startDate", queryDates[0]);
		params.put("endDate", queryDates[1]);
		return orderRepository.getUnprintOrderStatisticList(params);
	}

	@Override
	public List<OrderPrintInfo> getOrderPrintInfos(String deptCode,
			String orderTypeCode, String orderExecTypeCode, Date startDate,
			Date endDate, String orderPrinted, List<String> patIds) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		// 长嘱和临嘱
		if (StringUtils.isBlank(orderTypeCode)) {
			orderTypeCode = null;
		} else {
			orderTypeCode = OrderUtil.longTermType(orderTypeCode);
		}
		// 医嘱类型
		if (StringUtils.isBlank(orderExecTypeCode)) {
			orderExecTypeCode = null;
		}

		if (StringUtils.isBlank(orderPrinted)) {
			orderPrinted = null;
		}

		if (startDate == null || endDate == null) {
			Date[] dates = DateUtil.getQueryRegionDates(new Date());
			startDate = dates[0];
			endDate = dates[1];
		}

		params.put("deptCode", deptCode);
		params.put("orderTypeCode", orderTypeCode);
		params.put("orderExecTypeCode", orderExecTypeCode);
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		params.put("orderPrinted", orderPrinted);
		params.put("patIds", patIds);

		return orderRepository.getOrderPrintInfos(params);
	}

	@Override
	public String getOrderStatusByOrderGroupId(String orderGroupId,
			String type, String patId) {
		return orderRepository.getOrderStatusByOrderGroupId(orderGroupId, type,
				patId);
	}

	/**
	 * 发布 文书非医嘱类型 针对患者，护士需求通过手工录入一些信息，这些信息需要自动转抄到文书 比如：口腔护理
	 * 
	 * @param orderExecLog
	 * @param inTakeValue
	 */
	@Override
	public void publishDocReportByManual(
			DocReportEventEntity docReportEventEntity) {
		Date execDate = new Date();// 执行时间
		docReportEventEntity.setCreateTime(execDate);
		String execDateStr = DateUtil.format(execDate, DateFormat.FULL);
		String[] execDateStrs = execDateStr.split(" ");
		docReportEventEntity.setDate_list(execDateStrs[0]);
		docReportEventEntity.setTime_list(execDateStrs[1]);
		mnisEventPulisher
				.publish(new DocReportEvent(this, docReportEventEntity));
	}

	/**
	 * 获取医嘱查询时间
	 * 
	 * @param startDate
	 * @param endDate
	 * @param orderTypeCode
	 * @return
	 */
	private Date[] getOrderQueryDates(Date startDate, Date endDate,
			String orderTypeCode) {
		Date[] queryDates = new Date[2];
		if (null == startDate || null == endDate) {
			// 指定时间为null,去默认时间
			queryDates = DateUtil.getQueryRegionDates(new Date());
		} else {
			queryDates[0] = startDate;
			queryDates[1] = endDate;
		}

		if (StringUtils.isBlank(orderTypeCode)) {
			orderTypeCode = null;
		} else {
			if (OrderUtil.isTempOrder(orderTypeCode)) {
				// 如果临时医嘱,取当前时间前后12小时
				if (null == startDate) {
					queryDates = DateUtil.getQueryHourRegionDates(new Date(),
							-12, 12);
				}
			}
		}

		return queryDates;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getOrderExecTypes() {
		Map<String, Object> rs = new TreeMap<String, Object>();

		if (SuperCacheUtil.CACHE_DATA.containsKey("orderExecTypes")) {
			rs = (Map<String, Object>) SuperCacheUtil.CACHE_DATA
					.get("orderExecTypes");
			return rs;
		}
		List<Map<String, Object>> orderExecTypes = orderRepository
				.getOrderExecTypes();

		for (Map<String, Object> execTypeMap : orderExecTypes) {
			rs.put((String) execTypeMap.get("code"),
					(String) execTypeMap.get("name"));
		}
		SuperCacheUtil.CACHE_DATA.put("orderExecTypes", rs);
		return rs;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getOrderUsageTypes() {
		Map<String, Object> rs = new HashMap<String, Object>();

		if (SuperCacheUtil.CACHE_DATA.containsKey("orderUsageTypes")) {
			rs = (Map<String, Object>) SuperCacheUtil.CACHE_DATA
					.get("orderUsageTypes");
			return rs;
		}
		List<Map<String, Object>> orderExecTypes = orderRepository
				.getOrderUsageTypes();

		for (Map<String, Object> execTypeMap : orderExecTypes) {
			rs.put((String) execTypeMap.get("code"),
					(String) execTypeMap.get("name"));
		}
		SuperCacheUtil.CACHE_DATA.put("orderUsageTypes", rs);
		return rs;
	}

	@Override
	public List<OrderExecDocumentInfo> getOrdExecDocInfos(String deptCode,
			List<String> patIds, String startDate, String endDate,
			int execType, int docType) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		if (null == patIds || patIds.size() == 0) {
			patIds = null;
		}

		if (StringUtils.isBlank(startDate)) {
			startDate = null;
		}

		if (StringUtils.isBlank(endDate)) {
			endDate = null;
		}

		params.put("deptCode", deptCode);
		params.put("patIds", patIds);
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		params.put("execType", execType);
		params.put("docType", docType);

		// 执行医嘱中排除口服药医嘱
		List<String> orderSubNos = orderRepository
				.getOrderSubNoFromDrugBag(params);

		params.put("orderSubNos", orderSubNos);
		params.put("hospitalCode",
				SuperCacheUtil.getSYSTEM_CONFIGS().get("hospitalCode"));

		// 执行医嘱
		List<OrderExecDocumentInfo> orderExecDocumentInfos = orderRepository
				.getOrdExecDocInfos(params);
		// 包药机口服药
		if (orderSubNos != null && !orderSubNos.isEmpty()) {
			List<OrderExecDocumentInfo> oralOrderExecDocumentInfos = getOrdExecDocInfosToOral(
					deptCode, patIds, startDate, endDate, execType, docType);
			orderExecDocumentInfos = mergeOrderExecDocInfos(
					orderExecDocumentInfos, oralOrderExecDocumentInfos);
		}

		// 检验
		List<OrderExecDocumentInfo> labOrderExecDocumentInfos = getOrdExecDocInfosToLab(
				deptCode, patIds, startDate, endDate, execType, docType);
		orderExecDocumentInfos = mergeOrderExecDocInfos(orderExecDocumentInfos,
				labOrderExecDocumentInfos);
		// 输血
		if (orderSubNos != null && !orderSubNos.isEmpty()) {
			List<OrderExecDocumentInfo> bloodOrderExecDocumentInfos = getOrdExecDocInfosToBlood(
					deptCode, patIds, startDate, endDate, execType, docType);
			orderExecDocumentInfos = mergeOrderExecDocInfos(
					orderExecDocumentInfos, bloodOrderExecDocumentInfos);
		}

		// 合并项目
		return orderExecDocumentInfos;
	}

	@Override
	public List<OrderExecDocumentInfo> getOrdExecDocInfosToOral(
			String deptCode, List<String> patIds, String startDate,
			String endDate, int execType, int docType) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		if (null == patIds || patIds.size() == 0) {
			patIds = null;
		}
		if (StringUtils.isBlank(startDate)) {
			startDate = null;
		}

		if (StringUtils.isBlank(endDate)) {
			endDate = null;
		}
		params.put("deptCode", deptCode);
		params.put("patIds", patIds);
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		params.put("execType", execType);
		params.put("docType", docType);
		return orderRepository.getOrdExecDocInfosToOral(params);
	}

	@Override
	public List<OrderExecDocumentInfo> getOrdExecDocInfosToLab(String deptCode,
			List<String> patIds, String startDate, String endDate,
			int execType, int docType) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		if (null == patIds || patIds.size() == 0) {
			patIds = null;
		}
		if (StringUtils.isBlank(startDate)) {
			startDate = null;
		}

		if (StringUtils.isBlank(endDate)) {
			endDate = null;
		}
		params.put("deptCode", deptCode);
		params.put("patIds", patIds);
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		params.put("execType", execType);
		params.put("docType", docType);

		return orderRepository.getOrdExecDocInfosToLab(params);
	}

	@Override
	public List<OrderExecDocumentInfo> getOrdExecDocInfosToBlood(
			String deptCode, List<String> patIds, String startDate,
			String endDate, int execType, int docType) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		if (null == patIds || patIds.size() == 0) {
			patIds = null;
		}
		if (StringUtils.isBlank(startDate)) {
			startDate = null;
		}

		if (StringUtils.isBlank(endDate)) {
			endDate = null;
		}
		params.put("deptCode", deptCode);
		params.put("patIds", patIds);
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		params.put("execType", execType);
		params.put("docType", docType);

		return orderRepository.getOrdExecDocInfosToBlood(params);
	}

	@Override
	public List<OrderExecDocumentInfo> getOrdExecDocInfosOnInfuCard(
			String deptCode, List<String> patIds, String startDate,
			String endDate) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		if (null == patIds || patIds.size() == 0) {
			patIds = null;
		}
		if (StringUtils.isBlank(startDate)) {
			startDate = null;
		}

		if (StringUtils.isBlank(endDate)) {
			endDate = null;
		}
		params.put("deptCode", deptCode);
		params.put("patIds", patIds);
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		params.put("hospitalCode",
				SuperCacheUtil.getSYSTEM_CONFIGS().get("hospitalCode"));
		return orderRepository.getOrdExecDocInfosOnInfuCard(params);
	}

	@Override
	public List<OrderExecDocumentInfo> getOrdExecDocInfosOnLabel(
			String deptCode, List<String> patIds, String startDate,
			String endDate) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		if (null == patIds || patIds.size() == 0) {
			patIds = null;
		}
		if (StringUtils.isBlank(startDate)) {
			startDate = null;
		}

		if (StringUtils.isBlank(endDate)) {
			endDate = null;
		}
		params.put("deptCode", deptCode);
		params.put("patIds", patIds);
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		// 执行医嘱
		List<OrderExecDocumentInfo> orderExecDocumentInfos = orderRepository
				.getOrdExecDocInfosOnLabel(params);
		// 包药机口服药
		List<OrderExecDocumentInfo> oralOrderExecDocumentInfos = getOrdExecDocInfosToOral(
				deptCode, patIds, startDate, endDate, 0, 0);
		// 检验
		List<OrderExecDocumentInfo> labOrderExecDocumentInfos = getOrdExecDocInfosToLab(
				deptCode, patIds, startDate, endDate, 0, 0);

		// 合并项目
		return mergeOrderExecDocInfos(
				mergeOrderExecDocInfos(orderExecDocumentInfos,
						oralOrderExecDocumentInfos), labOrderExecDocumentInfos);
	}

	@Override
	public List<String> saveOrdExecDocPrtInfs(
			List<OrderExecDocumentPrintInfo> orderExecDocumentPrintInfos) {
		List<String> printIds = new ArrayList<String>();
		for (OrderExecDocumentPrintInfo orderExecDocumentPrintInfo : orderExecDocumentPrintInfos) {
			if (null == orderExecDocumentPrintInfo) {
				continue;
			}
			String printId = orderExecDocumentPrintInfo.getPrintId();
			if (StringUtils.isBlank(orderExecDocumentPrintInfo.getPrintId())
					|| (orderRepository
							.getOrdExecDocPrtInfCount(orderExecDocumentPrintInfo
									.getPrintId()) == 0)) {
				printId = orderRepository
						.saveOrdExecDocPrtInf(orderExecDocumentPrintInfo);
			} else {
				orderRepository
						.updateOrdExecDocPrtInf(orderExecDocumentPrintInfo);

			}

			List<String> orderNos = orderExecDocumentPrintInfo.getOrderNos();
			if (orderNos != null && orderNos.isEmpty()) {
				List<OrderApprove> orderApproves = new ArrayList<OrderApprove>();
				for (String orderNo : orderNos) {
					OrderApprove orderApprove = new OrderApprove();
					orderApprove.setApproveDate(orderExecDocumentPrintInfo
							.getCheckDate());
					orderApprove.setApproveNurseCode(orderExecDocumentPrintInfo
							.getCheckNurseCode());
					orderApprove.setApproveNurseName(orderExecDocumentPrintInfo
							.getCheckNurseName());
					orderApprove.setApproveType(orderExecDocumentPrintInfo
							.getOrdType());
					orderApprove
							.setPatId(orderExecDocumentPrintInfo.getPatId());
					orderApprove.setDeptCode(orderExecDocumentPrintInfo
							.getDeptCode());
					orderApprove.setOrderNo(orderNo);

					orderApproves.add(orderApprove);
				}
				batchSaveOrderApprove(orderApproves);
			}

			printIds.add(printId);
		}

		return printIds;
	}

	@Override
	public List<String> getOrderSubNoFromDrugBag(String deptCode,
			List<String> patIds, Date startDate, Date endDate) {
		HashMap<String, Object> params = new HashMap<String, Object>();

		params.put("deptCode", deptCode);
		params.put("patIds", patIds);
		params.put("startDate", startDate);
		params.put("endDate", endDate);

		return orderRepository.getOrderSubNoFromDrugBag(params);
	}

	/**
	 * 合并统一患者统一床号,并对床号重新排序
	 * 
	 * @param mainOrderExecDocumentInfos
	 *            主记录
	 * @param assistOrderExecDocumentInfos
	 *            辅记录
	 * @return
	 */
	private List<OrderExecDocumentInfo> mergeOrderExecDocInfos(
			List<OrderExecDocumentInfo> mainOrderExecDocumentInfos,
			List<OrderExecDocumentInfo> assistOrderExecDocumentInfos) {
		List<OrderExecDocumentInfo> rsltDocumentInfos = new ArrayList<OrderExecDocumentInfo>();
		List<OrderExecDocumentInfo> removeDocumentInfos = new ArrayList<OrderExecDocumentInfo>();
		if (mainOrderExecDocumentInfos.isEmpty()) {
			rsltDocumentInfos.addAll(assistOrderExecDocumentInfos);
		} else {

			for (OrderExecDocumentInfo mainDocumentInfo : mainOrderExecDocumentInfos) {
				if (StringUtils.isBlank(mainDocumentInfo.getBedCode())) {
					continue;
				}
				for (OrderExecDocumentInfo assistDocumentInfo : assistOrderExecDocumentInfos) {
					if (StringUtils.isBlank(assistDocumentInfo.getBedCode())) {
						continue;
					}
					// 判断主记录中是否有辅记录中的床号
					if (mainDocumentInfo.getPatId().equals(
							assistDocumentInfo.getPatId())
							&& mainDocumentInfo.getBedCode().equals(
									assistDocumentInfo.getBedCode())) {
						// 将已加入到主记录中的项目加入待删除项目中
						removeDocumentInfos.add(assistDocumentInfo);
						mainDocumentInfo.getOrderExecDocOrderInfos().addAll(
								assistDocumentInfo.getOrderExecDocOrderInfos());
					}
				}

				Collections.sort(mainDocumentInfo.getOrderExecDocOrderInfos(),
						new OrderExecDocOrderInfo());
			}
			assistOrderExecDocumentInfos.removeAll(removeDocumentInfos);
			rsltDocumentInfos.addAll(mainOrderExecDocumentInfos);
			rsltDocumentInfos.addAll(assistOrderExecDocumentInfos);
			Collections.sort(rsltDocumentInfos,
					new Comparator<OrderExecDocumentInfo>() {

						@Override
						public int compare(OrderExecDocumentInfo o1,
								OrderExecDocumentInfo o2) {
							if (StringUtils.isBlank(o1.getBedCode())
									|| StringUtils.isBlank(o2.getBedCode())
									|| o1.getBedCode().length() <= o2
											.getBedCode().length()
									|| o1.getBedCode().compareTo(
											o2.getBedCode()) <= 0) {
								return 0;
							}
							return 1;
						}

					});

		}

		return rsltDocumentInfos;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getCopyOrderExecTypes(String type) {
		Map<String, Object> copyMap = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(type)
				&& MnisConstants.COM_DIC_USAGE_COPY.equals(type)) {
			if (SuperCacheUtil.CACHE_DATA.containsKey("copyMap")) {
				copyMap = (Map<String, Object>) SuperCacheUtil.CACHE_DATA
						.get("copyMap");
				return copyMap;
			}

		} else {
			if (SuperCacheUtil.CACHE_DATA.containsKey("liquorMap")) {
				copyMap = (Map<String, Object>) SuperCacheUtil.CACHE_DATA
						.get("liquorMap");
				return copyMap;
			}
		}

		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("type", type);
		List<Map<String, Object>> copyTypeMaps = orderRepository
				.getCopyOrderExecTypes(params);
		for (Map<String, Object> map : copyTypeMaps) {
			copyMap.put((String) map.get("code"), map.get("name"));
		}
		if (StringUtils.isNotBlank(type)
				&& MnisConstants.COM_DIC_USAGE_COPY.equals(type)) {
			SuperCacheUtil.CACHE_DATA.put("copyMap", copyMap);
		} else {
			SuperCacheUtil.CACHE_DATA.put("liquorMap", copyMap);
		}
		return copyMap;
	}

	/**
	 * 闭环输液数据转抄
	 * 
	 * @param orderExecGroup
	 * @param deptCode
	 * @param dutyNurseCode
	 * @return
	 */
	private String publishInfusionManagerOrder(OrderExecGroup orderExecGroup,
			String deptCode, String dutyNurseCode) {
		OrderGroup orderGroup = orderExecGroup.getOrderGroup();
		// 1.创建infusionManagerInfo
		OrderInfusionManagerInfo info = new OrderInfusionManagerInfo();
		// 2.创建患者信息
		OrderInfusionManagerPatInfo patInfo = new OrderInfusionManagerPatInfo();
		List<PatientBaseInfo> patBaseInfos = patientService
				.getPatientBaseInfoByDeptCode(deptCode,
						orderGroup.getPatientId());
		if (patBaseInfos.isEmpty()) {
			return null;
		}
		PatientBaseInfo patBaseInfo = patBaseInfos.get(0);
		patInfo.setDeptCode(deptCode);
		patInfo.setPatName(patBaseInfo.getName());
		patInfo.setPatBedcode(patBaseInfo.getBedCode());
		patInfo.setPatBarcode(patBaseInfo.getPatBarcode());
		patInfo.setPatId(patBaseInfo.getPatId());
		patInfo.setPatInhospNo(patBaseInfo.getInHospNo());
		patInfo.setPatSex(patBaseInfo.getSex());
		patInfo.setDutyNurseCode(dutyNurseCode);
		// 3.创建医嘱信息
		OrderCount orderCount = orderRepository.getOrderCount(
				orderGroup.getPatientId(), orderGroup.getOrderExecTypeCode(),
				null, orderGroup.getPlanExecTime());
		OrderInfusionManagerOrderInfo orderInfo = new OrderInfusionManagerOrderInfo();
		orderInfo.setOrderBarcode(orderExecGroup.getOrderExecBarcode());
		orderInfo.setOrderNo(orderGroup.getOrderNo());
		orderInfo.setUsage(orderGroup.getUsageName());
		orderInfo.setOrderCount(orderCount.getPendingOrderCount());
		orderInfo.setOrderExeIndex(orderCount.getFinishedOrderCount());
		orderInfo.setFrequency(orderGroup.getDeliverFreqCode());
		// 4.创建医嘱项目信息
		List<OrderInfusionManagerOrderItem> infuOrderItems = new ArrayList<OrderInfusionManagerOrderItem>();
		List<OrderItem> orderItems = orderGroup.getOrderItems();
		for (OrderItem orderItem : orderItems) {
			OrderInfusionManagerOrderItem infuOrderItem = new OrderInfusionManagerOrderItem();
			infuOrderItem.setOrderNo(orderGroup.getOrderNo());
			infuOrderItem.setDrugName(orderItem.getOrderName());
			infuOrderItem.setDosageUnit(orderItem.getDosageUnit());
			infuOrderItem.setDrugDosage(String.valueOf(orderItem.getDosage()));
			infuOrderItem.setDrugId(orderItem.getOrderId());
			infuOrderItem.setDrugSpec(orderItem.getDrugSpec());

			infuOrderItems.add(infuOrderItem);

		}
		// 5.发送输液信息
		info.setOrderInfusionManagerOrderInfo(orderInfo);
		info.setOrderInfusionManagerPatInfo(patInfo);
		info.setOrderInfusionManagerOrderItems(infuOrderItems);

		String msg = GsonUtils.toJson(info);

		return msg;
	}

	@Override
	public int savePatOrderRemark(String barcode, String value, int type) {
		if (StringUtils.isBlank(barcode)) {
			throw new MnisException("传入的条码参数为null");
		}

		boolean isExistOrderRemark = orderRepository.isExistOrderRemark(
				barcode, type);
		int updateCount = 0;
		if (isExistOrderRemark) {
			updateCount = orderRepository.updatePatOrderRemarkByBarcode(value,
					barcode, type);
		} else {
			updateCount = orderRepository.insertPatOrderRemark(value, barcode,
					type);
		}

		return updateCount;
	}

	@Override
	public BarCodeVo getOrderBarcodeType(String barcode, String patId,
			boolean isQuery) {
		// 增加条码处理 create by qingzhi.liu 2015.09.28 沈阳胸科医院 系统没有配置该参数不影响
		String barCodeParams = identityService
				.getConfigure(MnisConstants.BARCODE);
		String barType = null;

		BarCodeVo vo = null;
		// 判断条码类型
		if (StringUtils.isNotBlank(barCodeParams)) { // 读取条码配置参数 为空不影响流程
			vo = BarcodeUtil.getBarCodeSet(barCodeParams, barcode);
		} else {

			vo = new BarCodeVo();
			if (isQuery) {
				barType = orderRepository.getOrderBarcodeType(barcode, patId);
			}
			vo.setBarType(barType);
			vo.setBarCode(barcode);
		}
		return vo;
	}

	@Override
	public int getOrderExecCountById(String barcode, String secBarcode,
			String patId) {
		return orderRepository
				.getOrderExecCountById(barcode, secBarcode, patId);
	}

	@Override
	public List<OrderItem> getOrderItemsByBarcode(String barcode, String barType) {
		if (StringUtils.isBlank(barcode)) {
			return null;
		}
		if (StringUtils.isBlank(barType)) {
			BarCodeVo vo = getOrderBarcodeType(barcode, null, true);
			if (vo != null) {
				barType = vo.getBarType();
				barcode = vo.getBarCode();
			}
		}
		if (StringUtils.isBlank(barType)) {
			throw new MnisException("无效条码!");
		} else {
			barType = barType.split(MnisConstants.LINE)[0];
		}
		List<OrderItem> orderItems = new ArrayList<OrderItem>();
		switch (barType) {
		case MnisConstants.ORDER_EXEC_TYPE_LAB:// 检验
			orderItems = orderRepository.getLabOrderItemsByBarcode(barcode);
			break;
		case MnisConstants.ORDER_EXEC_TYPE_ORAL:// 包药机
			orderItems = orderRepository.getOralOrderItemsByBarcode(barcode);
			break;
		default:
			orderItems = orderRepository.getOrderItemsByBarcode(barcode);
			break;
		}
		return orderItems;
	}

	@Override
	public List<OrderExecGroup> getOrdExecDocInfosToNda(String deptCode,
			List<String> patIds, String startDate, String endDate, int execType) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		if (null == patIds || patIds.size() == 0) {
			patIds = null;
		}

		if (StringUtils.isBlank(startDate)) {
			startDate = null;
		}

		if (StringUtils.isBlank(endDate)) {
			endDate = null;
		}

		params.put("deptCode", deptCode);
		params.put("patIds", patIds);
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		params.put("execType", execType);

		// 执行医嘱中排除口服药医嘱
		List<String> orderSubNos = orderRepository
				.getOrderSubNoFromDrugBag(params);

		params.put("orderSubNos", orderSubNos);
		params.put("hospitalCode",
				SuperCacheUtil.getSYSTEM_CONFIGS().get("hospitalCode"));

		// 执行医嘱
		List<OrderExecGroup> orderExecGroups = orderRepository
				.getOrdExecDocInfosToNda(params);
		if (orderSubNos != null && !orderSubNos.isEmpty()) {
			List<OrderExecGroup> orderExecGroupsOral = orderRepository
					.getOrdExecDocInfosToNdaOral(params);
			orderExecGroups.addAll(orderExecGroupsOral);
		}

		List<OrderExecGroup> orderExecGroupsLab = orderRepository
				.getOrdExecDocInfosToNdaLab(params);
		orderExecGroups.addAll(orderExecGroupsLab);

		List<OrderExecGroup> orderExecGroupsBlood = orderRepository
				.getOrdExecDocInfosToNdaBlood(params);
		orderExecGroups.addAll(orderExecGroupsBlood);
		return orderExecGroups;

	}

	@Override
	public List<HisOrderGroup> getPublishOriginalOrders(String deptCode,
			Date startDate, Date endDate) {

		if (StringUtils.isBlank(deptCode)) {
			return null;
		}
		if (null == startDate) {
			Date date = new Date();
			startDate = DateUtils.addHours(date, -2);
		}

		HashMap<String, Object> params = new HashMap<String, Object>();

		params.put("deptCode", deptCode);
		params.put("startTime", startDate);
		params.put("endTime", endDate);
		params.put("hospitalCode",
				SuperCacheUtil.getSYSTEM_CONFIGS().get("hospitalCode"));
		return orderRepository.getPublishOriginalOrders(params);
	}

	@Transactional
	@Override
	public void batchSaveOrderApprove(List<OrderApprove> orderApproves) {
		if (orderApproves == null || orderApproves.isEmpty()) {
			return;
		}
		for (OrderApprove orderApprove : orderApproves) {
			saveOrderApprove(orderApprove);
		}
	}

	private void saveOrderApprove(OrderApprove orderApprove) {
		if (null == orderApprove) {
			return;
		}

		if (StringUtils.isBlank(orderApprove.getOrderNo())
				|| StringUtils.isBlank(orderApprove.getPatId())) {
			LOGGER.debug("orderserviceImpl saveOrderApprove 参数为空：orderNo:"
					+ orderApprove.getOrderNo() + ";patId:"
					+ orderApprove.getPatId());
			return;
		}

		Integer count = orderRepository.getOrderApproveCount(
				orderApprove.getPatId(), orderApprove.getOrderNo());

		if (count != null && count > 0) {
			orderRepository.updateOrderApprove(orderApprove);
		} else {
			orderRepository.insertOrderApprove(orderApprove);
		}
	}
}
