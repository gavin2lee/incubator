package com.lachesis.mnis.core.bodysign;

import com.lachesis.mnis.core.IdentityService;
import com.lachesis.mnis.core.OrderService;
import com.lachesis.mnis.core.bodysign.entity.*;
import com.lachesis.mnis.core.bodysign.repository.BodySignRepository;
import com.lachesis.mnis.core.constants.MnisConstants;
import com.lachesis.mnis.core.exception.MnisException;
import com.lachesis.mnis.core.patient.entity.Patient;
import com.lachesis.mnis.core.patient.entity.PatientEvent;
import com.lachesis.mnis.core.util.DateUtil;
import com.lachesis.mnis.core.util.DateUtil.DateFormat;
import com.lachesis.mnis.core.util.SuperCacheUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.*;

/***
 * 
 * 处理生命体征数据
 *
 * @author yuliang.xu
 * @date 2015年6月24日 下午4:08:32
 *
 */
@Component("dataProcessor")
public class BodySignDataProcessor {

	@Autowired
	private BodySignRepository bodySignRepository;
	
	@Autowired
	private IdentityService identityService;

	@Autowired
	private OrderService orderService;
	
	public void assertPersistRule(BodySignRecord record,boolean isSave) {
		// 入出院只能录一次
		assertEventInfoRule(record,isSave);

		// 限制一天内录入次数的项
//	    assertSaveTimesRule(reocrd);
		//处理 入院当天出入量值设置(value+(入院到另一天7点时间差))
		assertRyOutInput(record);
		
		// 不允许同一时间点录入同样的项目(但应允许在降温后、止痛后的录入)
		assertSingleInputRule(record,isSave);
	}

	/**
	 * 确保那些一天只能保存若干次的规则: 血压、血糖一日三次
	 * 
	 * @param record
	 * @param oldItems
	 * @return
	 */
	void assertSaveTimesRule(BodySignRecord record) {
		
		if(null == record || !record.hasBodySignItems()){
			return;
		}
		Date recordDate = record.getFullDateTime();
		Date[] queryDates = DateUtil.getQueryRegionDates(recordDate);
		List<BodySignItem> existBodySignItems = null;
		for(BodySignItem bodySignItem:record.getBodySignItemList()){
			String itemCode = bodySignItem.getItemCode();
			
			if(BodySignUtil.getOncePerItemCodes().contains(itemCode)){
				//查询一天一次是否有录入，录入删除
				existBodySignItems = bodySignRepository.getBodySignItemByCode(
						null, itemCode, record.getDeptCode(),record.getPatientId(),queryDates[0],queryDates[1]);
			}else if(BodySignUtil.getOncePerItemCodes().contains(itemCode)){
				//查询一天两次
				if(recordDate.before(DateUtil.setAssignTime(queryDates[0], 12))){
					existBodySignItems = bodySignRepository.getBodySignItemByCode(
							null, itemCode, record.getDeptCode(),record.getPatientId(),queryDates[0],
							DateUtil.setAssignTime(queryDates[0], 12));
				}else {
					existBodySignItems = bodySignRepository.getBodySignItemByCode(
							null, itemCode, record.getDeptCode(),record.getPatientId(),
							DateUtil.setAssignTime(queryDates[0], 12),queryDates[1]);
				}
				
			}
		}
	}

	/**
	 * 入院、出院与死亡一个患者只能录一次
	 * 
	 * @param record
	 * @return
	 */
	private void assertEventInfoRule(BodySignRecord record,boolean isSave) {
		if(!isSave){
			return;
		}
		
		if(null == record.getEvent()){
			return;
		}
		PatientEvent event = record.getEvent();
		List<PatientEvent> fullDateList = bodySignRepository
				.getEventInfosByPatIdAndCode(record.getPatientId(),
						null,record.getFullDateTime());
		
		if(null != fullDateList && fullDateList.size() > 0){
			throw new MnisException("该时间点已录入事件,无法录入 [" +
					BodySignConstants.MAP_BODYSIGN_ITEM.get(event
							.getEventCode()) + " ]!");
		}
		
		
		if (event != null
				&& BodySignConstants.EVENT_UNIQUE_LIST.contains(event
						.getEventCode())) {
			List<PatientEvent> list = bodySignRepository
					.getEventInfosByPatIdAndCode(record.getPatientId(),
							event.getEventCode(),null);
			if (list != null && list.size() > 0) {
				throw new RuntimeException("[ "+
						BodySignConstants.MAP_BODYSIGN_ITEM.get(event
								.getEventCode()) + " ]事件只能录入一次!");
			}
		}
		
		
	}

