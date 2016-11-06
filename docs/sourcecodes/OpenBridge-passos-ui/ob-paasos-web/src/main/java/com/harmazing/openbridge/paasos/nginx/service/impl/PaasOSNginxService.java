package com.harmazing.openbridge.paasos.nginx.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.harmazing.framework.common.Page;
import com.harmazing.framework.util.ConfigUtil;
import com.harmazing.framework.util.FileUtil;
import com.harmazing.framework.util.PaasAPIUtil;
import com.harmazing.framework.util.StringUtil;
import com.harmazing.framework.util.ssh.ShellClient;
import com.harmazing.openbridge.paas.Constants;
import com.harmazing.openbridge.paas.deploy.model.PaasProjectDeployEnv;
import com.harmazing.openbridge.paas.nginx.model.PaasHost;
import com.harmazing.openbridge.paas.nginx.model.PaasNginxConf;
import com.harmazing.openbridge.paas.nginx.model.PaasNginxHost;
import com.harmazing.openbridge.paas.nginx.vo.BalanceConfVo;
import com.harmazing.openbridge.paas.nginx.vo.NginxConfVo;
import com.harmazing.openbridge.paas.util.EtcdUtil;
import com.harmazing.openbridge.paasos.nginx.dao.PaasNginxMapper;
import com.harmazing.openbridge.paasos.nginx.service.IPaasOSHostService;
import com.harmazing.openbridge.paasos.nginx.service.IPaasOSNginxService;
import com.jcraft.jsch.JSchException;

@Service
public class PaasOSNginxService implements IPaasOSNginxService {
	@Autowired
	private IPaasOSHostService hostService;
	@Autowired
	private PaasNginxMapper paasNginxMapper;

