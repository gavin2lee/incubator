package com.harmazing.openbridge.web.tablibs.form;

import java.util.List;

import org.apache.commons.io.FilenameUtils;

import com.alibaba.fastjson.JSONObject;
import com.harmazing.framework.util.StringUtil;
import com.harmazing.openbridge.sys.attachment.model.IAttachment;

@SuppressWarnings("serial")
public class UploadTag extends AbstractFormTag {
	private String operation;
	private String type;
	private List<? extends IAttachment> attachInfo;

	protected String acquireString(String body) throws Exception {
		StringBuffer sb = new StringBuffer();
		String divId = this.getId() + "_" + this.getType();
		String jsVar = "att" + this.getId() + "";
		sb.append("<div id='" + divId + "'></div>");
		sb.append("<script type=\"text/javascript\" src=\"" + getContentPath()
				+ "/common/js/attachment.js\"></script>");
		sb.append("<script>\r\n");
		sb.append("var " + jsVar + " = new Attachment('" + this.getId() + "','"
				+ divId + "','" + this.getType() + "','" + this.getOperation()
				+ "','" + jsVar + "');\r\n");
		sb.append(jsVar + ".render();\r\n");
		if (attachInfo != null && attachInfo.size() > 0) {
			for (IAttachment att : attachInfo) {
				JSONObject json = new JSONObject();
				json.put("fileId", att.getAttId());
				json.put("fileName", att.getAttName());
				String extName = FilenameUtils.getExtension(att.getAttName());
				if (StringUtil.isNull(extName)) {
					extName = "unknown";
				}
				json.put("fileExt", extName);
				json.put("fileSize", att.getAttSize() + " KB");
				sb.append(jsVar + ".addFile(" + json.toJSONString() + ");\r\n");
			}
		}

		sb.append("</script>\r\n");

		return super.acquireString(sb.toString());
	}

	public String getOperation() {
		if (!this.operation.trim().equals("")
				&& !this.operation.trim().equals("upload")) {
			return "download";
		}
		return "upload";
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<? extends IAttachment> getAttachInfo() {
		return attachInfo;
	}

	public void setAttachInfo(List<? extends IAttachment> attachInfo) {
		this.attachInfo = attachInfo;
	}

}
