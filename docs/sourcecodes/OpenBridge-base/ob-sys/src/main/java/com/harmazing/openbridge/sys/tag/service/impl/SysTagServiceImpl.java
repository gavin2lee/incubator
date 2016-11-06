package com.harmazing.openbridge.sys.tag.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.harmazing.framework.common.Page;
import com.harmazing.openbridge.sys.tag.dao.SysTagMapper;
import com.harmazing.openbridge.sys.tag.model.SysTag;
import com.harmazing.openbridge.sys.tag.service.ISysTagService;
import com.harmazing.framework.util.StringUtil;

/**
 * 
 * <pre>
 * 服务业务层实现
 * </pre>
 * 
 * @author hehuajun,taoshuangxi
 * 
 */
@Service
public class SysTagServiceImpl implements ISysTagService {

	@Autowired
	private SysTagMapper sysTagMapper;

	@Override
	@Transactional(readOnly = true)
	public String getTagId(String tagText) {
		List<Map<String, Object>> result = sysTagMapper
				.queryRecordByTagText(tagText);
		String tagId = "";
		if (result == null || result.size() == 0) {
			tagId = StringUtil.getUUID();
			sysTagMapper.saveComTag(new SysTag(tagId, tagText, new Date()));
		} else {
			tagId = result.get(0).get("tag_id").toString();
		}
		return tagId;
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Map<String, Object>> tagPage(Map<String, Object> params) {
		Page<Map<String, Object>> xpage = Page.create(params);
		xpage.setRecordCount(sysTagMapper.tagPageRecordCount(params));
		xpage.addAll(sysTagMapper.tagPage(params));
		return xpage;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteTag(String tagId) {
		//如果标签关联其他数据，先删除其他数据
		sysTagMapper.deleteTag(tagId);		
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void updateTagStatus(String tagId, Boolean status) {
		sysTagMapper.updateTagStatus(tagId,status);		
	}

	@Override
	@Transactional(readOnly = true)
	public List<String> getAllHotTags() {
		return sysTagMapper.getAllHotTags();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void batchDeleteTag(String[] tagids) {
		if(tagids!=null){
			for(String id: tagids){
				sysTagMapper.deleteTag(id);
			}
		}		
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int saveOrUpdateTag(SysTag tag) {
		SysTag oldTag = sysTagMapper.getTagByName(tag.getTagText());
		if(tag.getTagId()!=null){
			//确保修改的记录的tagText不能和已有的相同
			if(oldTag!=null && !oldTag.getTagId().equals(tag.getTagId())){
				return -1;
			}
			sysTagMapper.updateTagNameAndStatus(tag);
		}else{
			//确保新增的记录不能和已有的tagText相同
			if(oldTag !=null){
				return -1;
			}
			tag.setTagId(StringUtil.getUUID());			
			tag.setCreateTime(new Date());
			sysTagMapper.saveComTag(tag);
		}
		return 1;
	}

	@Override
	@Transactional(readOnly = true)
	public SysTag getTagById(String tagId) {
		return sysTagMapper.getTagById(tagId);
	}

}
