package com.harmazing.openbridge.sys.attachment.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.harmazing.openbridge.sys.attachment.dao.SysAttachmentMapper;
import com.harmazing.openbridge.sys.attachment.model.SysAttachment;
import com.harmazing.openbridge.sys.attachment.service.ISysAttachmentService;

@Service
public class SysAttachmentService implements ISysAttachmentService {
	@Autowired
	private SysAttachmentMapper attachMapper;
	
	/**
	 * 上传附件
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void addAttachment(SysAttachment attachment) {
		attachMapper.addAttachment(attachment);
	}

	/**
	 * 更新附近的业务数据
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void updateAttachmentBusiness(String attId, String businessId,
			String businessKey, String businessType) {
		attachMapper.updateAttachmentBusiness(attId, businessId, businessKey, businessType);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void updateAttachBusinessId(String attId, String businessId) {
		attachMapper.updateAttachBusinessId(attId, businessId);		
	}
	/**
	 * 根据业务数据查询其所有的附件
	 */
	@Override
	@Transactional(readOnly=true)
	public List<SysAttachment> getAttachmentByBusiness(String businessId,
			String businessKey, String businessType) {
		return attachMapper.getAttachmentByBusiness(businessId, businessKey, businessType);
	}

	/**
	 * 根据id查询附件
	 */
	@Override
	@Transactional(readOnly=true)
	public SysAttachment getAttachmentById(String attId) {
		return attachMapper.getAttachmentById(attId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteAttachById(String attId) {
		attachMapper.deleteAttachById(attId);
	}

	

}
