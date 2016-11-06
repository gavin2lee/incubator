package com.harmazing.openbridge.sys.attachment.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.harmazing.framework.common.JsonResponse;
import com.harmazing.framework.common.controller.AbstractController;
import com.harmazing.framework.util.ConfigUtil;
import com.harmazing.framework.util.DateUtil;
import com.harmazing.framework.util.MimeTypeUtil;
import com.harmazing.framework.util.StringUtil;
import com.harmazing.openbridge.sys.attachment.model.SysAttachment;
import com.harmazing.openbridge.sys.attachment.service.ISysAttachmentService;

@Controller
@RequestMapping("/file")
public class FileController extends AbstractController {
	@Autowired
	private ISysAttachmentService attachService;// add by shuangxt

	private static final Log logger = LogFactory.getLog(FileController.class);

	@RequestMapping("/upload")
	public void upload(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam("upfile") MultipartFile[] myfiles) {
		String serverName = request.getServerName();
		List<SysAttachment> attachFiles = new ArrayList<SysAttachment>();
		try {
			List<String> files = new ArrayList<String>();
			for (MultipartFile myfile : myfiles) {
				if (myfile.isEmpty()) {
					responseUploadString(response,
							getJSON(10, "未上传文件", null, ""), serverName);
					return;
				}
				long filesizeByte = myfile.getSize();
				String fileOriginalName = myfile.getOriginalFilename();
				logger.debug("文件长度: " + filesizeByte);
				logger.debug("文件类型: " + myfile.getContentType());
				logger.debug("文件名称: " + myfile.getName());
				logger.debug("文件原名: " + fileOriginalName);
				String extName = FilenameUtils.getExtension(fileOriginalName);
				if (StringUtil.isNull(extName)) {
					extName = "unknown";
				}
				// 生成新文件名
				Date date = new Date();
				String fileId = StringUtil.getUUID(date, UUID.randomUUID());
				String basePath = ConfigUtil.getConfigString("file.storage");
				String currentPath = DateUtil.format(date, DateUtil.YYYYMMDD);
				// 临时目录
				String folderPath = basePath + File.separator  +currentPath;
				// 文件名称
				String fileName = fileId + "." + extName.toLowerCase();
				// 生成新文件名
				File file = new File(folderPath + File.separator + fileName);
				// 这里不必处理IO流关闭的问题，因为FileUtils.copyInputStreamToFile()方法内部会自动把用到的IO流关掉
				try {
					FileUtils.copyInputStreamToFile(myfile.getInputStream(),
							file);
					String attIdInDB = StringUtil.getUUID();
					files.add(attIdInDB);
					SysAttachment attachment = new SysAttachment();
					attachment.setAttId(attIdInDB);
					attachment.setAttName(fileOriginalName);
					attachment.setFilePath(currentPath + File.separator
							+ fileName);
					int bytes = (int) (filesizeByte % 1024);
					int kByte = (int) (filesizeByte / 1024);
					attachment.setAttSize(bytes == 0 ? kByte : kByte + 1);
					attachment.setCreateTime(new Date());
					attachService.addAttachment(attachment);// add by shuangxt
					attachFiles.add(attachment);
					// sbFileList.append("</td><td><input type=\"hidden\" name=\"fileId\" value=\""+attIdInDB);
					// sbFileList.append("\"><input type=\"hidden\" name=\"fileName\" value=\""+fileOriginalName);
					// sbFileList.append("\"><a class=\"btn btn-default btn-sm\" href=\""+WebUtil.getServletContext().getContextPath()+"/file/download.do?fileId="+attIdInDB+"\" target=\"_blank\">下载</a>");
					// sbFileList.append("<a id=\"deleteFile"+attIdInDB+"\" class=\"btn btn-default btn-sm\" onclick=\"javascript:deleteFile('"+attIdInDB+"');\">删除</a></td></tr>");
				} catch (IOException e) {
					logger.error("上传文件过程中出错", e);
					responseUploadString(response,
							getJSON(20, "上传文件过程中出错", "", null), serverName);
					// "上传文件过程中出错"
					return;
				}
			}
			// 返回ID files和附件
			// JSONObject json = new JSONObject();
			// json.put("attachFiles", attachFiles);
			responseUploadString(response, getJSON(0, "", files, attachFiles),
					serverName);
		} catch (Throwable t) {
			logger.error("上传文件失败", t);
			responseUploadString(response, getJSON(40, "上传文件失败", null, ""),
					serverName);
		}
	}

