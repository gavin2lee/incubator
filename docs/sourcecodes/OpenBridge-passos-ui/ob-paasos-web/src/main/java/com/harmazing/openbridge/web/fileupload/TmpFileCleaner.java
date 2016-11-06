package com.harmazing.openbridge.web.fileupload;

import java.io.File;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;


public class TmpFileCleaner {
	protected  final static Logger logger = Logger.getLogger(TmpFileCleaner.class);
	private static final int DURATION = 120;//临时文件生存时长
	private static final int EXECUTE_INTERNAL = 30;// 执行间隔
	private static ScheduledExecutorService pool = Executors.newScheduledThreadPool(2);;
	
	public static void start(){
		pool.schedule(new Callable<Void>() {

			@Override
			public Void call() throws Exception {
				logger.info("--file cleaner execute--");
				clean(new File(FileUploadHandler.getTmpPath()),DURATION);
				start();
				return null;
			}
			
		}, EXECUTE_INTERNAL, TimeUnit.MINUTES);
	}
	private static void clean(File dir,int age){
		if(dir !=null && dir.exists() && dir.isDirectory()){
			File[] files = dir.listFiles();
			for(File file : files){
				if(file.isDirectory()){
					clean(file, age);
				}else{
					long lastModified = file.lastModified();
					if(lastModified > 0 && (new Date().getTime()-lastModified)>age*60*1000){
						file.delete();
					}
				}
			}
		}
	}
}
