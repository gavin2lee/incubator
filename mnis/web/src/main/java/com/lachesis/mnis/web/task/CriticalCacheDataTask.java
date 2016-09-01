package com.lachesis.mnis.web.task;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.lachesis.mnis.core.CriticalValueService;
import com.lachesis.mnis.core.critical.entity.CriticalValueRecord;
import com.lachesis.mnis.core.redis.RedisConstants;
import com.lachesis.mnis.core.util.DateUtil;

/**
 * 危急值数据缓存
 * 
 * @author liangming.deng
 *
 */
@Component
public class CriticalCacheDataTask {
	@Autowired
	private CriticalValueService criticalValueService;
	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	private static Logger LOGGER = LoggerFactory
			.getLogger(CriticalCacheDataTask.class);

	public void criticalCacheDataToRedis() {
		LOGGER.debug("criticalCacheDataToRedis start --------------");
		try {
			// 1.获取上次缓存时间
			String startDate = redisTemplate.opsForValue().get(
					RedisConstants.R_CRITICAL_PRE_TIME_KEY);
			//当前查询数据结束时间
			String endDate = DateUtil.format();
			if (StringUtils.isBlank(startDate)) {
				startDate = DateUtil.format();
			}
			// 2.获取缓存数据
			List<CriticalValueRecord> criticalValueRecords = criticalValueService
					.getCriticalValueRecord(null, null, startDate, endDate, false);
			LOGGER.debug("criticalCacheDataToRedis size: --------------" + criticalValueRecords.size());
			
			// 3.循环缓存数据key:critical_deptNo,hashkey:barcode
			for (CriticalValueRecord criticalValueRecord : criticalValueRecords) {
				// 部门不存在!
				String deptCode = criticalValueRecord.getDeptNo();
				if (StringUtils.isBlank(deptCode)) {
					LOGGER.debug("criticalCacheDataToRedis criticalValueRecord not exist ");
					continue;
				}

				criticalValueService.saveDataToRedis(
						RedisConstants.R_CRITICAL_KEY + deptCode,
						criticalValueRecord.getCriticalValueId(),
						criticalValueRecord);

			}

			redisTemplate.opsForValue().set(
					RedisConstants.R_CRITICAL_PRE_TIME_KEY, DateUtil.format());
		} catch (Exception e) {
			LOGGER.error("criticalCacheDataToRedis error:" + e.getMessage());
		}
		LOGGER.debug("criticalCacheDataToRedis end --------------");
	}
}
