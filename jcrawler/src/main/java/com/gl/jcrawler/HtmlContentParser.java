package com.gl.jcrawler;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class HtmlContentParser {
	public static final String ECODING = "UTF-8";
	private String url;

	public HtmlContentParser(String url) {
		super();
		this.url = url;
	}

	public String getHtml() {
		URL uri;
		try {
			uri = new URL(url);

			URLConnection connection = uri.openConnection();
			InputStream in = connection.getInputStream();
			byte[] buf = new byte[1024];
			int length = 0;
			StringBuffer sb = new StringBuffer();
			while ((length = in.read(buf, 0, buf.length)) > 0) {
				// TODO
				sb.append(new String(buf, ECODING));
			}
			in.close();
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
}
