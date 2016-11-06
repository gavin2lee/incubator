import com.alibaba.fastjson.JSONObject;
import com.harmazing.framework.util.FileUtil;
import com.harmazing.openbridge.paas.env.model.PaasEnv;

public class Test {

	public static void main(String[] args) {
		String text = FileUtil.readFile("/data/a.json");
		PaasEnv env = JSONObject.parseObject(text, PaasEnv.class);
		System.out.println(env);
	}

}
