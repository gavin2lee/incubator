package com.lachesis.mnisqm.core.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetDataByWeb {

	private static Logger logger = LoggerFactory.getLogger(GetDataByWeb.class);

	/**
	 * 
	 * @param url 服务地址
	 * @param body 请求体
	 * @return
	 * @throws Exception 
	 */
	public static String getData(String url, String body) throws Exception{
		PrintWriter printWriter = null;
		BufferedReader bufferedReader = null;
		StringBuffer responseResult = new StringBuffer();
		HttpURLConnection httpURLConnection = null;

		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			httpURLConnection = (HttpURLConnection) realUrl.openConnection();
			// 设置通用的请求属性
			httpURLConnection.setRequestMethod("POST");
			httpURLConnection.setRequestProperty("Content-Type", "text/xml;charset=UTF-8");
			httpURLConnection.setRequestProperty("Content-Length",
					String.valueOf(body.length()));
			
			// 发送POST请求必须设置如下两行
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			printWriter = new PrintWriter(httpURLConnection.getOutputStream());
			// 发送请求参数
			printWriter.write(body);
			// flush输出流的缓冲
			printWriter.flush();
			// 根据ResponseCode判断连接是否成功
			int responseCode = httpURLConnection.getResponseCode();
			if (responseCode != 200) {
				logger.error(" getWebServiceData error:" + responseCode + ";msg:" 
						+ httpURLConnection.getResponseMessage());
				return null;
				
			} else {
				logger.info("Post Success!");
			}
			// 定义BufferedReader输入流来读取URL的ResponseData
			bufferedReader = new BufferedReader(new InputStreamReader(
					httpURLConnection.getInputStream(),Charset.forName("UTF-8")));
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				responseResult.append(line);
			}
		} catch (Exception e) {
			logger.error("WebServiceUtil send post request error!" + e);
		} finally {
			httpURLConnection.disconnect();
			try {
				if (printWriter != null) {
					printWriter.close();
				}
				if (bufferedReader != null) {
					bufferedReader.close();
				}
			} catch (IOException ex) {
				logger.error("getWebServiceData close buffered error!" + ex);
			}

		}
		String rlts = responseResult.toString();
		if(StringUtils.isNotEmpty(rlts) && rlts.contains("<Row>")){
			rlts = rlts.replace("&lt;", '<' + "");
			rlts = rlts.replace("&gt;", '>' + "");
			logger.error("返回数据：" + rlts);
			int begin = rlts.indexOf("<Row>");
			int end = rlts.indexOf("</Table>");
			rlts = rlts.substring(begin,end);
			rlts = "<message>" + rlts + "</message>";
			logger.error("改造数据：" + rlts);
		}else{
			logger.info("webservice getMessage返回数据为空或者格式不正确，返回结果:" + rlts);
			return null;
		}
		logger.info("webservice getMessage:" + rlts);
		return rlts;
	}
	
}
