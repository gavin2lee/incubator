package com.harmazing.framework.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Vector;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public abstract class FileUtil extends FileUtils {
	public static String getFileString(URL fileUrl) throws IOException {
		return getFileString(fileUrl.openStream(), "UTF-8");
	}
  
	public static String getFileString(String fileName) throws IOException {
		return readFileToString(new File(fileName), "UTF-8");
	}

	public static String getFileString(InputStream stream, String charset) throws IOException {
		try {
			return IOUtils.toString(stream, charset);
		} finally {
			IOUtils.closeQuietly(stream);
		}
	}

	public static void saveStringToFile(String str, File file) {
		try {
			if (!file.exists()) {
				if (!file.getParentFile().exists()) {
					file.getParentFile().mkdirs();
				}
				file.createNewFile();
			}
			write(file, str, false);
		} catch (IOException e) {
			LogUtil.error(e);
		}
	}

	public static void savePropertyToFile(Map<String, String> map, File file) {
		try {
			if (!file.exists()) {
				if (!file.getParentFile().exists()) {
					file.getParentFile().mkdirs();
				}
				file.createNewFile();
			}
			Properties props = new SortedProperties();
			OutputStream fos = new FileOutputStream(file);
			Iterator<Entry<String, String>> e = map.entrySet().iterator();
			while (e.hasNext()) {
				Entry<String, String> entry = e.next();
				props.setProperty(entry.getKey(), entry.getValue());
			}
			props.store(fos, "");
		} catch (IOException e) {
			LogUtil.error(e);
		}
	}

	public static String readFile(String filePath) {
		StringBuilder str = new StringBuilder();
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "utf-8"));
			String s;
			while ((s = in.readLine()) != null)
				str.append(s + '\n');
		} catch (IOException e) {
			LogUtil.error(e);
		} finally {
			try {
				in.close();
			} catch (IOException e) {
			}
		}
		return str.toString();
	}

	public static void writeFile(String filePath, boolean append, String text) {
		File file=new File(filePath);
		if (!file.exists()) {
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
		}
		if (!StringUtil.hasText(text))
			return;
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new FileWriter(filePath, append));
			out.write(text);
		} catch (IOException e) {
			LogUtil.error(e); 
		} finally {
			try {
				out.close();
			} catch (IOException e) {
			}
		}
	}

	public static class SortedProperties extends Properties {
		@SuppressWarnings("unchecked")
		public synchronized Enumeration keys() {
			Enumeration keysEnum = super.keys();
			Vector keyList = new Vector();
			while (keysEnum.hasMoreElements()) {
				keyList.add(keysEnum.nextElement());
			}
			Collections.sort(keyList);
			return keyList.elements();
		}
	}
}
