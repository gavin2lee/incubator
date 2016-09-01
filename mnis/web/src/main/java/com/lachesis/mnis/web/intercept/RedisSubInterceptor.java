package com.lachesis.mnis.web.intercept;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;

import com.lachesis.mnis.core.IdentityService;
import com.lachesis.mnis.core.identity.entity.UserInformation;
import com.lachesis.mnis.core.patrol.entity.WardPatrolInfo;
import com.lachesis.mnis.core.redis.RedisConstants;
import com.lachesis.mnis.core.redis.RedisService;
import com.lachesis.mnis.core.skintest.entity.SkinTestGroup;
import com.lachesis.mnis.core.skintest.entity.SkinTestInfoLx;
import com.lachesis.mnis.core.skintest.entity.SkinTestItem;
import com.lachesis.mnis.core.util.GsonUtils;
import com.lachesis.mnis.web.common.vo.BaseDataVo;
import com.lachesis.mnis.web.common.vo.BaseMapVo;
import com.lachesis.mnis.web.task.notify.json.BaseJson;
import com.lachesis.mnis.web.task.notify.json.MsgJson;

/**
 * reids 订阅拦截器,主要用于操作触发订阅
 * @author liangming.deng
 *
 */
@Aspect
@Component
public class RedisSubInterceptor {
	private static final Logger LOGGER = LoggerFactory.getLogger(RedisSubInterceptor.class);
	
	@Autowired
	private RedisService redisService;
	
	@Autowired
	private IdentityService identityService;

