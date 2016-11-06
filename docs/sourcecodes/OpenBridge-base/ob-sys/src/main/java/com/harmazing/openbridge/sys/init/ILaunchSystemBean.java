package com.harmazing.openbridge.sys.init;

/**
 * 系统初始化接口， 如果在init.xml中应实现类配置了resource属性，其实现类必须提供带参数的构造函数
 * 
 * @author taoshuangxi
 *
 */
public interface ILaunchSystemBean {
	void onFirstStartup(InitContext context) throws Exception;
}
