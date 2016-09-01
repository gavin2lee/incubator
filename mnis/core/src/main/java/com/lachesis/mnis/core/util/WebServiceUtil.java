package com.lachesis.mnis.core.util;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lachesis.mnis.core.constants.MnisConstants;
import com.lachesis.mnis.core.order.entity.HttpResponeMsg;

public class WebServiceUtil {

	private static Logger LOG = LoggerFactory.getLogger(WebServiceUtil.class);

	/**
	 * 
	 * @param url
	 *            服务地址
	 * @param body
	 *            请求体
	 * @return
	 */
	public static String getWebServiceData(String url, String body, String table) {
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
			httpURLConnection.setRequestProperty("Content-Type",
					"text/xml;charset=UTF-8");
			httpURLConnection.setRequestProperty("Content-Length",
					String.valueOf(body.length()));

			// 发送POST请求必须设置如下两行
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setDoInput(true);
			httpURLConnection.setConnectTimeout(5*1000);
			// 获取URLConnection对象对应的输出流
			printWriter = new PrintWriter(httpURLConnection.getOutputStream());
			// 发送请求参数
			printWriter.write(body);
			// flush输出流的缓冲
			printWriter.flush();
			// 根据ResponseCode判断连接是否成功
			int responseCode = httpURLConnection.getResponseCode();
			if (responseCode != 200) {
				LOG.error(" getWebServiceData error:" + responseCode + ";msg:"
						+ httpURLConnection.getResponseMessage());
				return null;

			} else {
				LOG.info("Post Success!");
			}
			// 定义BufferedReader输入流来读取URL的ResponseData
			bufferedReader = new BufferedReader(new InputStreamReader(
					httpURLConnection.getInputStream(),
					Charset.forName("UTF-8")));
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				responseResult.append(line.replace("&quot;", "\""));
			}
		} catch (Exception e) {
			LOG.error("WebServiceUtil send post request error!" + e);
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
				LOG.error("getWebServiceData close buffered error!" + ex);
			}

		}
		String rslt = responseResult.toString();
		if (StringUtils.isNotBlank(rslt)) {
			rslt = parseMessage(rslt);
			/*
			 * if(StringUtils.isBlank(table)){ table = "table"; }
			 * WebServiceUtil.writeStrToFile(rslt,"E:/data/" + table + ".txt" );
			 */
		}
		return rslt;
	}

	/**
	 * 解析xml字符串
	 * 
	 * @param msg
	 * @return
	 */
	public static String parseMessage(String msg) {
		if (StringUtils.isBlank(msg)) {
			return msg;
		}
		String rslt = msg;
		// 替换xml转义字段
		rslt = replaceXmlMessage(rslt);
		rslt = rslt.substring(rslt.indexOf("<return>") + 8,
				rslt.indexOf("</return>"));
		return rslt;
	}

	/**
	 * 替换xml转义字段
	 * 
	 * @param msg
	 * @return
	 */
	private static String replaceXmlMessage(String msg) {
		if (StringUtils.isBlank(msg)) {
			return msg;
		}
		String rslt = msg;
		rslt = StringUtils.replace(rslt, "&lt;", '<' + "");
		rslt = StringUtils.replace(rslt, "&gt;", '>' + "");
		return rslt;
	}

	private static void writeStrToFile(String writeStr, String fileName) {
		FileWriter writer;
		try {
			writer = new FileWriter(fileName);
			writer.write(writeStr);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * webservice请求
	 * 
	 * @param webServiceMethod
	 *            ：对应的接口方法
	 * @param paramsValue
	 *            :接口参数值
	 * @return
	 */
	public static HttpResponeMsg webServiceRequest(boolean isHisSync,
			String address, String webServiceMethod, String paramsValue) {
		if (!isHisSync || StringUtils.isBlank(webServiceMethod)) {
			return null;
		}
		//生成url
		String url = StringUtil.createHttpUrl(address,
				MnisConstants.WEB_SERVICE_URL);
		if (StringUtils.isBlank(url)) {
			LOG.info("webServiceRequest url is null..............");
			return null;
		}

		long start = new Date().getTime();
		String body = MnisConstants.WEB_SERVICE_PARAM_VALUES.replace(
				"#{param1}", webServiceMethod)
				.replace("#{param2}", paramsValue);

		String responseMsg = WebServiceUtil.getWebServiceData(url, body, null);

		LOG.info("web service request time:" + (new Date().getTime() - start));
		if (StringUtils.isNotBlank(responseMsg)) {
			// 将请求的json转对象
			HttpResponeMsg vo = GsonUtils.fromJson(responseMsg,
					HttpResponeMsg.class);
			if (vo != null && "1".equals(vo.getRslt())) {
				return vo;
			}
		}

		return null;
	}
}
