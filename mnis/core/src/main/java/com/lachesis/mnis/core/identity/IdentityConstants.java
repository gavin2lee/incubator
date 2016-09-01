package com.lachesis.mnis.core.identity;
/**
 * @author xin.chen
 **/
public class IdentityConstants {
	public static final int LOGIN_SUCCESS = 1;
	public static final int LOGIN_ERROR_MULTIPLE_USER = -1;
	public static final int LOGIN_ERROR_NONEXIST_USER = -2;
	public static final int LOGIN_ERROR_WRONG_PWD = -3;
	public static final int LOGIN_ERROR_CA_LOGIN_FAIL = -4;

	public static final String LOGIN_ERROR_MULTIPLE_USER_MSG = "系统存在多个同名用户，请联系数据库管理员！";
	public static final String LOGIN_ERROR_NONEXIST_USER_MSG = "用户不存在！";
	public static final String LOGIN_ERROR_WRONG_PWD_MSG = "密码错误！";
	public static final String LOGIN_ERROR_INVALID_FP = "无效指纹";
	public static final String LOGIN_ERROR_CA_LOGIN_FAIL_MSG = "CA登录失败";

	public static final int VALIDATE_ERROR_WRONG_TOKEN = -2; // token 错误
	public static final int VALIDATE_ERROR_TIMEOUT = -3; // token过期
	public static final int VALIDATE_ERROR_LOGGED_OUT = -4; // 已登出
	public static final int VALIDATE_ERROR_SERVER_RESTART = -5; // 服务器重启

	public static final String VALIDATE_ERROR_WRONG_TOKEN_MSG = "token失效，请重新登录！";
	public static final String VALIDATE_ERROR_TIMEOUT_MSG = "token过期，请重新登录！";
	public static final String VALIDATE_ERROR_LOGGED_OUT_MSG = "用户已登出";

	public static final String APPROVE_ERROR_WRONG_PWD = "密码错误";
	public static final String APPROVE_ERROR_WRONG_SECRETKEY = "无效指纹";
	public static final String APPROVE_ERROR_UNAUTHED_USER = "无双核对权限或用户不存在";

	// 核对
	public static final String OPERATE_PERMISSION_VERIFY = "verify";
	// login password key
	public final static String ENCRYPT_PWD_KEY = "`1234567890-=~!@#$%^&*()_+qwertyuiop[]\\QWERTYUIOP{}|asdfghjkl;'ASDFGHJKL:zxcvbnm,./ZXCVBNM<>? \"";

	//体温单时间区间划分，根据指定时间节点的偏移量
	public static final int TEMPERATURE_TIME_OFFSET = 2;

	//体温单24小时制
	public static final int TEMPERATURE_24_HOURS = 24;
}
