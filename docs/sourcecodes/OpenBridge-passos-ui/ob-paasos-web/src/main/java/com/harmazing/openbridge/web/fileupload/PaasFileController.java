package com.harmazing.openbridge.web.fileupload;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.harmazing.framework.common.JsonResponse;
import com.harmazing.framework.util.FileUtil;
import com.harmazing.framework.util.StringUtil;

@Controller
@RequestMapping("/paas/file/")
public class PaasFileController {

	static{
		TmpFileCleaner.start();
	}
	/**
	 * @author chenjinfan
	 * @Description 上传文件，放在临时目录
	 * @param upfile
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("upload")
	public void upload(MultipartFile upfile,HttpServletResponse response) throws IOException{
		JsonResponse jsonResponse = FileUploadHandler.uploadFileToTmpDir(upfile);
		response.getWriter().print(JSON.toJSONString(jsonResponse));
	}
	@RequestMapping("view")
	public void view(String filePath,HttpServletRequest request,HttpServletResponse response) throws IOException{
		File file ;
		if(StringUtil.isNotNull(filePath)){
			if(filePath.indexOf("tmp")==-1){
				filePath = FileUploadHandler.getStoreDir()+filePath;
			}
			//如果本来上传了文件，但是某种不知名原因服务器该图片找不到了，所以还是要用默认图片。
			if(!new File(filePath).exists()){
				filePath = request.getSession().getServletContext().getRealPath("/") + "/assets/images/paas_ico_default.jpg";
			}
		}else{
			filePath = request.getSession().getServletContext().getRealPath("/") + "/assets/images/paas_ico_default.jpg";
		}
		file = new File(filePath);
		if(file.exists()){
			if(FilenameUtils.getExtension(file.getName()).equals("png") ||
					FilenameUtils.getExtension(file.getName()).equals("jpg")||
					FilenameUtils.getExtension(file.getName()).equals("jpeg")||
					FilenameUtils.getExtension(file.getName()).equals("gif")){
				FileUtil.copyFile(file, response.getOutputStream());
			}
		}
	}
}
