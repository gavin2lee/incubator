package com.harmazing.openbridge.alarm.dao;

import com.harmazing.framework.common.dao.IBaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;

/**
 * Created by 李杨 [liyang@yihecloud.com] on 2016/8/4 10:04.
 */
public interface GroupTemplateMapper extends IBaseMapper {
    @Insert("INSERT INTO grp_tpl (grp_id, tpl_id, bind_user) VALUES (#{0},#{1},#{2})")
    void insert(long grpId,long tplId,String bindUser );
    @Delete("DELETE FROM grp_tpl WHERE grp_id = #{grpId} ")
    int deleteByGrpId(long grpId);
    @Delete("DELETE FROM grp_tpl WHERE tpl_id = #{tplId} ")
    int deleteByTplId(long tplId);

}
