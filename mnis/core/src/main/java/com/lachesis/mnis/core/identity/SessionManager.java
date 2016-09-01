package com.lachesis.mnis.core.identity;

import org.apache.commons.lang.time.DateUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public final class SessionManager {
	// 存放session 信息 单点：{userId, {<prop,value>,...}}。多点{token,{}}
	private Map<String, Map<SessionAttribute, Object>> mapSession = new HashMap<>();
	/**
	 * 是否允许多点登录 <br>
	 * true: mapSession key = token <br>
	 * false：mapSession key = userId
	 */
	public static final boolean MULTIPLESIGN = true;
    public static final int MAXSESSION = Integer.MAX_VALUE;
	public enum SessionAttribute {
		LOGONDATETIME, LASTOPDATETIME, CLIENTIP, CLIENTOS, ALIVE, USERID, TOKEN
	}

	public static final Map<Integer, String> validateError = new HashMap<Integer, String>();
	static {
		validateError.put(IdentityConstants.VALIDATE_ERROR_WRONG_TOKEN,
				IdentityConstants.VALIDATE_ERROR_WRONG_TOKEN_MSG);
		validateError.put(IdentityConstants.VALIDATE_ERROR_TIMEOUT,
				IdentityConstants.VALIDATE_ERROR_TIMEOUT_MSG);
		validateError.put(IdentityConstants.VALIDATE_ERROR_LOGGED_OUT,
				IdentityConstants.VALIDATE_ERROR_LOGGED_OUT_MSG);
	}
	private SessionManager() {
	}
	private static SessionManager sessionManager;
	public static SessionManager getInstance() {
		if (sessionManager == null) {
			sessionManager = new SessionManager();
		}
		return sessionManager;
	}

	public Map<String, Map<SessionAttribute, Object>> getMapSession() {
		return mapSession;
	}

	public String buildSession(Integer userId, boolean alive, String uuid) {
		//超过最大session数，删除任意一条session记录
		if(mapSession.keySet().size() > MAXSESSION){
			mapSession.remove(mapSession.keySet().iterator().next());
		}
		Date currentDate = new Date();
		Map<SessionAttribute, Object> sessionAttr = new HashMap<SessionAttribute, Object>();
		if (alive) {
			sessionAttr.put(SessionAttribute.LOGONDATETIME, currentDate);
		}
		sessionAttr.put(SessionAttribute.LASTOPDATETIME, currentDate);
		sessionAttr.put(SessionAttribute.CLIENTIP, "");
		sessionAttr.put(SessionAttribute.CLIENTOS, "");
		sessionAttr.put(SessionAttribute.ALIVE, alive ? 1 : -1);
		if (SessionManager.MULTIPLESIGN) {
			sessionAttr.put(SessionAttribute.USERID, userId);
			mapSession.put(uuid, sessionAttr);
		} else {
			sessionAttr.put(SessionAttribute.TOKEN, uuid);
			mapSession.put(String.valueOf(userId), sessionAttr);
		}
		return uuid;
	}

	public Integer getUserIdByToken(String token) {
		String userId = null;
		if (MULTIPLESIGN) {
			// Key 为 token
			Map<SessionAttribute, Object> sa = mapSession.get(token);
			if(sa != null) {
				userId = sa.get(SessionAttribute.USERID).toString();
			}
		} else {
			for (Entry<String,Map<SessionManager.SessionAttribute,Object>> entry : mapSession.entrySet()) {
				if (entry.getValue().get(SessionAttribute.TOKEN).toString().equals(token)) {
					userId = entry.getKey();
					break;
				}
			}
		}
		
		return userId == null ? 0 : Integer.valueOf(userId);
	}
	
	public int validateSession(String token) {
		boolean haveToken = mapSession.containsKey(token);
		String sessionKey = null;
		if (MULTIPLESIGN) {
			// Key 为 token
			sessionKey = token;
		} else {
			// Key 为 UserId
			for (Entry<String,Map<SessionManager.SessionAttribute,Object>> entry : mapSession.entrySet()) {
				if (entry.getValue().get(SessionAttribute.TOKEN).toString().equals(token)) {
					sessionKey = entry.getKey();
					break;
				}
			}
		}
		Map<SessionAttribute, Object> value = mapSession.get(sessionKey);
		if (!haveToken) {
			return IdentityConstants.VALIDATE_ERROR_SERVER_RESTART;
		}
		if(sessionKey == null) {
			return IdentityConstants.VALIDATE_ERROR_WRONG_TOKEN;
		}
		if ((int) value.get(SessionAttribute.ALIVE) != 1) {
			return IdentityConstants.VALIDATE_ERROR_LOGGED_OUT;
		}
		
		if (checkTimeout((Date) value.get(SessionAttribute.LASTOPDATETIME))) {
			mapSession.remove(sessionKey);
			return IdentityConstants.VALIDATE_ERROR_TIMEOUT;
		}
		value.put(SessionAttribute.LASTOPDATETIME, new Date());
		return 0;
	}

	private boolean checkTimeout(Date lastCall) {
		if(lastCall == null){
			return false;
		}
		
		if (lastCall.getTime() -  System.currentTimeMillis() >= DateUtils.MILLIS_PER_HOUR) {
			return true;
		}
		
		return false;
	}
}
