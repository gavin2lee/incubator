package com.harmazing.openbridge.web.fileupload;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.harmazing.framework.common.JsonResponse;
import com.harmazing.framework.util.ConfigUtil;
import com.harmazing.framework.util.ExceptionUtil;
import com.harmazing.framework.util.StringUtil;

public class FileUploadHandler {
	protected  final static Logger logger = Logger.getLogger(FileUploadHandler.class);
	/**
	 * @author chenjinfan
	 * @Description 保存时把文件从临时目录移动到真实目录
	 * @param filePath
	 * @return
	 * @throws IOException 
	 */
	public static String cutTmpFileWhenSave(String filePath) throws IOException {
		int tmpIndex = filePath.indexOf("tmp");
		String retpath = filePath;
		if(tmpIndex!=-1){//在临时目录，则移动到正式目录
			String relativePath = filePath.substring(tmpIndex+4);
			String newPath = getStoreDir()+relativePath;
			File old = new File(filePath);
			File newFile = new File(newPath);
			FileUtils.copyFile(old, newFile);
			FileUtils.deleteQuietly(old);
			retpath = relativePath;
		}
		return retpath;
	} 
	
	/**
	 * @author chenjinfan
	 * @Description  删除旧文件，并把新上传文件从临时目录剪切至新目录
	 * @param originfilePath
	 * @param filePath
	 * @return 新文件路径
	 * @throws IOException
	 */
	public static String delOldFileAndCutNewFile(String originfilePath,String filePath) throws IOException{
		if(StringUtil.hasText(originfilePath)){
			if(!originfilePath.equals(filePath)){
				FileUtils.deleteQuietly(new File(originfilePath));
			}
		}
		return cutTmpFileWhenSave(filePath);
	}
	
	/**
	 * @author chenjinfan
	 * @Description 多个文件时，比对移除旧文件，移动临时文件
	 * @param originFiles
	 * @param buildFiles
	 * @param data 替换此字符串中的临时文件路径
	 * @return
	 * @throws IOException
	 */
	public static String delOldFileAndCutNewFile(List<String> originFiles,List<String> buildFiles,String data) throws IOException{
		for(String filePath : originFiles){//删除旧文件
			if(!buildFiles.contains(filePath)){
				FileUtils.deleteQuietly(new File(filePath));
			}
		}
		for(String filePath : buildFiles){//移动新文件
			String newPath = FileUploadHandler.cutTmpFileWhenSave(filePath);
			if(StringUtil.isNotNull(newPath)){
				data = data.replace(JSON.toJSONString(filePath), JSON.toJSONString(newPath));
			}
		}
		return data;
	}
	
	/**
	 * @author chenjinfan
	 * @Description 获取文件存储临时目录
	 * @return
	 */
	public static String getTmpPath(){
		return getStoreDir()+"tmp"+File.separator;
	}

	/**
	 * @author chenjinfan
	 * @Description 获取文件存储根目录
	 * @return
	 */
	public static String getStoreDir() {
		String dir = ConfigUtil.getConfigString("file.storage");
		if(!dir.endsWith(File.separator)){
			dir += File.separator;
		}
		return dir;
	}
	
	/**
	 * @author chenjinfan
	 * @Description 上传文件保存到临时目录
	 * @param upfile
	 * @return
	 */
	public static JsonResponse uploadFileToTmpDir(MultipartFile upfile) {
		JsonResponse jsonResponse = JsonResponse.success();
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		String filePath = getTmpPath()+df.format(new Date());
		File file = new File(filePath);
		if(!file.exists()){
			file.mkdirs();
		}
		String originalFilename = upfile.getOriginalFilename();
		String extension = FilenameUtils.getExtension(originalFilename);
		if(originalFilename.endsWith("tar.gz")){
			extension = "tar.gz";
		}
		file = new File(filePath+File.separator+StringUtil.getUUID()+"."+extension);
		try {
			file.createNewFile();
			upfile.transferTo(file);
			jsonResponse.addData("fileName", originalFilename);
			jsonResponse.addData("fileSize", file.length());
			jsonResponse.addData("filePath", file.getAbsolutePath());
		} catch (IllegalStateException | IOException e) {
			logger.error("",e);
			jsonResponse = JsonResponse.failure(40, ExceptionUtil.getExceptionString(e));
		}
		return jsonResponse;
	}
	
	public static void download(HttpServletResponse response,
			String fileName, String filePath) {
		if(!filePath.startsWith(getStoreDir())){
			filePath = getStoreDir()+filePath;
		}
		File file = new File(filePath);
		if(file.exists() && file.isFile()){
			try {
				response.reset();
				response.setHeader("Content-Disposition", "attachment;filename=\""+new String(fileName.getBytes(), "ISO8859-1")+"\"");
				response.setContentType("application/octet-stream");
				FileUtils.copyFile(file, response.getOutputStream());
			} catch (IOException e) {
				logger.error("下载失败。"+filePath,e);
			}
		}else{
			response.setContentType("text/html;charset=utf8");
			response.setCharacterEncoding("utf-8");
			try {
				response.getWriter().write("<script>alert('文件不存在！');history.back();</script>");
			} catch (IOException e) {
				
			}
		}
	}
}
