package com.harmazing.openbridge.project.util.oauth;

import java.util.HashMap;
import java.util.Map;

import com.harmazing.framework.common.service.IBaseService;
import com.harmazing.framework.util.ConfigUtil;
import com.harmazing.framework.util.StringUtil;
import com.harmazing.framework.util.oauth.OauthClient;

/**
 * 
 */
public abstract class AtlassianOAuthClient extends OauthClient {
	public static final String APP_FACTORY = "app.";
	public static final String API_MANAGER = "api.";
	protected String platform;// 客户端平台，不同平台oauth认证的相关设置不一样

	public abstract void initPlatform();

	public static interface SysConfigServicePrototype extends IBaseService {
		void saveSysConfig(Map<String, String> config);
	}

	private SysConfigServicePrototype sysConfigService;

	public String getPlatform() {
		return platform;
	}

	public void setSysConfigService(SysConfigServicePrototype sysConfigService) {
		this.sysConfigService = sysConfigService;
	}

	public AtlassianOAuthClient(SysConfigServicePrototype sysConfigService) {
		this();
		this.sysConfigService = sysConfigService;
	}

	public AtlassianOAuthClient() {
		serverName = "atlassian";
		this.consumerKey = ConfigUtil.getConfigString(platform + serverName
				+ ".oauth.consumerKey");
		this.privateKey = ConfigUtil.getConfigString(platform + serverName
				+ ".oauth.privateKey");
		this.serverBaseUri = ConfigUtil.getConfigString(platform + serverName
				+ ".oauth.serverBaseUr");
		this.callbackUri = ConfigUtil.getConfigString(platform + serverName
				+ ".oauth.callbackUri");
	}

	/**
	 * @param serverName
	 *            集成系统名称 根据clientName获取数据库中配置数据，生成client
	 */
	// public OauthClient(String serverName){
	// this.serverName = serverName;
	// }

	public AtlassianOAuthClient(String consumerKey, String privateKey,
			String serverBaseUri, String callbackUri) {
		super(consumerKey, privateKey, serverBaseUri, callbackUri);
	}

	private static final String SERVLET_BASE_URL = "/plugins/servlet";

	@Override
	protected String getAccessTokenUrl() {
		return serverBaseUri + SERVLET_BASE_URL + "/oauth/access-token";
	}

	@Override
	protected String getRequestTokenUrl() {
		return serverBaseUri + SERVLET_BASE_URL + "/oauth/request-token";
	}

	@Override
	public String getAuthorizeUrlForToken(String token) {
		return getAuthorizeUrl() + "?oauth_token=" + token;
	}

	@Override
	protected String getAuthorizeUrl() {
		return serverBaseUri + SERVLET_BASE_URL + "/oauth/authorize";
	}

	@Override
	public void persistAccessToken(String accessToken) {
		Map<String, String> config = new HashMap<String, String>();
		config.put(platform + serverName + ".oauth.accessToken", accessToken);
		// ConfigUtil.updateConfig(config);
		sysConfigService.saveSysConfig(config);
	}

	@Override
	public String getAccessToken() {
		String at = ConfigUtil.getConfigString(platform + serverName
				+ ".oauth.accessToken");
		if (StringUtil.isNull(at) || "NAN".equals(at)) {
			return null;
		}
		return at;
	}
}
