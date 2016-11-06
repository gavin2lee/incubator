package com.harmazing.openbridge.paas.util;

import com.harmazing.framework.util.NumberUtil;

public class StorageUtil {
	
	public static Double getMemory(String value){
		String storage = value.toUpperCase();
		Double applyStorage = null;
		if(storage.endsWith("G")){
			applyStorage = Double.valueOf(NumberUtil.rounded(
					Double.parseDouble(storage.substring(0, storage.length()-1))*1024, 2));
		}
		else if(storage.endsWith("M")){
			applyStorage = Double.valueOf(storage.substring(0, storage.length()-1));
		}
		else{
			applyStorage = Double.valueOf(storage);
		}
		return applyStorage;
	}
	
	public static Double getCpu(String value){
		String cpu = value.toUpperCase();
		Double applyCpu = null;
		if(cpu.endsWith("M")){
			applyCpu = Double.valueOf(NumberUtil.rounded(Double.parseDouble(cpu.substring(0, cpu.length()-1))/1000, 2));
		}
		return applyCpu;
	}
	
}