	/**
	 * 确保项目一个时间点只能录入一次，并返回同时间点已录入的项目以用于合并
	 * 
	 * @param record
	 * @return
	 */
	private void assertSingleInputRule(BodySignRecord record, boolean isSave) {

		List<BodySignItem> itemList = record.getBodySignItemList();
		
		if (!record.hasData()) {
			return;
		}
		//1.获取时间点的记录
		List<BodySignRecord> bodySignRecords = bodySignRepository.getBodySignRecordByRecordDate(
						DateUtil.format(record.getFullDateTime(),DateFormat.FULL),
						record.getPatientId(),record.getDeptCode());
		BodySignRecord removeRecord = new BodySignRecord();
		//2.判断记录是否存在
		if(null == bodySignRecords|| bodySignRecords.size() == 0 || !bodySignRecords.get(0).hasData()){
			return;
		}
		
		BodySignRecord bodySignRecord = bodySignRecords.get(0);
		if(isSave){
			//3.新增时重新设置事件
			if(bodySignRecord.getEvent() != null && record.getEvent() == null && StringUtils.isNotBlank(bodySignRecord.getEvent().getEventCode())){
				record.setEvent(bodySignRecord.getEvent());
			}
		}
		
		// 4.获取已记录过的体征项
		List<BodySignItem> oldItems = bodySignRecord.getBodySignItemList();
		if (oldItems == null || oldItems.isEmpty()) {
			return;
		}
		
		//5.重新设置记录用户
		record.setFirstDate(bodySignRecord.getFirstDate());
		record.setModifyTime(new Date());
		record.setModifyNurseCode(record.getRecordNurseCode());
		record.setModifyNurseName(record.getRecordNurseName());
		record.setRecordNurseCode(bodySignRecord.getRecordNurseCode());
		record.setRecordNurseName(bodySignRecord.getRecordNurseName());
		
		//待删除bodySignItem
		List<BodySignItem> removeOldItems = new ArrayList<BodySignItem>();
		
		// 6.如果同时间点有录入过其他项目(oldItem != null)，则将子项添加到detail表而不用更新master表
		if(null != itemList && !itemList.isEmpty()){
			for (BodySignItem newItem : itemList) {
				// 传入code为null,提示
				if (null == newItem.getItemCode()) {
					throw new MnisException("存在 [无] 的体征项!");
				}
				
				// 体征项和录入说明同时匹配，认为是同样的项目（如疼痛止痛前后只是measureNoteCode不同）
				// 同一体征同一时间点只能录入一下
				for (BodySignItem sameItem : oldItems) {
					// 其他1和其他2可以录入多项
					if (StringUtils.isBlank(sameItem.getItemCode())) {
						removeOldItems.add(sameItem);
						continue;
					}
					if (sameItem.getItemCode().equals(newItem.getItemCode())) {
						// 表示已存在
						newItem.setAdd(true);
						if(BodySignConstants.PPD.equals(sameItem.getItemCode())){
							//如果不存在配置，那么走原来的流程
							newItem.setIndex(sameItem.getIndex());
						}
						//已存在加入待删除项目中
						removeOldItems.add(sameItem);
					}else {
						//数据库已存在设置
						sameItem.setAdd(true);
					}
				}
			}
		}
		//7.删除为空item
		oldItems.removeAll(removeOldItems);
		if (isSave) {
			//8.重新设置
			record.getBodySignItemList().addAll(oldItems);
		}else{
			//记录已删除的项目
			bodySignRecords.clear();
			removeRecord.setPatientId(record.getPatientId());
			removeRecord.setDeptCode(record.getDeptCode());
			removeRecord.setFullDateTime(record.getFullDateTime());
			removeRecord.setBodySignItemList(oldItems);
			bodySignRecords.add(removeRecord);
			handleDeletePatBodySignDetail(bodySignRecords);
		}
	}

	/**
	 * 处理体征项中的特殊性以及记录时间
	 * 
	 * @param record
	 */
	public void handleBodySign(BodySignRecord record) {
		handleBodySignRecordDate(record);
		// 计算index
		List<Date> hours = BodySignUtil.getTimePointsByHour(
				record.getFullDateTime(), identityService.getBodySignDataStrategy());
		int indexInSix = BodySignUtil.getIndexByDivideHour(
				record.getFullDateTime(), hours, 6);
		int indexInThree = BodySignUtil.getIndexByDivideHour(
				record.getFullDateTime(), hours, 3);
		int indexInTwice = BodySignUtil.getIndexByDivideHour(
				record.getFullDateTime(), hours, 2);
		//把备注转换为体征明细信息保存
		handleRemarkToBodySignItem(record);
		//为每一个明细设置显示的序号index
		handlerSpecialBodySignItem(record, indexInSix, indexInThree,
				indexInTwice,hours);
		//事件处理
		handlePatientEventInfo(record, indexInSix, hours);
	}

