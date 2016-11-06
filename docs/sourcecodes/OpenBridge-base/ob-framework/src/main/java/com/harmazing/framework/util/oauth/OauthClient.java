package com.harmazing.framework.util.oauth;

import static net.oauth.OAuth.OAUTH_VERIFIER;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import net.oauth.OAuth;
import net.oauth.OAuthAccessor;
import net.oauth.OAuthConsumer;
import net.oauth.OAuthMessage;
import net.oauth.OAuthServiceProvider;
import net.oauth.client.OAuthClient;
import net.oauth.client.httpclient4.HttpClient4;
import net.oauth.signature.RSA_SHA1;

import com.google.common.collect.ImmutableList;

public abstract class OauthClient {

	protected String consumerKey;//平台授予的客户端key
	protected String privateKey;//私钥
	protected String serverBaseUri;//oauth服务器访问地址
	protected String callbackUri;//认证成功后，oauth服务器回调的地址
	protected String serverName;//服务端名称，如atlassian，表明用于atlassian平台Oauth认证，可用于查找accessToken等
	private OAuthAccessor accessor;

	public OauthClient(){
		
	}
    public OauthClient(String consumerKey, String privateKey, String serverBaseUri, String callbackUri)
    {
        this.consumerKey = consumerKey;
        this.privateKey = privateKey;
        this.serverBaseUri = serverBaseUri;
        this.callbackUri = callbackUri;
    }

    /**
     * @author chenjinfan
     * @Description 获取requestToken
     * @return
     */
    public TokenSecretVerifierHolder getRequestToken()
    {
        try
        {
            OAuthAccessor accessor = getAccessor();
            OAuthClient oAuthClient = new OAuthClient(new HttpClient4());
            List<OAuth.Parameter> callBack;
            if (callbackUri == null || "".equals(callbackUri))
            {
                callBack = Collections.<OAuth.Parameter>emptyList();
            }
            else
            {
                callBack = ImmutableList.of(new OAuth.Parameter(OAuth.OAUTH_CALLBACK, callbackUri));
            }

            OAuthMessage message = oAuthClient.getRequestTokenResponse(accessor, "POST", callBack);
            TokenSecretVerifierHolder tokenSecretVerifier = new TokenSecretVerifierHolder();
            tokenSecretVerifier.token = accessor.requestToken;
            tokenSecretVerifier.secret = accessor.tokenSecret;
            tokenSecretVerifier.verifier = message.getParameter(OAUTH_VERIFIER);
            tokenSecretVerifier.authorizeUrl = getAuthorizeUrlForToken(tokenSecretVerifier.token);
            return tokenSecretVerifier;
        }
        catch (Exception e)
        {
            throw new RuntimeException("Failed to obtain request token", e);
        }
    }

    /**
     * @author chenjinfan
     * @Description 根据requestToken及用户授权返回的verifier获取accessToken
     * @param requestToken
     * @param tokenSecret
     * @param oauthVerifier
     * @return
     */
    public String swapRequestTokenForAccessToken(String requestToken, String tokenSecret, String oauthVerifier)
    {
        try
        {
            OAuthAccessor accessor = getAccessor();
            OAuthClient client = new OAuthClient(new HttpClient4());
            accessor.requestToken = requestToken;
            accessor.tokenSecret = tokenSecret;
            OAuthMessage message = client.getAccessToken(accessor, "POST",
                    ImmutableList.of(new OAuth.Parameter(OAuth.OAUTH_VERIFIER, oauthVerifier)));
            persistAccessToken(message.getToken());
            return message.getToken();
        }
        catch (Exception e)
        {
            throw new RuntimeException("Failed to swap request token with access token", e);
        }
    }

    /**
     * @author chenjinfan
     * @Description 持久化accessToken
     * @param accessToken
     */
    public abstract void persistAccessToken(String accessToken);
    public abstract String getAccessToken();
    
    /**
     * @author chenjinfan
     * @Description 使用accessToken请求内容
     * @param url
     * @return
     */
    public String makeAuthenticatedRequest(String url)
    {
        try
        {
            OAuthAccessor accessor = getAccessor();
            OAuthClient client = new OAuthClient(new HttpClient4());
            accessor.accessToken = getAccessToken();
            OAuthMessage response = client.invoke(accessor, url, Collections.<Map.Entry<?, ?>>emptySet());
            return response.readBodyAsString();
        }
        catch (Exception e)
        {
            throw new RuntimeException("Failed to make an authenticated request.", e);
        }
    }
    /**
     * @author chenjinfan
     * @Description 使用oauth认证方式访问资源时，对请求进行封装
     * @param url
     * @return  OAuthMessage.URL 为封装后的URL
     */
    public OAuthMessage getRequestOauthProxy(String url){
    	try
        {
            OAuthAccessor accessor = getAccessor();
            accessor.accessToken = getAccessToken();
            OAuthMessage request = accessor.newRequestMessage(null, url, Collections.<Map.Entry<?, ?>>emptySet());
            url = OAuth.addParameters(url, request.getParameters());
            request.URL = url;
            return request;
        }
        catch (Exception e)
        {
            throw new RuntimeException("Failed to make an authenticated request.", e);
        }
    }
    private final OAuthAccessor getAccessor()
    {
        if (accessor == null)
        {
            OAuthServiceProvider serviceProvider = new OAuthServiceProvider(getRequestTokenUrl(), getAuthorizeUrl(), getAccessTokenUrl());
            OAuthConsumer consumer = new OAuthConsumer(callbackUri, consumerKey, null, serviceProvider);
            consumer.setProperty(RSA_SHA1.PRIVATE_KEY, privateKey);
            consumer.setProperty(OAuth.OAUTH_SIGNATURE_METHOD, OAuth.RSA_SHA1);
            accessor = new OAuthAccessor(consumer);
        }
        return accessor;
    }

    protected abstract String getAccessTokenUrl();

    protected abstract String getRequestTokenUrl();

    /**
     * @author chenjinfan
     * @Description 跳转到用户授权页面URL
     * @param token
     * @return
     */
    public abstract String getAuthorizeUrlForToken(String token);

    protected abstract String getAuthorizeUrl();

	public String getConsumerKey() {
		return consumerKey;
	}

	public String getServerBaseUri() {
		return serverBaseUri;
	}

	public String getCallbackUri() {
		return callbackUri;
	}

	public String getServerName() {
		return serverName;
	}
    
}
