package com.lachesis.mnis.core.redis;

import java.util.HashMap;
import java.util.Map;

/**
 *@author xin.chen
 *
 */
public final class RedisConstants {
	
	private RedisConstants(){
		throw new AssertionError();
	}
	//打开
	public static final String ENABLE = "on";
	//关闭
	public static final String DISABLE = "off";
	//成功
	public static final String SUCCESS = "ok";
	//键名称：是否启用 Pub/Sub
	public static final String KEY_ENABLE_PUBSUB = "enable.pubsub";
	//键名称：Pub/Sub 结果 
	public static final String KEY_PUBSUB_RESULT = "pubsub.result";
	//键名称：Pub/Sub 用户配置
	public static final String KEY_PUBSUB_USER_CONFIG = "pubsub.user.config:";
	//PubSub 用户配置属性:是否及时提醒
	public static final String FIELD_INTIME = "inTime";
	//PubSub 用户配置属性:定时提醒
	public static final String FIELD_ATTIME = "atTime";
	//PubSub 用户配置属性:整点提醒
	public static final String FIELD_HOURS = "hours";
	public static final long SKIN_TEST_TIME_MINUTE = 20;
	//PubSub 用户配置属性:提前提醒(小时)
	public static final String FIELD_BEFORETIME_HOUR = "beforeTimeHour";
	//PubSub 用户配置属性:提前提醒(分钟)
	public static final String FIELD_BEFORETIME_MINUTE = "beforeTimeMinute";
	public static final String FIELD_PLANEDTIME_MILLIS = "planedTimeMillis";
	public static final String FIELD_MSG = "msg";
	//PubSub 用户配置属性:每隔多长时间提醒(小时)
	public static final String FIELD_BETWEEN_HOUR = "betweenHour";
	//PubSub 用户配置属性:每隔多长时间提醒(分钟)
	public static final String FIELD_BETWEEN_MINUTE = "betweenMinute";
	//PubSub 用户配置属性:是否重复(永不，周一至周日)
	public static final String FIELD_REPEAT = "repeat";
	//PubSub 用户配置属性:是否启用Pub/Sub(1-是，0-否)
	public static final String FIELD_ENABLE = "enable";
	//PubSub 用户配置属性名称数组
	public static final String[] FIELDS_PUBSUB_USER_CONFIG = {FIELD_INTIME,FIELD_ATTIME,FIELD_HOURS,FIELD_BEFORETIME_HOUR,
		FIELD_BETWEEN_MINUTE,FIELD_BETWEEN_HOUR,FIELD_BETWEEN_MINUTE,FIELD_REPEAT};
	//医院危急值提醒频道名称
	public static final String CNL_CRITICAL_VALUE = "cnl.critical.value.";
	//医嘱状态改变提醒频道名称
    public static final String CNL_DOCTORADVICE_CHANGE = "cnl.doctoradvice.change.";
    //医嘱执行提醒频道名称
    public static final String CNL_DOCTORADVICE_EXEC = "cnl.doctoradvice.exec.";
    //生命体征测量提醒频道名称
    public static final String CNL_BODYSIGN_MEASUREMENT = "cnl.bodysign.measurement.";
    /** 检验报告提醒频道  */
    public static final String CNL_LISLAB = "cnl.lislab.";
    /** 检查报告通知频道     */
    public static final String CNL_INSPECTION = "cnl.inspection.";
    //病房巡视提醒频道名称
    public static final String CNL_WARD_PATROL = "cnl.ward.patrol.";
    //皮试提醒频道名称
    public static final String CNL_SKIN_TEST = "cnl.skintest.";
    //交班本未阅读内容提醒频道名称
    public static final String CNL_SHIFTEXCHANGE = "cnl.shiftexchange.";
    //任务清单
    public static final String CNL_NURSE_TASK = "cnl.nur.task.";
    /**
     * 通用消息频道
     */
    public static final String CNL_GENERAL_MESSAGE = "cnl.general.msg";
    
    //订阅频道的配置属性对应
    public static final String[] PUBSUB_CHANNELS = {CNL_CRITICAL_VALUE,CNL_DOCTORADVICE_CHANGE,CNL_DOCTORADVICE_EXEC,
    	CNL_BODYSIGN_MEASUREMENT,CNL_LISLAB,CNL_INSPECTION,CNL_NURSE_TASK,CNL_WARD_PATROL,CNL_SHIFTEXCHANGE,CNL_SKIN_TEST};
    //登录后需要订阅的频道
    public static final String[] LOGINSUB_CHANNELS ={CNL_CRITICAL_VALUE,CNL_DOCTORADVICE_CHANGE,CNL_DOCTORADVICE_EXEC,
    	CNL_BODYSIGN_MEASUREMENT,CNL_LISLAB,CNL_INSPECTION,CNL_NURSE_TASK};
    
    //用户所有已订阅频道集合的键名称
    public static final String SUBSCRIBE_LIST_USER = "subscribe.list.user.";
    
    //订阅频道所对应的提醒属性
    public static final Map<String,String[]> MAP_CHANNEL_PROPS = new HashMap<String, String[]>();
    static{
    	MAP_CHANNEL_PROPS.put(CNL_WARD_PATROL, new String[]{FIELD_BETWEEN_HOUR,FIELD_ENABLE});
    	MAP_CHANNEL_PROPS.put(CNL_DOCTORADVICE_EXEC, new String[]{FIELD_BEFORETIME_MINUTE,FIELD_ENABLE});
    	MAP_CHANNEL_PROPS.put(CNL_DOCTORADVICE_CHANGE, new String[]{FIELD_ENABLE});
    	MAP_CHANNEL_PROPS.put(CNL_CRITICAL_VALUE, new String[]{FIELD_ENABLE});
    	MAP_CHANNEL_PROPS.put(CNL_BODYSIGN_MEASUREMENT, new String[]{FIELD_ENABLE,FIELD_BEFORETIME_MINUTE});
    	MAP_CHANNEL_PROPS.put(CNL_LISLAB, new String[]{FIELD_ENABLE});
    	MAP_CHANNEL_PROPS.put(CNL_INSPECTION, new String[]{FIELD_ENABLE});
    	MAP_CHANNEL_PROPS.put(CNL_NURSE_TASK, new String[]{FIELD_ENABLE});
    	MAP_CHANNEL_PROPS.put(CNL_SKIN_TEST, new String[]{FIELD_ENABLE});
    }
    /**
     * 该key对应的value存储的是需要做病房巡视提醒的用户的id
     */
    public static final String WARD_PATROL_USERS_KEY = "push.ward.patrol.users";
    
    public static final String SKIN_TEST_USERS_KEY = "push.skin.test.users";
    /**
     * 该key对应的value存储的是所有需要提醒的用户的id
     */
    public static final String PUBSUB_USERS_KEY = "push.all.users";
    /**
     * 危急值前一次查询时间key
     */
    public static final String R_CRITICAL_PRE_TIME_KEY = "redis.critical.pre.time";
    /**
     * 危急值key
     */
    public static final String R_CRITICAL_KEY = "critical";
}
