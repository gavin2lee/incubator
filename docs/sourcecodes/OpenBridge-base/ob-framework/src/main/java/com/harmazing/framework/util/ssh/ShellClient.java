package com.harmazing.framework.util.ssh;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.harmazing.framework.util.Md5Util;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class ShellClient {
	private static final Log logger = LogFactory.getLog(ShellClient.class);
	private String password;
	private String host;
	private String userName = null;
	private int port;
	private JSch jsch = null;
	protected Session session = null;
	private int authType = -1;
	private String identity;
	private byte[] prv = null;
	private byte[] pub = null;

	public ShellClient(String host, int port, String userName, String password)
			throws JSchException {
		this.password = password;
		this.host = host;
		this.port = port;
		this.userName = userName;
		this.jsch = new JSch();
		this.authType = 1;
		init();
	}

	public ShellClient(String host, int port, String userName, byte[] prv,
			byte[] pub) throws JSchException {
		this.identity = Md5Util.getMD5String(prv);
		this.prv = prv;
		this.pub = pub;
		this.host = host;
		this.port = port;
		this.userName = userName;
		this.jsch = new JSch();
		this.authType = 0;
		init();
	}

	public String scpForm(String removeFile, String localFile) {
		String err = "";
		try {
			File file = new File(localFile);
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			ScpFromMessage message = new ScpFromMessage(this.session,
					removeFile, file, true);
			err = message.execute();
		} catch (Exception e) {
			logger.error(e);
			return "error:" + e.getMessage();
		} finally {
		}
		return err;
	}

	public String scpTo(String localFile, String removeFile) {
		String err = "";
		try {
			File file = new File(localFile);
			if (!file.exists()) {
				throw new FileNotFoundException("文件未找到:" + localFile);
			}
			ScpToMessage message = new ScpToMessage(true, session, file,
					removeFile);
			err = message.execute();
		} catch (Exception e) {
			logger.error(e);
			return "error:" + e.getMessage();
		} finally {
		}
		return err;
	}

	public String exec(String command, final StringBuilder output) {
		return exec(command, new OutputStream() {
			public void write(int b) throws IOException {

			}

			public void write(byte[] b, int off, int len) throws IOException {
				super.write(b, off, len);
				if (output != null)
					output.append(new String(b, off, len));
			}
		});
	}

	public String exec(String command, OutputStream output) {
		ByteArrayOutputStream err = new ByteArrayOutputStream();
		try {
			ChannelExec channel = (ChannelExec) session.openChannel("exec");
			channel.setCommand(command);
			channel.setInputStream(null);
			channel.setErrStream(err);
			InputStream in = channel.getInputStream();
			channel.connect();
			byte[] tmp = new byte[1024];
			while (true) {
				while (in.available() > 0) {
					int i = in.read(tmp, 0, 1024);
					if (i < 0)
						break;
					output.write(tmp, 0, i);
					output.flush();
				}
				if (channel.isClosed()) {
					if (in.available() > 0)
						continue;
					break;
				}
				try {
					Thread.sleep(1000);
				} catch (Exception ee) {
				}
			}
			channel.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return err.toString();
	}

	public void close() {
		try {
			this.session.disconnect();
		} catch (Exception e) {
			logger.error(e);
		}
	}

	@SuppressWarnings("static-access")
	protected void init() throws JSchException {
		try {
			validate();
			logger.info("JSCH identity:" + this.identity);
			jsch.setLogger(new JschLogger());
			jsch.setConfig("StrictHostKeyChecking", "no");

			if (authType == 0) {
				// jsch.addIdentity(this.keyFile.getAbsolutePath());
				jsch.addIdentity(this.identity, this.prv, this.pub, null);
			}
			session = jsch.getSession(this.userName, this.host, this.port);

			if (authType == 1)
				session.setPassword(this.password);
			session.connect();

			logger.info("JSCH session connect success.");
		} catch (JSchException e) {
			logger.error(e.getMessage());
			throw e;
		}
	}

	private static class JschLogger implements com.jcraft.jsch.Logger {

		@Override
		public boolean isEnabled(int level) {
			return true;
		}

		@Override
		public void log(int level, String message) {
			// System.out.println(String.format("[JSCH --> %s]", message));
			if (level == DEBUG) {
				logger.debug(String.format("[JSCH --> %s]", message));
			} else if (level == INFO) {
				logger.info(String.format("[JSCH --> %s]", message));
			} else if (level == WARN) {
				logger.warn(String.format("[JSCH --> %s]", message));
			} else if (level == FATAL) {
				logger.fatal(String.format("[JSCH --> %s]", message));
			} else if (level == ERROR) {
				logger.error(String.format("[JSCH --> %s]", message));
			}
		}
	}

	private void validate() throws JSchException {

		if (this.userName == null || this.userName.isEmpty()) {
			throw new JSchException("Parameter:username is empty.");
		}
		if (host == null || host.isEmpty()) {
			throw new JSchException("Parameter:host is empty.");
		} else {
			try {
				InetAddress inet = InetAddress.getByName(host);
				host = inet.getHostAddress();
				logger.info("JSCH connection address:" + host);
			} catch (UnknownHostException e) {
				throw new JSchException(e.getMessage(), e);
			}
		}

		if (authType == 0 && (prv == null)) {
			throw new JSchException("Parameter:identity is empty.");
		} else if (authType == 1 && (password == null || password.isEmpty())) {
			throw new JSchException("Parameter:userInfo is empty.");
		}
	}
}