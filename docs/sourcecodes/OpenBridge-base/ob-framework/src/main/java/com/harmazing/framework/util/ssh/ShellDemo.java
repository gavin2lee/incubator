package com.harmazing.framework.util.ssh;

import com.jcraft.jsch.JSchException;

public class ShellDemo {

	public static void main(String[] args) throws JSchException {
		StringBuilder sb = new StringBuilder();
		ShellClient ssh = new ShellClient("192.168.1.52", 22, "tomcat",
				"TOMCAT@19252");

		String err = ssh.exec("pwd\nls\ndir", sb);

		System.out.println(err);
		System.out.println(sb.toString());
	}

}
