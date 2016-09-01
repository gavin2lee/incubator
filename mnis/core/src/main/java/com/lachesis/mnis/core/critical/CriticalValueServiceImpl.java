package com.lachesis.mnis.core.critical;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lachesis.mnis.core.CriticalValueService;
import com.lachesis.mnis.core.IdentityService;
import com.lachesis.mnis.core.LabTestService;
import com.lachesis.mnis.core.constants.MnisConstants;
import com.lachesis.mnis.core.critical.entity.CriticalOperRecord;
import com.lachesis.mnis.core.critical.entity.CriticalValue;
import com.lachesis.mnis.core.critical.entity.CriticalValueRecord;
import com.lachesis.mnis.core.critical.repository.CriticalRepository;
import com.lachesis.mnis.core.exception.AlertException;
import com.lachesis.mnis.core.identity.entity.UserInformation;
import com.lachesis.mnis.core.labtest.entity.LabTestRecord;
import com.lachesis.mnis.core.redis.RedisConstants;
import com.lachesis.mnis.core.util.DateUtil;
import com.lachesis.mnis.core.util.PropertiesUtils;

@Service("criticalValueService")
public class CriticalValueServiceImpl implements CriticalValueService{
	
	private static Logger LOGGER = LoggerFactory.getLogger(CriticalValueServiceImpl.class);
	@Autowired
	private CriticalRepository criticalRepository;
	
	@Autowired
	private RedisTemplate<String, CriticalValueRecord> redisTemplate;
	
	@Autowired
	private LabTestService labTestService;
	
	@Autowired
	private IdentityService identityService;
	
	@Override
	public List<CriticalValue> getCriticalValue(String deptNo,List<String> patIds,Date startDate, Date endDate, boolean showAll) {
		return queryCriticalValueList(deptNo, patIds,startDate, endDate, showAll);
	}

	private List<CriticalValue> queryCriticalValueList(String deptNo,List<String> patIds,Date startDate, Date endDate, boolean showAll) {
		
		if(startDate==null || endDate == null){
			Date queryDate = new Date();
//			Date queryDate = DateUtil.parse("2016-05-02");
			startDate = DateUtil.setDateToDay(queryDate);
			endDate = DateUtil.setNextDayToDate(queryDate);
		}
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		if(deptNo != null){
			paramMap.put("deptno", deptNo);
		}
		paramMap.put("startTime", startDate);
		paramMap.put("endTime", endDate);
		paramMap.put("patIds", patIds);
		if(!showAll){
			paramMap.put("status", "0");
		}
		List<CriticalValue> criticalValues= criticalRepository.getCriticalValue(paramMap);
		for (Iterator<CriticalValue> iterator = criticalValues.iterator(); iterator.hasNext();) {
			CriticalValue criticalValue = iterator.next();
			List<LabTestRecord> record = labTestService.getLabTestRecordById(criticalValue.getCriticalValueId());
			criticalValue.setLabtestRecords(record);
		}
		
		return criticalValues;
	}

	@Override
	public CriticalValue getCriticalValue(String criticalId) {
		CriticalValue criticalValue = criticalRepository.getCriticalValueById(criticalId);
		List<LabTestRecord> record = labTestService.getLabTestRecordById(criticalValue.getCriticalValueId());
		criticalValue.setLabtestRecords(record);
		return criticalValue;
	}

