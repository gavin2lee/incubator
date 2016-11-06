package com.harmazing.openbridge.project.util;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.harmazing.framework.util.FileUtil;
import com.harmazing.framework.util.StringUtil;
import com.harmazing.framework.util.TemplateUtil;

public abstract class SourceUtil {
	public static void main(String[] args) throws Exception {
		Map<String, Object> ss = new HashMap<String, Object>();
		ss.put("groupId", "com.sss");
		ss.put("artifactId", "artifactId");
		System.out.println(transformSourceFolder(
				"/data/resource/framework/springmvc+mybatis", ss));
	}

	public static String transformSourceFolder(String folder,
			Map<String, Object> model) throws Exception {
		String temp = System.getProperty("java.io.tmpdir");
		if (!temp.endsWith("/")) {
			temp += "/";
		}
		temp += StringUtil.getUUID();
		transformFolder(folder, model, temp);
		return temp;
	}

	private static void transformFolder(String input,
			Map<String, Object> model, String output) throws Exception {
		File source = new File(input);
		if (source.exists()) {
			File[] files = source.listFiles();
			for (int i = 0; i < files.length; i++) {
				File file = files[i];
				if (file.getName().equals(".") || file.getName().equals("..")) {
					continue;
				}
				if (file.isFile()) {
					String name = file.getName();
					if (name.endsWith(".xfm")) {
						String templateStr = FileUtil.readFileToString(file);
						String out = TemplateUtil.getOutputByTemplate(
								templateStr, model);
						String fileName = output + "/"
								+ name.substring(0, (name.length() - 4));
						FileUtil.writeFile(fileName, false, out);
					} else {
						FileUtil.copyFile(file,
								new File(output + "/" + file.getName()));
					}
				} else if (file.isDirectory()) {
					File fold = new File(output + "/" + file.getName());
					fold.mkdirs();
					transformFolder(input + "/" + file.getName(), model, output
							+ "/" + file.getName());
				}
			}
		}
	}
}
