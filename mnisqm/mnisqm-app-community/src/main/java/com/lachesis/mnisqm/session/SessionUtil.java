package com.lachesis.mnisqm.session;


public class SessionUtil {
/*
	public static User getSessionUser() {
		User user = (User) ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession(true).getAttribute("user");
		return user;
	}


	public static void setSessionUser(User user) {
		((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession(true).setAttribute("user", user);

	}

	public static void setSessionKinsfolkUser(Kinsfolk user) {
		((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession(true).setAttribute("kinsfolkUser", user);

	}


	public static Kinsfolk getSessionKinsfolkUser() {
		Kinsfolk user = (Kinsfolk) ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession(true).getAttribute("kinsfolkUser");
		return user;
	}

	*//**
	 * 用session存储手机验证码
	 * @param mobile
	 * @param code
	 *//*
	public static void setSessionMobileCode(String mobile,String code) {
		((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession(true).setAttribute(mobile, code);
	}*/
}