	/**
	 * Upon saving wardPatrolInfo, add a pushing channel for this user and this patient.
	 * The intercepted method's second parameter must be emplCode.. 
	 * @param joinPoint
	 * @return
	 * @throws Throwable
	 */
	@Around("execution(* com.lachesis.mnis.web.PatientGlanceAction.saveWardPatrolInfo(..))")
	public Object wardPatrolEnablePubsub(final ProceedingJoinPoint joinPoint) throws Throwable {
		final BaseDataVo result = (BaseDataVo) joinPoint.proceed();
		
		final WardPatrolInfo wardPatrolInfo = (WardPatrolInfo) result.getData();
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					int tendLevel = wardPatrolInfo.getTendLevel();
					// 特级护理不提醒
					if(tendLevel == 0) {
						return;
					}
					
					// find userId
					UserInformation information = identityService.queryUserByCode((String) joinPoint.getArgs()[1]);
					if (information == null) {
						return;
					}
					// find patientId
					String patientId = (String) joinPoint.getArgs()[joinPoint.getArgs().length - 1];
//					patientId = patientId.replaceAll("[^\\d]", "");
					if (StringUtils.isEmpty(patientId) || wardPatrolInfo == null) {
						return;
					}
					
					String userId = String.valueOf(information.getUserId()) ;
					// Add or update a ward patrol push channel for user
					Jedis jedis = redisService.getRedis();
					jedis.sadd(RedisConstants.WARD_PATROL_USERS_KEY, userId);
					redisService.returnRedis(jedis);
					
					// The message to publish
					MsgJson msgJson = new MsgJson();
					msgJson.setDeptCode(wardPatrolInfo.getDeptId());
					BaseJson baseJsonParams = new BaseJson(wardPatrolInfo.getBedCode(), 
							wardPatrolInfo.getPatientName(), wardPatrolInfo.getPatientId());
					
					List<BaseJson> baseJsons = new ArrayList<BaseJson>();
					baseJsons.add(baseJsonParams);
					msgJson.setBaseJsons(baseJsons);
					
					// 病房巡视提醒的基本设置
					saveWardPatrolConfig(wardPatrolInfo.getDeptId(),userId, tendLevel, patientId, msgJson.toJson(),0);
				} catch (Exception e) {
					LOGGER.error("WardpatrolInterceptor Error:" + e.getMessage());
				}
			}
		}).start();
		
		return result;
	}
	
	/**
	 * 皮试执行 拦截订阅器
	 * @param joinPoint
	 * @return
	 * @throws Throwable
	 */
	@SuppressWarnings("unchecked")
	@Around("execution(* com.lachesis.mnis.web.SkinTestOrderAction.execSkinTestForNda(..))")
	public Object skinTestEnablePubsub(final ProceedingJoinPoint joinPoint) throws Throwable {
		final BaseMapVo result = (BaseMapVo) joinPoint.proceed();
		
		Map<String, Object> datas = result.getData();
		if(null == datas){
			return null;
		}
		final List<SkinTestInfoLx> skinTestInfoLxs = (List<SkinTestInfoLx>) datas.get("list");
		String skinTestItemStr =(String) joinPoint.getArgs()[0];
		if(StringUtils.isBlank(skinTestItemStr)){
			return skinTestItemStr;
		}
		final SkinTestItem skinTestItem = GsonUtils.fromJson(skinTestItemStr, SkinTestItem.class);
		if(null == skinTestItem){
			return null;
		}
		new Thread(new Runnable() {
			@SuppressWarnings("unused")
			@Override
			public void run() {
				try {
					// find patientId
					String patientId = skinTestItem.getPatId();
//					patientId = patientId.replaceAll("[^\\d]", "");
					if (StringUtils.isEmpty(patientId)) {
						return;
					}
					
					//UserAccount userAccount = ObjConvertUtil.convertUserInfo(information);
					String userId = String.valueOf(skinTestItem.getSkinTestItemExecNurseId()) ;
					// Add or update a ward patrol push channel for user
					Jedis jedis = redisService.getRedis();
					jedis.sadd(RedisConstants.SKIN_TEST_USERS_KEY, userId);
					redisService.returnRedis(jedis);
					// The message to publish
					SkinTestInfoLx currentSkinTestInfoLx = null;
					for (SkinTestInfoLx skinTestInfoLx : skinTestInfoLxs) {
						for (SkinTestGroup testGroup : skinTestInfoLx.getSkinTestGroups()) {
							if(StringUtils.isNotBlank(testGroup.getSkinTestId())
									&& testGroup.getSkinTestId().equals(skinTestItem.getSkinTestId())){
								currentSkinTestInfoLx = skinTestInfoLx;
								break;
							}
						}
					}
					
					if(currentSkinTestInfoLx == null){
						return;
					}
					
					MsgJson msgJson = new MsgJson();
					msgJson.setDeptCode(skinTestItem.getDeptCode());
					List<BaseJson> baseJsons = new ArrayList<BaseJson>();
					BaseJson baseJson = new BaseJson(currentSkinTestInfoLx.getBedNo(),
							currentSkinTestInfoLx.getPatientName(),currentSkinTestInfoLx.getPatientId());
					baseJsons.add(baseJson);
					msgJson.setBaseJsons(baseJsons);
					// 皮试基本设置
					saveWardPatrolConfig(skinTestItem.getDeptCode(),userId, 0, patientId, msgJson.toJson(),1);
				} catch (Exception e) {
					LOGGER.error("WardpatrolInterceptor Error!");
				}
			}
		}).start();
		
		return result;
	}
	
	/**
	 * 一级护理(tendLevel=1)每1小时巡视，二级护理(tendLevel=2)每2小时巡视，三级护理(tendLevel=3)每3小时巡视，
	 * 用户所管理患者需要病房巡视时予以提醒
	 * @param userId
	 * @param tendLevel
	 * @param enable
	 */
	private void saveWardPatrolConfig(String deptCode,String userId, int tendLevel, String patientId, String pushMsg, int subType) {
		String channel = StringUtils.EMPTY;
		switch (subType) {
		case 0:
			channel = RedisConstants.CNL_WARD_PATROL;
			break;
		case 1:
			channel = RedisConstants.CNL_SKIN_TEST;
			break;
		default:
			break;
		}
		
		/// 1. 用户特定的配置
		Map<String, String> genericConfig = redisService.getPubSubUserConfigAll(userId, null, channel);
		// 如果配置尚不存在则建立之
		if(genericConfig.get(RedisConstants.FIELD_ENABLE) == null) {
			// 启动推送
			genericConfig.put(RedisConstants.FIELD_ENABLE, "1");
			// 提前提醒时间的设置:默认为5分钟
			genericConfig.put(RedisConstants.FIELD_BEFORETIME_MINUTE, "5");
			//设置护士对应的科室
			genericConfig.put("deptCode", deptCode);
			redisService.savePubSubUserConfig(userId, null, channel, genericConfig);
		}
		
		/// 2. 病人特定的配置
		Map<String, String> patientWiseConfig = redisService.getPubSubUserConfigAll(userId, patientId, channel);
		// 每隔多少小时提醒: 由护理等级决定
		int betweenHour = tendLevel;
		
		patientWiseConfig.put(RedisConstants.FIELD_MSG, pushMsg);
		
		long planedPushTime = new Date().getTime();
		switch (subType) {
		case 0:
			patientWiseConfig.put(RedisConstants.FIELD_BETWEEN_HOUR, String.valueOf(betweenHour));
//			planedPushTime = planedPushTime + betweenHour * DateUtils.MILLIS_PER_HOUR;
			planedPushTime = planedPushTime + 6 * DateUtils.MILLIS_PER_MINUTE;
			LOGGER.warn("Setting next alert time !!" + planedPushTime);
			patientWiseConfig.put(RedisConstants.FIELD_PLANEDTIME_MILLIS, String.valueOf(planedPushTime));
			break;
		case 1:
//			planedPushTime = planedPushTime + RedisConstants.SKIN_TEST_TIME_MINUTE * DateUtils.MILLIS_PER_MINUTE;
			planedPushTime = planedPushTime + 1* DateUtils.MILLIS_PER_MINUTE;
			LOGGER.warn("Setting next alert time !!" + planedPushTime);
			patientWiseConfig.put(RedisConstants.FIELD_PLANEDTIME_MILLIS, String.valueOf(planedPushTime));
			break;
		default:
			break;
		}
		redisService.savePubSubUserConfig(userId, patientId, channel, patientWiseConfig);
	}
}