	/**
	 * 关注转化为bodysignItem保存
	 * 
	 * @param record
	 */
	private void handleRemarkToBodySignItem(BodySignRecord record) {
		if(record!=null
				&&record.getBodySignItemList()!=null){
			for (BodySignItem item : record.getBodySignItemList()) {
				item.setPatId(record.getPatientId());
				item.setRecordDate(record.getFullDateTime());
			}
		}
		
		if (StringUtils.isBlank(record.getRemark())) {
			return;
		}
		BodySignItem remarkItem = new BodySignItem();
		remarkItem.setItemName(BodySignConstants.MAP_BODYSIGN_ITEM
				.get(BodySignConstants.REMARK));
		remarkItem.setItemCode(BodySignConstants.REMARK);
		remarkItem.setMeasureNoteCode(BodySignConstants.BODYSIGN_ITEM_MOREN);
		remarkItem.setMeasureNoteName(BodySignConstants.MAP_BODYSIGN_ITEM
				.get(BodySignConstants.BODYSIGN_ITEM_MOREN));
		remarkItem.setItemValue(record.getRemark());
		remarkItem.setPatId(record.getPatientId());
		remarkItem.setRecordDate(record.getFullDateTime());
		record.addToBodySignItems(remarkItem);
	}

	private void handleBodySignRecordDate(BodySignRecord record) {
		Date date = DateUtil.parse(record.getRecordDay() + MnisConstants.EMPTY
				+ record.getRecordTime());
		if (date == null) {
			date = new Date();
		}
		record.setFullDateTime(date);
		if (record.getFirstDate() == null) {
			record.setFirstDate(new Date());
		}
	}

	private void handlerSpecialBodySignItem(BodySignRecord record,
			int indexInSix, int indexInThree, int indexInTwice,List<Date> hours) {
		List<BodySignItem> list = record.getBodySignItemList();
		if (list != null) {
			List<BodySignItem> needRemoveList = new ArrayList<>(list.size());
			for (BodySignItem item : list) {
				// 处理降温后体温
				/*
				 * if ( record.getCooled() > 0 &&
				 * BodySignConstants.TEMPERATURE.equals(item.getItemCode())) {
				 * item.setItemCode(BodySignConstants.COOLED_TEMP);
				 * item.setItemName
				 * (BodySignService.BODY_SIGN_DICT_MAP.get(BodySignConstants
				 * .COOLED_TEMP).getItemName()); }
				 */

				// 设置index
				if (BodySignConstants.SIX_TIMES_PER_DAY_ITEMS.contains(item
						.getItemCode())) {
					item.setIndex(indexInSix);
				} else if (BodySignConstants.THREE_TIMES_PER_DAY_ITEMS
						.contains(item.getItemCode())) {
					item.setIndex(indexInThree);
				} else if (BodySignConstants.TWICE_PER_DAY_ITEMS.contains(item
						.getItemCode())) {
					item.setIndex(indexInTwice);
				} else if (StringUtils.isBlank(item.getItemCode())) {
					needRemoveList.add(item);
				}
				//针对PPD皮试
				if(BodySignConstants.PPD.equals(item.getItemCode())){
					//如果不存在配置，那么走原来的流程
					int eventIndexInSix = BodySignUtil.getIndexByDivideHour(new Date(), hours, 6);
					item.setIndex(eventIndexInSix);
				}
			}
			// 事件或者皮试为空item
			record.getBodySignItemList().removeAll(needRemoveList);
		}
	}

	/**
	 * 处理事件
	 * @param record	:体征记录
	 * @param indexInSix:默认显示的位置
	 * @param hours		:体征时间区间
	 */
	private void handlePatientEventInfo(BodySignRecord record, int indexInSix, List<Date> hours) {
		PatientEvent eventInfo = record.getEvent();
		if (eventInfo == null || !eventInfo.isValid()) {
			record.setEvent(null);
			return;
		}
		
		//计算事件的index
		int eventIndexInSix = indexInSix;
		if(null != record.getEvent() && StringUtils.isNotBlank(record.getEvent().getRecordDate())){
//			Date eventTime = DateUtil.parse(record.getEvent().getRecordDate());
			Date eventTime = record.getFullDateTime();
			//获取特殊时间段的配置点
			String eventHours = identityService.getConfigure(BodySignConstants.BODY_SIGN_EVENT_TIME);
			if(!StringUtils.isEmpty(eventHours)){
				//如果存在配置
				eventIndexInSix = BodySignUtil.getEventIndex(eventTime,eventHours);
			}else{
				//如果不存在配置，那么走原来的流程
				eventIndexInSix = BodySignUtil.getIndexByDivideHour(eventTime, hours, 6);
			}
		}

		eventInfo.setPatientId(record.getPatientId());
		eventInfo.setPatientName(record.getPatientName());
		eventInfo.setEventName(BodySignConstants.MAP_BODYSIGN_ITEM
				.get(eventInfo.getEventCode()));
		eventInfo.setIndex(eventIndexInSix);
		eventInfo.setRecord_date(record.getFullDateTime());
		//设置中文时间
		if (StringUtils.isNotBlank(eventInfo.getRecordDate())) {
			if (eventInfo.getRecordDate().length() > 10) {
				eventInfo.setRecordDate(eventInfo.getRecordDate().substring(11,
						16));
			}
			
			String hourType = identityService
					.getConfigure(MnisConstants.SYS_CONFIG_HOURTYPE);
			boolean bool = (!StringUtils.isEmpty(hourType) && "1"
					.equals(hourType)) ? true : false;
			eventInfo.setChineseEventDate(DateUtil
					.getChineseHourMinute(eventInfo.getRecordDate(),bool));
		}
	}

