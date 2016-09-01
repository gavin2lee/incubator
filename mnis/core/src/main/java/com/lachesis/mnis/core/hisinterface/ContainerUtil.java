package com.lachesis.mnis.core.hisinterface;

import org.apache.axis.utils.StringUtils;

import com.lachesis.mnis.core.bodysign.entity.BodySignRecord;
import com.lachesis.mnis.core.util.SuperCacheUtil;

/**
 * 数据反写容器工具类
 * @author qingzhi.liu
 *
 */
public class ContainerUtil {
	
	/**
	 * 体征反写给his   先调用isBodySign()方法，true才调用该方法
	 * @param bodySignRecord
	 * @return
	 */
	public static boolean insertBodySign(BodySignRecord bodySignRecord){
		boolean bool = false;
		
		String className =  SuperCacheUtil.getSYSTEM_CONFIGS().get("bodySignInterface");
		try {
			IHisBodySign bodySignService = (IHisBodySign) Class.forName(className).newInstance();
			bool = bodySignService.insertBodySign(bodySignRecord);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return bool;
	}
	
	/**
	 * 判断是否需要反写给his  
	 * @return  true:反写  false：不反写
	 */
	public static boolean isBodySign(){
		boolean bool = false;
		String className =  SuperCacheUtil.getSYSTEM_CONFIGS().get("bodySignInterface");
		if(!StringUtils.isEmpty(className)){
			bool = true;
		}
		return bool;
	}
	
}
