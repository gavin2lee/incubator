package com.harmazing.framework.authorization;

public interface IUserService {
	IUser getUserById(String id);

	IUser getUserByToken(String token);

	void writeUserLoginLog(LoginLog log);
}