	/**
	 * 根据病人入院和出院日， 获得更新的参考日期（以参考日期所在的周为单元进行体温单查询）： 选择max(day,入院日) 选择min(day,出院日)
	 * 
	 * @param day
	 * @param in
	 *            入院日期
	 * @param out
	 *            出院日期
	 * @return
	 */
	public Date getRefDate(Date day, Date in, Date out) {
		Date targetDate = day;
		// 选择max(day,入院日)
		if (in != null && targetDate.before(in)) {
			targetDate = in;
		}

		// 选择min(day,出院日)
		if (out != null && targetDate.after(out)) {
			targetDate = out;
		}
		return targetDate;
	}

	/**
	 * 获取指定日期refDate，以startDate 为起始时间的非自然周的时间段
	 * 
	 * @param refDate
	 *            参考日期
	 * @param weekOffset
	 *            周偏移量(-1:上周 0:本周 1:下周 2:下下周等等)
	 * @param startDate
	 *            哪天算一周的第一天
	 * @return yyyy-MM-dd 格式的日期
	 */
	public Date[] getWeekDaysAroundDate(Date ref, int weekOffset, Date start) {
		if (ref == null || start == null) {
			return null;
		}

		Date[] weekDays = new Date[DateUtil.DAY_PER_WEEK];
		// 两时间相差多少天
		Date startDay = DateUtil.setDateToDay(start);
		Date endDay = DateUtil.setDateToDay(ref);
		int dayPoor = DateUtil.calDatePoor(startDay, endDay);
		// 两时间从开始时间计算需要偏移的周数
		int weekPoorOffest = dayPoor / DateUtil.DAY_PER_WEEK + weekOffset;
		// 偏移之后的开始日期
		Date weekStartDay = DateUtils.addDays(startDay, weekPoorOffest
				* DateUtil.DAY_PER_WEEK);
		for (int i = 0; i < DateUtil.DAY_PER_WEEK; i++) {
			weekDays[i] = DateUtils.addDays(weekStartDay, i);
		}

		return weekDays;
	}

	/**
	 * 获取日期列表中各项对应的住院天数或手术后天数.(当天表示 第0天)
	 * 
	 * @param inDate
	 *            非null时计算住院天数，为null时返回空列表
	 * @param outDate
	 *            出院日期：出院后数据失效，应不显示
	 * @param recordDates
	 *            应为连续的日期
	 * @return
	 */
	public List<String> calcDaysAfterByInAndOutDay(Date inDate, Date outDate,
			Date[] recordDates) {
		if (inDate == null) {
			return null;
		}

		List<String> list = new ArrayList<String>(recordDates.length);
		int days = 0;
		for (Date recordDate : recordDates) {
			// 已经出院时，不显示天数
			if (outDate != null && recordDate.after(outDate)) {
				list.add(MnisConstants.EMPTY);
				continue;
			}

			days = DateUtil.calDatePoor(inDate, recordDate);
			list.add(days < 0 ? MnisConstants.EMPTY : String.valueOf(days + 1));
		}

		return list;
	}

	/**
	 * 获取日期列表中各项对应的手术后天数.(当天表示 第0天)(规则一)
	 * 
	 * 当在手术14天内进行第二次手术，则停写第一次术后日数，第二次手术 当日填写1-0，依次填入第二次手术后第14天
	 * 
	 * @param inDate
	 *            非null时计算住院天数，为null时返回空列表
	 * @param outDate
	 *            出院日期：出院后数据失效，应不显示
	 * @param recordDates
	 *            应为连续的日期
	 * @return
	 */
	public List<String> calcDaysAfterEventRuleOne(List<Date> eventDates, Date outDate,
			Date[] recordDates) {
		if (eventDates == null || eventDates.size() == 0) {
			return null;
		}

		List<String> list = new ArrayList<String>();
		int days = 0;
		// 术后天数
		String eventDays;
		int eventDaysListSize = 0;
		// 记录事件与recordDate的术后所有天数
		List<String> eventDaysList = new ArrayList<>();
		for (Date recordDate : recordDates) {
			// 已经出院时，不显示天数
			if (outDate != null && recordDate.after(outDate)) {
				list.add(MnisConstants.EMPTY);
				continue;
			}

			eventDaysList.clear();
			eventDays = MnisConstants.EMPTY;
			// 记录所有术后天数
			for (int i = 0; i < eventDates.size(); i++) {
				days = DateUtil.calDatePoor(eventDates.get(i), recordDate);
				// 连续术后14天
				if (eventDates.size() == 1) {
					eventDaysList.add(days > 0 && days <= 14 ? String
							.valueOf(days) : MnisConstants.EMPTY);
					break;
				} else {
					eventDaysList.add(days >= 0 && days <= 14 ? String
							.valueOf(days) : MnisConstants.EMPTY);
				}

			}

			eventDaysListSize = eventDaysList.size();
			if (eventDates.size() == 1) {
				eventDays = eventDaysList.get(0);
			} else {
				//显示为1-0规则
				for (int i = eventDaysListSize - 1; i >= 0; i--) {
					if (MnisConstants.EMPTY.equals(eventDaysList.get(i))) {
						continue;
					} else if (MnisConstants.ZERO.equals(eventDaysList.get(i))) {
						if (i != 0) {
							eventDays = String.valueOf(i) + MnisConstants.LINE
									+ MnisConstants.ZERO;
							break;
						}
					} else {
						eventDays = eventDaysList.get(i);
						break;
					}
				}
			}

			list.add(eventDays);
		}

		return list;
	}
	
