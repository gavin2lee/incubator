package com.harmazing.openbridge.project.util.oauth;

public class AppAtlassianOAuthClient extends AtlassianOAuthClient {

	public AppAtlassianOAuthClient() {
		initPlatform();
	}

	@Override
	public void initPlatform() {
		platform = AtlassianOAuthClient.APP_FACTORY;
	}
}
