package com.harmazing.openbridge.web;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;

import com.harmazing.framework.authorization.impl.YiheTokenGenerator;
import com.harmazing.framework.util.Base58;
import com.harmazing.framework.util.Md5Util;
import com.harmazing.framework.util.PaasAPIUtil;
import com.harmazing.framework.util.StringUtil;

public class Test {
	public static String getBaseUrl() {
		String u1 = "http://openbridge.f3322.net:88/app";
		String u2 = "http://localhost:8080/app";
		return u1;
	}

	public static void main(String[] args) throws FileNotFoundException {

		String response = PaasAPIUtil
				.get("admin",
						getBaseUrl()
								+ "/app/rest/version/list.do?appId=6p7fg2nomeqglxzg6jx9gquftcdhhyj");
		System.out.println(response);

		download();

		// download2(getBaseUrl()
		// +
		// "/app/version/download.do?appId=6p7fg2nomeqglxzg6jx9gquftcdhhyj&versionId=6p7fgmge4ajdd4bh2thjtuw3qfqarei&fileId=20160323153710.zip");
	}

	private static void signature(String user, HttpRequestBase request) {
		// 数据签名放入请求头中
		try {
			String timestamp = System.currentTimeMillis() + "";
			String noncestr = StringUtil.getUUID();
			String data = user + "|" + timestamp + "|" + noncestr;
			String sign = Md5Util
					.macMd5Encode(YiheTokenGenerator.getTokenSecretKey(), data);
			data += "|" + sign;
			data = Base58.encode(data.getBytes());
			request.addHeader(YiheTokenGenerator.getTokenHeaderName(), data);
		} catch (InvalidKeyException e) {
			throw new RuntimeException("数据签名失败", e);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("数据签名失败", e);
		}
	}

	private static void download() throws FileNotFoundException {

		File target = new File("/data/temp/a.aa");
		FileOutputStream output = new FileOutputStream(target);
		String xxx = PaasAPIUtil
				.download(
						"admin",
						getBaseUrl()
								+ "/app/version/download.do?appId=6p7fg2nomeqglxzg6jx9gquftcdhhyj&versionId=6p7fgmge4ajdd4bh2thjtuw3qfqarei&fileId=20160323153710.zip");
		System.out.println(xxx);
	}

	private static void download2(String url) {
		HttpClient httpClient1 = new DefaultHttpClient();
		HttpGet httpGet1 = new HttpGet(url);
		try {
			signature("admin", httpGet1);
			HttpResponse httpResponse1 = httpClient1.execute(httpGet1);

			StatusLine statusLine = httpResponse1.getStatusLine();
			if (statusLine.getStatusCode() == 200) {
				String filePath = "/data/temp/aaa.aaa"; // 文件路径
				File file = new File(filePath);
				FileOutputStream outputStream = new FileOutputStream(file);
				InputStream inputStream = httpResponse1.getEntity()
						.getContent();
				byte b[] = new byte[1024];
				int j = 0;
				while ((j = inputStream.read(b)) != -1) {
					outputStream.write(b, 0, j);
				}
				outputStream.flush();
				outputStream.close();
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();

		} finally {
			httpClient1.getConnectionManager().shutdown();
		}

	}

}
