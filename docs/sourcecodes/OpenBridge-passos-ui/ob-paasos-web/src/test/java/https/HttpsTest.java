package https;

import com.harmazing.framework.util.PaasAPIUtil;

public class HttpsTest {

	public static void main(String[] args) throws Exception {

		String xxx = PaasAPIUtil.get("181f1ab4e4a6baac5f9158b265767ebc",
				"http://test.yihecloud.com:88/os/project/index.do");
		System.out.println(xxx);
		

		String xxxs = PaasAPIUtil.get("181f1ab4e4a6baac5f9158b265767ebc",
				"https://test.yihecloud.com/os/project/index.do");
		System.out.println(xxxs);
	}
}
