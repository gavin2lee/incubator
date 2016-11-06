package com.harmazing.openbridge.project.util.oauth;

public class ApiAtlassianOAuthClient extends AtlassianOAuthClient {
	public ApiAtlassianOAuthClient() {
		initPlatform();
	}

	@Override
	public void initPlatform() {
		platform = AtlassianOAuthClient.API_MANAGER;
	}
}
