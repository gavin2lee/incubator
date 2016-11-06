package com.harmazing.openbridge.alarm.dao;

import com.harmazing.framework.common.dao.IBaseMapper;
import com.harmazing.openbridge.alarm.model.Group;
import com.harmazing.openbridge.alarm.model.Host;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

/**
 * Created by 李杨 [liyang@yihecloud.com] on 2016/7/29 10:07.
 */
public interface GroupHostMapper extends IBaseMapper {
    @Insert("INSERT INTO grp_host (grp_id, host_id) VALUES (#{0},#{1})")
    void insert(long grpId,long hostId);
    @Delete("DELETE FROM grp_host WHERE grp_id = #{id} ")
    int deleteById(long id);
}
