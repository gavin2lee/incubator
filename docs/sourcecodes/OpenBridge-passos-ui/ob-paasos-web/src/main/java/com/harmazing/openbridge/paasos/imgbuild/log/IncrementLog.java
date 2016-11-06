package com.harmazing.openbridge.paasos.imgbuild.log;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.commons.io.IOUtils;

import com.harmazing.framework.util.StringUtil;

public class IncrementLog {

	private String log;
	private int lineNumber;
	private boolean end;
	public IncrementLog(String log, int lineNumber){
		this.lineNumber = lineNumber;
		this.log = log;
	}
	public String getLog() {
		return log;
	}
	public int getLineNumber() {
		return lineNumber;
	}
	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}
	public void setLog(String log) {
		this.log = log;
	}
	
	public boolean isEnd() {
		return end;
	}
	public void setEnd(boolean end) {
		this.end = end;
	}
	/**
	 * @author chenjinfan
	 * @Description
	 * @param allLog 
	 * @param startLine 起始行
	 * @return 起始行后的内容，并返回读取到的行数
	 */
	public static IncrementLog getLines(String allLog,int startLine){
		int lineNumber = 0;
		String log = allLog;
		try {
			if(StringUtil.isNotNull(allLog)){
				List<String> linesList = IOUtils.readLines(new ByteArrayInputStream(allLog.getBytes("utf-8")), "utf-8");
				lineNumber = linesList.size();
				if(startLine>0 && startLine<=lineNumber){
					log = "";
					StringBuffer newLog = new StringBuffer();
					for(int i=startLine;i<lineNumber;i++){
						newLog.append(linesList.get(i)+System.getProperty("line.separator"));
					}
					log = newLog.toString();
				}
			}
		} catch (UnsupportedEncodingException e) {
			
		} catch (IOException e) {
			
		}
		IncrementLog logInstance = new IncrementLog(log,lineNumber);
		return logInstance;
	}
}
