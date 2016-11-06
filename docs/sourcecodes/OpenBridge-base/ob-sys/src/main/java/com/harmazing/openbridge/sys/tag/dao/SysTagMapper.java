package com.harmazing.openbridge.sys.tag.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.harmazing.framework.common.dao.IBaseMapper;
import com.harmazing.openbridge.sys.tag.model.SysTag;

/**
 * 
 * <pre>
 * 标签数据库访问层实现
 * </pre>
 * 
 * @author hehuajun,taoshuangxi
 *
 */
public interface SysTagMapper extends IBaseMapper {

	/**
	 * 
	 * <pre>
	 * 根据标签值查询记录
	 * </pre>
	 * 
	 * @param tagText
	 * @return
	 */
	@Select("select tag_id,tag_text,create_time,hot from sys_tag ct where ct.tag_text=#{tagText}")
	List<Map<String, Object>> queryRecordByTagText(
			@Param("tagText") String tagText);

	/**
	 * 
	 * <pre>
	 * 保存记录
	 * </pre>
	 * 
	 * @param comTag
	 */
	@Insert("insert into sys_tag(tag_id,tag_text,create_time,hot) values(#{tagId},#{tagText},#{createTime},#{hot})")
	void saveComTag(SysTag sysTag);
	
	/**
	 * 查询标签记录
	 * @param params
	 * @return
	 */
	List<Map<String, Object>> tagPage(Map<String, Object> params);

	/**
	 * 查询标签分页信息
	 * @param params
	 * @return
	 */
	Integer tagPageRecordCount(Map<String, Object> params);
	
	/**
	 * 删除标签
	 * @param tagId
	 */
	@Delete("delete from sys_tag where tag_id=#{tagId}")
	void deleteTag(@Param("tagId")String tagId);
	
	/**
	 * 修改标签，改变是否热门状态
	 * @param tagId
	 */
	@Update("update sys_tag set hot=#{status} where tag_id=#{tagId}")
	void updateTagStatus(@Param("tagId")String tagId, @Param("status")Boolean status);
	
	/**
	 * 
	 * @return 所有热门标签名称
	 */
	@Select("select tag_text from sys_tag where hot=1")
	List<String> getAllHotTags();
	
	@Update("update sys_tag set tag_text=#{tagText},hot=#{hot} where tag_id=#{tagId}")
	void updateTagNameAndStatus(SysTag tag);
	
	@Select("select * from sys_tag where tag_id=#{tagId}")
	SysTag getTagById(@Param("tagId")String tagId);
	
	@Select("select * from sys_tag where tag_text=#{tagText} limit 0,1")
	SysTag getTagByName(@Param("tagText")String tagText);

}
