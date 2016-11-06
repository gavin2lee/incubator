package com.harmazing.openbridge.project.service;

import java.util.List;

import com.harmazing.openbridge.project.vo.ApiVersionProtocol;

/**
* @ClassName: IAppBuildService 
* @Description: APP 下载指引接口
* @author weiyujia
* @date 2016年7月19日 上午10:22:14 
*
 */
public interface IApiGuideService {

	/**
	* @Title: createApiDevelopSdkSources 
	* @author weiyujia
	* @Description: APP下载指引生成下载代码并打包
	* @param model  app版本信息
	 * @param appModel 
	* @param apiInfoList app依赖的服务列表
	 * @param app 
	 * @param request 
	* @return String    返回类型 
	* @date 2016年7月19日 上午10:22:31
	 */
//	String createApiDevelopSdkSources(List<ApiVersionProtocol> apiInfoList);

	/**
	* @Title: initApiDevelopSdk 
	* @author weiyujia
	* @Description: 初始化代码
	* @param apiInfoList
	* @date 2016年7月25日 下午8:18:46
	 */
	String initApiDevelopSdk(List<ApiVersionProtocol> apiInfoList);

	/**
	* @Title: getDepenciesApiInfoList 
	* @author weiyujia
	* @Description: 获取依赖API列表
	* @param versionId
	 * @param type 
	* @return  设定文件 
	* @return List<ApiVersionProtocol>    返回类型 
	* @throws 
	* @date 2016年7月26日 下午7:49:41
	 */
	List<ApiVersionProtocol> getDepenciesApiInfoList(String versionId, String type);


}