	private JSONObject getJSON(int code, String msg, Object data,
			Object attachFiles) {
		JSONObject json = new JSONObject();
		json.put("code", code);
		json.put("msg", msg);
		json.put("data", data);
		json.put("attachlist", attachFiles);
		return json;
	}

	private void responseUploadString(HttpServletResponse response,
			JSONObject json, String serverName) {
		try {
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter writer = response.getWriter();
			writer.append("<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" /></head><body><script type=\"text/javascript\">");
			// writer.append("document.domain=\"" + serverName + "\";");
			writer.append("frameElement.callback(" + json.toJSONString()
					+ ");</script></body></html>");
		} catch (Exception e) {
			logger.error(e);
		}
	}

	@RequestMapping("/download")
	public void download(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try {
			String fileId = request.getParameter("fileId");
			if (fileId.indexOf("/") >= 0) {
				throw new Exception("文件ID异常");
			}
			SysAttachment attach = attachService.getAttachmentById(fileId);// add
																			// by
																			// shuangxt
			if (attach == null) {
				throw new Exception("File Not Found!");
			}
			String filePath = attach.getFilePath();// add by shuangxt
			String fileName = request.getParameter("fileName");

			if (fileName == null || fileName.trim().equals("")) { // add by
																	// shuangxt
				fileName = attach.getAttName();
			}
			//response.setContentType(MimeTypeUtil.getContentType(fileId));
			
			//edit by luoan，加了下一句，就会nullpointerexception，并且app增加编辑页面图标不会显示，注释掉即可以显示。
			if(MimeTypeUtil.getContentType(fileName)!=null)
				response.setContentType(MimeTypeUtil.getContentType(fileName));// add by shuangxt
			request.setCharacterEncoding("UTF-8");
			// String uuid = fileId;
			// if (fileId.indexOf(".") > 0)
			// uuid = fileId.substring(0, fileId.indexOf("."));
			//
			// String folder = DateUtil.format(StringUtil.getUUIDDate(uuid),
			// DateUtil.YYYYMMDD);
			//
			// File file = new File(ConfigUtil.getConfigString("file.storage")
			// + File.separator + folder + File.separator + fileId);
			File file = new File(ConfigUtil.getConfigString("file.storage")
					+ File.separator + filePath);// add by shuangxt
			if (StringUtil.isNotNull(fileName)) {
				// response.setHeader("Content-disposition",
				// "attachment; filename="
				// + new String(file.getName().getBytes("utf-8"),
				// "ISO8859-1"));
				response.setHeader(
						"Content-disposition",
						"attachment; filename="
								+ new String(fileName.getBytes("utf-8"),
										"ISO8859-1"));
			}
			response.setHeader("Content-Length", String.valueOf(file.length()));
			bis = new BufferedInputStream(new FileInputStream(file));
			bos = new BufferedOutputStream(response.getOutputStream());
			byte[] buff = new byte[2048];
			int bytesRead;
			while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
				bos.write(buff, 0, bytesRead);
			}
		} catch (Exception e) {
			logger.error("文件下载失败：" + e);
			throw e;
		} finally {
			try {
				bis.close();
			} catch (Exception e2) {
			}
			try {

				bos.close();
			} catch (Exception e2) {
			}
		}
	}

	/**
	 * 删除附件
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public JsonResponse deleteAttachment(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			String attachId = request.getParameter("attachmentId");
			System.out.println(attachId);
			SysAttachment attachment = attachService
					.getAttachmentById(attachId);
			if (attachment != null) {
				String filePath = attachment.getFilePath();
				File file = new File(ConfigUtil.getConfigString("file.storage")
						+ File.separator + filePath);
				file.delete();
			}
			attachService.deleteAttachById(attachId);
			return JsonResponse.success();
		} catch (Exception e) {
			logger.error("删除附件出错", e);
			request.setAttribute("exception", e);
			return JsonResponse.failure(500, e.getMessage());
		}
	}
}