	@Override
	public boolean confirmCritical(String criticalId, String nurseId) {
		try{
			int count = criticalRepository.configCritical(criticalId, nurseId);
			if(count > 0)
				return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		
		return false;
	}

	@Override
	public int saveCriticalOperRecord(CriticalOperRecord criticalOperRecord,UserInformation userInformation) {
		if(null == criticalOperRecord 
				||StringUtils.isBlank(criticalOperRecord.getBarcode())){
			LOGGER.error("criticalValue getCriticalValueRecord CriticalOperRecord is null");
			throw new AlertException("参数为空!");
		}
		if(criticalRepository.getCritiveRecordCount(criticalOperRecord.getBarcode()) > 0){
			LOGGER.error(criticalOperRecord.getBarcode() + "[已处理]");
			return 0;
		}
		
		if(null == criticalOperRecord.getOperTime()){
			criticalOperRecord.setOperTime(new Date());
		}
		
		if(StringUtils.isBlank(criticalOperRecord.getOperNurseCode())){
			criticalOperRecord.setOperNurseCode(userInformation.getCode());
			criticalOperRecord.setOperNurseName(userInformation.getName());
		}
		//缓存中删除(不影响流程)
		try {
			delDataToRedis(RedisConstants.R_CRITICAL_KEY+userInformation.getDeptCode(), 
					criticalOperRecord.getBarcode());
		} catch (Exception e) {
			LOGGER.error("saveCriticalOperRecord delDataToRedis error:" + e.getMessage());
		}
		int count = criticalRepository.insertCriticalOperRecord(criticalOperRecord);
		
		return count;
	}

	@Override
	public List<CriticalValueRecord> getCriticalValueRecord(String deptCode,
			String patId, String startDate,String endDate, boolean isAll) {
		List<CriticalValueRecord> criticalValueRecords = null;
		//缓存获取
		try {
			criticalValueRecords = getAllDataList(RedisConstants.R_CRITICAL_KEY+deptCode);
		} catch (Exception e) {
			LOGGER.error("getCriticalValueRecord redis error:" + e.getMessage());
		}
		if(criticalValueRecords != null && !criticalValueRecords.isEmpty()){
			return criticalValueRecords;
		}
		
		//数据库获取
		Date startTime = null;
		Date endTime = null;
		
		if(StringUtils.isBlank(startDate)){
			startTime = new Date();
		}else{
			startTime = DateUtil.parse(startDate);
		}
		
		//危急值开始时间(解决历史遗留数据)
		String criticalStartDateStr = PropertiesUtils.findPropertiesKey(MnisConstants.PROP_PATH,
				MnisConstants.PROP_CRITICAL_START_DATE_KEY);
//		String criticalStartDateStr = "2016-05-02";
		if(StringUtils.isNotBlank(criticalStartDateStr)){
			Date criticalStartDate = DateUtil.parse(criticalStartDateStr);
			if(startTime.getTime() <= criticalStartDate.getTime()){
				startTime = criticalStartDate;
			}
		}
		
		if(StringUtils.isNotBlank(endDate)){
			endTime = DateUtil.parse(endDate);
		}
		
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("deptCode", deptCode);
		params.put("patId", patId);
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		if(isAll){
			params.put("isAll", isAll);
		}else{
			params.put("isAll", null);
		}
		criticalValueRecords = criticalRepository.getCriticalValueRecord(params);
		return criticalValueRecords;
	}

	@Transactional
	@Override
	public int batchSaveCriticalOperRecord(
			List<CriticalOperRecord> criticalOperRecords,String loginName,String password) throws Exception {
		if(null == criticalOperRecords){
			LOGGER.error("criticalValue batchSaveCriticalOperRecord criticalOperRecords is null");
			throw new AlertException("参数为空!");
		}
		//用户校验
		UserInformation userInformation = identityService.verifyUser(loginName, password, null, false);
		if(null == userInformation){
			LOGGER.error("batchSaveCriticalOperRecord userInformation is null");
			throw new AlertException("用户名或密码错误!");
		}
		int count = 0;
		for (CriticalOperRecord criticalOperRecord : criticalOperRecords) {
			count = saveCriticalOperRecord(criticalOperRecord,userInformation);
		}
		
		return count;
	}

	@Override
	public void saveDataToRedis(String key, String hashKey,
			CriticalValueRecord t) {
		//hashkey存在,就不操作
		if(redisTemplate.opsForHash().hasKey(key, hashKey)){
			return;
		}
		
		redisTemplate.opsForHash().put(key, hashKey, t);
	}

	@Override
	public void delDataToRedis(String key, String hashKey) {
		redisTemplate.opsForHash().delete(key, hashKey);
	}

	@Override
	public CriticalValueRecord getDataByIdFromRedis(String key, String hashKey) {
		return (CriticalValueRecord) redisTemplate.opsForHash().get(key, hashKey);
	}

	@Override
	public Map<Object, Object> getAllData(String key) {
		return redisTemplate.opsForHash().entries(key);
	}


	@Override
	public List<CriticalValueRecord> getAllDataList(String key) throws Exception {
		Map<Object,Object> dataMap = getAllData(key);
		List<CriticalValueRecord>  criticalValueRecords = new ArrayList<CriticalValueRecord>();
		for (Object obj : dataMap.keySet()) {
			Object valObject = dataMap.get(obj);
			if(null != valObject){
				criticalValueRecords.add((CriticalValueRecord) valObject);
			}
		}
		return criticalValueRecords;
	}

}
