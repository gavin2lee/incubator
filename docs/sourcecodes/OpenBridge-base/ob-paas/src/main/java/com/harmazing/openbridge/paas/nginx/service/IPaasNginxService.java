package com.harmazing.openbridge.paas.nginx.service;

import java.util.List;
import java.util.Map;

import com.harmazing.framework.authorization.IUser;
import com.harmazing.framework.common.Page;
import com.harmazing.framework.common.service.IBaseService;
import com.harmazing.openbridge.paas.nginx.model.PaasNginxConf;
import com.harmazing.openbridge.paas.nginx.vo.NginxConfVo;

public interface IPaasNginxService extends IBaseService {

	/**
	 * 创建一个nginx的配置（含文件上传）
	 * 
	 * @param serviceId
	 * @param envType
	 * @param versionId
	 * @param confContent
	 *            Json字符串,格式：{"domain":"?";"server":[{"ip":"?";"port":"?"}...]}
	 * @throws Exception
	 */
	public void createNginxConf(PaasNginxConf conf, IUser user,String platform, StringBuilder sb)
			throws Exception;

	public void deleteNginxConf(String confId, IUser user) throws Exception;

	public void updateNginxConf(PaasNginxConf conf, IUser user, StringBuilder sb)
			throws Exception;

	public PaasNginxConf findConfByConfId(String confId, String userId);

	public NginxConfVo findConfVoByConfId(String confId, String userId) throws Exception;

	public List<NginxConfVo> findNginxConf(String serviceId, String envType, String userId)
			throws Exception;

	public List<NginxConfVo> getNginxListByEnvId(String envId, String userId) throws Exception;


	Page<PaasNginxConf> queryNginxPage(Map<String, Object> params, String userId);
}
