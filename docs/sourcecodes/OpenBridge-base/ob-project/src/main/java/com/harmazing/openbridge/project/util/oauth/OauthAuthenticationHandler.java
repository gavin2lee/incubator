package com.harmazing.openbridge.project.util.oauth;

import java.net.URI;
import java.net.URISyntaxException;

import net.oauth.OAuthMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.atlassian.httpclient.api.Request;
import com.atlassian.jira.rest.client.api.AuthenticationHandler;
import com.harmazing.framework.util.StringUtil;
import com.harmazing.framework.util.oauth.TokenSecretVerifierHolder;

public class OauthAuthenticationHandler implements AuthenticationHandler {
	private static Log logger = LogFactory.getLog(OauthAuthenticationHandler.class);
	
	public static class NeedAuthorizeException extends Exception{
		//
		private static final long serialVersionUID = 1L;
		private TokenSecretVerifierHolder tokenSecretVerifierHolder;
		public NeedAuthorizeException(){
			
		}
		public NeedAuthorizeException(String message)
	    {
	        super(message);
	    }

	    public NeedAuthorizeException(Throwable cause)
	    {
	        super(cause);
	    }

	    public NeedAuthorizeException(String message, Throwable cause)
	    {
	        super(message, cause);
	    }
		public NeedAuthorizeException(TokenSecretVerifierHolder tokenSecretVerifierHolder){
			super();
			this.tokenSecretVerifierHolder = tokenSecretVerifierHolder;
		}
		public TokenSecretVerifierHolder getTokenSecretVerifierHolder() {
			return tokenSecretVerifierHolder;
		}
		
	}
	private AtlassianOAuthClient atlassianOAuthClient;
	public OauthAuthenticationHandler(AtlassianOAuthClient AtlassianOAuthClient) throws NeedAuthorizeException{
		this.atlassianOAuthClient = AtlassianOAuthClient;
		if(StringUtil.isNull(atlassianOAuthClient.getAccessToken())){
			throw new NeedAuthorizeException(atlassianOAuthClient.getRequestToken());
		}
	}
	
	@Override
	public void configure(Request arg0) {
		/*Map<String, String> params = new HashMap<String, String>();
		params.put("oauth_consumer_key", jiraoAuthClient.getConsumerKey());
		params.put("oauth_token", jiraoAuthClient.getAccessToken());
		params.put("oauth_signature_method", "RSASHA1");
		params.put("oauth_version", "1.0");
		arg0.setHeaders(params);*/
		OAuthMessage om = atlassianOAuthClient.getRequestOauthProxy(arg0.getUri().toString());
		try {
			arg0.setUri(new URI(om.URL));
		} catch (URISyntaxException e) {
			logger.error("Invalid oauth Url: "+om.URL,e);
		}
	}

}
