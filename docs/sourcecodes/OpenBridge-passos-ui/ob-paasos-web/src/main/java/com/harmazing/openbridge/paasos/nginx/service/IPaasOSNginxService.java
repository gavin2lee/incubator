package com.harmazing.openbridge.paasos.nginx.service;

import java.util.List;
import java.util.Map;

import com.harmazing.framework.common.Page;
import com.harmazing.framework.common.service.IBaseService;
import com.harmazing.openbridge.paas.deploy.model.PaasProjectDeployEnv;
import com.harmazing.openbridge.paas.nginx.model.PaasNginxConf;
import com.harmazing.openbridge.paas.nginx.vo.NginxConfVo;
import com.harmazing.openbridge.paasos.project.model.vo.ThreeTuple;

public interface IPaasOSNginxService extends IBaseService {

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
	public void createNginxConf(PaasNginxConf conf, String platform, StringBuilder sb)
			throws Exception;

	public void deleteNginxConf(String confId) throws Exception;

	public void updateNginxConf(PaasNginxConf conf, StringBuilder sb)
			throws Exception;

	public PaasNginxConf findConfByConfId(String confId);

	public NginxConfVo findConfVoByConfId(String confId) throws Exception;

	public List<NginxConfVo> findNginxConf(String serviceId, String envType)
			throws Exception;

	public List<NginxConfVo> getNginxListByEnvId(String envId) throws Exception;

	public boolean checkNginxConfExist(String serviceId, String envType,
			String versionId);

	public PaasNginxConf findNginxConf(String serviceId, String envType,
			String versionId);

	Page<PaasNginxConf> queryNginxPage(Map<String, Object> params);
	
	void deleteNginxHostById(String hostId);
	
	List<PaasNginxConf> findNginx(PaasNginxConf params);
	
	List<PaasProjectDeployEnv> getNginxEnvs(String envType);

	public List<NginxConfVo> getNginxListByVersionIds(List<String> versionIds, String envType,String envMark) throws Exception;
	
}
