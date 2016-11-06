package com.harmazing.openbridge.alarm.dao;

import com.harmazing.framework.common.dao.IBaseMapper;
import com.harmazing.openbridge.alarm.model.Action;
import com.harmazing.openbridge.alarm.model.Group;
import com.harmazing.openbridge.alarm.model.vo.ActionFalconDTO;
import com.harmazing.openbridge.alarm.model.vo.GroupIndexDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by 李杨 [liyang@yihecloud.com] on 2016/8/5 9:20.
 */
public interface ActionMapper extends IBaseMapper {
    @Select("SELECT id, uic, url, callback, before_callback_sms, before_callback_mail, after_callback_sms, " +
            "after_callback_mail FROM action WHERE id = #{id} ")
    Action findById(long id);
    @Select("SELECT id, uic, url, callback, before_callback_sms, before_callback_mail, after_callback_sms, " +
            "after_callback_mail FROM action WHERE id = #{id} ")
    ActionFalconDTO findFlaconById(long id);
    @Select("SELECT id, uic, url, callback, before_callback_sms, before_callback_mail, after_callback_sms, " +
            "after_callback_mail FROM action WHERE id = #{uic} ")
    Action findByUic(String uic);
    @Select("SELECT id, uic, url, callback, before_callback_sms, before_callback_mail, after_callback_sms, " +
            "after_callback_mail FROM action ORDER BY id desc ")
    List<Action> findAll();
    
    @Select("SELECT count(1) FROM action WHERE uic = #{uic} ")
     int getCountByUic(String uic);
    
    @Insert("INSERT INTO action (uic, url) VALUES (#{uic},#{url}) ")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Action action);
    @Update("UPDATE action SET uic = #{uic}, url = #{url} WHERE id = #{id} ")
    int update(Action action);
    @Delete("DELETE FROM action WHERE id = #{id} ")
    int deleteById(long id);
}