	/**
	 * 获取日期列表中各项对应的手术后天数.(当天表示 第0天)(规则二)
	 * 
	 * 当在手术14天内进行第二次手术，第二次术后日期/第一次术后日期，依次填入第二次手术后第14天
	 * 
	 * @param inDate
	 *            非null时计算住院天数，为null时返回空列表
	 * @param outDate
	 *            出院日期：出院后数据失效，应不显示
	 * @param recordDates
	 *            应为连续的日期
	 * @return
	 */
	public List<String> calcDaysAfterEventRuleTwo(List<Date> eventDates, Date outDate,
			Date[] recordDates) {
		if (eventDates == null || eventDates.size() == 0) {
			return null;
		}

		List<String> list = new ArrayList<String>();
		// 术后天数
		// 记录事件与recordDate的术后所有天数
		for (Date recordDate : recordDates) {
			// 已经出院时，不显示天数
			if (outDate != null && recordDate.after(outDate)) {
				list.add(MnisConstants.EMPTY);
				continue;
			}

			String eventDays = MnisConstants.EMPTY;
			// 记录所有术后天数
			for (int i = 0; i < eventDates.size(); i++) {
				int days = DateUtil.calDatePoor(eventDates.get(i), recordDate);
				// 连续术后14天
				if(days==0){
					//如果为0天的情况，获取手术天数
					String ssDay = "0";
					String lm = BodySignUtil.getSSDay(String.valueOf(i+1));
					if(!StringUtils.isEmpty(lm)){
						ssDay = lm + "-" + ssDay;
					}
					eventDays = ssDay;
				}else if(days>0 && days<=14){
					eventDays = String.valueOf(days) ;
				}
			}
			list.add(eventDays);
		}

		return list;
	}

