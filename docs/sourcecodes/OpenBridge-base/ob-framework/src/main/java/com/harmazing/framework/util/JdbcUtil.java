package com.harmazing.framework.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.support.JdbcUtils;

public class JdbcUtil extends JdbcUtils {

	public static Connection getConnection(String driver, String url,
			String user, String password) throws SQLException,
			ClassNotFoundException {
		Class.forName(driver);
		// 当连接不通时对外抛出异常
		return DriverManager.getConnection(url, user, password);
	}

	public static void getResultSet(Connection conn, String sql,
			IResultSetCallback callback) throws SQLException {
		ResultSet rs = conn.prepareCall(sql).executeQuery();
		callback.exec(rs);
		closeResultSet(rs);
	}

	public interface IResultSetCallback {
		public void exec(ResultSet rs) throws SQLException;
	}
}
