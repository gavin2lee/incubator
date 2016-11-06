package com.harmazing.framework.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;

public class ZipUtil {
	public static void doZip(String zipDirectory, OutputStream output,
			boolean rootFolder) throws IOException {
		File zipDir = new File(zipDirectory);

		ZipOutputStream zipOut = new ZipOutputStream(new BufferedOutputStream(
				output));
		int len = 1;
		if (rootFolder) {
			len = zipDir.getParent().length() + 1;
		} else {
			len = zipDir.getParent().length() + 1 + zipDir.getName().length()
					+ 1;
		}
		// 压缩文件
		handleDir(zipDir, len, zipOut);

		zipOut.close();
	}

	public static void doZip(String zipDirectory, String zipPath,
			boolean rootFolder) {
		File zipDir = new File(zipDirectory);
		if (zipPath == null) {
			zipPath = zipDir.getParent() + "/" + zipDir.getName() + ".zip";
		}
		try {
			doZip(zipDirectory, new FileOutputStream(zipPath), rootFolder);
		} catch (IOException e) {
			LogUtil.error(e);
			e.printStackTrace();
		}
	}

	public static void doZip(String zipDirectory, String zipPath) {
		doZip(zipDirectory, zipPath, true);
	}

	/**
	 * 由doZip调用,递归完成目录文件读取
	 * 
	 * @param dir
	 *            :(需要)压缩的文件夹(File 类型)
	 * @param len
	 *            :一个参数(记录压缩文件夹的parent路径的长度)
	 * @param zipOut
	 *            :需要压缩进的压缩文件
	 * @throws IOException
	 *             :如果出错,会抛出IOE异常
	 */
	private static void handleDir(File dir, int len, ZipOutputStream zipOut)
			throws IOException {
		FileInputStream fileIn = null;
		File[] files = dir.listFiles();
		if (files != null) {
			if (files.length > 0) { // 如果目录不为空,则分别处理目录和文件.
				for (File fileName : files) {
					if (fileName.isDirectory()) {
						handleDir(fileName, len, zipOut);
					} else {
						fileIn = new FileInputStream(fileName);
						zipOut.putNextEntry(new ZipEntry(fileName.getPath()
								.substring(len).replaceAll("\\\\", "/")));
						byte[] buf = new byte[2048];
						int readedBytes = 0;
						while ((readedBytes = fileIn.read(buf)) > 0) {
							zipOut.write(buf, 0, readedBytes);
						}
						fileIn.close();
						zipOut.closeEntry();
					}
				}
			} else { // 如果目录为空,则单独创建之.
				zipOut.putNextEntry(new ZipEntry(dir.getPath().substring(len)
						+ "/"));
				zipOut.closeEntry();
			}
		} else {// 如果是一个单独的文件
			fileIn = new FileInputStream(dir);
			zipOut.putNextEntry(new ZipEntry(dir.getPath().substring(len)));
			byte[] buf = new byte[2048];
			int readedBytes = 0;
			while ((readedBytes = fileIn.read(buf)) > 0) {
				zipOut.write(buf, 0, readedBytes);
			}
			fileIn.close();
			zipOut.closeEntry();
		}
	}

	/**
	 * 解压指定zip文件
	 * 
	 * @param unZipfileName
	 *            :需要解压的zip文件路径
	 * @param unZipPath
	 *            :文件解压的路径,该路径可以为null,null表示解压到原文件的同级目录
	 */
	public static boolean unZip(String unZipfileName, String unZipPath) {// unZipfileName需要解压的zip文件名
		FileOutputStream fileOut = null;
		InputStream inputStream = null;
		File file = null;
		if (unZipPath == null) {
			unZipPath = new File(unZipfileName).getParent();
			// System.out.println("1 -> " + unZipPath);
			if (!(unZipPath.substring(unZipPath.length()).endsWith("/") || unZipPath
					.substring(unZipPath.length()).endsWith("\\"))) {
				unZipPath += "/";
			}
		} else {
			unZipPath = new File(unZipPath).getPath() + "/";
		}

		try {
			ZipFile zipFile = new ZipFile(unZipfileName);

			for (Enumeration<?> entries = zipFile.getEntries(); entries
					.hasMoreElements();) {
				ZipEntry entry = (ZipEntry) entries.nextElement(); 

				file = new File(unZipPath + entry.getName());

				if (entry.isDirectory()) {
					file.mkdirs();
				} else {
					// 如果指定文件的目录不存在,则创建之.
					File parent = file.getParentFile();
					if (!parent.exists()) {
						parent.mkdirs();
					}
					inputStream = zipFile.getInputStream(entry);
					fileOut = new FileOutputStream(file);
					byte[] buf = new byte[2048];
					int readedBytes = 0;
					while ((readedBytes = inputStream.read(buf)) > 0) {
						fileOut.write(buf, 0, readedBytes);
					}
					fileOut.close();
					inputStream.close();
				}
			}
			zipFile.close();
			return true;
		} catch (IOException ioe) {
			LogUtil.error(ioe);
			ioe.printStackTrace();
			return false;
		}
	}

	public static void main(String[] args) {
		ZipUtil.doZip(
				"E:\\data\\resource\\build\\70044j9ikgzakk663bvrpzapem4xpkm",
				"e:/abc.zip",false);
	}
}
