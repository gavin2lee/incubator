package com.anyi.report.json;


public interface ResultCst {
	String SUCCESS = "0";
	// 失败
	String FAILURE = "-1";

	//需报警错误
	String ALERT_ERROR = "-100099";
	
	// 系统出现异常，用于未知错误
	String SYSTEM_ERROR = "-100001";	
	// 无效参数
	String INVALID_PARAMETER = "-100003";
	// 其他NDA端登陆
	String LOGIN_OTHER = "-100004";
	// 会话超时，或未登陆
	String LOGIN_TIMEOUT = "-100005";
	// 必填字段为空参数
	String NOT_FILL_MUST_FIELD = "-100006";
	
	// 登陆失败
	String LOGIN_FAILD = "100002";
	
	//	指纹登记数量不允许超过3个
	String OVER_FLOW_FINGER_COUNT = "100004";
	
	//记录不存在
	String NOT_EXIST_RECORD = "100005";
	
	//用户账号不存在
	String  NOT_EXIST_USER = "100006";
	
	//	非本人配置
	String USERCONFIG_NOT_SELF = "109001";
	
	
	//	重复配置
	String USERCONFIG_RE_CONFIG = "109002";
	
	//	无效的用户配置配置，主要是用户配置为空同时系统配置为空。
	String USERCONFIG_INVALID_CONFIG = "109003";
	
	//	无效的用户配置配置值，配置值不对应系统选项或非开关值。
	String USERCONFIG_INVALID_CONFIG_VALUE = "109004";
	
	//	无效的系统配置配置，主要是用户配置为空同时系统配置为空。
	String SYSCONFIG_INVALID_CONFIG = "109005";
	
	//	无效的系统配置配置值，配置值不对应系统选项或非开关值。
	String SYSCONFIG_INVALID_CONFIG_VALUE = "109006";
	
	// 设置Pub/Sub用户配置信息失败！
	String SYSCONFIG_PUB_SUB_FAILED = "109007";

	// 传入参数不能为空！
	String NOT_NULL_PARAMETER = "100020";
	
	// 传入日期格式有误！
	String ILLEGAL_DATE_PARAMETER = "100021";
	
	//该段时间已录入！
	String BODYSIGN_EXISTS_SAME_CODE = "601001";
	
	String SHIFT_BOOK_ERROR = "-100007";
}

	