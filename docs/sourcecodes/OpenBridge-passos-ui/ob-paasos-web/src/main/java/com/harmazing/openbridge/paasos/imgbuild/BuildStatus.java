/**
 * Project Name:ob-paasos-web
 * File Name:BuildStatus.java
 * Package Name:com.harmazing.openbridge.paasos.imgbuild
 * Date:2016年4月27日下午5:03:46
 * Copyright (c) 2016
 *
*/

package com.harmazing.openbridge.paasos.imgbuild;

import java.util.*;

/**
 * ClassName:BuildStatus <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2016年4月27日 下午5:03:46 <br/>
 * @author   dengxq
 * @version  
 * @since    JDK 1.6
 * @see 	 
 */
public interface BuildStatus {
	
	//镜像创建状态 1已保存  5创建中 10创建成功 0创建失败   
	//1已保存  5创建中 6变更中 10操作成功 0创建失败  11 删除失败  
	public static Integer SAVED=1;
	
	public static Integer BUILD_DOING=5;
	
	public static Integer BUILD_SUCCESS=10;
	
	public static Integer BUILD_FAIL = 0;
	
	
	public static Integer BUILD_CHANGEING=6;
	
	public static Integer DELETE_FAIL = 11;

}