	/***
	 * 生命体征特殊处理
	 *
	 * @param bodySignRecords
	 * @param index
	 * @param weekDay
	 * @throws
	 */
	public void processTempSheetDataForDay(
			List<BodySignRecord> bodySignRecords, int index, Date weekDay) {
		Map<String, BodySignRecord> skinTestRemoveRepeatmap = new HashMap<>();
		Map<String, boolean[]> existData = new HashMap<>();
		List<BodySignRecord> removes = new ArrayList<>(bodySignRecords.size());
		List<String> bodySignDataStrategy = identityService
				.getBodySignDataStrategy();
		// bodySignRecords 是一天的数据, 取最新的一条或者最早的一条
		boolean isLatestData = (0 == Integer.valueOf(bodySignDataStrategy
				.get(0)));
		boolean isSixPoint = (2 == Integer.valueOf(bodySignDataStrategy.get(0)));
		if (isLatestData) {
			Collections.reverse(bodySignRecords);
		}
		for (BodySignRecord record : bodySignRecords) {
			// 如果非体温单时间点,取固定六个时间点
			if (isSixPoint) {
				String timeString = MnisConstants.ZERO_HM_TIME
						+ MnisConstants.COMMA
						+ identityService.getConfigure("tempInputTimeSelector");
				// 体温单是否包含记录时间
				
				if (record.getRecordTime() != null) {
					boolean isIncludeTime = (DateUtil.getFixTypeFromTime(
							record.getRecordTime(), Calendar.SECOND) == 0 && Arrays
							.asList(timeString.split(MnisConstants.COMMA))
							.contains(record.getRecordTime().substring(0, 5)));
					if(!isIncludeTime){
						removes.add(record);
						continue;
					}
				}
			}

			// 处理事件
			if (record.getEvent() != null) {
				record.getEvent().setIndex(
						6 * index + record.getEvent().getIndex());
			}

			// 处理皮试，去掉重复药物
			if (record.getSkinTestInfo() != null) {
				record.getSkinTestInfo().setIndex(index);
				if (skinTestRemoveRepeatmap.containsKey(record
						.getSkinTestInfo().getDrugName())) {
					record.setSkinTestInfo(null);
				} else {
					skinTestRemoveRepeatmap.put(record.getSkinTestInfo()
							.getDrugName(), record);
				}
			}

			List<BodySignItem> bodySignItems = record.getBodySignItemList();
			if (bodySignItems != null) {
				if (isLatestData) {
					Collections.reverse(bodySignItems);
				}
				for (BodySignItem item : bodySignItems) {
					// 判断是否为第一次处理该itemCode
					if (existData.containsKey(item.getItemCode())) {
						boolean[] processed = existData.get(item.getItemCode());
						// 已经取值，将其余值设置为不可见
						if (processed[item.getIndex()]) {
							item.setShow(false);
						} else {
							processed[item.getIndex()] = true;
						}
					} else {
						// 第一次处理该itemCode，创建数组，表示处理过该itemCode，并将处理过的项设为true
						boolean[] processed = new boolean[6];
						processed[item.getIndex()] = true;
						item.setShow(true);
						existData.put(item.getItemCode(), processed);
					}

					if (item.isShow()) {
						if (BodySignConstants.SIX_TIMES_PER_DAY_ITEMS
								.contains(item.getItemCode())) {
							item.setIndex(6 * index + item.getIndex());
						} else if (BodySignConstants.THREE_TIMES_PER_DAY_ITEMS
								.contains(item.getItemCode())) {
							item.setIndex(3 * index + item.getIndex());
						} else if (BodySignConstants.TWICE_PER_DAY_ITEMS
								.contains(item.getItemCode())) {
							item.setIndex(2 * index + item.getIndex());
						} else if (BodySignUtil.getOncePerItemCodes()
								.contains(item.getItemCode())) {
							//PPD皮试6个格子
							if(BodySignConstants.PPD.equals(item.getItemCode())){
								item.setIndex(6 * index + item.getIndex());
							}else{
								item.setIndex(index);
							}
						}
					}

				}
			}

			if (!record.hasData()) {
				removes.add(record);
			}
		}

		if (!removes.isEmpty())
			bodySignRecords.removeAll(removes);

		// 重新按原数据排序
		if (isLatestData) {
			Collections.reverse(bodySignRecords);
		}
		// 事件可能需要重排序
	}

	/**
	 * 设置基础属性
	 * 
	 * @param bodyTempSheet
	 * @param weekDays
	 * @param listInHosDays
	 * @throws ParseException
	 */
	public void setCommonProperties(BodyTempSheet bodyTempSheet,
			Date[] weekDays, List<String> listInHosDays) {
		// 是否还有能翻页：-1—没有上一页,0—可以左右翻页,1—没有下一页(此项必须在disposeDateFormat(listDates)方法前处理)
		bodyTempSheet.setNextFlag(setisNextWeek(listInHosDays,
				bodyTempSheet.getPatientInfo(), weekDays));
		// 住院日期首页第1日及跨年度第1日需填写年-月-日（如：2010－03－26）。
		// 每页体温单的第1日及跨月的第1日需填写月-日（如03-26），其余只填写日期
		List<String> simpleWeekDays = processDateFormat(weekDays);
		for (int i = 0; i < bodyTempSheet.getBodySignDailyRecordList().size(); i++) {
			BodySignDailyRecord bodySignDailyRecord = bodyTempSheet
					.getBodySignDailyRecordList().get(i);
			bodySignDailyRecord.setFullRecordDate(DateUtil.format(weekDays[i],
					DateFormat.YMD));
			bodySignDailyRecord.setRecordDate(simpleWeekDays.get(i));
		}
	}

	/**
	 * 归一化生命体征记录：删除无效数据(体征项，事件，皮试，remark)
	 * 
	 * @param bodySignRecords
	 */
	public void normalizeBodySignRecord(List<BodySignRecord> bodySignRecords) {
		if (bodySignRecords == null) {
			return;
		}
		for (BodySignRecord record : bodySignRecords) {
			if (record.getSkinTestInfo() == null
					|| StringUtils.isEmpty(record.getSkinTestInfo()
							.getTestResult())) {
				record.setSkinTestInfo(null);
			}

			if (record.getEvent() == null || !record.getEvent().isValid()) {
				record.setEvent(null);
			}

			List<BodySignItem> bodySignItems = record.getBodySignItemList();
			if (bodySignItems == null || bodySignItems.size() == 0) {
				record.setBodySignItemList(null);
			}
		}
	}
	
