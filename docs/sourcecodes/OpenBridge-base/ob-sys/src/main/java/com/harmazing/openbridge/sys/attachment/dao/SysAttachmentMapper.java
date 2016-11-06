package com.harmazing.openbridge.sys.attachment.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.harmazing.framework.common.dao.IBaseMapper;
import com.harmazing.openbridge.sys.attachment.model.SysAttachment;

public interface SysAttachmentMapper extends IBaseMapper {
	
	@Insert("insert into sys_attachment(att_id,att_name,file_path,att_size,create_time) values(#{attId},#{attName},#{filePath},#{attSize},#{createTime})")
	void addAttachment(SysAttachment attachment);
	
	@Update("update sys_attachment set business_id =#{businessId},business_key=#{businessKey},business_type=#{businessType} where att_id=#{attId}")
	void updateAttachmentBusiness(@Param("attId")String attId,@Param("businessId")String businessId,@Param("businessKey")String businessKey,@Param("businessType")String businessType);
	
	@Update("update sys_attachment set business_id =#{businessId} where att_id=#{attId}")
	void updateAttachBusinessId(@Param("attId")String attId,@Param("businessId")String businessId);
	
	@Select("select * from sys_attachment where business_id =#{businessId} and business_key=#{businessKey} and business_type=#{businessType}")
	List<SysAttachment> getAttachmentByBusiness(@Param("businessId")String businessId,@Param("businessKey")String businessKey,@Param("businessType")String businessType);
	
	@Select("select * from sys_attachment where att_id=#{attId}")
	SysAttachment getAttachmentById(@Param("attId")String attId);
	
	@Delete("delete from sys_attachment where att_id=#{attId}")
	void deleteAttachById(@Param("attId")String attId);
}
