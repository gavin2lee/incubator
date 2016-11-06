package com.harmazing.framework.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class CMDUtil {

	public static String process(String command, StringBuilder result) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ByteArrayOutputStream err = new ByteArrayOutputStream();
		try {
			String[] commands = null;
			String[] envs = null;
			if (OSUtil.isWindows()) {
				commands = new String[] { "cmd", "/c", command };
			} else {
				commands = new String[] { "/bin/sh", "-c", command };
				envs = new String[] { "LANG=UTF-8" };
			}
			//, envs
			Process process = Runtime.getRuntime().exec(commands);
			new Thread(new ProcessCMD(process.getErrorStream(), err)).start();
			new Thread(new ProcessCMD(process.getInputStream(), out)).start();
			int code = process.waitFor();

			if (OSUtil.isWindows()) {
				result.append(out.toString("gbk"));
				if (code != 0) {
					return err.toString("gbk");
				}
			} else {
				result.append(out.toString());
				if (code != 0) {
					return err.toString();
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}
		return err.toString();
	}

	private static class ProcessCMD implements Runnable {
		private final InputStream is;
		private final OutputStream os;

		public ProcessCMD(InputStream is, OutputStream os) {
			this.is = is;
			this.os = os;
		}

		public void run() {
			final byte[] buffer = new byte[4096];
			try {
				int bytesRead;
				while ((bytesRead = is.read(buffer)) != -1) {
					os.write(buffer, 0, bytesRead);
				}
				os.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