	/**
	 * 处理血压1，血压2
	 * @param bodySignRecords
	 */
	public void processBloodPressToRecord(List<BodySignRecord> bodySignRecords){
		
		List<BodySignItem> bodySignItems = new ArrayList<BodySignItem>();
		for (BodySignRecord bodySignRecord : bodySignRecords) {
			bodySignItems = bodySignRecord.getBodySignItemList();
			if(null == bodySignItems ||  bodySignItems.size() == 0){
				continue;
			}
			//将血压1,血压2转为体温单血压
			for (BodySignItem bodySignItem : bodySignItems) {
				if(BodySignConstants.BLOODPRESS1.equals(bodySignItem.getItemCode())){
					bodySignItem.setIndex(0);
					bodySignItem.setItemCode(BodySignConstants.BLOODPRESS);
				}else if(BodySignConstants.BLOODPRESS2.equals(bodySignItem.getItemCode())){
					bodySignItem.setIndex(1);
					bodySignItem.setItemCode(BodySignConstants.BLOODPRESS);
				}
			}
		}
		
	}

	/**
	 * 根据出院日期和入院日期返回是否可以翻页
	 * 
	 * @param listInHosDays
	 * @param patientInfo
	 * @param dateTime
	 * @return
	 * @throws ParseException
	 */
	private int setisNextWeek(List<String> listInHosDays, Patient patientInfo,
			Date[] dateTime) {
		int flag = 0;
		int mon = 0;
		if (listInHosDays.isEmpty()) {
			return flag;
		}
		if (StringUtils.isNotEmpty(listInHosDays.get(0))) {
			// 获取本周星期一病人住院天数
			mon = Integer.parseInt(listInHosDays.get(0));
		}
		if (mon <= 1) {
			flag = -1;
		}
		// 当前周出院或者当天就在当前周不可翻下一页
		if (patientInfo.getOutDate() != null
				&& DateUtil.calDatePoor(dateTime[0], patientInfo.getOutDate()) < 8) {
			if (mon <= 1) {
				flag = 2;
			} else {
				flag = 1;
			}
		}
		return flag;
	}

	/**
	 * 
	 * 住院日期首页第1日及跨年度第1日需填写年-月-日（如：2010－03－26）。
	 * 每页体温单的第1日及跨月的第1日需填写月-日（如03-26），其余只填写日期。
	 * 
	 * @param listDates
	 */
	@SuppressWarnings("deprecation")
	private List<String> processDateFormat(Date[] listDates) {
		boolean isOtherYear = true;
		List<String> list = new ArrayList<String>();
		list.add(DateUtil.format(listDates[0], DateFormat.YMD));

		for (int i = 1; i < listDates.length; i++) {
			list.add(DateUtil.format(listDates[i], DateFormat.YMD));
			if (isOtherYear
					&& listDates[i - 1].getYear() != listDates[i].getYear()) {
				isOtherYear = false;
				continue;
			}

			if (listDates[i - 1].getMonth() == listDates[i].getMonth()) {
				list.set(i, list.get(i).substring(8, 10));
			} else {
				list.set(i, list.get(i).substring(5));
			}
		}

		return list;
	}
	
	/**
	 * 保存生命体征时，分离一天一次,一天二次的体征项
	 * 预处理固定时间点的生命体征
	 * @param bodySignRecords
	 * @return
	 */
	public List<BodySignRecord> assertFixTimeBodySignRecords(BodySignRecord bodySignRecord){
		List<BodySignRecord> bodySignRecords = new ArrayList<BodySignRecord>();
		if (null == bodySignRecord || !bodySignRecord.hasBodySignItems()) {
			bodySignRecords.add(bodySignRecord);
			return bodySignRecords;
		}

		// 一天一次的项目
		List<String> oncePerItemCodeList = BodySignUtil.getOncePerItemCodes();
		List<BodySignItem> oncePerBodySignItems = new ArrayList<BodySignItem>();
		List<BodySignItem> bodySignItems = bodySignRecord.getBodySignItemList();
		// 判断一天一次数据
		for (BodySignItem bodySignItem : bodySignItems) {
			if (oncePerItemCodeList.contains(bodySignItem.getItemCode())) {
				oncePerBodySignItems.add(bodySignItem);
			}
		}

		// 增加一天一次的项目

		BodySignRecord oncePerBodySignRecord = new BodySignRecord();
		if (!oncePerBodySignItems.isEmpty()) {
			// 一天一次增加项目
			oncePerBodySignRecord.addToBodySignItems(oncePerBodySignItems);
			// 原始删除一天一次的项目
			bodySignRecord.getBodySignItemList()
					.removeAll(oncePerBodySignItems);
		}
		// 初始化数据
		oncePerBodySignRecord.setBedCode(bodySignRecord.getBedCode());
		oncePerBodySignRecord.setPatientId(bodySignRecord.getPatientId());
		oncePerBodySignRecord.setPatientName(bodySignRecord.getPatientName());
		oncePerBodySignRecord.setDeptCode(bodySignRecord.getDeptCode());
		oncePerBodySignRecord.setInHospNo(bodySignRecord.getInHospNo());
		oncePerBodySignRecord.setRecordDay(bodySignRecord.getRecordDay());
		oncePerBodySignRecord.setRecordTime(MnisConstants.ZERO_HM_TIME);
		oncePerBodySignRecord.setFullDateTime(DateUtil.parse(bodySignRecord
				.getRecordDay()));
		oncePerBodySignRecord.setFirstDate(bodySignRecord.getFirstDate());
		oncePerBodySignRecord.setModifyTime(bodySignRecord.getModifyTime());
		if (bodySignRecord.getSkinTestInfo() != null) {
			oncePerBodySignRecord.setSkinTestInfo(bodySignRecord
					.getSkinTestInfo());
		}
		bodySignRecords.add(oncePerBodySignRecord);
		// 增加原始项
		if (bodySignRecord.hasBodySignItems()) {
			bodySignRecords.add(bodySignRecord);
		}

		return bodySignRecords;
	}
	
