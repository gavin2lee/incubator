package com.harmazing.openbridge.sys.tag.service;

import java.util.List;
import java.util.Map;

import com.harmazing.framework.common.Page;
import com.harmazing.framework.common.service.IBaseService;
import com.harmazing.openbridge.sys.tag.model.SysTag;

/**
 * 
 * <pre>
 * 标签业务处理层接口
 * </pre>
 * 
 * @author hehuajun,taoshuangxi
 *
 */
public interface ISysTagService extends IBaseService {

	String getTagId(String tagText);
	
	Page<Map<String,Object>> tagPage(Map<String, Object> param);
	
	void deleteTag(String tagId);
	
	void updateTagStatus(String tagId,Boolean status);
	
	List<String> getAllHotTags();
	
	void batchDeleteTag(String[] tagids);
	
	int saveOrUpdateTag(SysTag tag);
	
	SysTag getTagById(String tagId);
	
}