	private List<PaasNginxHost> getAllNginxHost(String type, String platform) {
		List<PaasNginxHost> ret = new ArrayList<PaasNginxHost>();
		List<PaasHost> list = hostService.getHostByType(Constants.NGINX
				.getName(), platform);
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getEnvType().equals(type)) {
				ret.add(new PaasNginxHost(list.get(i)));
			}
		}
		return ret;
	}

	public PaasNginxHost getNginxHost(String hostId) {
		return new PaasNginxHost(hostService.getHostById(hostId));
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void updateNginxConf(PaasNginxConf conf,StringBuilder ret) throws Exception {
		PaasNginxConf oldNginx = paasNginxMapper.findNginxBySvrIdAndName(
				conf.getServiceId(), conf.getNginxName());
		if (oldNginx != null && !oldNginx.getConfId().equals(conf.getConfId())) {
			ret.append("同一服务的Nginx配置名称必须唯一");
			throw new Exception("同一服务的Nginx配置名称必须唯一");
		}
		if(isDomainConflict(conf)){
			throw new Exception("域名必须唯一");
		}
		this.paasNginxMapper.updateNginxConf(conf);
		if(ConfigUtil.getOrElse("SSH_TRANSFOR_NGINX_CONFIG", "false").equals("false")){
			this.generateLocalNginxConf(conf, ret);
		}else{
			this.transferNginxConf(conf, ret);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void deleteNginxConf(String confId) throws Exception {
		StringBuilder result = new StringBuilder();
		PaasNginxConf conf = this.findConfByConfId(confId);
		if (conf != null) {
			if(ConfigUtil.getOrElse("SSH_TRANSFOR_NGINX_CONFIG", "false").equals("false")){
				this.deleteLocalNginxConf(conf, result);
			}else{
				this.delNginxConf(conf, result);
			}
			this.paasNginxMapper.deleteNginxConf(confId);
		} else {
			throw new RuntimeException("删除的负载配置不存在！");
		}
	}

	@Transactional(readOnly = true)
	@Override
	public List<NginxConfVo> findNginxConf(String serviceId, String envType)
			throws Exception {
		return this.convert(this.paasNginxMapper.findNginxConf(serviceId,
				envType, null));
	}

	@Override
	@Transactional(readOnly = true)
	public List<NginxConfVo> getNginxListByEnvId(String envId) throws Exception {
		return this.convert(this.paasNginxMapper.getNginxListByEnvId(envId));
	}

	@Transactional(readOnly = true)
	@Override
	public PaasNginxConf findNginxConf(String serviceId, String envType,
			String versionId) {
		List<PaasNginxConf> result = this.paasNginxMapper.findNginxConf(
				serviceId, envType, versionId);
		if (result != null && !result.isEmpty()) {
			return result.get(0);
		} else {
			return null;
		}
	}

	@Transactional(readOnly = true)
	@Override
	public PaasNginxConf findConfByConfId(String confId) {
		return this.paasNginxMapper.findByConfId(confId);
	}

	@Transactional(readOnly = true)
	@Override
	public NginxConfVo findConfVoByConfId(String confId) throws Exception {
		NginxConfVo vo = this
				.convert(this.paasNginxMapper.findByConfId(confId));
		if (vo != null) {
			return vo;
		} else {
			throw new Exception("负载均衡配置服务器不存在");
		}
	}

	@Transactional(readOnly = true)
	@Override
	public boolean checkNginxConfExist(String serviceId, String envType,
			String versionId) {
		List<PaasNginxConf> confList = this.paasNginxMapper.findNginxConf(
				serviceId, envType, versionId);
		if (confList != null && confList.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 申请宿主机器
	 */
	public PaasNginxHost applyHost(String type, String platform) throws Exception{
		List<PaasNginxHost> list = this.getAllNginxHost(type, platform);
		if (list.size() <= 0)
			throw new Exception("没有可用的主机");

		List<Map<String, Object>> ccount = this.paasNginxMapper
				.getLightNginxHost(type);
		for (int j = 0; j < list.size(); j++) {
			boolean exist = false;
			for (int i = 0; i < ccount.size(); i++) {
				if (ccount.get(i).get("hostId").toString()
						.equals(list.get(j).getHostId())) {
					exist = true;
					break;
				}
			}
			if (exist == false) {
				// 如果该主机还没有Docker容器，就在该主机创建
				return list.get(j);
			}
		}
		// 数据库SQL排序。按最小的个数排序创建
		for (int i = 0; i < ccount.size(); i++) {
			String hostId = ccount.get(i).get("hostId").toString();
			for (int j = 0; j < list.size(); j++) {
				if (list.get(j).getHostId().equals(hostId)) {
					return list.get(j);
				}
			}
		}
		if (list.size() > 0) {
			return list.get(0);
		}
		throw new RuntimeException("没有可用的主机");
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void createNginxConf(PaasNginxConf config, String platform,
			StringBuilder sb) throws Exception {
		String serviceId = config.getServiceId();
		String envType = config.getEnvType();
		String nginxConfigName = config.getNginxName();
		PaasNginxConf oldNginx = paasNginxMapper.findNginxBySvrIdAndName(
				serviceId, nginxConfigName);
		if (oldNginx != null) {
			throw new Exception("同一服务的负载均衡配置名称必须唯一");
		}
		if(isDomainConflict(config)){
			throw new Exception("域名必须唯一");
		}
		PaasNginxHost goalHost = applyHost(envType,platform);
		if (goalHost != null
				&& paasNginxMapper.getExistConfNum(goalHost.getHostId()) <= Integer
						.valueOf(Constants.MAX_NGINX_WEIGTH.getName())) {
			try{
				config.setHostId(goalHost.getHostId());
				this.paasNginxMapper.createNginxConf(config);
				if(ConfigUtil.getOrElse("SSH_TRANSFOR_NGINX_CONFIG", "false").equals("false")){
					this.generateLocalNginxConf(config, sb);
				}else{
					this.transferNginxConf(config, sb);
				}
				sb.append("成功添加负载均衡配置");
			}catch(Exception ex){
				throw new RuntimeException("添加nginx失败"+ex.getMessage());
			}
		} else {
			throw new Exception("Nginx服务器已经超载，请增加Nginx服务器");
		}
	}

	@Override
	@Transactional(readOnly = true)
	public Page<PaasNginxConf> queryNginxPage(Map<String, Object> params) {
		Page<PaasNginxConf> xpage = Page.create(params);
		xpage.setRecordCount(paasNginxMapper.queryNginxCount(params));
		xpage.addAll(paasNginxMapper.queryNginxPage(params));
		return xpage;
	}

	private void transferNginxConf(PaasNginxConf conf, StringBuilder result)
			throws Exception {
		PaasNginxHost goalHost = new PaasNginxHost(hostService.getHostById(conf
				.getHostId()));
		//默认同步到skyDns
		if("true".equals(ConfigUtil.getOrElse("NGINX_SYNC_SKY_DNS", "false"))){
			syncSkyDns(conf,goalHost.getVirtualHost());
		}
		
		ShellClient ssh = getShellClientByHost(goalHost);
		// 配置文件存放地址，写入配置文件
		String storage = ConfigUtil.getConfigString("file.storage");
		this.folderCreate(ssh, "~/nginx", result);
		String remoteFile = "~/nginx/" + conf.getConfId() + ".conf";
		String localFile = storage + "/nginx/" + conf.getConfId() + ".conf";

		String sslFilePath = "";
		// 将SSL证书拷贝到nginx服务器上
		if (conf.getIsSupportSsl()) {
			String sslCrtId = conf.getSslCrtId();
			if (StringUtil.isNotNull(sslCrtId)) {
				String remoteSSLCrtFile = "~/nginx/" + conf.getConfId()
						+ ".crt";
				String localSSLCrtFile = storage + "/"
						+ sslCrtId.split("\\|")[1];
				String err = ssh.scpTo(localSSLCrtFile, remoteSSLCrtFile);
				if (StringUtil.isNotNull(err)) {
					// 此处抛出运行时异常，控制事务回滚,删除该配置的ssl文件
					String remoteDeleteFile = "~/nginx/" + conf.getConfId()
							+ ".*";
					String temp = ssh
							.exec("rm -rf " + remoteDeleteFile, result);
					throw new RuntimeException(err);
				}
			} else {
				throw new RuntimeException("sslCrtId 为空");
			}
			String sslKeyId = conf.getSslKeyId();
			if (StringUtil.isNotNull(sslKeyId)) {
				String remoteSSLKeyFile = "~/nginx/" + conf.getConfId()
						+ ".key";
				String localSSLKeyFile = storage + "/"
						+ sslKeyId.split("\\|")[1];
				;
				String err = ssh.scpTo(localSSLKeyFile, remoteSSLKeyFile);
				if (StringUtil.isNotNull(err)) {
					// 此处抛出运行时异常，控制事务回滚,删除该配置的ssl文件
					String remoteDeleteFile = "~/nginx/" + conf.getConfId()
							+ ".*";
					String temp = ssh
							.exec("rm -rf " + remoteDeleteFile, result);
					throw new RuntimeException(err);
				}
			} else {
				throw new RuntimeException("sslKeyId 为空");
			}
			sslFilePath = this.getNginxPath(ssh);
		}

		FileUtil.saveStringToFile(this.covertPorperty(conf, sslFilePath, false),
				new File(localFile));

		String err = ssh.scpTo(localFile, remoteFile);
		if (StringUtil.isNotNull(err)) {
			// 此处抛出运行时异常，控制事务回滚,并删除错误的nginx配置文件
			String remoteDeleteFile = "~/nginx/" + conf.getConfId() + ".*";
			String temp = ssh.exec("rm -rf " + remoteDeleteFile, result);
			throw new RuntimeException(err);
		}
		err = ssh.exec("sudo nginx -s reload", result);
		if (StringUtil.isNotNull(err)) {
			// 此处抛出运行时异常，控制事务回滚,并删除错误的nginx配置文件
			String remoteDeleteFile = "~/nginx/" + conf.getConfId() + ".*";
			String temp = ssh.exec("rm -rf " + remoteDeleteFile, result);
			throw new RuntimeException(err);
		}
	}

	
	private void delNginxConf(PaasNginxConf conf, StringBuilder result)
			throws Exception {
		PaasNginxHost goalHost = new PaasNginxHost(hostService.getHostById(conf
				.getHostId()));
		
		//清除skydns问题
		if("true".equals(ConfigUtil.getOrElse("NGINX_SYNC_SKY_DNS", "false"))){
			syncSkyDns(conf,null);
		}
		
		if (goalHost != null) {
			ShellClient ssh = getShellClientByHost(goalHost);
			String storage = ConfigUtil.getConfigString("file.storage");
			String localFile = storage + "/nginx/" + conf.getConfId() + ".conf";
			File file = new File(localFile);
			if (file.exists()) {
				FileUtil.forceDelete(file);
			}

			String remoteFile = "~/nginx/" + conf.getConfId() + ".conf";
			String err = ssh.exec("rm -rf " + remoteFile, result);
			if (StringUtil.isNotNull(err)) {
				throw new Exception("\r\nerror:" + err);
			}
		}

	}
	
	private void folderCreate(ShellClient ssh, String path, StringBuilder result)
			throws Exception {
		String err = ssh.exec("mkdir -p " + path, result);
		if (StringUtil.isNotNull(err)) {
			throw new Exception("\r\nerror:" + err);
		}
	}

	private String getNginxPath(ShellClient ssh) throws Exception {
		String command = "cd ~/nginx\npwd";
		StringBuilder sb = new StringBuilder();
		String err = ssh.exec(command, sb);
		if (StringUtil.isNotNull(err)) {
			throw new Exception("\r\nerror:" + err);
		} else {
			String tempResult = sb.toString();
			return tempResult.replace("\n", "");
		}
	}

	/**
	 * 格式参考 http://zyan.cc/post/306/
	 * 
	 * @param conf
	 * @return
	 * @throws Exception
	 */
	private String covertPorperty(PaasNginxConf conf, String sslFilePath, boolean seperateLog)
			throws Exception {
		/**
		 * nginx 的 upstream目前支持 4 种方式的分配 1)、轮询（默认）
		 * 每个请求按时间顺序逐一分配到不同的后端服务器，如果后端服务器down掉，能自动剔除。 2)、weight
		 * 指定轮询几率，weight和访问比率成正比，用于后端服务器性能不均的情况。 2)、ip_hash
		 * 每个请求按访问ip的hash结果分配，这样每个访客固定访问一个后端服务器，可以解决session的问题。 3)、fair（第三方）
		 * 按后端服务器的响应时间来分配请求，响应时间短的优先分配。 4)、url_hash（第三方）
		 */
		NginxConfVo vo = this.convert(conf);
		List<BalanceConfVo> voList = vo.getConfList();
		if (voList != null && !voList.isEmpty()) {
			StringBuffer result = new StringBuffer();

			result.append("upstream " + vo.getConfId() + " {\n");// 定义策略名称

			for (BalanceConfVo v : voList) {
				result.append("    server " + v.getIp() + ":" + v.getPort());
				if (StringUtil.isNotNull(v.getWeight())) {
					result.append(" weight=" + v.getWeight());
				}
				result.append(";\n");
			}
			result.append("}\n");
			result.append("server {\n");
			 result.append("    listen  80;\n");
			 
			 result.append("    server_name " + vo.getUrl());
			if(vo.getSlaveDomain()!=null && vo.getSlaveDomain().size()>0){
				for(String d : vo.getSlaveDomain()){
					result.append(" "+d);
				}
			}
			result.append(";\n");
//			else{
//				result.append("    server_name " + vo.getUrl() + ";\n");
//			}
			result.append("    set $service_id \"" + conf.getServiceId()
					+ "\";\n");
			result.append("    set $version_id \"" + conf.getVersionId()
					+ "\";\n");
			result.append("    set $env_id \"" + conf.getEnvId() + "\";\n");
			result.append("    set $env_type \"" + conf.getEnvType() + "\";\n");
			result.append("    set $business_type \"" + conf.getBusinessType()
					+ "\";\n");

			if (!conf.getSkipAuth()) {
				result.append("    set $auth_address \""
						+ ConfigUtil.getConfigString("api.auth.server")
						+ "\";\n");
			}
			if (conf.getIsSupportSsl()) {
				// 支持SSL访问
				result.append("    listen  443 ssl;\n");
				result.append("    ssl_certificate " + sslFilePath + "/"
						+ conf.getConfId() + ".crt;\n");
				result.append("    ssl_certificate_key " + sslFilePath + "/"
						+ conf.getConfId() + ".key;\n");
			}
			if(seperateLog){
				result.append("    access_log   /data/logs/"+vo.getConfId()+".access.log main;\n");
			}else{
				result.append("    access_log   /data/logs/access.log main;\n");
			}
			result.append("    location / {\n");
			if (!conf.getSkipAuth()) {
				result.append("        access_by_lua_file  /usr/local/openresty/nginx/conf/auth.lua;\n");
			}
			if(vo.getDomainCross() != null && vo.getDomainCross()){
				result.append("        add_header Access-Control-Allow-Origin *;\n");
			}
			result.append("        proxy_pass http://" + vo.getConfId() + ";\n"); // 应用策略
			result.append("        proxy_set_header Host $host;\n");
			result.append("        proxy_set_header X-Real-IP $remote_addr;\n");
			result.append("        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;\n");
			result.append("        proxy_set_header X-Forwarded-Proto $scheme;\n");
			result.append("        proxy_set_header X-Forwarded-Port $server_port;\n");
			result.append("    }\n");
			result.append("}");

			return result.toString();
		} else {
			throw new RuntimeException("负载尚未配置任何代理！");
		}

	}

	private NginxConfVo convert(PaasNginxConf conf) {
		NginxConfVo vo = new NginxConfVo();
		if (conf != null) {
			vo.setConfId(conf.getConfId());
			vo.setServiceId(conf.getServiceId());
			vo.setNginxConfigName(conf.getNginxName());
			vo.setIsSupportSSL(conf.getIsSupportSsl());
			vo.setSslCrtId(conf.getSslCrtId());
			vo.setSslKeyId(conf.getSslKeyId());
			vo.setSkipAuth(conf.getSkipAuth());
			vo.setEnvType(conf.getEnvType());
			vo.setProjectCode(conf.getProjectCode());
			
			
			PaasHost host = hostService.getHostById(conf.getHostId());
			if (host != null) {
				PaasNginxHost goalHost = new PaasNginxHost(host);
				if (goalHost != null) {
					vo.setNginxIp(goalHost.getVirtualHost());
//					vo.setNginxIp(goalHost.getHostIp());
				}

				vo.setVersionId(conf.getVersionId());
				vo.setVersionCode("");

				if (StringUtil.isNotNull(conf.getConfContent())) {
					JSONObject obj = JSONObject.parseObject(conf
							.getConfContent());
					vo.setUrl(obj.getString("domain"));
					if(obj.containsKey("synDeployIP")){
						vo.setSynDeployIP(obj.getString("synDeployIP"));
					}
					if(obj.containsKey("domainCross")){
						vo.setDomainCross(obj.getBoolean("domainCross"));
					}
					if(obj.containsKey("versionCode")){
						vo.setVersionCode(obj.getString("versionCode"));
					}

					if(obj.containsKey("slaveDomain") && StringUtils.hasText(obj.getString("slaveDomain"))){
						List<String> slaveDomain = JSONArray.parseArray(obj.getString("slaveDomain"), String.class);
						if(slaveDomain!=null && slaveDomain.size()>0){
							vo.setSlaveDomain(slaveDomain);
						}
					}
					
					JSONArray arr = obj.getJSONArray("server");
					if (arr != null && arr.size() > 0) {
						List<BalanceConfVo> voList = new ArrayList<BalanceConfVo>();
						for (int i = 0; i < arr.size(); i++) {
							BalanceConfVo v = new BalanceConfVo();
							JSONObject t = JSONObject.parseObject(arr
									.getString(i));
							v.setIp(t.getString("ip"));
							v.setPort(t.getString("port"));
							v.setWeight(t.getString("weight"));
							voList.add(v);
						}
						vo.setConfList(voList);
					}
				}
			} else {
				return null;
			}
		}
		return vo;
	}

	private List<NginxConfVo> convert(List<PaasNginxConf> confList)
			throws Exception {
		List<NginxConfVo> voList = new ArrayList<NginxConfVo>();
		if (confList != null && !confList.isEmpty()) {
			for (PaasNginxConf conf : confList) {
				NginxConfVo vo = this.convert(conf);
				if (vo != null) {
					voList.add(vo);
				}
			}
		}
		return voList;
	}

	
	private void syncSkyDns(PaasNginxConf config, String ips){
		String configId = config.getConfId();
		JSONObject obj = JSONObject.parseObject(config.getConfContent());
		String domain = obj.getString("domain");
		
		List<String> domains = new ArrayList<String>();
		if(StringUtils.hasText(domain)){
			domains.add(domain);
		}
		
		if(obj.containsKey("slaveDomain") && StringUtils.hasText(obj.getString("slaveDomain"))){
			List<String> slaveDomain = JSONArray.parseArray(obj.getString("slaveDomain"), String.class);
			if(slaveDomain!=null && slaveDomain.size()>0){
				domains.addAll(slaveDomain);
			}
		}
		EtcdUtil.updateDNS(configId, domains, ips);
	}
	
	/**
	 * 当主服务器无法建立连接时，尝试与备用服务器建立连接
	 * @param goalHost
	 * @return
	 * @throws JSchException
	 */
	private ShellClient getShellClientByHost(PaasNginxHost goalHost) throws JSchException{
		ShellClient ssh = null;
		try{
			ssh =new ShellClient(goalHost.getHostIp(),
				goalHost.getHostPort(), goalHost.getHostUser(), goalHost
						.getHostKeyPrv().getBytes(), goalHost
						.getHostKeyPub().getBytes());
		}catch(Exception e){
			if(StringUtil.isNotNull(goalHost.getBackupHost()) && !goalHost.getBackupHost().equals(goalHost.getHostIp())){
				ssh =new ShellClient(goalHost.getBackupHost(),
						goalHost.getHostPort(), goalHost.getHostUser(), goalHost
								.getHostKeyPrv().getBytes(), goalHost
								.getHostKeyPub().getBytes());
			}else{
				throw e;
			}
		}
		return ssh;
	}
	
	/**
	 * @author taoshuangxi
	 * add on 20160623
	 * generate nginx conf file on api/app service, using rsync or Unison to sync file to nginx server
	 * @param conf
	 * @param result
	 * @throws Exception
	 */
	private void generateLocalNginxConf(PaasNginxConf conf, StringBuilder result)
			throws Exception {
		PaasNginxHost goalHost = new PaasNginxHost(hostService.getHostById(conf
				.getHostId()));
		if(StringUtil.isNull(goalHost.getDirectoryName())){
			throw new RuntimeException("Nginx主机缺少文件目录配置");
		}
		//默认不同步到skyDns
		if("true".equals(ConfigUtil.getOrElse("NGINX_SYNC_SKY_DNS", "false"))){
			syncSkyDns(conf,goalHost.getVirtualHost());
		}
		
		// 配置文件存放地址，写入配置文件
		String storage = ConfigUtil.getConfigString("file.storage");
		String directoryPath = storage + "/nginx/" + goalHost.getDirectoryName();
		File directory = new File(directoryPath);
		if(!directory.exists()){
			directory.mkdir();
		}
		String localFilePrefix = directoryPath +"/" + conf.getConfId();

		String sslFilePath = "";
		// 将SSL证书拷贝到nginx服务器上
		if (conf.getIsSupportSsl()) {
			String sslCrtId = conf.getSslCrtId();
			if (StringUtil.isNotNull(sslCrtId)) {
				String SSLCrtFile = localFilePrefix	+ ".crt";
				String localSSLCrtFile = storage + "/"
						+ sslCrtId.split("\\|")[1];
				FileUtil.copyFile(new File(localSSLCrtFile), new File(SSLCrtFile));
			} else {
				throw new RuntimeException("sslCrtId 为空");
			}
			String sslKeyId = conf.getSslKeyId();
			if (StringUtil.isNotNull(sslKeyId)) {
				String SSLKeyFile = localFilePrefix + ".key";
				String localSSLKeyFile = storage + "/"
						+ sslKeyId.split("\\|")[1];
				FileUtil.copyFile(new File(localSSLKeyFile), new File(SSLKeyFile));
			} else {
				throw new RuntimeException("sslKeyId 为空");
			}
			sslFilePath = "/data/config";
		}
		boolean generateLog = false;// api默认由nginx收集日志， app如果需要使用tomcat收集日志，则需要配置该项为true
		if(ConfigUtil.getOrElse("PAASOS_REST_LOG_ENABLE","false").equals("false")){
			generateLog = true;
		}

		FileUtil.saveStringToFile(this.covertPorperty(conf, sslFilePath, generateLog),
				new File(localFilePrefix+".conf"));
		//调用远程刷新url，重启载入nginx配置
		String mainIp = goalHost.getHostIp();
		String backupIp = goalHost.getBackupHost();
		try{
			String Url = "http://"+mainIp+"/rsync";	
			PaasAPIUtil.get("", Url);
		}catch(Exception e){
			e.printStackTrace();
		}
		if(StringUtil.isNotNull(backupIp) && !backupIp.equals(mainIp)){
			try{
				String Url = "http://"+backupIp+"/rsync";	
				PaasAPIUtil.get("", Url);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * @author taoshuangxi
	 * add on 20160623
	 * delete nginx conf file on api/app service, using rsync or Unison to sync file to nginx server
	 * @param conf
	 * @param result
	 * @throws Exception
	 */
	private void deleteLocalNginxConf(PaasNginxConf conf, StringBuilder result)
			throws Exception {
		PaasNginxHost goalHost = new PaasNginxHost(hostService.getHostById(conf
				.getHostId()));
		
		//清除skydns问题
		if("true".equals(ConfigUtil.getOrElse("NGINX_SYNC_SKY_DNS", "false"))){
			syncSkyDns(conf,null);
		}
		
		if(StringUtil.isNull(goalHost.getDirectoryName())){
			return;
		}
		
		//清除本地的conf, key和crt文件
		String storage = ConfigUtil.getConfigString("file.storage");
		String directoryPath = storage + "/nginx/" + goalHost.getDirectoryName() + "/";
		String localFile = directoryPath +conf.getConfId()+".conf";
		File confFile = new File(localFile);
		if (confFile.exists()) {
			FileUtil.forceDelete(confFile);
		}
		if(conf.getIsSupportSsl()){
			String sslKey = directoryPath +conf.getConfId()+".key";
			File sslKeFile = new File(sslKey);
			if (sslKeFile.exists()) {
				FileUtil.forceDelete(sslKeFile);
			}
			String sslCrt = directoryPath +conf.getConfId()+".crt";
			File sslCrtFile = new File(sslCrt);
			if (sslCrtFile.exists()) {
				FileUtil.forceDelete(sslCrtFile);
			}
		}
		//调用远程刷新url，重启载入nginx配置
		String mainIp = goalHost.getHostIp();
		String backupIp = goalHost.getBackupHost();
		try{
			String Url = "http://"+mainIp+"/rsync";	
			PaasAPIUtil.get("", Url);
		}catch(Exception e){
			e.printStackTrace();
		}
		if(StringUtil.isNotNull(backupIp) && !backupIp.equals(mainIp)){
			try{
				String Url = "http://"+backupIp+"/rsync";	
				PaasAPIUtil.get("", Url);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * check the domain in current conf whether already exists
	 * @param conf
	 * @return
	 */
	private boolean isDomainConflict(PaasNginxConf conf){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("start", 0);
		params.put("size", 999999);
		List<PaasNginxConf> confList = this.paasNginxMapper.queryNginxPage(params);
		if(confList!=null && confList.size()>0){
			if (StringUtil.isNotNull(conf.getConfContent())) {
				JSONObject obj = JSONObject.parseObject(conf.getConfContent());
				String newDomains = obj.getString("domain");
				String[] newDomainArr = newDomains.split(";");
				for(int i=0;i<confList.size();i++){
					PaasNginxConf temp = confList.get(i);
					if(!temp.getConfId().equals(conf.getConfId())){
						if (StringUtil.isNotNull(temp.getConfContent())) {
							JSONObject anotherObj = JSONObject.parseObject(temp.getConfContent());
							String domains = anotherObj.getString("domain");
							String[] domainArr = domains.split(";");
							for(int newIndex =0; newIndex<newDomainArr.length;newIndex++){
								for(int index=0;index<domainArr.length;index++){
									String existsDomain = domainArr[index];
									if(newDomainArr[newIndex].equals(existsDomain)){
										return true;
									}
								}
							}
						}
					}
				}
			}
		}
		return false;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteNginxHostById(String hostId) {
		int count = paasNginxMapper.getExistConfNum(hostId);
		if(count > 0){
			throw new RuntimeException("该服务器正在代理应用");
		}
		hostService.deleteHostById(hostId);
	}
	
	@Override
	public List<PaasNginxConf> findNginx(PaasNginxConf params){
		return paasNginxMapper.findNginx(params);
	}
	
	/**
	 * 根据nginx配置获取部署环境
	 */
	@Override
	public List<PaasProjectDeployEnv> getNginxEnvs(String envType){
		PaasNginxConf params = new PaasNginxConf();
		params.setEnvType(envType);
		List<PaasNginxConf> result = findNginx(params);
		if(result==null || result.size()==0){
			return null;
		}
		List<PaasProjectDeployEnv> a = new ArrayList<PaasProjectDeployEnv>(); 
		
		Map<String,PaasProjectDeployEnv> ref = new HashMap<String,PaasProjectDeployEnv>();
		
		for(PaasNginxConf p : result){
			String projectCode = p.getProjectCode();
			String confContent = p.getConfContent();
			if(confContent==null || confContent.toString().equals("")){
				continue ;
			}
			JSONObject j = JSONObject.parseObject(confContent+"");
			String value = j.getString("domain");
			String code = j.getString("versionCode");
			if(StringUtils.isEmpty(value)){
				continue ;
			}
			String key = "PAASOS_URL_"+projectCode.toString().toUpperCase().replaceAll("[^a-zA-Z0-9_]", "_");
			if(StringUtils.hasText(p.getVersionId()) && StringUtils.hasText(code)){
				key = key+"_"+code.toUpperCase().replaceAll("[^a-zA-Z0-9_]", "_");
			}
			if( !(value.toUpperCase().startsWith("HTTP://")||value.toUpperCase().startsWith("HTTPS://"))){
				if(p.getIsSupportSsl()){
					value = "https://"+value;
				}
				else{
					value = "http://"+value;
				}
			}
			if(!ref.containsKey(key)){
				PaasProjectDeployEnv env = new PaasProjectDeployEnv();
				env.setKey(key);
				env.setValue(value);
//				env.setHidden("1");
				a.add(env);
				ref.put(key, env);
			}
			else{
				PaasProjectDeployEnv env = ref.get(key);
				env.setValue(env.getValue()+";"+value);
			}
		}
		
		return a;
	}

	@Override
	public List<NginxConfVo> getNginxListByVersionIds(List<String> versionIds, String envType,String envMark) throws Exception{
		return this.convert(this.paasNginxMapper.getNginxListByVersionIds(versionIds,envType,envMark));
	}
}
