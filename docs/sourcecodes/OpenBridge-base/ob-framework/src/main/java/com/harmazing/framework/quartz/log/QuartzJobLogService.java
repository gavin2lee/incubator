package com.harmazing.framework.quartz.log;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;

import javax.sql.DataSource;

import com.harmazing.framework.quartz.IJobLogService;

public class QuartzJobLogService implements IJobLogService {

	private DataSource dataSource;

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public void add(QuartzJobLog log) throws Exception {
		Connection conn = dataSource.getConnection();
		try {
			PreparedStatement ps = conn
					.prepareStatement("insert into quartz_job_log (log_id, job_name, start_time, end_time, success, job_class, error_info, server_ip) values (?, ?, ?, ?, ?, ?, ?, ?)");
			ps.setString(1, log.getLogId());
			ps.setString(2, log.getJobName());
			ps.setDate(3, new Date(log.getJobStartTime().getTime()));
			ps.setDate(4, new Date(log.getJobEndTime().getTime()));
			ps.setBoolean(5, log.getSuccess());
			ps.setString(6, log.getJobClass());
			ps.setString(7, log.getErrorInfo());
			ps.setString(8, log.getServerIp());
			ps.execute();
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				conn.close();
			} catch (Exception e2) {

			}
		}
	}
}
