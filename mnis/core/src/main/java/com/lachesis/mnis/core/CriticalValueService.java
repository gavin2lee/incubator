package com.lachesis.mnis.core;

import java.util.Date;
import java.util.List;

import com.lachesis.mnis.core.critical.entity.CriticalOperRecord;
import com.lachesis.mnis.core.critical.entity.CriticalValue;
import com.lachesis.mnis.core.critical.entity.CriticalValueRecord;
import com.lachesis.mnis.core.identity.entity.UserInformation;
import com.lachesis.mnis.core.redis.RedisDataService;


/**
 * 危急值相关服务
 * @author wenhuan.cui
 *
 */
public interface CriticalValueService extends RedisDataService<CriticalValueRecord>{
	/**
	 * 获取查询用户所在科室的指定日期的病人危急内容
	 * @param patientId 病人id
	 * @param userId 用户Id
	 * @param startDate 指定开始日期 yyyy-MM-dd
	 * @param endDate 指定截至日期 yyyy-MM-dd
	 * @return 
	 */
	List<CriticalValue> getCriticalValue(String deptNo,List<String> patIds,Date startDate, Date endDate, boolean showAll);

	CriticalValue getCriticalValue(String criticalId);

	boolean confirmCritical(String criticalId, String nurseId) ;
	/**
	 * 插入危急值
	 * @param criticalOperRecord
	 * @return
	 */
	int saveCriticalOperRecord(CriticalOperRecord criticalOperRecord,UserInformation userInformation) throws Exception;
	
	int batchSaveCriticalOperRecord(List<CriticalOperRecord> criticalOperRecords,String loginName,String password) throws Exception;
	
	List<CriticalValueRecord> getCriticalValueRecord(String deptCode,String patId,String startDate,String endDate,boolean isAll) throws Exception;	
}
