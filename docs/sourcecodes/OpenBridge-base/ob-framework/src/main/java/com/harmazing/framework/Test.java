package com.harmazing.framework;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.harmazing.framework.util.PaasAPIUtil;
import com.harmazing.framework.util.PaasAPIUtil.DataType;
import com.harmazing.framework.util.PaasAPIUtil.HttpFormFile;

public class Test {
	/**
	 * 解释校验表达式，支持用[]进行优先级处理<br>
	 * 样例：<br>
	 * 输入：[a(1)|b(2)]&c(3)<br>
	 * 输出：{1}&c(3), a(1)|b(2)<br>
	 * 其中{1}表示该项具体内容存放在list.get(1)中
	 * 
	 * @param validatorExpression
	 *            校验表达式
	 * @return 解释结果
	 */
	private static List getValidatorExpressionList(String validatorExpression) {
		List rtnList = new ArrayList();
		List lvList = new ArrayList();
		int lv = 0;
		StringBuffer s = new StringBuffer();
		rtnList.add(s);
		lvList.add(s);
		for (int i = 0; i < validatorExpression.length(); i++) {
			char c = validatorExpression.charAt(i);
			switch (c) {
			case '[':
				lv++;
				if (lvList.size() > lv)
					lvList.set(lv, new StringBuffer());
				else
					lvList.add(new StringBuffer());
				break;
			case ']':
				rtnList.add(lvList.get(lv));
				lv--;
				((StringBuffer) lvList.get(lv)).append("{"
						+ (rtnList.size() - 1) + "}");
				break;
			default:
				((StringBuffer) lvList.get(lv)).append(c);
			}
		}
		return rtnList;
	}

	public static void main(String[] args) {
		// System.out.println(getValidatorExpressionList("[a(1)|b(2)]&c(3)&[d(0)|e(0)]"));

//		String response = PaasAPIUtil.get("sss", "");
//		Map<String, HttpFormFile> sss = new HashMap<String, HttpFormFile>();
//		HttpFormFile x = new HttpFormFile("c://s.zip", "a.zip");
//		sss.put("file1", x);
//
//		PaasAPIUtil.post("asadfasf", "http://sasdfasd/asdasdf.do",
//				DataType.FILE, sss);
		
		Date d = new Date(1463051250l);
		System.out.println(d);
 
	}

}
