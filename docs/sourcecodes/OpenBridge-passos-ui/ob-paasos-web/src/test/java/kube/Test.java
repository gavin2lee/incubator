package kube;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.harmazing.framework.util.PaasAPIUtil;
import com.harmazing.framework.util.ZipUtil;

public class Test {
	public static void main2(String[] args) {
		ZipUtil.unZip("/data/temp/x.zip", "/data/temp/x");
	}

	public static void main3(String[] sss) {
		String mysqlConfigUrl = "http://test.yihecloud.com:88/os/resource/mysql/getConfig.do?resId=7085jmng0mzvu3torrvztxqhrmp4p4m";
		String restReponseStr = PaasAPIUtil.get("admin", mysqlConfigUrl);
		System.out.println(restReponseStr);
	}

	private static String inputStream2String(InputStream in) throws IOException {
		StringBuffer out = new StringBuffer();
		byte[] b = new byte[4096];
		for (int n; (n = in.read(b)) != -1;) {
			out.append(new String(b, 0, n));
		}
		return out.toString();
	}

	public static void main(String[] args) throws Exception {
		URL url = new URL(
				"http://test.yihecloud.com:88/os/resource/mysql/getConfig.do?resId=7085jmng0mzvu3torrvztxqhrmp4p4m");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(5 * 1000);
		conn.setRequestMethod("GET");

		int code = conn.getResponseCode();
		if (code == 200) {
			System.out.println("aaa");
		} else {
			InputStream inStream = conn.getInputStream();
			String result = inputStream2String(inStream);
			System.out.println(result);
		}
	}
}
