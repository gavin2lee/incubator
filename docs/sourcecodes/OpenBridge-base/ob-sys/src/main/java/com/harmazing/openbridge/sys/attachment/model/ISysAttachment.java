package com.harmazing.openbridge.sys.attachment.model;

import java.util.List;
import java.util.Map;

public interface ISysAttachment {
	Map<String, List<SysAttachment>> getAttachment();

	void setAttachment(Map<String, List<SysAttachment>> att);
}
