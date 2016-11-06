package com.harmazing.openbridge.sys.attachment.service;

import java.util.List;

import com.harmazing.framework.common.service.IBaseService;
import com.harmazing.openbridge.sys.attachment.model.SysAttachment;

public interface ISysAttachmentService extends IBaseService {
	
	//上传附件
	void addAttachment(SysAttachment attachment);
	
	//更新附件业务数据
	void updateAttachmentBusiness(String attId, String businessId,String businessKey, String businessType);
	
	//更新附件业务ID
	void updateAttachBusinessId(String attId, String businessId);
	
	//查询业务数据的附件
	List<SysAttachment> getAttachmentByBusiness(String businessId, String businessKey, String businessType);
	
	//根据id查询附件
	SysAttachment getAttachmentById(String attId);

	//删除附件
	void deleteAttachById(String attId);
	
}