	/**
	 * 设置入院出入量统计时间差
	 * @param record
	 */
	private void assertRyOutInput(BodySignRecord record){
		if(!record.hasBodySignItems()){
			return;
		}
		//获取入院时间差需统计的项目编号
		String ryHourDiffTempItemsStr = SuperCacheUtil.getSYSTEM_CONFIGS().get("ryHourDiffTempItems");
		
		if(StringUtils.isBlank(ryHourDiffTempItemsStr)){
			return;
		}
		List<BodySignItem> newItems = record.getBodySignItemList();
		
		for (BodySignItem newItem : newItems) {
			//首次入院出入量需要增加时间差
			if(ryHourDiffTempItemsStr.contains(newItem.getItemCode())
					&& StringUtils.isNotBlank(newItem.getItemValue())){
				newItem.setRyHourDiff(handleRyHourDiff(record.getPatientId(), record.getRecordDay()));
			}
		}
	}
	
	/**
	 * 处理入院事件差
	 * @param patId
	 * @param recordDay
	 * @return
	 */
	private String handleRyHourDiff(String patId,String recordDay){
		String hourDiff = MnisConstants.EMPTY;
//		//1.获取入院时间
//		String ryDateStr = bodySignRepository.getEventDate(patId,"ry");
//		if(StringUtils.isBlank(ryDateStr)){
//			return hourDiff;
//		}
//		if(!recordDay.substring(0,10).equals(ryDateStr.substring(0,10))){
//			return hourDiff;
//		}
		//根据三院的要求，时间差的计算从出入量医嘱的开立开始算起
		String ryDateStr = orderService.getCreateDateTimeOfPatInoutOrder(patId, recordDay);
		if(StringUtils.isBlank(ryDateStr)){
			return hourDiff;
		}
		if(!recordDay.substring(0,10).equals(ryDateStr.substring(0,10))){
			return hourDiff;
		}
		//2.获取时
		int hour = DateUtil.getHour(ryDateStr.substring(0,19));
		
		if(hour < BodySignConstants.RY_IN_OUT_PUT_STATISTIC_HOUR){
			hourDiff = "("+ (BodySignConstants.RY_IN_OUT_PUT_STATISTIC_HOUR-hour) +"h)";
		}else if(hour > BodySignConstants.RY_IN_OUT_PUT_STATISTIC_HOUR){
			hourDiff = "("+ (24 - hour + BodySignConstants.RY_IN_OUT_PUT_STATISTIC_HOUR) +"h)";
		}
		
		return hourDiff;
	}
	
	public void handleDeletePatBodySignDetail(List<BodySignRecord> records){
		if(null == records || records.size()== 0){
			return;
		}
		Date date = new Date();
		BodySignDeleteRecord record = null;
		for (BodySignRecord bodySignRecord : records) {
			record = new BodySignDeleteRecord();
			record.setPatId(bodySignRecord.getPatientId());
			record.setDeptCode(bodySignRecord.getDeptCode());
			record.setRecordDate(bodySignRecord.getFullDateTime());
			record.setOperDate(date);
			
			//处理事件
			if(null != bodySignRecord.getEvent() 
					&& StringUtils.isNotBlank(bodySignRecord.getEvent().getEventCode())){
				record.setCode(bodySignRecord.getEvent().getEventCode());
				bodySignRepository.insertBodySignDeleteRecord(record);
			}
			
			//处理体征项目
			if(bodySignRecord.hasBodySignData()){
				for (BodySignItem item : bodySignRecord.getBodySignItemList()) {
					if(StringUtils.isNotBlank(item.getItemCode())){
						if(BodySignConstants.TEMPERATURE.equals(item.getItemCode())){
							BodySignDeleteRecord tempRecord = new BodySignDeleteRecord();
							tempRecord.setPatId(bodySignRecord.getPatientId());
							tempRecord.setDeptCode(bodySignRecord.getDeptCode());
							tempRecord.setRecordDate(bodySignRecord.getFullDateTime());
							tempRecord.setOperDate(date);
							tempRecord.setCode(item.getMeasureNoteCode());
							bodySignRepository.insertBodySignDeleteRecord(tempRecord);
						}
						record.setCode(item.getItemCode());
						bodySignRepository.insertBodySignDeleteRecord(record);
					}
				}
			}
		}
	}
}
