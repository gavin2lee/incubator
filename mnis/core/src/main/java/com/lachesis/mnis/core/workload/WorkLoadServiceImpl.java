package com.lachesis.mnis.core.workload;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lachesis.mnis.core.WorkLoadService;
import com.lachesis.mnis.core.exception.MnisException;
import com.lachesis.mnis.core.util.DateUtil;
import com.lachesis.mnis.core.util.DateUtil.DateFormat;
import com.lachesis.mnis.core.workload.entity.WorkLoadInfo;
import com.lachesis.mnis.core.workload.entity.WorkLoadType;
import com.lachesis.mnis.core.workload.repository.WorkLoadRepository;

@Service
public class WorkLoadServiceImpl implements WorkLoadService {

	private static Logger LOGGER = LoggerFactory
			.getLogger(WorkLoadServiceImpl.class);
	@Autowired
	private WorkLoadRepository workLoadRepository;

	@Override
	public List<WorkLoadInfo> getWorkLoadInfosByNurse(String deptCode,
			String nurseCode, List<String> types, String startDate,
			String endDate) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		if (StringUtils.isBlank(deptCode)) {
			LOGGER.error("WorkLoadServiceImpl getWorkLoadInfos deptCode is null!");
			throw new MnisException("科室为空!");
		}
		Date date = new Date();
		if (StringUtils.isBlank(startDate) || StringUtils.isBlank(endDate)) {
			Date[] dates = DateUtil.getQueryRegionDates(date);
			startDate = DateUtil.format(dates[0], DateFormat.FULL);
			endDate = DateUtil.format(dates[1], DateFormat.FULL);
		}

		if (StringUtils.isBlank(nurseCode)) {
			nurseCode = null;
		}

		params.put("deptCode", deptCode);
		params.put("types", types);
		params.put("nurseCode", nurseCode);
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		//对比当天获取区间
		int region = getQueryDateRegion(startDate, endDate, DateUtil.format(date,DateFormat.YMD));
		List<WorkLoadInfo> workLoadInfos = null;
		switch (region) {
		case 1:
			workLoadInfos = workLoadRepository
					.getTodayWorkLoadInfosByNurse(params);
			break;
		case 2:
			workLoadInfos = workLoadRepository
					.getPreTodayWorkLoadInfosByNurse(params);
			break;
		default:
			workLoadInfos = workLoadRepository.getWorkLoadInfosByNurse(params);
			break;
		}

		return workLoadInfos;
	}

	@Override
	public List<WorkLoadInfo> getWorkLoadInfosByNurseType(String deptCode,
			String nurseCode, List<String> types, List<String> childrenTypes,
			String startDate, String endDate) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		if (StringUtils.isBlank(deptCode)) {
			LOGGER.error("WorkLoadServiceImpl getWorkLoadInfos deptCode is null!");
			throw new MnisException("科室为空!");
		}
		Date date = new Date();
		if (StringUtils.isBlank(startDate) || StringUtils.isBlank(endDate)) {
			Date[] dates = DateUtil.getQueryRegionDates(date);
			startDate = DateUtil.format(dates[0], DateFormat.FULL);
			endDate = DateUtil.format(dates[1], DateFormat.FULL);
		}
		if (StringUtils.isBlank(nurseCode)) {
			nurseCode = null;
		}
		params.put("deptCode", deptCode);
		params.put("nurseCode", nurseCode);
		params.put("types", types);
		params.put("childrenTypes", childrenTypes);
		params.put("startDate", startDate);
		params.put("endDate", endDate);

		int region = getQueryDateRegion(startDate, endDate, DateUtil.format(date,DateFormat.YMD));
		List<WorkLoadInfo> workLoadInfos = null;
		switch (region) {
		case 1:
			workLoadInfos = workLoadRepository
					.getTodayWorkLoadInfosByNurseType(params);
			break;
		case 2:
			workLoadInfos = workLoadRepository
					.getPreTodayWorkLoadInfosByNurseType(params);
			break;
		default:
			workLoadInfos = workLoadRepository
					.getWorkLoadInfosByNurseType(params);
			break;
		}

		return workLoadInfos;
	}

	@Override
	public List<WorkLoadType> getWorkLoadTypes() {
		List<WorkLoadType> topTypes = workLoadRepository.getTopWorkLoadTypes();

		for (WorkLoadType workLoadType : topTypes) {
			List<WorkLoadType> childrenTypes = workLoadRepository
					.getChildrenWorkLoadTypes(workLoadType.getType());
			workLoadType.setChildren(childrenTypes);
		}
		return topTypes;
	}

	@Override
	public HashMap<String, String> getWorkLoadTypesMap() {
		HashMap<String, String> workLoadTypeMap = new HashMap<String, String>();
		List<WorkLoadType> topTypes = workLoadRepository.getTopWorkLoadTypes();

		for (WorkLoadType workLoadType : topTypes) {
			workLoadTypeMap.put(workLoadType.getType(), workLoadType.getName());
			List<WorkLoadType> childrenTypes = workLoadRepository
					.getChildrenWorkLoadTypes(workLoadType.getType());
			for (WorkLoadType child : childrenTypes) {
				workLoadTypeMap.put(workLoadType.getType() + child.getType(),
						child.getName());
			}
		}
		return workLoadTypeMap;
	}

	/**
	 * 根据查询时间对比当天获取区间 0：当天和当天以前 1: 当天 2: 当天以前
	 * 
	 * @param start
	 * @param end
	 * @param now
	 * @return
	 */
	private int getQueryDateRegion(String start, String end, String now) {
		int region = 0;
		long startLong = DateUtil.parse(start).getTime();
		long endLong = DateUtil.parse(end).getTime();
		long nowLong = DateUtil.parse(now).getTime();

		if (nowLong == startLong) {
			region = 1;
		} else if (nowLong >= endLong) {
			region = 2;
		} else {
			region = 0;
		}
		return region;
	}

}
